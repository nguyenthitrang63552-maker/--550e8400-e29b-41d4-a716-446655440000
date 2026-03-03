<template>
  <div style="padding: 20px;">
    <p v-if="loading" style="color: #409eff;">加载中...</p>
    <video 
      v-if="videoUrl" 
      controls 
      preload="metadata"
      style="width:100%; max-height: 80vh; background: #000; border-radius: 4px;" 
      :src="videoUrl"
      @error="handleError"
    >
      您的浏览器不支持视频播放
    </video>
    <div v-if="error" style="color: #f56c6c; margin-top: 10px;">{{ error }}</div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch, onUnmounted } from 'vue'
import request from '@/utils/request'

const props = defineProps({ file: Object })
const videoUrl = ref('')
const error = ref('')
const loading = ref(false)

const loadVideo = async () => {
  error.value = ''
  loading.value = true
  
  if (videoUrl.value) {
    URL.revokeObjectURL(videoUrl.value)
    videoUrl.value = ''
  }
  
  if (!props.file?.contentUrl) {
    loading.value = false
    return
  }

  try {
    const urlObj = new URL(props.file.contentUrl, window.location.origin)
    const path = urlObj.searchParams.get('path')
    
    const blob = await request({
      url: '/api/file/content',
      method: 'get',
      params: { path },
      responseType: 'blob'
    })
    videoUrl.value = URL.createObjectURL(blob)
  } catch (e) {
    error.value = '视频加载失败: ' + (e.message || '未知错误')
  } finally {
    loading.value = false
  }
}

const handleError = () => {
  if (videoUrl.value) {
    error.value = '视频格式不支持或解码错误'
  }
}

onMounted(loadVideo)
watch(() => props.file, loadVideo)

onUnmounted(() => {
  if (videoUrl.value) {
    URL.revokeObjectURL(videoUrl.value)
  }
})
</script>
