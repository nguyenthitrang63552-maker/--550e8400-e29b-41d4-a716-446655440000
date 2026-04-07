<template>
  <div class="app-container notice-page">
    <el-form
      ref="queryRef"
      :model="queryParams"
      :inline="true"
      v-show="showSearch"
      label-width="80px"
    >
      <el-form-item label="公告标题" prop="noticeTitle">
        <el-input
          v-model="queryParams.noticeTitle"
          placeholder="请输入公告标题"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发布人" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入发布人"
          clearable
          style="width: 240px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公告类型" prop="noticeType">
        <el-select
          v-model="queryParams.noticeType"
          placeholder="请选择公告类型"
          clearable
          style="width: 240px"
        >
          <el-option
            v-for="item in noticeTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="notice-toolbar mb8">
      <div class="notice-toolbar__actions">
        <button
          class="notice-toolbar-btn notice-toolbar-btn--primary"
          type="button"
          @click="handleAdd"
          v-hasPermi="['system:notice:add']"
        >
          <span class="notice-toolbar-btn__icon">
            <svg fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <line x1="12" x2="12" y1="5" y2="19"></line>
              <line x1="5" x2="19" y1="12" y2="12"></line>
            </svg>
          </span>
          <span class="notice-toolbar-btn__label">新增</span>
        </button>
        <button
          class="notice-toolbar-btn notice-toolbar-btn--accent"
          type="button"
          @click="handleUpdate"
          v-hasPermi="['system:notice:edit']"
        >
          <span class="notice-toolbar-btn__icon">
            <svg viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
              <path d="M410.3 231l11.3-11.3-33.9-33.9-62.1-62.1L291.7 89.8l-11.3 11.3-22.6 22.6L58.6 322.9c-10.4 10.4-18 23.3-22.2 37.4L1 480.7c-2.5 8.4-.2 17.5 6.1 23.7s15.3 8.5 23.7 6.1l120.3-35.4c14.1-4.2 27-11.8 37.4-22.2L387.7 253.7 410.3 231zM160 399.4l-9.1 22.7c-4 3.1-8.5 5.4-13.3 6.9L59.4 452l23-78.1c1.4-4.9 3.8-9.4 6.9-13.3l22.7-9.1v32c0 8.8 7.2 16 16 16h32zM362.7 18.7L348.3 33.2 325.7 55.8 314.3 67.1l33.9 33.9 62.1 62.1 33.9 33.9 11.3-11.3 22.6-22.6 14.5-14.5c25-25 25-65.5 0-90.5L453.3 18.7c-25-25-65.5-25-90.5 0zm-47.4 168l-144 144c-6.2 6.2-16.4 6.2-22.6 0s-6.2-16.4 0-22.6l144-144c6.2-6.2 16.4-6.2 22.6 0s6.2 16.4 0 22.6z"></path>
            </svg>
          </span>
          <span class="notice-toolbar-btn__label">修改</span>
        </button>
        <button
          class="notice-toolbar-btn notice-toolbar-btn--danger"
          type="button"
          @click="handleDelete"
          v-hasPermi="['system:notice:remove']"
        >
          <span class="notice-toolbar-btn__icon">
            <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M24 20.188l-8.315-8.209 8.2-8.282-3.697-3.697-8.212 8.318-8.31-8.203-3.666 3.666 8.321 8.24-8.206 8.313 3.666 3.666 8.237-8.318 8.285 8.203z"></path>
            </svg>
          </span>
          <span class="notice-toolbar-btn__label">删除</span>
        </button>
      </div>
      <right-toolbar class="notice-toolbar__tools" v-model:showSearch="showSearch" @queryTable="getList" />
    </div>

    <el-table class="notice-table" v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="公告编号" align="center" prop="noticeId" width="100" />
      <el-table-column label="公告标题" align="center" prop="noticeTitle" min-width="220" show-overflow-tooltip />
      <el-table-column label="公告类型" align="center" prop="noticeType" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.noticeType === '1' ? 'primary' : 'success'">
            {{ noticeTypeMap[scope.row.noticeType] || "未知" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="发布人" align="center" prop="createBy" width="120" show-overflow-tooltip />
      <el-table-column label="发布时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="140" class-name="small-padding fixed-width">
        <template #default="scope">
          <div class="notice-row-actions">
            <button
              class="notice-icon-btn notice-icon-btn--edit"
              type="button"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:notice:edit']"
            >
              <svg viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
                <path d="M410.3 231l11.3-11.3-33.9-33.9-62.1-62.1L291.7 89.8l-11.3 11.3-22.6 22.6L58.6 322.9c-10.4 10.4-18 23.3-22.2 37.4L1 480.7c-2.5 8.4-.2 17.5 6.1 23.7s15.3 8.5 23.7 6.1l120.3-35.4c14.1-4.2 27-11.8 37.4-22.2L387.7 253.7 410.3 231zM160 399.4l-9.1 22.7c-4 3.1-8.5 5.4-13.3 6.9L59.4 452l23-78.1c1.4-4.9 3.8-9.4 6.9-13.3l22.7-9.1v32c0 8.8 7.2 16 16 16h32zM362.7 18.7L348.3 33.2 325.7 55.8 314.3 67.1l33.9 33.9 62.1 62.1 33.9 33.9 11.3-11.3 22.6-22.6 14.5-14.5c25-25 25-65.5 0-90.5L453.3 18.7c-25-25-65.5-25-90.5 0zm-47.4 168l-144 144c-6.2 6.2-16.4 6.2-22.6 0s-6.2-16.4 0-22.6l144-144c6.2-6.2 16.4-6.2 22.6 0s6.2 16.4 0 22.6z"></path>
              </svg>
            </button>
            <button
              class="notice-icon-btn notice-icon-btn--delete"
              type="button"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:notice:remove']"
            >
              <svg viewBox="0 0 448 512" xmlns="http://www.w3.org/2000/svg">
                <path d="M135.2 17.7L128 32H32C14.3 32 0 46.3 0 64S14.3 96 32 96H416c17.7 0 32-14.3 32-32s-14.3-32-32-32H320l-7.2-14.3C307.4 6.8 296.3 0 284.2 0H163.8c-12.1 0-23.2 6.8-28.6 17.7zM416 128H32L53.2 467c1.6 25.3 22.6 45 47.9 45H346.9c25.3 0 46.3-19.7 47.9-45L416 128z"></path>
              </svg>
            </button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" v-model="open" width="900px" append-to-body>
      <el-form ref="noticeRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="公告标题" prop="noticeTitle">
          <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" maxlength="50" show-word-limit />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-radio-group v-model="form.noticeType">
                <el-radio
                  v-for="item in noticeTypeOptions"
                  :key="item.value"
                  :value="item.value"
                >
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :value="dict.value"
                >
                  {{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="公告内容" prop="noticeContent">
          <editor v-model="form.noticeContent" :min-height="260" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="cancel">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Notice">
import { addNotice, delNotice, getNotice, listNotice, updateNotice } from "@/api/system/notice"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const noticeList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const noticeTypeOptions = [
  { label: "通知", value: "1" },
  { label: "公告", value: "2" }
]

const noticeTypeMap = noticeTypeOptions.reduce((result, item) => {
  result[item.value] = item.label
  return result
}, {})

const stripHtml = (value) => (value || "")
  .replace(/<[^>]+>/g, "")
  .replace(/&nbsp;/gi, "")
  .trim()

const validateNoticeContent = (_, value, callback) => {
  if (!stripHtml(value)) {
    callback(new Error("公告内容不能为空"))
    return
  }
  callback()
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    noticeTitle: undefined,
    createBy: undefined,
    noticeType: undefined
  },
  rules: {
    noticeTitle: [
      { required: true, message: "公告标题不能为空", trigger: "blur" }
    ],
    noticeType: [
      { required: true, message: "公告类型不能为空", trigger: "change" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    noticeContent: [
      { required: true, validator: validateNoticeContent, trigger: "change" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listNotice(queryParams.value).then(response => {
    noticeList.value = response.rows
    total.value = response.total
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    noticeId: undefined,
    noticeTitle: undefined,
    noticeType: "1",
    noticeContent: "",
    status: "0"
  }
  proxy.resetForm("noticeRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.noticeId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加通知公告"
}

function handleUpdate(row) {
  reset()
  const noticeId = row.noticeId || ids.value[0]
  getNotice(noticeId).then(response => {
    form.value = response.data
    if (!form.value.noticeContent) {
      form.value.noticeContent = ""
    }
    open.value = true
    title.value = "修改通知公告"
  })
}

function submitForm() {
  proxy.$refs.noticeRef.validate(valid => {
    if (!valid) {
      return
    }
    const request = form.value.noticeId !== undefined ? updateNotice(form.value) : addNotice(form.value)
    request.then(() => {
      proxy.$modal.msgSuccess(form.value.noticeId !== undefined ? "修改成功" : "新增成功")
      open.value = false
      getList()
    })
  })
}

function handleDelete(row) {
  const noticeIds = row.noticeId || ids.value
  proxy.$modal.confirm(`是否确认删除通知公告编号为"${noticeIds}"的数据项？`).then(function () {
    return delNotice(noticeIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.notice-page {
  --notice-primary: #409eff;
  --notice-primary-deep: #337ecc;
  --notice-primary-soft: #ecf5ff;
  --notice-accent: #7c3aed;
  --notice-accent-deep: #6d28d9;
  --notice-accent-soft: #f5f3ff;
  --notice-danger: #dc2626;
  --notice-danger-deep: #b91c1c;
  --notice-danger-soft: #fef2f2;
  --notice-text: #1f2937;
}

.notice-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  margin: 4px 0 18px;
}

.notice-toolbar__actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.notice-toolbar__tools {
  margin-left: auto;
}

.notice-toolbar-btn {
  min-width: 112px;
  height: 42px;
  padding: 0 18px;
  border: 1px solid transparent;
  border-radius: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  font-size: 14px;
  font-weight: 600;
  color: var(--notice-text);
  background: #ffffff;
  cursor: pointer;
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
  transition: transform 0.2s ease, box-shadow 0.2s ease, border-color 0.2s ease, background-color 0.2s ease;
}

.notice-toolbar-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 28px rgba(15, 23, 42, 0.12);
}

.notice-toolbar-btn:active {
  transform: translateY(0);
}

.notice-toolbar-btn__label {
  line-height: 1;
}

.notice-toolbar-btn__icon {
  width: 16px;
  height: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.notice-toolbar-btn__icon svg {
  width: 100%;
  height: 100%;
  fill: none;
  stroke: currentColor;
  stroke-width: 2;
  stroke-linecap: round;
  stroke-linejoin: round;
}

.notice-toolbar-btn--primary {
  color: var(--notice-primary-deep);
  background: linear-gradient(135deg, #ffffff 0%, var(--notice-primary-soft) 100%);
  border-color: #b3d8ff;
}

.notice-toolbar-btn--accent {
  color: var(--notice-accent-deep);
  background: linear-gradient(135deg, #ffffff 0%, var(--notice-accent-soft) 100%);
  border-color: #ddd6fe;
}

.notice-toolbar-btn--danger {
  color: var(--notice-danger-deep);
  background: linear-gradient(135deg, #ffffff 0%, var(--notice-danger-soft) 100%);
  border-color: #fecaca;
}

.notice-toolbar-btn--accent .notice-toolbar-btn__icon svg,
.notice-toolbar-btn--danger .notice-toolbar-btn__icon svg,
.notice-icon-btn svg {
  fill: currentColor;
  stroke: none;
}

.notice-table {
  border-radius: 16px;
  overflow: hidden;
  background: #ffffff;
}

.notice-row-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.notice-icon-btn {
  width: 34px;
  height: 34px;
  padding: 0;
  border: 0;
  border-radius: 10px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease, background-color 0.2s ease;
}

.notice-icon-btn:hover {
  transform: translateY(-1px);
}

.notice-icon-btn svg {
  width: 16px;
  height: 16px;
}

.notice-icon-btn--edit {
  color: var(--notice-primary-deep);
  background: var(--notice-primary-soft);
  box-shadow: inset 0 0 0 1px #b3d8ff;
}

.notice-icon-btn--delete {
  color: var(--notice-danger-deep);
  background: var(--notice-danger-soft);
  box-shadow: inset 0 0 0 1px #fecaca;
}

.notice-page :deep(.el-form--inline .el-form-item) {
  margin-bottom: 16px;
}

.notice-page :deep(.el-table th.el-table__cell) {
  background: #f8fafc;
  color: var(--notice-text);
  font-weight: 600;
}

.notice-page :deep(.el-table td.el-table__cell) {
  padding: 14px 0;
}

.notice-page :deep(.el-table__inner-wrapper::before) {
  height: 0;
}

@media (max-width: 768px) {
  .notice-toolbar {
    align-items: stretch;
  }

  .notice-toolbar__actions {
    width: 100%;
  }

  .notice-toolbar-btn {
    flex: 1 1 140px;
  }

  .notice-toolbar__tools {
    width: 100%;
    margin-left: 0;
  }
}
</style>
