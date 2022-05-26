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

/**
 * @author fangtingfei
 * @version 1.0
 * @date 2022-05-25 22:15
 */

@TableName("decision_result")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecisionResult {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String stockCode;
    private String stockName;
    private Integer strategyNo;
    private Integer buyPrice;
    private Integer sellPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date buyTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sellTime;
    private Integer profit;
    private Boolean isDelete;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
}
