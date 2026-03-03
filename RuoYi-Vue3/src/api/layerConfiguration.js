import request from '@/utils/request'

// 保存图层配置
export function saveLayerConfiguration(data) {
  return request({
    url: '/system/layerConfiguration/save',
    method: 'post',
    data
  })
}

// 获取图层配置
export function getLayerConfiguration() {
  return request({
    url: '/system/layerConfiguration/get',
    method: 'get'
  })
}

// 重置图层配置
export function resetLayerConfiguration() {
  return request({
    url: '/system/layerConfiguration/reset',
    method: 'post'
  })
}

