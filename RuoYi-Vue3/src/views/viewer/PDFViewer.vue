<template>
  <div>
    <div v-if="error" class="error-message">
      <el-alert :title="error" type="error" :closable="false" />
    </div>
    <iframe 
      v-else-if="pdfUrl"
      :src="pdfUrl"
      style="width: 100%; height: 600px; border: none;"
    />
    <div v-else>
      <el-empty description="正在加载PDF..." />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  file: Object
})

const error = ref('')

const pdfUrl = computed(() => {
  if (!props.file || !props.file.contentUrl) return ''
  try {
    return props.file.contentUrl
  } catch (e) {
    error.value = 'URL 构造失败'
    return ''
  }
})
</script>

<style scoped>
.error-message {
  margin: 20px 0;
}
</style>
