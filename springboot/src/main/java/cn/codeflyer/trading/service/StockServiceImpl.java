package cn.codeflyer.trading.service;

import cn.codeflyer.trading.entity.SinaStockMarketDTO;
import cn.codeflyer.trading.entity.Stock;
import cn.codeflyer.trading.entity.TradeDecision;
import cn.codeflyer.trading.mapper.StockMapper;
import cn.codeflyer.trading.mapper.TradeDecisionMapper;
import cn.codeflyer.trading.utils.DateUtils;
import cn.codeflyer.trading.utils.HTTPUtils;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-22 06:07
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService {
    @Resource
    private StockMapper stockMapper;
    @Resource
    private TradeDecisionMapper tradeDecisionMapper;

    @Override
    public Boolean add(String stockCode) throws Exception {
        List<Stock> stocks = stockMapper.selectByKeyWord(buildLikeSql(stockCode));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stock_code", stockCode);
        Stock existStock = stockMapper.selectOne(queryWrapper);
        if (existStock != null) {
            throw new Exception("该基金/股票已添加，请搜索检查！stockCode=" + stockCode);
        }
        //使用腾讯API    https://qt.gtimg.cn/q=sh000858
        String url = "https://qt.gtimg.cn/q=" + stockCode;
        String res = HTTPUtils.get(url);
        if (Strings.isBlank(res) || res.contains("none_match")) {
            throw new Exception("未找到该编码的基金/股票，stockCode=" + stockCode);
        }
        String stockNamae = res.split("~")[1];
        Stock stock = Stock.builder().stockCode(stockCode).stockName(stockNamae).status(true).build();
        stockMapper.insert(stock);
        return true;
    }

    @Override
    public List<Stock> list(String keyWord) throws Exception {
        List<Stock> stocks = stockMapper.selectByKeyWord(buildLikeSql(keyWord));
        for (Stock stock : stocks) {
            String url = "https://qt.gtimg.cn/q=" + stock.getStockCode();
            String res = HTTPUtils.get(url);
            try {
                Integer price = Integer.parseInt(res.split("~")[3].replace(".", ""));
                stock.setPrice(price);
            } catch (Exception e) {
                log.error("实时获取股票价格失败,stockCode={}", stock.getStockCode());
            }
        }
        return stocks;
    }

    @Override
    public void parse(String date) throws Exception {
        Date inputDate = getAndJudgeTime(date);
        int dayInWeek = DateUtil.dayOfWeek(inputDate);
        //西方的星期，从星期日算第一天...所以需要做以下兼容
        if(dayInWeek == 7 || dayInWeek == 1){
            throw new RuntimeException("星期天不能交易，也不能分析！");
        }
        dayInWeek--;
        int t_1 = -2;
        int t_2 = -1;
        if (dayInWeek == 1) {
            t_1 = -4;
            t_2 = -3;
        }
        if (dayInWeek == 2) {
            t_1 = -4;
            t_2 = -1;
        }
        QueryWrapper<Stock> queryStockWrapper = new QueryWrapper();
        queryStockWrapper.notIn("status", 0);
        queryStockWrapper.notIn("is_delete", 1);
        List<Stock> stocks = stockMapper.selectList(queryStockWrapper);
        for (Stock stock : stocks) {
            log.info("开始分析  date={},stock={}", date, stock);
            String url = "https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=%s&scale=240&ma=%s&datalen=30";
            String url5 = String.format(url, stock.getStockCode(), 5);
            String json5 = HTTPUtils.get(url5);
            JSONArray jsonArray5 = JSONUtil.parseArray(json5);
            List<SinaStockMarketDTO> sinaStockMarket5DTOS = jsonArray5.toList(SinaStockMarketDTO.class);
            double ma_price_5_r2 = getMaPrice(sinaStockMarket5DTOS, 5, DateUtils.addDay(date, t_1));
            double ma_price_5_r1 = getMaPrice(sinaStockMarket5DTOS, 5, DateUtils.addDay(date, t_2));
            String url10 = String.format(url, stock.getStockCode(), 10);
            String json10 = HTTPUtils.get(url10);
            JSONArray jsonArray10 = JSONUtil.parseArray(json10);
            List<SinaStockMarketDTO> sinaStockMarket10DTOS = jsonArray10.toList(SinaStockMarketDTO.class);
            double ma_price_10_r2 = getMaPrice(sinaStockMarket10DTOS, 10, DateUtils.addDay(date, t_1));
            double ma_price_10_r1 = getMaPrice(sinaStockMarket10DTOS, 10, DateUtils.addDay(date, t_2));
            SinaStockMarketDTO sinaStockMarketDTO = getSinaStockMarket10DTO(sinaStockMarket10DTOS, DateUtils.addDay(date, t_2));
            double open = Double.parseDouble(sinaStockMarketDTO.getOpen());
            double close = Double.parseDouble(sinaStockMarketDTO.getClose());
//            if (ma_price_5_r2 < ma_price_10_r2 && ma_price_5_r1 > ma_price_10_r1 && open < close) {
            if(true){
                log.info("今日命中买入决策  stockCode={},stockName={}date={},ma_price_5_r2={},ma_price_10_r2={},ma_price_5_r1={},ma_price_10_r1={},open={},close={}", stock.getStockCode(), stock.getStockName(), date, ma_price_5_r2, ma_price_10_r2, ma_price_5_r1, ma_price_10_r1, open, close);
                recordBuyDecision(stock,date);
                recordBuyPrice(stock,date,sinaStockMarket5DTOS);
            }
        }
    }

    private void recordBuyDecision(Stock stock,String date){
        TradeDecision tradeDecision = TradeDecision.builder()
                .stockCode(stock.getStockCode())
                .stockName(stock.getStockName())
                .amount(1)
                .tradeStatus(1)
                .tradeTime(getAndJudgeTime(date))
                .tradeType(0)
                .build();
        tradeDecisionMapper.insert(tradeDecision);
    }

    private void recordBuyPrice(Stock stock,String date,List<SinaStockMarketDTO> sinaStockMarketDTOS){
        Date parseTime = getAndJudgeTime(date);
        DateTime parseEndTimeOfDay = DateUtil.endOfDay(parseTime);
        for (SinaStockMarketDTO sinaStockMarketDTO : sinaStockMarketDTOS) {
            if (sinaStockMarketDTO.getDay().equals(date)) {
                int openPrice = Integer.parseInt(Double.toString(Double.parseDouble(sinaStockMarketDTO.getOpen()) * 100.0).split("\\.")[0]);
                UpdateWrapper<TradeDecision> updateWrapper = new UpdateWrapper<TradeDecision>();
                updateWrapper.eq("stock_code",stock.getStockCode());
                updateWrapper.eq("trade_type",0);
                updateWrapper.ge("trade_time",parseTime);
                updateWrapper.le("trade_time",parseEndTimeOfDay);
                updateWrapper.set("trade_price",openPrice);
                tradeDecisionMapper.update(null,updateWrapper);
            }
        }
    }


    private String buildLikeSql(String keyWord) {
        return "%" + keyWord + "%";
    }

    private Date getAndJudgeTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        try {
            startDate = sdf.parse(date);
            //todo 需加强时间校验，仅时间范围内可进行分析
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("输入的日期不合法，请校验！date=" + date);
        }
        return startDate;
    }

    private Double getMaPrice(List<SinaStockMarketDTO> sinaStockMarketDTOS, Integer maValue, String date) {
        for (SinaStockMarketDTO sinaStockMarketDTO : sinaStockMarketDTOS) {
            if (sinaStockMarketDTO.getDay().equals(date)) {
                if (maValue == 5) {
                    return sinaStockMarketDTO.getMa_price5();
                } else if (maValue == 10) {
                    return sinaStockMarketDTO.getMa_price10();
                } else {
                    throw new RuntimeException("均值天数不合法，当前仅支持5日10日，请校验！maValue=" + maValue);

                }
            }
        }
        throw new RuntimeException("获取均值日期不合法-1，请校验！date=" + date);
    }

    private SinaStockMarketDTO getSinaStockMarket10DTO(List<SinaStockMarketDTO> sinaStockMarketDTOS, String date) {
        for (SinaStockMarketDTO sinaStockMarketDTO : sinaStockMarketDTOS) {
            if (sinaStockMarketDTO.getDay().equals(date)) {
                return sinaStockMarketDTO;
            }
        }
        throw new RuntimeException("获取均值日期不合法-2，请校验！date=" + date);
    }

    public static void main(String[] args) {
//
//        String s = HttpUtil.get("https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sz000001&scale=240&ma=5&datalen=10");
//        System.out.println(s);

        String mm= "34.0";
        String[] split = mm.split("\\.");


    }
}
