package com.fly.basic.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.fly.basic.api.ICommonDao;
import com.fly.basic.domain.Record;
import com.fly.shiro.DataSourceConfigFactory;
import com.fly.util.DataSourceConfig;
import com.fly.util.EpointDateUtil;
import com.fly.util.StringUtil;

public class CommonDao implements ICommonDao
{

    /**
     * 
     */
    private static final long serialVersionUID = 7881452756025841688L;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static CommonDao instance;
    private static Map<String, DruidDataSource> druidDataSourcemap = new HashMap<String, DruidDataSource>();
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>(); 

    private CommonDao() {

    }

    public static synchronized CommonDao getInstance() {
        DruidDataSource druidtemp = DataSourceConfigFactory.getDruidDataSource();
        if (instance == null) {
            instance = new CommonDao();
            //将框架数据源放入静态map对象中
            if (!druidDataSourcemap.containsKey(druidtemp.getUrl())) {
                druidDataSourcemap.put(druidtemp.getUrl(), druidtemp);
            }

        }
        //建立连接
        try {
            threadLocal.set(druidtemp.getConnection());
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return instance;
    }

    public static synchronized CommonDao getInstance(DataSourceConfig config) {
        if (instance == null) {
            instance = new CommonDao();
        }
        //将自定义数据源放入静态map对象中
        if (config != null && StringUtil.isNotBlank(config.getUrl())) {
            DruidDataSource druidtemp = null;
            if (! druidDataSourcemap.containsKey(config.getUrl())) {
                druidtemp = DataSourceConfigFactory.getDruidByDruidDataSource(config.getUrl(), config.getUsername(),
                        config.getPassword());
                druidDataSourcemap.put(druidtemp.getUrl(), druidtemp);
            }
            else {
                druidtemp = druidDataSourcemap.get(config.getUrl());
            }

            try {
                threadLocal.set(druidtemp.getConnection());
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return instance;
    }

    public <T> T find(String sql, Class<T> cls, Object... args) {
        T A = null;
        List<T> list = findList(sql, cls, args);
        if (list != null && list.size() > 0) {
            A = list.get(0);
        }
        return A;
    }

    public <T> T find(Class<T> cls, Object primaryKey) {
        String tablename = cls.getAnnotation(Table.class).name();
        String primaryKeyName = cls.getAnnotation(Table.class).catalog();
        String sql = "select * from " + tablename + " where " + primaryKeyName + " = '" + primaryKey + "'";
        try {
            Connection conn = threadLocal.get();
            Statement stat = conn.createStatement();
            ResultSet rst = stat.executeQuery(sql);
           
            T obj = null;
            Boolean flag = true;
            while (rst.next() && flag == true) {
                try {
                    obj = cls.newInstance();
                }
                catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    log.error("InstantiationException:", e);
                }
                catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    log.error("IllegalAccessException:", e);
                }
                HashMap<String, Object> rec = (HashMap<String, Object>) obj;
                ResultSetMetaData rsmd = rst.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    //rec.put(rsmd.getColumnName(i + 1).toLowerCase(), rst.getObject(rsmd.getColumnName(i + 1)));
                    rec.put(rsmd.getColumnLabel(i + 1).toLowerCase(), rst.getObject(rsmd.getColumnLabel(i + 1)));
                }
                flag = false;

            }
            rst.close();
            stat.close();
            conn.close();
           
            return obj;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    public String ChangeParamsToSql(String sql, Object... args) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                sql = sql.replaceAll("\\=\\s*\\?\\s*" + (i + 1), "= '" + args[i] + "'");//替换所有等于查询变量
                sql = sql.replaceAll("like\\s*\\?\\s*" + (i + 1), "like '%" + args[i] + "%'");
                sql = sql.replaceAll("\\>\\s*\\?\\s*" + (i + 1), "> '" + args[i] + "'");
                sql = sql.replaceAll("\\<\\s*\\?\\s*" + (i + 1), "< '" + args[i] + "'");
                sql = sql.replaceAll("in\\s*\\?\\s*" + (i + 1), "in (" + args[i] + ")");
            }
        }
        return sql;
    }

    public <T> Object querySingle(String sql, Class<T> cls, Object... args) {
        sql = ChangeParamsToSql(sql, args);
        try {
            Connection conn = threadLocal.get();
            Statement stat = conn.createStatement();
            ResultSet rst = stat.executeQuery(sql);
            
            Object value = null;
            Boolean flag = true;
            while (rst.next() && flag == true) {
                ResultSetMetaData rsmd = rst.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    value = rst.getObject(rsmd.getColumnName(i + 1));
                    flag = false;
                }

            }
            rst.close();
            stat.close();
            conn.close();
            return value;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }

    }

    public String queryString(String sql, Object... args) {
        return querySingle(sql, String.class, args).toString();
    }

    public Integer queryInt(String sql, Object... args) {
        return Integer.parseInt(querySingle(sql, Integer.class, args).toString());
    }

    public Boolean queryBoolean(String sql, Object... args) {
        int count = Integer.parseInt(querySingle(sql, Integer.class, args).toString());
        return count > 0 ? true : false;
    }

    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> cls, Object... args) {
        sql = ChangeParamsToSql(sql, args);
        if (pageNumber > 0 && pageSize > 0) {
            int start = (pageNumber - 1) * pageSize;
            sql += " limit " + start + "," + pageSize;
        }
        Connection conn = null;
        Statement stat = null;
        ResultSet rst = null;
       
        try {
            conn = threadLocal.get();
            stat = conn.createStatement();
            rst = stat.executeQuery(sql);
            List<T> retlst = new ArrayList<T>();
            while (rst.next()) {
                T obj = null;
                try {
                    obj = cls.newInstance();//Integer没有默认构造器，但是Integer有构造器Integer(int i)，所以无法直接调用无参的newInstance()
                }
                catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    log.error("InstantiationException:", e);
                }
                catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    log.error("IllegalAccessException:", e);
                }
                ResultSetMetaData rsmd = rst.getMetaData();
                if ("class java.lang.String".equals(cls.toString())) {
                    String rec = rst.getObject(rsmd.getColumnName(1)).toString();
                    retlst.add((T) rec);
                }
                else {
                    HashMap<String, Object> rec = (HashMap<String, Object>) obj;
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        //rec.put(rsmd.getColumnName(i + 1).toLowerCase(), rst.getObject(rsmd.getColumnName(i + 1)));
                        rec.put(rsmd.getColumnLabel(i + 1).toLowerCase(), rst.getObject(rsmd.getColumnLabel(i + 1)));//getColumnName取不到别名
                    }
                    retlst.add(obj);
                }
            }

            return retlst;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        finally {
            try {
                rst.close();
                stat.close();
                conn.close();
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    public <T> List<T> findList(String sql, Class<T> cls, Object... args) {
        return findList(sql, 0, 0, cls, args);
    }

    public <T> List<T> findList(String sql, Class<T> cls) {
        return findList(sql, 0, 0, cls);

    }

    @Override
    public <T extends Record> int delete(T record) {
        // TODO Auto-generated method stub
        String tablename = record.getClass().getAnnotation(Table.class).name();
        String primaryKey = record.getClass().getAnnotation(Table.class).catalog();
        String sql = "delete from " + tablename + " where " + primaryKey + " = '" + record.getStr(primaryKey)+"'";
        try {
            Connection conn = threadLocal.get();
            Statement stat = conn.createStatement();
            
            int i = stat.executeUpdate(sql);
            stat.close();
            conn.close();
            return i;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Record> int deleteByIds(Class<T> cls, String ids) {
        // TODO Auto-generated method stub
        String tablename = cls.getAnnotation(Table.class).name();
        String primaryKey = cls.getAnnotation(Table.class).catalog();
        String sql = "delete from " + tablename + " where " + primaryKey + " in (" + ids + ")";
        try {
            Connection conn = threadLocal.get();
            Statement stat = conn.createStatement();
            int i = stat.executeUpdate(sql);
            stat.close();
            conn.close();
            return i;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public <T extends Record> int execute(String sql, Object... args) {
        // TODO Auto-generated method stub
        sql = ChangeParamsToSql(sql, args);
        Connection conn = null;
        Statement stat = null;
        try {
             conn = threadLocal.get();
             stat = conn.createStatement();
            int i = stat.executeUpdate(sql);
          
            return i;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        finally{
            try {
                stat.close();
                conn.close();
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        }
    }
    
   
    @Override
    public <T extends Record> int update(T record) {
        // TODO Auto-generated method stub
        String tablename = record.getClass().getAnnotation(Table.class).name();
        String primaryKey = record.getClass().getAnnotation(Table.class).catalog();
        String sql = "update " + tablename + " set ";
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            sql = sql + entry.getKey() + " = " + converDateToString(entry.getValue()) + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql + " where " + primaryKey + " = '" + record.getStr(primaryKey) + "'";
        sql = sql.replaceAll("\\\\", "\\\\\\\\");
        try {
            Connection conn = threadLocal.get();
            Statement stat = conn.createStatement();
            int i = stat.executeUpdate(sql);
            stat.close();
            conn.close();
            return i;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends Record> int insert(T record) {
        // TODO Auto-generated method stub
        String tablename = record.getClass().getAnnotation(Table.class).name();
        String primaryKey = record.getClass().getAnnotation(Table.class).catalog();
        String sql = "insert into " + tablename + "(";
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            sql = sql + entry.getKey() + ", ";
        }
        sql = sql.substring(0, sql.length() - 2) + ") values (";
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            sql = sql + converDateToString(entry.getValue()) + ", ";
        }
        sql = sql.substring(0, sql.length() - 2) + ")";
        sql = sql.replaceAll("\\\\", "\\\\\\\\");
        try {
            Connection conn = threadLocal.get();
            Statement stat = conn.createStatement();
            int i = stat.executeUpdate(sql);
            stat.close();
            conn.close();
            return i;

        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    public String converDateToString(Object object) {
        if (object instanceof Date) {
            return "'" + EpointDateUtil.convertDate2String((Date) object, "yyyy-MM-dd HH:mm:ss") + "'";
        }
        else {
            return "'" + object + "'";
        }
    }

}
