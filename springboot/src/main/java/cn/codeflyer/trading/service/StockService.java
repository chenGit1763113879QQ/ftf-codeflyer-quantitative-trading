package cn.codeflyer.trading.service;

import cn.codeflyer.trading.entity.Stock;

import java.util.List;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-22 06:05
 */
public interface StockService {
    Boolean add(String stockCode) throws Exception;
    List<Stock> list(String keyWord) throws Exception;
    void buyParse(String date) throws Exception;
    void sellParse(String date) throws Exception;
    void updateBuyPrice(String date) throws InterruptedException, Exception;
    void updateSellPrice(String date) throws InterruptedException, Exception;
}
