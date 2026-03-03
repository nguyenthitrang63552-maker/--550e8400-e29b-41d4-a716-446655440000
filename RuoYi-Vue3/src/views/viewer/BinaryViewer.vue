<template>
  <div style="padding: 20px;">
    <el-button type="primary" icon="Download" :loading="loading" @click="handleDownload">
      下载 {{ file.name }}
    </el-button>
    <div v-if="error" style="color: #f56c6c; margin-top: 10px;">{{ error }}</div>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import request from '@/utils/request'

const props = defineProps({ file: Object })
const loading = ref(false)
const error = ref('')

const handleDownload = async () => {
  if (!props.file?.contentUrl) return
  
  loading.value = true
  error.value = ''

  try {
    const urlObj = new URL(props.file.contentUrl, window.location.origin)
    const path = urlObj.searchParams.get('path')

    const blob = await request({
      url: '/api/file/content',
      method: 'get',
      params: { path },
      responseType: 'blob'
    })

    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', props.file.name || 'download')
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (e) {
    error.value = '下载失败: ' + (e.message || '未知错误')
  } finally {
    loading.value = false
  }
}
</script>
