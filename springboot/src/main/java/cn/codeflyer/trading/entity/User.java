package cn.codeflyer.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@TableName("user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String nickName;
    private String password;
    private String avatar;
    private Boolean isSendEmail;
    private String emailAddress;
    private Boolean isDelete;
    private Date createTime;
    private Date updateTime;
}
