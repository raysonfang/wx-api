package com.github.niefy.common.nokey;

public enum NoahSqlMethod {
    /**
     * 插入
     */
    INSERT_BATCH("insertBatch", "插入一条数据（选择字段插入）", "<script>\nINSERT INTO %s %s VALUES %s\n</script>"),
    ;
    private final String method;
    private final String desc;
    private final String sql;

    NoahSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }
}
