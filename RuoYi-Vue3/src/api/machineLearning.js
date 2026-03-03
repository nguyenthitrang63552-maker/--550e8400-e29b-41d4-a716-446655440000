import request from '@/utils/request'

// 保存机器学习环境配置
export function saveMachineLearningConfiguration(data) {
  return request({
    url: '/system/machineLearning/save',
    method: 'post',
    data
  })
}

// 获取机器学习环境配置
export function getMachineLearningConfiguration() {
  return request({
    url: '/system/machineLearning/get',
    method: 'get'
  })
}

// 重置机器学习环境配置
export function resetMachineLearningConfiguration() {
  return request({
    url: '/system/machineLearning/reset',
    method: 'post'
  })
}

