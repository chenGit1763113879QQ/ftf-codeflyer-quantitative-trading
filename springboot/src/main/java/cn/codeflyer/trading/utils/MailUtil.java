package cn.codeflyer.trading.utils;

import cn.codeflyer.trading.entity.EmailMessage;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {
 
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(EmailMessage emailMessage,String emailAddress)  {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("1159851944@qq.com");
            helper.setTo("1159851944@qq.com");
            if(Strings.isNotBlank(emailAddress)){
                helper.setTo(emailAddress);
            }
            helper.setSubject("CodeFlyerQuantitativeTradingSystem <每日报告>");
            String sb = MailUtil.toHtml2(emailMessage);
            helper.setText(sb, emailMessage.getHtml());
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
 
    public static  String toHtml2(EmailMessage emailMessage) {
        if (!emailMessage.getHtml()) {
            return emailMessage.getContent();
        }
//        String suject = message1.getSubject();
//        String name = message1.getFullname();
//        String msg = message1.getMessage();
//        String from = message1.getEmailaddress();
//        String phone = message1.getPhonenumber();
//        StringBuffer sb = new StringBuffer();
//        sb.append("<h3 style='color: #d9001f'>您有新的留言：</h3>");
//        sb.append("<p><strong>访客：</strong>"+name+"</p>");
//        sb.append("<p><strong>留言主题：</strong>"+suject+"</p>");
//        sb.append("<p><strong>他的电话：</strong>"+phone+"</p>");
//        sb.append("<p><strong>他的邮箱：</strong>"+from+"</p>");
//        sb.append("<p><strong>正文：</strong>"+msg+"</p>");
//        return sb.toString();
        return "内容为空，请检查邮件内容！";
    }
 
}

