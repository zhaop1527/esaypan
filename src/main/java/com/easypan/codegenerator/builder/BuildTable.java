package com.easypan.codegenerator.builder;

import com.easypan.codegenerator.utils.PropertiesUtils;
import com.mysql.cj.protocol.Resultset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class BuildTable {

    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static Connection conn = null;
    private static String SQL_SHOW_TABLE_STATUS = "show table status";

    static{
        String dirverName = PropertiesUtils.getString("spring.datasource.driver-class-name");
        String url = PropertiesUtils.getString("spring.datasource.url");
        String user = PropertiesUtils.getString("spring.datasource.username");
        String password = PropertiesUtils.getString("spring.datasource.password");
        try {
            Class.forName(dirverName);
            conn = DriverManager.getConnection(url,user,password);
        } catch (Exception e) {
            logger.error("数据库连接失败",e);
        }
    }

    public static void getTables(){
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            tableResult = ps.executeQuery();
            while(tableResult.next()){
                String tableName = tableResult.getString("Name");
                String comment = tableResult.getString("Comment");
                logger.info("tableName:{},comment:{}",tableName,comment);
            }
        } catch (SQLException throwables) {
            logger.error("读取表失败",throwables);
        }finally {
            if(tableResult != null){
                try {
                    tableResult.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }
    }
}
