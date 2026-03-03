<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-form ref="formRef" :model="formData" :rules="rules" size="default" label-width="150px">
        <el-col :span="24">
          <el-row gutter="15">
            <el-col :span="14">
              <el-form-item label="Python 解释器路径" prop="field119">
                <el-input v-model="formData.field119" type="text" placeholder="请选择Python 解释器可执行文件（python.exe）"
                  clearable :style="{width: '1200px'}"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="14">
          <el-form-item label="Python 版本选择" prop="field104">
            <el-select v-model="formData.field104" placeholder="请选择Python 版本选择" clearable
              :style="{width: '100%'}">
              <el-option v-for="(item, index) in field104Options" :key="index" :label="item.label"
                :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="自动安装依赖库" prop="field105">
            <el-checkbox-group v-model="formData.field105" size="default">
              <el-checkbox v-for="(item, index) in field105Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="依赖库安装源" prop="field107">
            <el-select v-model="formData.field107" placeholder="请选择依赖库安装源" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in field107Options" :key="index" :label="item.label"
                :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="虚拟环境启用" prop="field108">
            <el-checkbox-group v-model="formData.field108" size="default">
              <el-checkbox v-for="(item, index) in field108Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-row gutter="15">
            <el-col :span="14">
              <el-form-item label="虚拟环境路径" prop="field109">
                <el-input v-model="formData.field109" type="text" placeholder="请输入虚拟环境根目录" :disabled='true'
                  clearable :style="{width: '100%'}"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="24">
          <el-form-item label="虚拟环境自动创建" prop="field111">
            <el-checkbox-group v-model="formData.field111" size="default" :disabled='true'>
              <el-checkbox v-for="(item, index) in field111Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="常用库版本锁定" prop="field112">
            <el-checkbox-group v-model="formData.field112" size="default">
              <el-checkbox v-for="(item, index) in field112Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Python 自动启动" prop="field113">
            <el-checkbox-group v-model="formData.field113" size="default">
              <el-checkbox v-for="(item, index) in field113Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="配置后自动验证" prop="field114">
            <el-checkbox-group v-model="formData.field114" size="default">
              <el-checkbox v-for="(item, index) in field114Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="环境配置日志" prop="field115">
            <el-checkbox-group v-model="formData.field115" size="default">
              <el-checkbox v-for="(item, index) in field115Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-row gutter="15">
            <el-col :span="14">
              <el-form-item label="Matlab 安装路径" prop="field124">
                <el-input v-model="formData.field124" type="text" placeholder="请选择Matlab根目录（如 Matlab R2023a）"
                  clearable :style="{width: '100%'}"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="14">
          <el-form-item label="Matlab 版本选择" prop="field125">
            <el-select v-model="formData.field125" placeholder="请选择Matlab 版本选择" clearable
              :style="{width: '100%'}">
              <el-option v-for="(item, index) in field125Options" :key="index" :label="item.label"
                :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Matlab 引擎API启用" prop="field126">
            <el-checkbox-group v-model="formData.field126" size="default">
              <el-checkbox v-for="(item, index) in field126Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Matlab 自动启动" prop="field127">
            <el-checkbox-group v-model="formData.field127" size="default">
              <el-checkbox v-for="(item, index) in field127Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="缓存自动清理" prop="field128">
            <el-checkbox-group v-model="formData.field128" size="default">
              <el-checkbox v-for="(item, index) in field128Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="自动配置环境变量" prop="field129">
            <el-checkbox-group v-model="formData.field129" size="default">
              <el-checkbox v-for="(item, index) in field129Options" :key="index" :label="item.value"
                :value="item.label" :disabled="item.disabled" />
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-row gutter="15">
            <el-col :span="14">
              <el-form-item label="启动执行脚本" prop="field131">
                <el-input v-model="formData.field131" type="text" placeholder="请输入Matlab启动脚本路径（.m文件，可选）"
                  :disabled='true' clearable :style="{width: '100%'}"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" @click="submitForm">保存环境配置</el-button>
            <el-button @click="resetForm">恢复默认配置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script setup>
import { ref, reactive, toRefs, onMounted, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { saveMachineLearningConfiguration, getMachineLearningConfiguration, resetMachineLearningConfiguration } from '@/api/machineLearning'

const {
  proxy
} = getCurrentInstance()
const formRef = ref()
const loading = ref(false)
const data = reactive({
  formData: {
    field119: undefined,
    field104: ' ',
    field105: ['配置完成后自动安装常用机器学习库（numpy、pandas、scikit-learn等）'],
    field107: undefined,
    field108: ['使用独立虚拟环境（venv/conda）'],
    field109: undefined,
    field111: ['若路径不存在，自动创建虚拟环境'],
    field112: ['锁定核心库版本，避免版本冲突'],
    field113: [],
    field114: ['保存配置后，自动验证Python环境是否可用'],
    field115: ['生成环境配置日志文件（便于排查问题）'],
    field124: undefined,
    field125: ' ',
    field126: ['启用Matlab Engine API for Python（支持Python调用Matlab函数）'],
    field127: [],
    field128: ['关闭软件时自动清理Matlab临时缓存文件'],
    field129: ['自动添加Matlab路径到系统环境变量（无需手动配置）'],
    field131: undefined,
  },
  rules: {
    field119: [{
      required: true,
      message: '请选择Python 解释器可执行文件（python.exe）',
      trigger: 'blur'
    }],
    field104: [{
      required: true,
      message: '请选择Python 版本选择',
      trigger: 'change'
    }],
    field105: [],
    field107: [{
      required: true,
      message: '请选择依赖库安装源',
      trigger: 'change'
    }],
    field108: [],
    field109: [],
    field111: [],
    field112: [],
    field113: [],
    field114: [],
    field115: [],
    field124: [{
      required: true,
      message: '请选择Matlab根目录（如 Matlab R2023a）',
      trigger: 'blur'
    }],
    field125: [{
      required: true,
      message: '请选择Matlab 版本选择',
      trigger: 'change'
    }],
    field126: [],
    field127: [],
    field128: [],
    field129: [],
    field131: [],
  }
})
const {
  formData,
  rules
} = toRefs(data)
const field104Options = ref([{
  "label": "Python 3.7",
  "value": 1
}, {
  "label": "Python 3.8",
  "value": 2
}, {
  "label": "Python 3.9",
  "value": 3
}, {
  "label": "Python 3.10",
  "value": 4
}, {
  "label": "Python 3.11",
  "value": 5
}])
const field105Options = ref([{
  "label": "配置完成后自动安装常用机器学习库（numpy、pandas、scikit-learn等）",
  "value": "配置完成后自动安装常用机器学习库（numpy、pandas、scikit-learn等）"
}])
const field107Options = ref([{
  "label": "官方源（默认）",
  "value": 1
}, {
  "label": "阿里镜像源",
  "value": 2
}, {
  "label": "清华大学镜像源",
  "value": 3
}, {
  "label": "中国科学技术大学镜像源",
  "value": 4
}])
const field108Options = ref([{
  "label": "使用独立虚拟环境（venv/conda）",
  "value": "使用独立虚拟环境（venv/conda）"
}])
const field111Options = ref([{
  "label": "若路径不存在，自动创建虚拟环境",
  "value": "若路径不存在，自动创建虚拟环境"
}])
const field112Options = ref([{
  "label": "锁定核心库版本，避免版本冲突",
  "value": "锁定核心库版本，避免版本冲突"
}])
const field113Options = ref([{
  "label": "软件启动时自动启动Python后台服务（无需手动启动）",
  "value": "软件启动时自动启动Python后台服务（无需手动启动）"
}])
const field114Options = ref([{
  "label": "保存配置后，自动验证Python环境是否可用",
  "value": "保存配置后，自动验证Python环境是否可用"
}])
const field115Options = ref([{
  "label": "生成环境配置日志文件（便于排查问题）",
  "value": "生成环境配置日志文件（便于排查问题）"
}])
const field125Options = ref([{
  "label": "R2020b",
  "value": 1
}, {
  "label": "R2021a",
  "value": 2
}, {
  "label": "R2021b",
  "value": 3
}, {
  "label": "R2022a",
  "value": 4
}, {
  "label": "R2022b",
  "value": 5
}, {
  "label": "R2023a",
  "value": 6
}, {
  "label": "R2023b",
  "value": 7
}])
const field126Options = ref([{
  "label": "启用Matlab Engine API for Python（支持Python调用Matlab函数）",
  "value": "启用Matlab Engine API for Python（支持Python调用Matlab函数）"
}])
const field127Options = ref([{
  "label": "软件启动时自动启动Matlab后台服务",
  "value": "软件启动时自动启动Matlab后台服务"
}])
const field128Options = ref([{
  "label": "关闭软件时自动清理Matlab临时缓存文件",
  "value": "关闭软件时自动清理Matlab临时缓存文件"
}])
const field129Options = ref([{
  "label": "自动添加Matlab路径到系统环境变量（无需手动配置）",
  "value": "自动添加Matlab路径到系统环境变量（无需手动配置）"
}])
/**
 * @name: 表单提交
 * @description: 表单提交方法
 * @return {*}
 */
function submitForm() {
  formRef.value.validate((valid) => {
    if (!valid) return
    loading.value = true
    saveMachineLearningConfiguration(formData.value)
      .then(() => {
        ElMessage.success('保存成功')
      })
      .catch((error) => {
        console.error('保存失败:', error)
        ElMessage.error('保存失败，请稍后重试')
      })
      .finally(() => {
        loading.value = false
      })
  })
}
/**
 * @name: 表单重置
 * @description: 表单重置方法
 * @return {*}
 */
function resetForm() {
  ElMessageBox.confirm('确定要恢复默认配置吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      loading.value = true
      resetMachineLearningConfiguration()
        .then(() => {
          ElMessage.success('已恢复默认配置')
          loadConfiguration()
        })
        .catch((error) => {
          console.error('重置失败:', error)
          ElMessage.error('重置失败，请稍后重试')
        })
        .finally(() => {
          loading.value = false
        })
    })
    .catch(() => {
      // 用户取消，不做处理
    })
}

/**
 * @name: 加载配置
 * @description: 从后端加载当前机器学习环境配置
 */
function loadConfiguration() {
  loading.value = true
  getMachineLearningConfiguration()
    .then((res) => {
      // ruoyi 后端返回 { code, msg, data }
      const cfg = res && res.data ? res.data : res
      if (cfg) {
        Object.assign(formData.value, cfg)
      }
    })
    .catch((error) => {
      console.error('加载配置失败:', error)
      ElMessage.error('加载配置失败，请稍后重试')
    })
    .finally(() => {
      loading.value = false
    })
}

// 进入页面时自动从后端拉取一次配置
onMounted(() => {
  loadConfiguration()
})

</script>
<style>
  .el-form-item {
    margin-bottom: 10px;
  }
</style>
