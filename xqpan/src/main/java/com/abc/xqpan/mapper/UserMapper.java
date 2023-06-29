package com.abc.xqpan.mapper;

import com.abc.xqpan.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User queryUserByUsername(String username);


}
