import request from '@/utils/request'

// 查询商品分类列表
export function listCategory(query) {
  return request({
    url: '/xidian.ry/category/list',
    method: 'get',
    params: query
  })
}

// 查询商品分类详细
export function getCategory(catId) {
  return request({
    url: '/xidian.ry/category/' + catId,
    method: 'get'
  })
}

// 新增商品分类
export function addCategory(data) {
  return request({
    url: '/xidian.ry/category',
    method: 'post',
    data: data
  })
}

// 修改商品分类
export function updateCategory(data) {
  return request({
    url: '/xidian.ry/category',
    method: 'put',
    data: data
  })
}

// 删除商品分类
export function delCategory(catId) {
  return request({
    url: '/xidian.ry/category/' + catId,
    method: 'delete'
  })
}
