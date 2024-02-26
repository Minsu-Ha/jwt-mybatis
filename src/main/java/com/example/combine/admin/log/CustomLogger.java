package com.example.combine.admin.log;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;

public class CustomLogger implements Log {

    private final String clazz;

    public CustomLogger(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean isDebugEnabled() {
        return true; // 디버그 레벨 로깅 활성화
    }

    @Override
    public boolean isTraceEnabled() {
        return true; // 트레이스 레벨 로깅 활성화
    }

    @Override
    public void error(String s, Throwable e) {
        System.err.println("[ERROR] " + clazz + " - " + s);
        e.printStackTrace(System.err);
    }

    @Override
    public void error(String s) {
        System.err.println("[ERROR] " + clazz + " - " + s);
    }

    @Override
    public void debug(String s) {
        System.out.println("[DEBUG] " + clazz + " - " + s);
    }

    @Override
    public void trace(String s) {
        System.out.println("[TRACE] " + clazz + " - " + s);
    }

    @Override
    public void warn(String s) {
        System.out.println("[WARN] " + clazz + " - " + s);
    }

    // BoundSql을 사용하여 실제 SQL 쿼리를 로깅
    public void logBoundSql(BoundSql boundSql) {
        if (isDebugEnabled()) {
            debug("Bound SQL: " + boundSql.getSql());
        }
    }
}

