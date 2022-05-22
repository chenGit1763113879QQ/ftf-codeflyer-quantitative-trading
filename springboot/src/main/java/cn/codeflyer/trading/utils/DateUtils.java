package cn.codeflyer.trading.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-22 16:24
 */
@Slf4j
public class DateUtils {
    /**
     * format:yyyy-MM-dd
     *
     * @param cnt
     * @return
     */
    public static String addDay(String date,Integer cnt) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(date);
        Date endDate = null;
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.add(Calendar.DATE, cnt);
            endDate = c.getTime();
        }catch (Exception e){
            log.error("转换日期格式出错",e);
        }
        return sdf.format(endDate);
    }
}
