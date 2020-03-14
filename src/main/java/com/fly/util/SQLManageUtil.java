package com.fly.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fly.basic.domain.PageData;
import com.fly.basic.domain.Record;
import com.fly.basic.impl.CommonDao;





public class SQLManageUtil
{
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    public <T> PageData<T> getDbListByPage(Class<? extends Record> baseClass, Map<String, String> conditionMap,
            Integer pageNumber, Integer pageSize, String sortField, String sortOrder) {
        PageData pageData = new PageData();
        String tableName = baseClass.getAnnotation(Table.class).name();
        StringBuffer sb = new StringBuffer();
        String fields = "*";
        String sortCondition = "";
        ArrayList params = new ArrayList();
        sb.append(this.buildSql(conditionMap, params));
        if (conditionMap != null) {
            if (conditionMap.containsKey("#fields")) {
                fields = (String) conditionMap.get("#fields");
                if (StringUtil.isBlank(fields)) {
                    fields = "*";
                }
            }

            if (StringUtil.isNotBlank(conditionMap.get("#join"))) {
                tableName = this.getTable(tableName + " a", (String) conditionMap.get("#join"));
            }
        }
        if (StringUtil.isNotBlank(sortField)) {
            sortCondition = " order by " + sortField + " " + sortOrder;
           
        }

        String sqlRecord = "select " + fields + " from " + tableName + sb.toString() + sortCondition;
        String sqlCount = "select count(*) from " + tableName + sb.toString();
        Object[] paramsobject = params.toArray();
        List dataList = CommonDao.getInstance().findList(sqlRecord, pageNumber, pageSize.intValue(), baseClass,
                paramsobject);
        int dataCount = CommonDao.getInstance().queryInt(sqlCount, paramsobject).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }
    
    public String buildSql(Map<String, String> conditionMap, List<String> params) {
        if (params == null) {
            this.log.info("params参数不能为null！");
            return "";
        } else {
            StringBuffer sb = new StringBuffer(" where 1=1 ");
            sb.append(this.buildPatchSql(conditionMap, params));
            return sb.toString();
        }
    }
    
    public String buildPatchSql(Map<String, String> conditionMap, List<String> params) {
        if (params == null) {
            this.log.info("params参数不能为null！");
            return "";
        } else {
            return this.buildSqlParams(conditionMap, Boolean.valueOf(true), params);
        }
    }
    
    private String buildSqlParams(Map<String, String> conditionMap, Boolean useParameterize, List<String> params) {
        StringBuffer sb = new StringBuffer();
        if (conditionMap != null && !conditionMap.isEmpty()) {
            int i = 1;
          for (Map.Entry<String, String> entry : conditionMap.entrySet()){
              if (entry.getKey().trim().indexOf("#zwfw#") < 0){
                 continue;
              }
              String[] fieldAreaas = entry.getKey().trim().split("#zwfw#");
              String fieldName = fieldAreaas[0];
              String operateType = fieldAreaas[1];
              String valueType = fieldAreaas[2];
              String value = entry.getValue() == null ? "" : (String) entry.getValue();
              switch(operateType){
                  case "eq" :
                      sb.append(" and " + fieldName + " =?"+i);params.add(value);i++;break;
                  case "nq" :
                      sb.append(" and " + fieldName + " !=?"+i);params.add(value);i++;break;
                  case "ge" :
                      sb.append(" and " + fieldName + " >=?"+i);params.add(value);i++;break; 
                  case "gt" :
                      sb.append(" and " + fieldName + " >?"+i);params.add(value);i++;break;    
                  case "le" :
                      sb.append(" and " + fieldName + " <=?"+i);params.add(value);i++;break; 
                  case "lt" :
                      sb.append(" and " + fieldName + " <?"+i);params.add(value);i++;break;  
                  case "in" :
                      sb.append(" and " + fieldName + " in?"+i);params.add(value);i++;break;
                  case "notin" :
                      sb.append(" and " + fieldName + " notin?"+i);params.add(value);i++;break;
                  case "like" :
                      sb.append(" and " + fieldName + " like?"+i);params.add(value);i++;break;
              }
          }
        }

        return sb.toString();
    }

    private String getTable(String leftTablestr, String join) {
        String[] joinTables = join.split(";");
        String joinType = "";
        if (joinTables[0].startsWith("#left#")) {
            joinType = " left join ";
        }
        else if (joinTables[0].startsWith("#right#")) {
            joinType = " right join ";
        }
        else if (joinTables[0].startsWith("#inner#")) {
            joinType = " inner join ";
        }

        String[] field = joinTables[0].split("#");
        String str = leftTablestr + joinType + field[2] + " on " + field[3] + "=" + field[4];
        return joinTables.length > 1 ? this.getTable(str, joinTables[1]) : str;
    }
}
