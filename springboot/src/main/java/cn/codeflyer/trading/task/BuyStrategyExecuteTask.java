package cn.codeflyer.trading.task;

import cn.codeflyer.trading.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    @Scheduled(cron = "0 15 22 * * ?")
    private void buyStrategyExecute() throws Exception {
        log.info("买入决策任务开始执行");
        long startTime=System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        stockService.parse(dateNowStr);
        long endTime=System.currentTimeMillis();

        log.info("买入决策任务执行完成 用时"+(endTime-startTime)+"ms");
    }
}

