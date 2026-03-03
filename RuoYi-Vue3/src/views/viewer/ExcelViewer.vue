<template>
  <el-card shadow="never">
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>{{ file.name }}</span>
        <span v-if="loading" style="font-size: 12px; color: #909399;">加载中...</span>
        <span v-if="error" style="font-size: 12px; color: #f56c6c;">{{ error }}</span>
      </div>
    </template>

    <el-table v-if="tableData.length > 0" :data="tableData" border height="600" stripe>
      <el-table-column
        v-for="(col, index) in columns"
        :key="index"
        :label="col || `列${index + 1}`"
        :prop="index.toString()"
        align="center"
      />
    </el-table>
    <el-empty v-else description="暂无数据" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const props = defineProps({ file: Object })

const tableData = ref([])
const columns = ref([])
const loading = ref(false)
const error = ref('')

onMounted(async () => {
  loading.value = true
  try {
    // 从contentUrl中提取path参数
    const url = new URL(props.file.contentUrl, window.location.origin)
    const path = url.searchParams.get('path')
    
    if (!path) {
      error.value = '文件路径为空'
      loading.value = false
      return
    }

    // 调用后端Excel预览API
    const res = await request({
      url: '/api/file/excel/preview',
      method: 'get',
      params: { path: path }
    })

    const rows = res.data
    
    if (!rows || rows.length === 0) {
      error.value = 'Excel文件为空'
      loading.value = false
      return
    }

    // 第一行作为列标题
    columns.value = rows[0] || []

    // 其余行作为数据
    tableData.value = rows.slice(1).map((row, rowIndex) => {
      const obj = {}
      row.forEach((cell, colIndex) => {
        obj[colIndex] = cell
      })
      return obj
    })

    error.value = ''
  } catch (err) {
    error.value = 'Excel加载失败: ' + (err.message || '未知错误')
    console.error('Excel加载错误:', err)
  } finally {
    loading.value = false
  }
})
</script>
