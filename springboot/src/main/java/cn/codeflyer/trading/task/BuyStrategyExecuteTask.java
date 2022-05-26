package cn.codeflyer.trading.task;

import cn.codeflyer.trading.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-24 21:57
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class BuyStrategyExecuteTask {

    @Resource
    private StockService stockService;

    @Scheduled(cron = "0 0 5 * * ?")
    private void buyStrategyExecute() throws Exception {
        log.info("买入决策任务开始执行");
        long startTime=System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        stockService.buyParse(dateNowStr);
        long endTime=System.currentTimeMillis();

        log.info("买入决策任务执行完成 用时"+(endTime-startTime)+"ms");
    }

    @Scheduled(cron = "0 0 7 * * ?")
    private void sellStrategyExecute() throws Exception {
        log.info("卖出决策任务开始执行");
        long startTime=System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        stockService.sellParse(dateNowStr);
        long endTime=System.currentTimeMillis();

        log.info("卖出决策任务执行完成 用时"+(endTime-startTime)+"ms");
    }

    @Scheduled(cron = "0 41 9 * * ?")
    private void updateBuyPriceExecute() throws Exception {
        log.info("买入价格更新执行");
        long startTime=System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        stockService.updateBuyPrice(dateNowStr);
        long endTime=System.currentTimeMillis();

        log.info("买入决策任务执行完成 用时"+(endTime-startTime)+"ms");
    }

    @Scheduled(cron = "0 42 9 * * ?")
    private void updateSellPriceExecute() throws Exception {
        log.info("卖出价格更新执行");
        long startTime=System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        stockService.updateSellPrice(dateNowStr);
        long endTime=System.currentTimeMillis();

        log.info("卖出决策任务执行完成 用时"+(endTime-startTime)+"ms");
    }
}

