package com.ming.test;

import com.ming.dao.impl.UserDaoImpl;
import com.ming.po.User;
import com.ming.service.UserService;
import com.ming.service.impl.UserServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ming
 * @time 2020/9/8 16:14
 */
public class TestSpring {

    @Test
    public void test(){

        //这样写法，，需要 知道 UserService 它的子接口。 即使用者需要取进行业务对象的构造进行了解，这样明显
        UserService service = new UserServiceImpl();

        Map<String,Object> params = new HashMap<String, Object>();
        List<User> users = service.queryUsers(params);

        System.out.println(users);
    }

    @Test
    public void test2(){

        UserServiceImpl userService = new UserServiceImpl();
        UserDaoImpl userDao = new UserDaoImpl();
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/ming");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("root");

        userDao.setDataSource(basicDataSource);
        userService.setUserDao(userDao);

        Map<String,Object> params = new HashMap<String, Object>();
        params.put("name", "风骚小妲己");
        List<User> users = userService.queryUsers(params);

        System.out.println(users);
    }
}
