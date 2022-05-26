package cn.codeflyer.trading.controller;

import cn.codeflyer.trading.common.Result;
import cn.codeflyer.trading.entity.EmailMessage;
import cn.codeflyer.trading.entity.Stock;
import cn.codeflyer.trading.entity.TradeDecision;
import cn.codeflyer.trading.mapper.StockMapper;
import cn.codeflyer.trading.mapper.TradeDecisionMapper;
import cn.codeflyer.trading.service.StockService;
import cn.codeflyer.trading.utils.HTTPUtils;
import cn.codeflyer.trading.utils.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-22 05:35
 */

@RestController
@RequestMapping("/stock")
@Slf4j
public class StockController {

    @Resource
    private MailUtil mailUtil;

    @Resource
    private StockService stockService;

    @Resource
    private StockMapper stockMapper;

    @Resource
    private TradeDecisionMapper tradeDecisionMapper;

    @PostMapping("/add")
    public Result<?> add(@RequestParam String stockCode) {
        log.info("股票池添加 stockCode={}", stockCode);
        try {
            stockService.add(stockCode);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "添加失败-" + e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam String search, @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize) throws Exception {
        LambdaQueryWrapper<Stock> wrapper = Wrappers.<Stock>lambdaQuery().orderByDesc(Stock::getId);
        wrapper.eq(Stock::getIsDelete,false);
        if (Strings.isNotBlank(search)) {
            wrapper.like(Stock::getStockCode, search)
                    .or()
                    .like(Stock::getStockName, search);
        }

        Page<Stock> stockPage = stockMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        List<Stock> stocks = stockPage.getRecords();
        for (Stock stock : stocks) {
            String url = "https://qt.gtimg.cn/q=" + stock.getStockCode();
            String res = HTTPUtils.get(url);
            try {
                Integer price = Integer.parseInt(res.split("~")[3].replace(".", ""));
                stock.setPrice(price);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("实时获取股票价格失败,stockCode={}", stock.getStockCode());
            }
        }
        return Result.success(stockPage);
    }
    @PostMapping("/status")
    public Result<?> statusChange(@RequestParam String id) {
        log.info("上下线股票 id={}", id);
        try {
            Stock stock = stockMapper.selectById(id);
            boolean toStatus = !stock.getStatus();
            stock.setStatus(toStatus);
            stockMapper.updateById(stock);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "上下线失败-" + e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/parse")
    public Result<?> parse(@RequestParam String date) {
        log.info("量化分析 买卖全量 接口入参 date={}", date);
        try {
            stockService.buyParse(date);
            stockService.sellParse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "过程异常-" + e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/parse-buy")
    public Result<?> parseBuy(@RequestParam String date) {
        log.info("量化分析 买入 接口入参 date={}", date);
        try {
            stockService.sellParse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "过程异常-" + e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/parse-sell")
    public Result<?> parseSell(@RequestParam String date) {
        log.info("量化分析 卖出 接口入参 date={}", date);
        try {
            stockService.sellParse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "过程异常-" + e.getMessage());
        }
        return Result.success();
    }

    @GetMapping("/trade-decision/list")
    public Result<?> tradeDecisionList(@RequestParam String search, @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize) throws Exception {
        LambdaQueryWrapper<TradeDecision> wrapper = Wrappers.<TradeDecision>lambdaQuery().orderByDesc(TradeDecision::getId);
        wrapper.eq(TradeDecision::getIsDelete,false);

        Page<TradeDecision> tradeDecisionPage = tradeDecisionMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(tradeDecisionPage);
    }

    @GetMapping("/email/send")
    public Result<?> emailSend(@RequestParam String content) throws Exception {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setContent("我的测试邮件。。。\n嗯嗯啊哈哈");
        if(Strings.isNotBlank(content)){
            content = content.replaceAll("  ", "\n");
            emailMessage.setContent(content);
        }
        mailUtil.sendEmail(emailMessage,null);
        return Result.success(emailMessage.getContent());
    }

}



