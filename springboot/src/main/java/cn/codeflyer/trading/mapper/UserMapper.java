package cn.codeflyer.trading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.codeflyer.trading.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username=#{username}")
    User selectByName(String username);

}
