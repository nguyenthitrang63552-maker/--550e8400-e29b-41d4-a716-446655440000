package com.ruoyi.framework.manager.factory;

import java.util.Date;
import java.util.TimerTask;

import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
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
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.service.ISysLogininforService;
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
                operLog.setMethod("com.ruoyi.framework.web.service.SysLoginService.login()"); // 模拟一个方法名

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
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
