package cn.codeflyer.trading.mapper;

import cn.codeflyer.trading.entity.Stock;
import cn.codeflyer.trading.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {

    @Select("select * from stock where stock_code like #{keyWord} or stock_name like #{keyWord} and is_delete=0")
    List<Stock> selectByKeyWord(String keyWord);

    @Select("select * from stock where stock_code=#{username} and is_delete=0")
    Stock selectByCode(String stockCode);

}
