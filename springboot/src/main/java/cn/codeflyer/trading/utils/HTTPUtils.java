package cn.codeflyer.trading.utils;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-22 07:39
 */
@Slf4j
public class HTTPUtils {
    public static String get(String url) throws InterruptedException {
        String res = "";
        int retryTime = 5;
        log.info("发送请求：url={}", url);
        try {
            res = HttpUtil.get(url);
        } catch (Exception e) {
            log.info("发送请求[1s retry]：url={}", url);
            Thread.sleep(1000);
            if (retryTime >= 0) {
                return res;
            }
            res = HttpUtil.get(url);
            retryTime--;
        }
        log.info("请求响应: res={}", res);
        return res;
    }
}
