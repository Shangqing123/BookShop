package com.class166room223.bookshop;

import com.class166room223.bookshop.dao.UserMapper;
import com.class166room223.bookshop.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookshopApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void contextLoads() {
        User user = userMapper.selectByPrimaryKey(1);
        System.out.println(user.toString());
        Date date = new Date();
        System.out.println(date);
    }

}
