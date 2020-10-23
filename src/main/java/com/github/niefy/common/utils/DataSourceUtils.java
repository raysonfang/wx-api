package com.github.niefy.common.utils;

import com.alibaba.druid.util.JdbcConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DataSourceUtils {

  public static boolean isSQLServer() {
      DataSource dataSource = SpringContextUtils.applicationContext.getBean(DataSource.class);

      // 检查数据库类型
      DatabaseMetaData dbmd = null;
      try {
          dbmd = dataSource.getConnection().getMetaData();
          String dbType = dbmd.getDatabaseProductName().toLowerCase();
          if(dbType.contains("sql server") || dbType.contains("sqlserver")) {
            return true;
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

    return false;
  }


}
