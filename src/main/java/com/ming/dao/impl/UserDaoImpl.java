package com.ming.dao.impl;

import com.ming.dao.UserDao;
import com.ming.po.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author ming
 * @time 2020/9/8 16:09
 */
public class UserDaoImpl implements UserDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> queryUsers(Map param) {
        System.out.println("queryUsers====");


        List<User> resultLists = new ArrayList<User>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取conn
            conn = dataSource.getConnection();

            String sql = " select * from t_user where name = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, param.get("name"));

            rs = ps.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAge(rs.getInt("age"));
                user.setSex(rs.getString("sex"));

                resultLists.add(user);
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return resultLists;
    }
}
