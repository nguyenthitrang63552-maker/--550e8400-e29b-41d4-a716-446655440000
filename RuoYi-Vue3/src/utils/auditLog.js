export const auditEventTypeOptions = [
  { value: "normal", label: "普通" },
  { value: "abnormal", label: "异常" },
  { value: "violation", label: "违规" }
]

export const auditRiskLevelOptions = [
  { value: 1, label: "1级" },
  { value: 2, label: "2级" },
  { value: 3, label: "3级" },
  { value: 4, label: "4级" }
]

export const auditBizCategoryOptions = [
  { value: "登录", label: "登录" },
  { value: "权限", label: "权限" },
  { value: "数据导出", label: "数据导出" },
  { value: "配置变更", label: "配置变更" }
]

export const auditHighlightTagOptions = [
  { value: "越权", label: "越权" },
  { value: "批量导出", label: "批量导出" },
  { value: "敏感访问", label: "敏感访问" },
  { value: "夜间登录", label: "夜间登录" },
  { value: "频繁失败", label: "频繁失败" },
  { value: "异常IP", label: "异常IP" }
]

const auditEventTypeTagTypes = {
  normal: "success",
  abnormal: "warning",
  violation: "danger"
}

export function getAuditOptionLabel(options, value) {
  if (value === undefined || value === null || value === "") {
    return ""
  }
  const option = options.find(item => item.value === value)
  return option ? option.label : String(value)
}

export function getAuditEventTypeTagType(value) {
  return auditEventTypeTagTypes[value] || "info"
}

export function getAuditRiskLevelLabel(value) {
  if (value === undefined || value === null || value === "") {
    return ""
  }
  return `${value}级`
}

export function getAuditRiskLevelTagType(value) {
  const level = Number(value)
  if (!Number.isFinite(level) || level <= 1) {
    return "info"
  }
  if (level === 2) {
    return "warning"
  }
  return "danger"
}

export function splitAuditHighlightTags(value) {
  return String(value || "")
    .split(",")
    .map(item => item.trim())
    .filter(Boolean)
}
