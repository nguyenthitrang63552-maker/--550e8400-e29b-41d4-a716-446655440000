package com.ruoyi.framework.audit;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.OperatorType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.domain.SysOperLog;

public final class AuditLogTagSupport
{
    public static final String EVENT_TYPE_NORMAL = "normal";
    public static final String EVENT_TYPE_ABNORMAL = "abnormal";
    public static final String EVENT_TYPE_VIOLATION = "violation";

    public static final String BIZ_CATEGORY_LOGIN = "\u767b\u5f55";
    public static final String BIZ_CATEGORY_PERMISSION = "\u6743\u9650";
    public static final String BIZ_CATEGORY_EXPORT = "\u6570\u636e\u5bfc\u51fa";
    public static final String BIZ_CATEGORY_CONFIG = "\u914d\u7f6e\u53d8\u66f4";

    public static final String TAG_OVER_PERMISSION = "\u8d8a\u6743";
    public static final String TAG_BATCH_EXPORT = "\u6279\u91cf\u5bfc\u51fa";
    public static final String TAG_SENSITIVE_ACCESS = "\u654f\u611f\u8bbf\u95ee";
    public static final String TAG_NIGHT_LOGIN = "\u591c\u95f4\u767b\u5f55";
    public static final String TAG_FREQUENT_FAILURE = "\u9891\u7e41\u5931\u8d25";
    public static final String TAG_ABNORMAL_IP = "\u5f02\u5e38IP";
    public static final String TAG_CONCURRENT_LOGIN = "\u5e76\u53d1\u767b\u5f55";

    private static final String KEYWORD_LOGIN = "\u767b\u5f55";
    private static final String KEYWORD_LOGOUT = "\u6ce8\u9500";
    private static final String KEYWORD_REGISTER = "\u6ce8\u518c";
    private static final String KEYWORD_PERMISSION = "\u6743\u9650";
    private static final String KEYWORD_OVER_PERMISSION = "\u8d8a\u6743";
    private static final String KEYWORD_UNAUTHORIZED = "\u672a\u6388\u6743";
    private static final String KEYWORD_ROLE = "\u89d2\u8272";
    private static final String KEYWORD_MENU = "\u83dc\u5355";
    private static final String KEYWORD_GRANT = "\u6388\u6743";
    private static final String KEYWORD_CONFIG = "\u914d\u7f6e";
    private static final String KEYWORD_PARAM = "\u53c2\u6570";
    private static final String KEYWORD_LAYER = "\u56fe\u5c42";
    private static final String KEYWORD_MACHINE_LEARNING = "\u673a\u5668\u5b66\u4e60";
    private static final String KEYWORD_DICT = "\u5b57\u5178";
    private static final String KEYWORD_EXPORT = "\u5bfc\u51fa";
    private static final String KEYWORD_DOWNLOAD = "\u4e0b\u8f7d";
    private static final String KEYWORD_EXPERIMENT = "\u8bd5\u9a8c";
    private static final String KEYWORD_BIZ_DATA = "\u4e1a\u52a1\u6570\u636e";
    private static final String KEYWORD_SENSITIVE = "\u654f\u611f";
    private static final String KEYWORD_NON_INTERNAL_IP = "\u975e\u5185\u7f51";
    private static final String KEYWORD_UNKNOWN_ADDRESS = "\u672a\u77e5\u5730\u5740";
    private static final String KEYWORD_BUSINESS = "\u4e1a\u52a1";

    private static final int DEFAULT_RISK_LEVEL = 1;
    private static final int NIGHT_START_HOUR = 22;
    private static final int NIGHT_END_HOUR = 6;
    private static final int LOGIN_FAILURE_THRESHOLD = 3;

    private AuditLogTagSupport()
    {
    }

    public static void tagLoginInfo(SysLogininfor logininfor, Integer failureCount)
    {
        if (logininfor == null)
        {
            return;
        }

        logininfor.setBizCategory(BIZ_CATEGORY_LOGIN);
        promote(logininfor, EVENT_TYPE_NORMAL, DEFAULT_RISK_LEVEL, null);

        if (!Constants.SUCCESS.equals(logininfor.getStatus()))
        {
            promote(logininfor, EVENT_TYPE_ABNORMAL, 2, null);
        }

        if (failureCount != null && failureCount >= LOGIN_FAILURE_THRESHOLD)
        {
            promote(logininfor, EVENT_TYPE_ABNORMAL, 3, TAG_FREQUENT_FAILURE);
        }

        if (Constants.SUCCESS.equals(logininfor.getStatus()) && isNightHour())
        {
            promote(logininfor, EVENT_TYPE_ABNORMAL, 2, TAG_NIGHT_LOGIN);
        }

        if (hasAbnormalIp(logininfor.getIpaddr(), logininfor.getLoginLocation(), logininfor.getMsg()))
        {
            promote(logininfor, EVENT_TYPE_ABNORMAL, 2, TAG_ABNORMAL_IP);
        }
    }

    public static void tagLoginOperLog(SysOperLog operLog, String loginStatus, Integer failureCount)
    {
        if (operLog == null)
        {
            return;
        }

        operLog.setBizCategory(BIZ_CATEGORY_LOGIN);
        promote(operLog, EVENT_TYPE_NORMAL, DEFAULT_RISK_LEVEL, null);

        if (!Constants.LOGIN_SUCCESS.equals(loginStatus)
                && !Constants.LOGOUT.equals(loginStatus)
                && !Constants.REGISTER.equals(loginStatus))
        {
            promote(operLog, EVENT_TYPE_ABNORMAL, 2, null);
        }

        if (failureCount != null && failureCount >= LOGIN_FAILURE_THRESHOLD)
        {
            promote(operLog, EVENT_TYPE_ABNORMAL, 3, TAG_FREQUENT_FAILURE);
        }

        if (Constants.LOGIN_SUCCESS.equals(loginStatus) && isNightHour() && !IpUtils.internalIp(operLog.getOperIp()))
        {
            promote(operLog, EVENT_TYPE_ABNORMAL, 2, TAG_NIGHT_LOGIN);
        }

        if (hasAbnormalIp(operLog.getOperIp(), operLog.getOperLocation(), operLog.getErrorMsg()))
        {
            promote(operLog, EVENT_TYPE_ABNORMAL, 2, TAG_ABNORMAL_IP);
        }
    }

    public static void tagConcurrentLogin(SysOperLog operLog)
    {
        if (operLog == null)
        {
            return;
        }
        promote(operLog, EVENT_TYPE_VIOLATION, 4, TAG_CONCURRENT_LOGIN);
    }

    public static void tagConcurrentLogin(SysLogininfor logininfor)
    {
        if (logininfor == null)
        {
            return;
        }
        promote(logininfor, EVENT_TYPE_VIOLATION, 4, TAG_CONCURRENT_LOGIN);
    }

    public static void tagOperLog(SysOperLog operLog, LoginUser loginUser)
    {
        if (operLog == null)
        {
            return;
        }

        promote(operLog, EVENT_TYPE_NORMAL, DEFAULT_RISK_LEVEL, null);
        String bizCategory = resolveBizCategory(operLog);
        if (StringUtils.isNotEmpty(bizCategory))
        {
            operLog.setBizCategory(bizCategory);
        }

        if (isFailure(operLog))
        {
            promote(operLog, EVENT_TYPE_ABNORMAL, 2, null);
        }

        if (StringUtils.equals(BIZ_CATEGORY_PERMISSION, operLog.getBizCategory()) && looksLikePermissionViolation(operLog))
        {
            promote(operLog, EVENT_TYPE_VIOLATION, 4, TAG_OVER_PERMISSION);
        }

        if (StringUtils.equals(BIZ_CATEGORY_EXPORT, operLog.getBizCategory()) && isSensitiveResource(operLog))
        {
            promote(operLog, EVENT_TYPE_ABNORMAL, 2, TAG_SENSITIVE_ACCESS);
            if (!isAdmin(loginUser) && !hasBusinessRole(loginUser))
            {
                promote(operLog, EVENT_TYPE_VIOLATION, 3, TAG_BATCH_EXPORT);
            }
        }

        if (StringUtils.equals(BIZ_CATEGORY_CONFIG, operLog.getBizCategory()))
        {
            promote(operLog, EVENT_TYPE_NORMAL, 2, null);
        }
    }

    public static SysOperLog buildAccessDeniedOperLog(HttpServletRequest request, LoginUser loginUser, String permission,
            String errorMsg)
    {
        SysOperLog operLog = new SysOperLog();
        operLog.setTitle("\u7ba1\u7406\u63a5\u53e3\u8bbf\u95ee");
        operLog.setBusinessType(BusinessType.GRANT.ordinal());
        operLog.setMethod(StringUtils.defaultIfBlank(permission, "permission:unknown"));
        operLog.setRequestMethod(request.getMethod());
        operLog.setOperatorType(OperatorType.MANAGE.ordinal());
        operLog.setOperUrl(StringUtils.substring(request.getRequestURI(), 0, 255));
        operLog.setOperIp(IpUtils.getIpAddr(request));
        operLog.setOperParam(StringUtils.substring("requiredPermission=" + StringUtils.defaultString(permission), 0, 2000));
        operLog.setStatus(BusinessStatus.FAIL.ordinal());
        operLog.setErrorMsg(StringUtils.substring(StringUtils.defaultString(errorMsg), 0, 2000));
        operLog.setOperTime(new Date());
        operLog.setCostTime(0L);
        if (loginUser != null)
        {
            operLog.setOperName(loginUser.getUsername());
            if (loginUser.getUser() != null)
            {
                operLog.setDeptName(loginUser.getUser().getNickName());
            }
        }
        promote(operLog, EVENT_TYPE_VIOLATION, 4, TAG_OVER_PERMISSION);
        operLog.setBizCategory(BIZ_CATEGORY_PERMISSION);
        return operLog;
    }

    private static boolean isFailure(SysOperLog operLog)
    {
        return operLog.getStatus() != null && operLog.getStatus().intValue() == BusinessStatus.FAIL.ordinal();
    }

    private static boolean looksLikePermissionViolation(SysOperLog operLog)
    {
        String text = mergeText(operLog.getTitle(), operLog.getMethod(), operLog.getOperUrl(), operLog.getErrorMsg());
        return StringUtils.containsAnyIgnoreCase(text, "denied", "forbidden", "unauthorized", "access", "permission",
                KEYWORD_PERMISSION, KEYWORD_OVER_PERMISSION, KEYWORD_UNAUTHORIZED);
    }

    private static boolean hasAbnormalIp(String ip, String location, String message)
    {
        if (StringUtils.containsAnyIgnoreCase(StringUtils.defaultString(message), KEYWORD_NON_INTERNAL_IP, TAG_ABNORMAL_IP,
                "unknown", KEYWORD_UNKNOWN_ADDRESS))
        {
            return true;
        }
        return StringUtils.isBlank(ip)
                || StringUtils.isBlank(location)
                || StringUtils.equalsIgnoreCase(location, "unknown")
                || StringUtils.containsAnyIgnoreCase(location, "\u672a\u77e5");
    }

    private static String resolveBizCategory(SysOperLog operLog)
    {
        String text = mergeText(operLog.getTitle(), operLog.getMethod(), operLog.getOperUrl(), operLog.getOperParam(),
                operLog.getRequestMethod());
        if (StringUtils.containsAnyIgnoreCase(text, "login", "logout", "register", KEYWORD_LOGIN, KEYWORD_LOGOUT,
                KEYWORD_REGISTER))
        {
            return BIZ_CATEGORY_LOGIN;
        }
        if (isExportAction(operLog, text))
        {
            return BIZ_CATEGORY_EXPORT;
        }
        if (operLog.getBusinessType() != null && operLog.getBusinessType().intValue() == BusinessType.GRANT.ordinal())
        {
            return BIZ_CATEGORY_PERMISSION;
        }
        if (StringUtils.containsAnyIgnoreCase(text, "grant", "role", "menu", "auth", "perm", "permission", KEYWORD_ROLE,
                KEYWORD_MENU, KEYWORD_PERMISSION, KEYWORD_GRANT))
        {
            return BIZ_CATEGORY_PERMISSION;
        }
        if (StringUtils.containsAnyIgnoreCase(text, "config", "configuration", "layer", "machinelearning", "dict",
                KEYWORD_CONFIG, KEYWORD_PARAM, KEYWORD_LAYER, KEYWORD_MACHINE_LEARNING, KEYWORD_DICT))
        {
            return BIZ_CATEGORY_CONFIG;
        }
        if (operLog.getBusinessType() != null && isMutationBusinessType(operLog.getBusinessType()))
        {
            return BIZ_CATEGORY_CONFIG;
        }
        return StringUtils.defaultString(operLog.getBizCategory());
    }

    private static boolean isExportAction(SysOperLog operLog, String text)
    {
        return operLog.getBusinessType() != null && operLog.getBusinessType().intValue() == BusinessType.EXPORT.ordinal()
                || StringUtils.containsAnyIgnoreCase(text, "export", "download", KEYWORD_EXPORT, KEYWORD_DOWNLOAD);
    }

    private static boolean isSensitiveResource(SysOperLog operLog)
    {
        String text = mergeText(operLog.getTitle(), operLog.getMethod(), operLog.getOperUrl(), operLog.getOperParam(),
                operLog.getJsonResult());
        return StringUtils.containsAnyIgnoreCase(text, "experiment", "businessdata", "sensitive", "secret", "token",
                "password", KEYWORD_EXPERIMENT, KEYWORD_BIZ_DATA, KEYWORD_SENSITIVE, KEYWORD_EXPORT, KEYWORD_DOWNLOAD);
    }

    private static boolean isMutationBusinessType(Integer businessType)
    {
        int code = businessType.intValue();
        return code == BusinessType.INSERT.ordinal()
                || code == BusinessType.UPDATE.ordinal()
                || code == BusinessType.DELETE.ordinal()
                || code == BusinessType.CLEAN.ordinal();
    }

    private static boolean isAdmin(LoginUser loginUser)
    {
        return loginUser != null && loginUser.getUser() != null && loginUser.getUser().isAdmin();
    }

    private static boolean hasBusinessRole(LoginUser loginUser)
    {
        if (loginUser == null || loginUser.getUser() == null || loginUser.getUser().getRoles() == null)
        {
            return false;
        }
        for (SysRole role : loginUser.getUser().getRoles())
        {
            String roleText = mergeText(role.getRoleKey(), role.getRoleName()).toLowerCase(Locale.ROOT);
            if (roleText.contains("business") || roleText.contains("sales") || roleText.contains(KEYWORD_BUSINESS))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isNightHour()
    {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour >= NIGHT_START_HOUR || hour < NIGHT_END_HOUR;
    }

    private static void promote(SysOperLog operLog, String eventType, int riskLevel, String tag)
    {
        operLog.setEventType(selectEventType(operLog.getEventType(), eventType));
        operLog.setRiskLevel(Math.max(defaultRisk(operLog.getRiskLevel()), riskLevel));
        operLog.setHighlightTag(appendTag(operLog.getHighlightTag(), tag));
    }

    private static void promote(SysLogininfor logininfor, String eventType, int riskLevel, String tag)
    {
        logininfor.setEventType(selectEventType(logininfor.getEventType(), eventType));
        logininfor.setRiskLevel(Math.max(defaultRisk(logininfor.getRiskLevel()), riskLevel));
        logininfor.setHighlightTag(appendTag(logininfor.getHighlightTag(), tag));
    }

    private static int defaultRisk(Integer riskLevel)
    {
        return riskLevel == null ? DEFAULT_RISK_LEVEL : riskLevel.intValue();
    }

    private static String selectEventType(String current, String candidate)
    {
        return eventPriority(candidate) > eventPriority(current) ? candidate : StringUtils.defaultIfBlank(current, candidate);
    }

    private static int eventPriority(String eventType)
    {
        if (StringUtils.equals(EVENT_TYPE_VIOLATION, eventType))
        {
            return 3;
        }
        if (StringUtils.equals(EVENT_TYPE_ABNORMAL, eventType))
        {
            return 2;
        }
        if (StringUtils.equals(EVENT_TYPE_NORMAL, eventType))
        {
            return 1;
        }
        return 0;
    }

    private static String appendTag(String existing, String tag)
    {
        if (StringUtils.isBlank(tag))
        {
            return existing;
        }
        Set<String> tags = new LinkedHashSet<String>();
        if (StringUtils.isNotBlank(existing))
        {
            for (String item : existing.split(","))
            {
                if (StringUtils.isNotBlank(item))
                {
                    tags.add(item.trim());
                }
            }
        }
        tags.add(tag);
        return StringUtils.join(tags, ",");
    }

    private static String mergeText(String... texts)
    {
        StringBuilder builder = new StringBuilder();
        for (String text : texts)
        {
            if (StringUtils.isNotBlank(text))
            {
                if (builder.length() > 0)
                {
                    builder.append(' ');
                }
                builder.append(text);
            }
        }
        return builder.toString();
    }
}
