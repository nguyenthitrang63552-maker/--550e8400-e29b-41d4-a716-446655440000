<template>
  <el-card shadow="never">
    <template #header>
      <span>JSON 内容预览</span>
    </template>

    <el-input
      type="textarea"
      :rows="20"
      v-model="jsonText"
      readonly
    />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const props = defineProps({
  file: {
    type: Object,
    required: true
  }
})

const jsonText = ref('')

onMounted(async () => {
  try {
    const res = await request({
      url: props.file.contentUrl,
      method: 'get'
    })
    jsonText.value = JSON.stringify(res, null, 2)
  } catch (e) {
    jsonText.value = 'JSON 解析失败或内容为空'
  }
})
</script>
