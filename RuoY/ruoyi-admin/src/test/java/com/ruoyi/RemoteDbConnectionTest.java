package com.ruoyi;

import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.framework.datasource.DynamicDataSourceContextHolder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

/**
 * 远程数据库连接测试类
 * 修正版：兼容 DM8 语法
 */
@SpringBootTest
public class RemoteDbConnectionTest
{
    private static final Logger log = LoggerFactory.getLogger(RemoteDbConnectionTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testRemoteConnection()
    {
        // ---------------------------------------------------------
        // 1. 测试默认数据源 (Master/本地 MySQL)
        // ---------------------------------------------------------
        log.info("====== 开始测试：本地主库 (Master) ======");
        try {
            String localDbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            log.info("连接成功！本地 MySQL 数据库名称: {}", localDbName);
        } catch (Exception e) {
            log.error("本地主库连接失败", e);
        }

        // ---------------------------------------------------------
        // 2. 测试远程数据源 (Slave/远程 DM8)
        // ---------------------------------------------------------
        log.info("====== 开始测试：远程从库 (Slave) ======");

        // 【关键步骤】手动切换数据源上下文到 SLAVE
        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE.name());

        try
        {
            // 验证 1: 打印当前数据源类型
            String currentKey = DynamicDataSourceContextHolder.getDataSourceType();
            log.info("数据源已切换为: {}", currentKey);

            // 验证 2: 简单的连通性测试 (DM8 和 MySQL 都支持 FROM DUAL)
            // 这行代码只是为了证明网络通了，账号密码对了
            Integer checkVal = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            log.info("物理连接测试成功，返回值为: {}", checkVal);

            // 验证 3: 获取当前 Schema 名称 (使用达梦/Oracle 语法)
            // ❌ 不要用 SELECT DATABASE()
            // ✅ 使用 SYS_CONTEXT('USERENV','CURRENT_SCHEMA')
            String currentSchema = jdbcTemplate.queryForObject("SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') FROM DUAL", String.class);
            log.info("业务连接成功！当前达梦模式(Schema)为: {}", currentSchema);

            // (可选) 验证 4: 获取达梦具体版本信息
            String dmVersion = jdbcTemplate.queryForObject("SELECT BANNER FROM V$VERSION LIMIT 1", String.class);
            log.info("达梦数据库版本信息: {}", dmVersion);

        }
        catch (Exception e)
        {
            log.error("连接远程数据库失败！", e);
            throw e;
        }
        finally
        {
            // 【重要】清理上下文
            DynamicDataSourceContextHolder.clearDataSourceType();
            log.info("====== 测试结束，已清理数据源上下文 ======");
        }
    }
}