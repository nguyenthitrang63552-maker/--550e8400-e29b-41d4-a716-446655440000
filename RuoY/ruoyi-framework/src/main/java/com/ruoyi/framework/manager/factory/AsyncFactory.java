package com.ruoyi.framework.manager.factory;

import java.util.Collection;
import java.util.Date;
import java.util.TimerTask;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.LogUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.UserAgentUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.audit.AuditLogTagSupport;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args)
    {
        final String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        final String ip = IpUtils.getIpAddr();
        // 在提交异步任务前先固化当前用户名的重试次数，避免连续失败时被异步执行时序覆盖。
        final Integer retryCount = getLoginRetryCount(username);
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);

                // 打印信息到控制台日志 (保持不变)
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                sys_user_logger.info(s.toString(), args);

                // --- 开始修改：封装 SysOperLog 对象代替 SysLogininfor ---
                SysOperLog operLog = new SysOperLog();

                // 1. 系统模块
                operLog.setTitle("系统登录");

                // 2. 操作类型 (使用 0=其它，或者你可以在 BusinessType 枚举中增加 LOGIN 类型)
                operLog.setBusinessType(BusinessType.OTHER.ordinal());

                // 3. 请求类型 (登录通常是 POST)
                operLog.setRequestMethod("POST");

                // 4. 操作人员
                operLog.setOperName(username);

                // 5. 主机 (IP)
                operLog.setOperIp(ip);

                // 6. 操作地点
                operLog.setOperLocation(address);

                // 7. 状态 (映射字符串状态到 Integer)
                if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status) || Constants.REGISTER.equals(status)) {
                    operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
                } else {
                    operLog.setStatus(BusinessStatus.FAIL.ordinal());
                    // 如果失败，将错误消息放入 errorMsg
                    operLog.setErrorMsg(message);
                }

                // 8. 操作时间
                operLog.setOperTime(new Date());

                // 9. 操作详情 (可以将浏览器信息等放入请求参数或返回参数中，以便在详情中查看)
                String browser = UserAgentUtils.getBrowser(userAgent);
                String os = UserAgentUtils.getOperatingSystem(userAgent);
                operLog.setOperParam("Browser: " + browser + ", OS: " + os);
                operLog.setJsonResult(message); // 将提示消息放入返回参数中

                // 设置其他默认值
                operLog.setOperatorType(OperatorType.MANAGE.ordinal()); // 操作类别：后台用户
                operLog.setMethod("com.ruoyi.framework.web.service.SysLoginService.login()");// 模拟一个方法名
                AuditLogTagSupport.tagLoginOperLog(operLog, status, retryCount);

                // 插入到操作日志表
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */

    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 1. 若依原有的：保存到本地数据库
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
                // 2. 新增的：推送到甲方系统 (伪代码)
                try {
                    String targetUrl = "http://party-a-api.com/api/logs/receive";
                    // 将 operLog 转换为 JSON 字符串
                    String jsonLog = JSON.toJSONString(operLog);
                    // 使用 HttpUtils 或 RestTemplate 发送 POST 请求
                    HttpUtils.sendPost(targetUrl, jsonLog);
                } catch (Exception e) {
                    // 注意：推送失败不能影响本地业务，只打印错误即可
                    sys_user_logger.error("推送操作日志到甲方系统失败：" + e.getMessage(), e);
                }
            }
        };
    }

    private static Integer getLoginRetryCount(String username)
    {
        if (StringUtils.isBlank(username))
        {
            return 0;
        }
        try
        {
            RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
            String normalizedUsername = StringUtils.trim(username);

            Integer retryCount = readRetryCount(redisCache, CacheConstants.PWD_ERR_CNT_KEY + username);
            if (retryCount != null)
            {
                return retryCount;
            }

            if (!StringUtils.equals(username, normalizedUsername))
            {
                retryCount = readRetryCount(redisCache, CacheConstants.PWD_ERR_CNT_KEY + normalizedUsername);
                if (retryCount != null)
                {
                    return retryCount;
                }
            }

            Collection<String> cacheKeys = redisCache.keys(CacheConstants.PWD_ERR_CNT_KEY + "*");
            if (cacheKeys != null)
            {
                for (String cacheKey : cacheKeys)
                {
                    String cachedUsername = StringUtils.removeStart(cacheKey, CacheConstants.PWD_ERR_CNT_KEY);
                    if (StringUtils.equalsIgnoreCase(normalizedUsername, StringUtils.trim(cachedUsername)))
                    {
                        retryCount = readRetryCount(redisCache, cacheKey);
                        if (retryCount != null)
                        {
                            return retryCount;
                        }
                    }
                }
            }
        }
        catch (Exception ignored)
        {
        }
        return 0;
    }

    private static Integer readRetryCount(RedisCache redisCache, String cacheKey)
    {
        Object cacheValue = redisCache.getCacheObject(cacheKey);
        if (cacheValue == null)
        {
            return null;
        }
        if (cacheValue instanceof Number)
        {
            return ((Number) cacheValue).intValue();
        }

        String retryCountText = StringUtils.trim(String.valueOf(cacheValue));
        return StringUtils.isNumeric(retryCountText) ? Integer.valueOf(retryCountText) : null;
    }
}
