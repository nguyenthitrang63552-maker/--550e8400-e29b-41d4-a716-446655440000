import request from '@/utils/request'

// 查询菜品列表
export function listDish(query) {
  return request({
    url: '/xidian-ry/dish/list',
    method: 'get',
    params: query
  })
}

// 查询菜品详细
export function getDish(id) {
  return request({
    url: '/xidian-ry/dish/' + id,
    method: 'get'
  })
}

// 新增菜品
export function addDish(data) {
  return request({
    url: '/xidian-ry/dish',
    method: 'post',
    data: data
  })
}

// 修改菜品
export function updateDish(data) {
  return request({
    url: '/xidian-ry/dish',
    method: 'put',
    data: data
  })
}

// 删除菜品
export function delDish(id) {
  return request({
    url: '/xidian-ry/dish/' + id,
    method: 'delete'
  })
}
