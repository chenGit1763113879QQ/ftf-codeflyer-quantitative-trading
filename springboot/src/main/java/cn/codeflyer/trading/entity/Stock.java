package cn.codeflyer.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("stock")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String stockCode;
    private String stockName;
    /**
     * 类型0 基金 1 股票 default 0
     */
    private Integer type;
    /**
     * 在线状态 0不在线 1 在线 default 0
     */
    private Boolean status;
    @TableField(exist = false)
    private Integer price;
    private Boolean isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
}
