package com.fly.basic.api;

import java.io.Serializable;
import java.util.List;

import com.fly.basic.domain.Record;


public interface ICommonDao  extends Serializable
{
    <T> T find(String sql, Class<T> cls, Object... args);
    
    <T> T find(Class<T> cls, Object primaryKey);
    
    String ChangeParamsToSql(String sql, Object... args);
    
    <T> Object querySingle(String sql, Class<T> cls, Object... args);
    
    String queryString(String sql, Object... args);
    
    Integer queryInt(String sql, Object... args);
    
    Boolean queryBoolean(String sql, Object... args);
    
    <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> cls, Object... args);
    
    <T> List<T> findList(String sql, Class<T> cls, Object... args);
    
    <T> List<T> findList(String sql, Class<T> cls);
    
    <T extends Record> int delete(T record);
    
    <T extends Record> int deleteByIds(Class<T> cls, String ids);
    
    <T extends Record> int update(T record);
    
    <T extends Record> int insert(T record);
    
    <T extends Record> int execute(String sql, Object... args);

}
