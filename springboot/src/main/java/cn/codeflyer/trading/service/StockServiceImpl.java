package cn.codeflyer.trading.service;

import cn.codeflyer.trading.entity.Stock;
import cn.codeflyer.trading.mapper.StockMapper;
import cn.codeflyer.trading.utils.HTTPUtils;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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

    private String buildLikeSql(String keyWord) {
        return "%" + keyWord + "%";
    }

    public static void main(String[] args) {

        String s = HttpUtil.get("https://money.finance.sina.com.cn/quotes_service/api/json_v2.php/CN_MarketData.getKLineData?symbol=sz000001&scale=240&ma=5&datalen=10");
        System.out.println(s);
    }
}
