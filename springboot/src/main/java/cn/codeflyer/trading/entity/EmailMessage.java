package cn.codeflyer.trading.entity;

import lombok.Data;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-23 23:03
 */
@Data
public class EmailMessage {
    private Boolean html = false;
    private String title;
    private String subTitle;
    private String username;
    private String content;
    private String time;
}
