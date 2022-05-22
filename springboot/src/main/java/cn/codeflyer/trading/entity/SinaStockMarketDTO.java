package cn.codeflyer.trading.entity;

import lombok.Data;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-22 16:18
 */
@Data
public class SinaStockMarketDTO {
    private String day;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private double ma_price5;
    private double ma_price10;
    private long ma_volume5;
}
