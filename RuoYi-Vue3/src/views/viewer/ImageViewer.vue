<template>
  <div style="padding: 20px;">
    <p v-if="loading" style="color: #409eff;">加载中...</p>
    <p v-if="error" style="color: #f56c6c;">{{ error }}</p>
    <img v-if="imageUrl" :src="imageUrl" style="max-width:100%; border: 1px solid #ddd;" />
  </div>
</template>
<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import request from '@/utils/request'

const props = defineProps({ file: Object })
const imageUrl = ref('')
const error = ref('')
const loading = ref(false)

const loadImage = async () => {
  if (!props.file?.contentUrl) return
  
  loading.value = true
  error.value = ''
  if (imageUrl.value) {
    URL.revokeObjectURL(imageUrl.value)
    imageUrl.value = ''
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

    imageUrl.value = URL.createObjectURL(blob)
  } catch (err) {
    error.value = '图片加载失败: ' + (err.message || '未知错误')
  } finally {
    loading.value = false
  }
}

onMounted(loadImage)
watch(() => props.file, loadImage)

onUnmounted(() => {
  if (imageUrl.value) {
    URL.revokeObjectURL(imageUrl.value)
  }
})
</script>
