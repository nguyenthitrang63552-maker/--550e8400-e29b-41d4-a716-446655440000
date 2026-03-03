<template>
  <el-input type="textarea" :rows="20" v-model="content" />
</template>

<script setup>
import request from '@/utils/request'
import { ref, onMounted } from 'vue'

const props = defineProps({ file: Object })
const content = ref('')

onMounted(async () => {
  content.value = await request({
    url: props.file.contentUrl,
    method: 'get'
  })
})
</script>
