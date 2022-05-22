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

@TableName("trade_decision")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeDecision {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String stockCode;
    @TableField(exist = false)
    private String stockName;
    /**
     * 交易类型  0买入 1卖出
     */
    private Integer tradeType;
    /**
     * 数量 默认1
     */
    private Integer amount;
    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tradeTime;
    /**
     * 交易状态 0结束 1进行中
     */
    private Integer tradeStatus;
    /**
     * 策略ID
     */
    private Integer strategyId;
    private Boolean isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
}
