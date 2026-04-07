<template>
  <div class="app-container system-log-page">
    <div class="system-log-shell">
      <el-card class="system-log-files" shadow="never">
        <template #header>
          <div class="panel-header">
            <div>
              <div class="panel-title">系统日志浏览</div>
              <div class="panel-subtitle">按文件筛选、按级别聚合、按内容快速定位问题</div>
            </div>
            <div class="panel-actions">
              <el-input
                v-model="fileKeyword"
                clearable
                class="panel-search"
                placeholder="搜索日志文件"
              />
              <el-button icon="Refresh" @click="getList">刷新列表</el-button>
            </div>
          </div>
        </template>

        <div class="overview-grid">
          <div
            v-for="item in fileOverviewCards"
            :key="item.label"
            class="overview-card"
            :class="{ 'overview-card--action': item.clickable }"
            @click="handleOverviewCardClick(item)"
          >
            <span class="overview-card__label">{{ item.label }}</span>
            <strong class="overview-card__value">{{ item.value }}</strong>
            <span class="overview-card__hint">{{ item.hint }}</span>
          </div>
        </div>

        <div class="file-filter-bar">
          <el-radio-group v-model="fileCategoryFilter" size="small" @change="handleCategoryChange">
            <el-radio-button
              v-for="item in fileCategoryFilters"
              :key="item.value"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio-button>
          </el-radio-group>
          <span class="file-filter-count">共 {{ filteredFileList.length }} 个文件</span>
        </div>

        <div v-if="loading" class="panel-state">
          <el-skeleton :rows="6" animated />
        </div>
        <el-empty v-else-if="filteredFileList.length === 0" description="没有匹配的日志文件" />
        <div v-else class="file-list">
          <div
            v-for="row in filteredFileList"
            :key="getFileName(row)"
            class="file-item"
            :class="{ 'is-active': activeFileName === getFileName(row) }"
            @click="handlePreview(row)"
          >
            <div class="file-item__body">
              <div class="file-item__title-row">
                <div class="file-item__title-wrap">
                  <span class="file-item__title">{{ getFileName(row) }}</span>
                  <el-tag
                    size="small"
                    effect="plain"
                    :type="getFileCategoryMeta(getFileName(row)).tagType"
                  >
                    {{ getFileCategoryMeta(getFileName(row)).label }}
                  </el-tag>
                  <el-tag
                    v-if="activeFileName === getFileName(row)"
                    size="small"
                    effect="plain"
                    type="primary"
                  >
                    预览中
                  </el-tag>
                </div>
              </div>
              <div class="file-item__summary">
                {{ buildFileSummary(getFileName(row)) }}
              </div>
            </div>

            <div class="file-item__actions" @click.stop>
              <el-button link type="primary" @click="handlePreview(row, true)">预览</el-button>
              <el-button link type="info" @click="handleDownload(row)">下载</el-button>
              <el-dropdown trigger="click">
                <el-button link type="success">
                  导出
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleExportWord(row)">导出 Word</el-dropdown-item>
                    <el-dropdown-item @click="handleExportPdf(row)">导出 PDF</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>
      </el-card>

      <el-card class="system-log-preview" shadow="never">
        <template #header>
          <div class="panel-header panel-header--preview">
            <div>
              <div class="panel-title">{{ activeFileName || "日志预览" }}</div>
              <div class="panel-subtitle">
                {{ activeFileName ? "结构化查看日志明细，快速筛选异常与关键字" : "从左侧选择一个日志文件开始浏览" }}
              </div>
            </div>
            <div v-if="activeFileName" class="panel-actions">
              <el-button @click="refreshActivePreview">重新加载</el-button>
              <el-button type="primary" plain @click="copyCurrentPreview">复制当前视图</el-button>
            </div>
          </div>
        </template>

        <div v-if="previewLoading" class="panel-state">
          <el-skeleton :rows="10" animated />
        </div>
        <el-empty
          v-else-if="!activeFileName"
          description="选择左侧日志文件后，会在这里显示结构化预览"
        />
        <div v-else class="preview-panel">
          <div class="overview-grid overview-grid--preview">
            <div
              v-for="item in previewOverviewCards"
              :key="item.label"
              class="overview-card"
            >
              <span class="overview-card__label">{{ item.label }}</span>
              <strong class="overview-card__value">{{ item.value }}</strong>
              <span class="overview-card__hint">{{ item.hint }}</span>
            </div>
          </div>

          <div class="preview-toolbar">
            <el-input
              v-model="previewKeyword"
              clearable
              class="preview-toolbar__search"
              placeholder="搜索日志内容、类名、线程名"
            />
            <el-select v-model="previewLevelFilter" class="preview-toolbar__level" placeholder="日志级别">
              <el-option
                v-for="item in previewLevelOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-switch
              v-model="wrapLines"
              inline-prompt
              active-text="换行"
              inactive-text="单行"
            />
          </div>

          <el-alert
            v-if="previewError"
            type="error"
            :closable="false"
            show-icon
            :title="previewError"
            class="preview-alert"
          />
          <el-alert
            v-else-if="isPreviewTruncated"
            type="info"
            :closable="false"
            show-icon
            class="preview-alert"
            :title="`当前匹配 ${filteredPreviewItems.length} 行，为保证浏览体验，仅渲染前 ${MAX_RENDERED_LOG_LINES} 行。请继续缩小筛选范围。`"
          />

          <el-empty
            v-if="!previewError && filteredPreviewItems.length === 0"
            description="没有匹配的日志内容"
          />
          <div
            v-else-if="!previewError"
            class="preview-list"
            :class="{ 'is-nowrap': !wrapLines }"
          >
            <div
              v-for="item in renderedPreviewItems"
              :key="`${activeFileName}-${item.lineNumber}`"
              class="preview-line"
              :class="[`is-${item.levelClass}`, { 'is-continuation': item.isContinuation }]"
            >
              <div class="preview-line__meta">
                <span class="preview-line__number">{{ item.lineNumber }}</span>
                <el-tag
                  size="small"
                  effect="plain"
                  :type="item.tagType"
                  class="preview-line__level"
                >
                  {{ item.levelLabel }}
                </el-tag>
                <span v-if="item.timestamp" class="preview-line__time">{{ item.timestamp }}</span>
                <span v-if="item.source" class="preview-line__source">{{ item.source }}</span>
                <span v-if="item.thread" class="preview-line__thread">#{{ item.thread }}</span>
              </div>
              <pre class="preview-line__content">{{ item.displayMessage }}</pre>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog
      v-model="guideDialogOpen"
      title="日志说明"
      width="min(760px, calc(100vw - 32px))"
      append-to-body
      destroy-on-close
      class="log-guide-dialog"
    >
      <div class="log-guide log-guide--dialog">
        <div class="log-guide__header">
          <span class="log-guide__title">日志说明</span>
          <span class="log-guide__subtitle">帮助用户快速理解不同日志文件的职责和命名方式</span>
        </div>
        <div class="log-guide__grid">
          <div
            v-for="item in logGuideCards"
            :key="item.title"
            class="log-guide-card"
          >
            <div class="log-guide-card__head">
              <span class="log-guide-card__title">{{ item.title }}</span>
              <el-tag size="small" effect="plain" :type="item.tagType">
                {{ item.tag }}
              </el-tag>
            </div>
            <div class="log-guide-card__desc">{{ item.description }}</div>
            <div class="log-guide-card__rule">
              <span class="log-guide-card__rule-label">命名规则</span>
              <code class="log-guide-card__rule-value">{{ item.pattern }}</code>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { saveAs } from 'file-saver'
import { blobValidate } from '@/utils/ruoyi'
import { listSysLogs, previewLog, downloadLog, downloadLogWord, downloadLogPdf } from '@/api/monitor/sysLog'

const MAX_RENDERED_LOG_LINES = 600
const LOG_LEVELS = ["ERROR", "WARN", "INFO", "DEBUG", "TRACE", "FATAL", "PLAIN"]

const loading = ref(false)
const previewLoading = ref(false)
const fileList = ref([])
const fileKeyword = ref("")
const fileCategoryFilter = ref("all")
const guideDialogOpen = ref(false)
const activeFileName = ref("")
const previewRows = ref([])
const previewTotal = ref(0)
const previewKeyword = ref("")
const previewLevelFilter = ref("ALL")
const previewError = ref("")
const wrapLines = ref(true)

const fileCategoryFilters = [
  { value: "all", label: "全部文件" },
  { value: "info", label: "系统运行日志" },
  { value: "error", label: "错误异常日志" },
  { value: "user", label: "用户访问日志" }
]

const logGuideCards = [
  {
    title: "sys-info.log",
    tag: "系统运行",
    tagType: "info",
    description: "记录系统常规运行信息、业务流程输出和普通提示，适合先看整体运行状态。",
    pattern: "活动文件：sys-info.log；按天归档：sys-info.YYYY-MM-DD.log"
  },
  {
    title: "sys-error.log",
    tag: "错误异常",
    tagType: "danger",
    description: "记录 ERROR 级别异常、报错堆栈和关键故障，排查问题时建议优先关注。",
    pattern: "活动文件：sys-error.log；按天归档：sys-error.YYYY-MM-DD.log"
  },
  {
    title: "sys-user.log",
    tag: "用户访问",
    tagType: "warning",
    description: "记录登录、退出、用户访问链路和相关审计侧文件日志，也可能出现该链路的异步异常。",
    pattern: "活动文件：sys-user.log；按天归档：sys-user.YYYY-MM-DD.log"
  },
  {
    title: "文件命名约定",
    tag: "阅读提示",
    tagType: "success",
    description: "不带日期的是当前正在写入的活动日志；带日期后缀的是历史归档日志，通常按天切分。",
    pattern: "示例：sys-info.log / sys-info.2026-04-01.log"
  }
]

const filteredFileList = computed(() => {
  const keyword = fileKeyword.value.trim().toLowerCase()
  return fileList.value.filter((row) => {
    const fileName = getFileName(row)
    const matchesKeyword = !keyword || fileName.toLowerCase().includes(keyword)
    const matchesCategory = matchesFileFilter(fileName, fileCategoryFilter.value)
    return matchesKeyword && matchesCategory
  })
})

const fileOverviewCards = computed(() => {
  const totalFiles = fileList.value.length
  const infoFiles = fileList.value.filter((item) => getFileCategoryMeta(getFileName(item)).key === "info").length
  const errorFiles = fileList.value.filter((item) => getFileCategoryMeta(getFileName(item)).key === "error").length
  const userFiles = fileList.value.filter((item) => getFileCategoryMeta(getFileName(item)).key === "user").length
  return [
    {
      label: "日志说明",
      value: "点击查看",
      hint: "了解 sys-info、sys-error、sys-user 的职责和命名规则",
      clickable: true,
      action: "guide"
    },
    { label: "日志文件数", value: totalFiles, hint: "当前目录可浏览的日志文件总数" },
    { label: "系统运行类", value: infoFiles, hint: "sys-info 当前文件与按天归档文件" },
    { label: "错误类文件", value: errorFiles, hint: "优先关注可能包含异常堆栈的文件" },
    { label: "用户访问类", value: userFiles, hint: "sys-user 登录与访问链路相关文件" },
    { label: "当前预览", value: activeFileName.value || "--", hint: activeFileName.value ? "右侧已载入该文件内容" : "点击左侧文件后开始预览" }
  ]
})

const parsedPreviewItems = computed(() => {
  const items = []
  let lastDetectedLevel = "PLAIN"
  previewRows.value.forEach((line, index) => {
    const parsed = parseLogLine(line, index + 1, lastDetectedLevel)
    if (!parsed.isContinuation) {
      lastDetectedLevel = parsed.level
    }
    items.push(parsed)
  })
  return items
})

const previewLevelOptions = computed(() => {
  const counts = buildLevelCountMap(parsedPreviewItems.value)
  return [
    { value: "ALL", label: `全部级别 (${parsedPreviewItems.value.length})` },
    ...LOG_LEVELS.map((level) => ({
      value: level,
      label: `${getLevelLabel(level)} (${counts[level] || 0})`
    }))
  ]
})

const filteredPreviewItems = computed(() => {
  const keyword = previewKeyword.value.trim().toLowerCase()
  return parsedPreviewItems.value.filter((item) => {
    const matchesLevel = previewLevelFilter.value === "ALL" || item.level === previewLevelFilter.value
    const haystack = [
      item.raw,
      item.displayMessage,
      item.source,
      item.thread,
      item.timestamp
    ].filter(Boolean).join(" ").toLowerCase()
    const matchesKeyword = !keyword || haystack.includes(keyword)
    return matchesLevel && matchesKeyword
  })
})

const renderedPreviewItems = computed(() => filteredPreviewItems.value.slice(0, MAX_RENDERED_LOG_LINES))

const isPreviewTruncated = computed(() => filteredPreviewItems.value.length > MAX_RENDERED_LOG_LINES)

const previewOverviewCards = computed(() => {
  const counts = buildLevelCountMap(parsedPreviewItems.value)
  return [
    { label: "总行数", value: previewTotal.value || parsedPreviewItems.value.length, hint: "文件中的原始日志行数" },
    { label: "错误级别", value: (counts.ERROR || 0) + (counts.FATAL || 0), hint: "优先处理的异常与致命错误" },
    { label: "告警级别", value: counts.WARN || 0, hint: "可能需要跟进的风险提示" },
    { label: "当前匹配", value: filteredPreviewItems.value.length, hint: "受搜索词和级别筛选影响" }
  ]
})

onMounted(() => {
  getList()
})

function getList() {
  loading.value = true
  listSysLogs()
    .then((res) => {
      const rows = Array.isArray(res.rows) ? res.rows : []
      fileList.value = rows
        .map((item) => normalizeFileRow(item))
        .sort((a, b) => getFileName(b).localeCompare(getFileName(a), "zh-Hans-CN", { numeric: true, sensitivity: "base" }))

      const firstVisibleFile = filteredFileList.value[0] || fileList.value[0]
      if (!activeFileName.value && firstVisibleFile) {
        handlePreview(firstVisibleFile, true)
        return
      }

      if (activeFileName.value) {
        const exists = fileList.value.some((item) => getFileName(item) === activeFileName.value)
        if (!exists) {
          if (firstVisibleFile) {
            handlePreview(firstVisibleFile, true)
          }
          else {
            clearPreviewState()
          }
        }
      }
    })
    .catch(() => {
      ElMessage.error('获取日志文件列表失败')
    })
    .finally(() => {
      loading.value = false
    })
}

function normalizeFileRow(row) {
  if (typeof row === "object" && row !== null) {
    return row
  }
  return { fileName: String(row || "") }
}

function getFileName(row) {
  return row?.fileName || row?.name || String(row || "")
}

function matchesFileFilter(fileName, filterKey) {
  if (filterKey === "all") {
    return true
  }
  return getFileCategoryMeta(fileName).key === filterKey
}

function getFileCategoryMeta(fileName) {
  const lowerName = String(fileName || "").toLowerCase()
  if (lowerName.startsWith("sys-info")) {
    return { key: "info", label: "系统运行日志", tagType: "info" }
  }
  if (lowerName.startsWith("sys-error") || lowerName.includes("exception")) {
    return { key: "error", label: "错误异常日志", tagType: "danger" }
  }
  if (lowerName.startsWith("sys-user") || lowerName.includes("login") || lowerName.includes("auth")) {
    return { key: "user", label: "用户访问日志", tagType: "warning" }
  }
  if (lowerName.includes("error")) {
    return { key: "error", label: "错误异常日志", tagType: "danger" }
  }
  return { key: "other", label: "其他日志", tagType: "success" }
}

function buildFileSummary(fileName) {
  const meta = getFileCategoryMeta(fileName)
  if (meta.key === "info") {
    return "用于查看系统运行轨迹、业务输出和服务状态变化。"
  }
  if (meta.key === "error") {
    return "适合优先排查报错堆栈、启动失败和运行时异常。"
  }
  if (meta.key === "user") {
    return "更适合排查登录、鉴权、访问来源和接口调用轨迹。"
  }
  return "当前文件不属于 sys-info、sys-error、sys-user 三类标准日志，请按文件名进一步判断。"
}

function clearPreviewState() {
  activeFileName.value = ""
  previewRows.value = []
  previewTotal.value = 0
  previewKeyword.value = ""
  previewLevelFilter.value = "ALL"
  previewError.value = ""
}

function handlePreview(row, forceReload = false) {
  const fileName = getFileName(row)
  if (!fileName) {
    return
  }
  if (!forceReload && activeFileName.value === fileName && previewRows.value.length > 0) {
    return
  }

  activeFileName.value = fileName
  previewError.value = ""
  previewLoading.value = true
  previewLog(fileName)
    .then((res) => {
      const payload = res.data || {}
      const rows = Array.isArray(payload.rows) ? payload.rows : []
      previewRows.value = rows.map((line) => String(line ?? ""))
      previewTotal.value = Number(payload.total ?? rows.length)
      previewKeyword.value = ""
      previewLevelFilter.value = "ALL"
    })
    .catch(() => {
      previewRows.value = []
      previewTotal.value = 0
      previewError.value = "日志预览失败，请稍后重试。"
      ElMessage.error('预览失败')
    })
    .finally(() => {
      previewLoading.value = false
    })
}

function refreshActivePreview() {
  if (!activeFileName.value) {
    return
  }
  handlePreview({ fileName: activeFileName.value }, true)
}

function handleCategoryChange() {
  const activeStillVisible = filteredFileList.value.some((item) => getFileName(item) === activeFileName.value)
  if (activeStillVisible) {
    return
  }

  const firstVisibleFile = filteredFileList.value[0]
  if (firstVisibleFile) {
    handlePreview(firstVisibleFile, true)
  }
  else {
    clearPreviewState()
  }
}

function handleOverviewCardClick(item) {
  if (item?.action === "guide") {
    guideDialogOpen.value = true
  }
}

function normalizeLogLevel(level) {
  const normalized = String(level || "").toUpperCase()
  return LOG_LEVELS.includes(normalized) ? normalized : "PLAIN"
}

function parseLogLine(line, lineNumber, fallbackLevel = "PLAIN") {
  const raw = String(line ?? "")
  const springPattern = /^(\d{4}-\d{2}-\d{2}[ T]\d{2}:\d{2}:\d{2}(?:[.,]\d{3})?)\s+(TRACE|DEBUG|INFO|WARN|ERROR|FATAL)\s+\d+\s+---\s+\[([^\]]*)\]\s+([^:]+)\s+:\s+(.*)$/
  const bracketPattern = /^(\d{4}-\d{2}-\d{2}[ T]\d{2}:\d{2}:\d{2}(?:[.,]\d{3})?)\s+\[([^\]]+)\]\s+(TRACE|DEBUG|INFO|WARN|ERROR|FATAL)\s+([^:]+?)\s*[-:]\s*(.*)$/
  const simplePattern = /^(\d{4}-\d{2}-\d{2}[ T]\d{2}:\d{2}:\d{2}(?:[.,]\d{3})?)\s+(TRACE|DEBUG|INFO|WARN|ERROR|FATAL)\s+(.*)$/

  let match = raw.match(springPattern)
  if (match) {
    return buildPreviewItem({
      lineNumber,
      raw,
      timestamp: match[1],
      level: match[2],
      thread: match[3],
      source: match[4],
      displayMessage: match[5]
    })
  }

  match = raw.match(bracketPattern)
  if (match) {
    return buildPreviewItem({
      lineNumber,
      raw,
      timestamp: match[1],
      thread: match[2],
      level: match[3],
      source: match[4],
      displayMessage: match[5]
    })
  }

  match = raw.match(simplePattern)
  if (match) {
    return buildPreviewItem({
      lineNumber,
      raw,
      timestamp: match[1],
      level: match[2],
      displayMessage: match[3]
    })
  }

  const looseLevelMatch = raw.match(/\b(TRACE|DEBUG|INFO|WARN|ERROR|FATAL)\b/)
  if (looseLevelMatch) {
    return buildPreviewItem({
      lineNumber,
      raw,
      level: looseLevelMatch[1],
      displayMessage: raw
    })
  }

  const isContinuation = /^\s+at\s+/.test(raw)
    || /^\s*Caused by:/.test(raw)
    || /^\s*\.\.\.\s+\d+\s+more/.test(raw)

  return buildPreviewItem({
    lineNumber,
    raw,
    level: isContinuation ? fallbackLevel : "PLAIN",
    displayMessage: raw,
    isContinuation
  })
}

function buildPreviewItem(item) {
  const level = normalizeLogLevel(item.level)
  return {
    lineNumber: item.lineNumber,
    raw: item.raw,
    timestamp: item.timestamp || "",
    source: item.source ? String(item.source).trim() : "",
    thread: item.thread ? String(item.thread).trim() : "",
    level,
    levelLabel: getLevelLabel(level),
    levelClass: getLevelClass(level),
    tagType: getLevelTagType(level),
    displayMessage: String(item.displayMessage ?? item.raw ?? ""),
    isContinuation: !!item.isContinuation
  }
}

function getLevelLabel(level) {
  const labelMap = {
    ERROR: "ERROR",
    WARN: "WARN",
    INFO: "INFO",
    DEBUG: "DEBUG",
    TRACE: "TRACE",
    FATAL: "FATAL",
    PLAIN: "TEXT"
  }
  return labelMap[level] || "TEXT"
}

function getLevelClass(level) {
  if (level === "ERROR" || level === "FATAL") {
    return "danger"
  }
  if (level === "WARN") {
    return "warning"
  }
  if (level === "INFO") {
    return "success"
  }
  if (level === "DEBUG" || level === "TRACE") {
    return "info"
  }
  return "plain"
}

function getLevelTagType(level) {
  if (level === "ERROR" || level === "FATAL") {
    return "danger"
  }
  if (level === "WARN") {
    return "warning"
  }
  if (level === "INFO") {
    return "success"
  }
  return "info"
}

function buildLevelCountMap(rows) {
  return rows.reduce((accumulator, item) => {
    accumulator[item.level] = (accumulator[item.level] || 0) + 1
    return accumulator
  }, {})
}

function copyCurrentPreview() {
  if (renderedPreviewItems.value.length === 0) {
    ElMessage.warning("当前没有可复制的日志内容")
    return
  }
  const text = renderedPreviewItems.value
    .map((item) => `[${item.lineNumber}] ${item.raw}`)
    .join("\n")

  navigator.clipboard.writeText(text)
    .then(() => {
      ElMessage.success("当前视图已复制到剪贴板")
    })
    .catch(() => {
      ElMessage.error("复制失败，请检查浏览器权限")
    })
}

function handleDownload(row) {
  const fileName = getFileName(row)
  downloadBinary(downloadLog(fileName), fileName, '下载失败')
}

function handleExportWord(row) {
  const fileName = getFileName(row)
  downloadBinary(downloadLogWord(fileName), buildExportFileName(fileName, '.docx'), '导出 Word 失败')
}

function handleExportPdf(row) {
  const fileName = getFileName(row)
  downloadBinary(downloadLogPdf(fileName), buildExportFileName(fileName, '.pdf'), '导出 PDF 失败')
}

async function downloadBinary(requestPromise, downloadName, errorMessage) {
  try {
    const data = await requestPromise
    if (!blobValidate(data)) {
      const text = await data.text()
      const result = JSON.parse(text)
      ElMessage.error(result.msg || errorMessage)
      return
    }
    saveAs(data, downloadName)
  } catch (error) {
    ElMessage.error(errorMessage)
  }
}

function buildExportFileName(fileName, extension) {
  const lastDotIndex = fileName.lastIndexOf('.')
  const baseName = lastDotIndex > 0 ? fileName.slice(0, lastDotIndex) : fileName
  return `${baseName}${extension}`
}
</script>

<style scoped lang="scss">
.system-log-page {
  padding: 0;
}

.system-log-shell {
  display: grid;
  grid-template-columns: minmax(320px, 420px) minmax(0, 1fr);
  gap: 20px;
  align-items: stretch;
}

.system-log-files,
.system-log-preview {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 22px;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.96), rgba(248, 250, 252, 0.96));
}

.system-log-files :deep(.el-card__body),
.system-log-preview :deep(.el-card__body) {
  display: flex;
  flex: 1;
  min-height: 0;
  flex-direction: column;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.panel-header--preview {
  align-items: center;
}

.panel-title {
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
  line-height: 1.2;
}

.panel-subtitle {
  margin-top: 6px;
  font-size: 13px;
  color: #64748b;
}

.panel-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
}

.panel-search {
  width: 220px;
}

.log-guide.log-guide--dialog {
  margin-bottom: 0;
}

.log-guide {
  margin-bottom: 16px;
  padding: 16px;
  border-radius: 20px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  background:
    radial-gradient(circle at top right, rgba(64, 94, 254, 0.08), transparent 42%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.95), rgba(248, 250, 252, 0.92));
}

.log-guide__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
  flex-wrap: wrap;
}

.log-guide__title {
  font-size: 15px;
  font-weight: 700;
  color: #0f172a;
}

.log-guide__subtitle {
  font-size: 12px;
  color: #64748b;
}

.log-guide__grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.log-guide-card {
  padding: 14px;
  border-radius: 18px;
  border: 1px solid rgba(226, 232, 240, 0.85);
  background: rgba(255, 255, 255, 0.78);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.log-guide-card__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.log-guide-card__title {
  font-size: 14px;
  font-weight: 700;
  color: #0f172a;
  word-break: break-all;
}

.log-guide-card__desc {
  font-size: 13px;
  line-height: 1.7;
  color: #475569;
}

.log-guide-card__rule {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.log-guide-card__rule-label {
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
}

.log-guide-card__rule-value {
  display: block;
  padding: 10px 12px;
  border-radius: 12px;
  background: #eff6ff;
  color: #1e3a8a;
  font-size: 12px;
  line-height: 1.6;
  word-break: break-word;
  white-space: pre-wrap;
}

.overview-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.overview-grid--preview {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.overview-card {
  padding: 16px;
  border-radius: 18px;
  background:
    radial-gradient(circle at top right, rgba(64, 94, 254, 0.12), transparent 48%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.92), rgba(241, 245, 249, 0.9));
  border: 1px solid rgba(226, 232, 240, 0.85);
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-height: 108px;
}

.overview-card--action {
  cursor: pointer;
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;
}

.overview-card--action:hover {
  transform: translateY(-2px);
  border-color: rgba(64, 94, 254, 0.28);
  box-shadow: 0 14px 28px rgba(64, 94, 254, 0.12);
}

.overview-card--action .overview-card__value {
  color: #405efe;
}

.overview-card__label {
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.overview-card__value {
  font-size: 22px;
  color: #0f172a;
  line-height: 1.2;
  word-break: break-word;
}

.overview-card__hint {
  font-size: 12px;
  color: #94a3b8;
  line-height: 1.5;
}

.file-filter-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
}

.file-filter-count {
  font-size: 13px;
  color: #64748b;
  white-space: nowrap;
}

.panel-state {
  padding: 12px 4px;
}

.file-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: calc(100vh - 320px);
  overflow: auto;
  padding-right: 4px;
}

.file-item {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  justify-content: space-between;
  padding: 16px;
  border-radius: 18px;
  border: 1px solid rgba(226, 232, 240, 0.9);
  background: rgba(255, 255, 255, 0.72);
  cursor: pointer;
  transition: transform 0.22s ease, box-shadow 0.22s ease, border-color 0.22s ease;
}

.file-item:hover {
  transform: translateY(-2px);
  border-color: rgba(64, 94, 254, 0.28);
  box-shadow: 0 14px 28px rgba(15, 23, 42, 0.08);
}

.file-item.is-active {
  border-color: rgba(64, 94, 254, 0.42);
  background:
    linear-gradient(180deg, rgba(64, 94, 254, 0.08), rgba(255, 255, 255, 0.92));
  box-shadow: 0 18px 36px rgba(64, 94, 254, 0.12);
}

.file-item__body {
  min-width: 0;
  flex: 1;
}

.file-item__title-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.file-item__title-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.file-item__title {
  font-size: 15px;
  font-weight: 700;
  color: #0f172a;
  word-break: break-all;
}

.file-item__summary {
  margin-top: 10px;
  font-size: 13px;
  line-height: 1.7;
  color: #64748b;
}

.file-item__actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.preview-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
  flex: 1;
  min-height: 0;
}

.preview-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.preview-toolbar__search {
  flex: 1;
  min-width: 240px;
}

.preview-toolbar__level {
  width: 180px;
}

.preview-alert {
  margin-top: -2px;
}

.preview-list {
  flex: 1;
  min-height: 520px;
  border-radius: 20px;
  border: 1px solid rgba(226, 232, 240, 0.95);
  background: #0f172a;
  color: #e2e8f0;
  overflow: auto;
  padding: 14px;
}

.system-log-preview .panel-state,
.system-log-preview :deep(.el-empty) {
  min-height: 520px;
}

.preview-list.is-nowrap .preview-line__content {
  white-space: pre;
}

.preview-line {
  padding: 12px 14px;
  border-radius: 14px;
  background: rgba(15, 23, 42, 0.72);
  border: 1px solid rgba(51, 65, 85, 0.8);
}

.preview-line + .preview-line {
  margin-top: 10px;
}

.preview-line.is-danger {
  border-color: rgba(248, 113, 113, 0.45);
  background: linear-gradient(180deg, rgba(127, 29, 29, 0.32), rgba(15, 23, 42, 0.8));
}

.preview-line.is-warning {
  border-color: rgba(251, 191, 36, 0.38);
  background: linear-gradient(180deg, rgba(120, 53, 15, 0.26), rgba(15, 23, 42, 0.8));
}

.preview-line.is-success {
  border-color: rgba(74, 222, 128, 0.26);
}

.preview-line.is-info {
  border-color: rgba(96, 165, 250, 0.26);
}

.preview-line.is-continuation {
  margin-left: 24px;
  opacity: 0.92;
}

.preview-line__meta {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  margin-bottom: 8px;
}

.preview-line__number {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 44px;
  height: 28px;
  padding: 0 8px;
  border-radius: 999px;
  background: rgba(30, 41, 59, 0.9);
  color: #94a3b8;
  font-size: 12px;
  font-weight: 700;
}

.preview-line__time,
.preview-line__source,
.preview-line__thread {
  font-size: 12px;
  color: #94a3b8;
}

.preview-line__source {
  color: #cbd5e1;
  font-weight: 600;
}

.preview-line__thread {
  padding: 0 8px;
  border-left: 1px solid rgba(148, 163, 184, 0.25);
}

.preview-line__content {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
  font-family: "JetBrains Mono", "Fira Code", Consolas, monospace;
  font-size: 13px;
  line-height: 1.7;
  color: #e2e8f0;
}

:deep(.system-log-page .el-card),
.log-guide-card,
.overview-card,
.file-item,
.preview-list,
.preview-line {
  border-radius: 12px;
}

.preview-line__number {
  border-radius: 8px;
}

@media (max-width: 1280px) {
  .system-log-shell {
    grid-template-columns: 1fr;
  }

  .overview-grid--preview {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .file-list,
  .preview-list {
    max-height: none;
  }

  .preview-list,
  .system-log-preview .panel-state,
  .system-log-preview :deep(.el-empty) {
    min-height: 360px;
  }
}

@media (max-width: 768px) {
  .panel-header,
  .log-guide__header,
  .file-filter-bar,
  .preview-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .panel-actions,
  .file-item {
    flex-direction: column;
    align-items: stretch;
  }

  .panel-search,
  .preview-toolbar__search,
  .preview-toolbar__level {
    width: 100%;
  }

  .log-guide__grid,
  .overview-grid,
  .overview-grid--preview {
    grid-template-columns: 1fr;
  }

  .preview-line.is-continuation {
    margin-left: 0;
  }
}
</style>
