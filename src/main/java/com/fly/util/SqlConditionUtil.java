package com.fly.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SqlConditionUtil {
    private Map<String, String> conditionMap = new HashMap();

    public SqlConditionUtil() {
      
    }

    public SqlConditionUtil(Map<String, String> map) {
        if (map != null && "1".equals(map.get("#isnew"))) {
            this.conditionMap.putAll(map);
        } else {
            this.conditionMap.put("#isnew", "1");
        }

    }

    public void eq(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "eq" + "#zwfw#" + "S", fieldValue);
    }

    public void nq(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "nq" + "#zwfw#" + "S", fieldValue);
    }

    public void gt(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "gt" + "#zwfw#" + "S", fieldValue);
    }

    public void gt(String fieldName, Date fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "gt" + "#zwfw#" + "D",
                EpointDateUtil.convertDate2String(fieldValue, "yyyy-MM-dd HH:mm:ss"));
    }

    public void ge(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "ge" + "#zwfw#" + "S", fieldValue);
    }

    public void ge(String fieldName, Date fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "ge" + "#zwfw#" + "D",
                EpointDateUtil.convertDate2String(fieldValue, "yyyy-MM-dd HH:mm:ss"));
    }

    public void lt(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "lt" + "#zwfw#" + "S", fieldValue);
    }

    public void lt(String fieldName, Date fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "lt" + "#zwfw#" + "D",
                EpointDateUtil.convertDate2String(fieldValue, "yyyy-MM-dd HH:mm:ss"));
    }

    public void le(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "le" + "#zwfw#" + "S", fieldValue);
    }

    public void le(String fieldName, Date fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "le" + "#zwfw#" + "D",
                EpointDateUtil.convertDate2String(fieldValue, "yyyy-MM-dd HH:mm:ss"));
    }

    public void like(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "like" + "#zwfw#" + "S", fieldValue);
    }

    public void leftLike(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "leftlike" + "#zwfw#" + "S", fieldValue);
    }

    public void rightLike(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "rightlike" + "#zwfw#" + "S", fieldValue);
    }

    public void in(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "in" + "#zwfw#" + "S", fieldValue);
    }

    public void notin(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "noin" + "#zwfw#" + "S", fieldValue);
    }

    public void between(String fieldName, Date fromDate, Date endDate) {
        this.conditionMap.put(fieldName + "#zwfw#" + "btw" + "#zwfw#" + "D",
                EpointDateUtil.convertDate2String(fromDate, "yyyy-MM-dd HH:mm:ss") + "#zwfw#"
                        + EpointDateUtil.convertDate2String(endDate, "yyyy-MM-dd HH:mm:ss"));
    }

    public void isBlank(String fieldName) {
        this.isBlankOrValue(fieldName, "");
    }

    public void isBlankOrValue(String fieldName, String fieldValue) {
        this.conditionMap.put(fieldName + "#zwfw#" + "null" + "#zwfw#" + "S", fieldValue);
    }

    public void isNotBlank(String fieldName) {
        this.conditionMap.put(fieldName + "#zwfw#" + "notn" + "#zwfw#" + "S", "");
    }

    public void setOrderDesc(String fieldName) {
        this.setOrder(fieldName, "desc");
    }

    public void setOrderAsc(String fieldName) {
        this.setOrder(fieldName, "asc");
    }

    public void setOrder(String sortField, String sortOrder) {
        String sort = (String) this.conditionMap.get("#sort");
        if (StringUtil.isBlank(sort)) {
            this.conditionMap.put("#sort", sortField + " " + sortOrder);
        } else {
            this.conditionMap.put("#sort", sort + "," + sortField + " " + sortOrder);
        }

    }

    public void setSelectCounts(Integer count) {
        this.conditionMap.put("#count", count.toString());
    }

    public void setSelectFields(String fields) {
        this.conditionMap.put("#fields", fields);
    }

    public void setLeftJoinTable(String tableName, String leftTableField, String rightTableField) {
        this.conditionMap.put("#join", "#left#"
                + (StringUtil.isBlank(this.conditionMap.get("#join")) ? "" : (String) this.conditionMap.get("#join"))
                + tableName + "#" + leftTableField + "#" + rightTableField + ";");
    }

    public void setRightJoinTable(String tableName, String leftTableField, String rightTableField) {
        this.conditionMap.put("#join", "#right#"
                + (StringUtil.isBlank(this.conditionMap.get("#join")) ? "" : (String) this.conditionMap.get("#join"))
                + tableName + "#" + leftTableField + "#" + rightTableField + ";");
    }

    public void setInnerJoinTable(String tableName, String leftTableField, String rightTableField) {
        this.conditionMap.put("#join", "#inner#"
                + (StringUtil.isBlank(this.conditionMap.get("#join")) ? "" : (String) this.conditionMap.get("#join"))
                + tableName + "#" + leftTableField + "#" + rightTableField + ";");
    }

    public Map<String, String> getMap() {
        return this.conditionMap;
    }

    public void clear() {
        this.conditionMap.clear();
        this.conditionMap.put("#isnew", "1");
    }
}