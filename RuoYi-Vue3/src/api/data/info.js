import request from '@/utils/request'

// 查询试验信息主列表
export function listInfo(query) {
  return request({
    url: '/data/info/list',
    method: 'get',
    params: query
  })
}

// 查询试验信息主详细
export function getInfo(id, type) {
  return request({
    url: '/data/info/' + id,
    method: 'get',
    params: {type}
  })
}

// 新增试验信息主
export function addInfo(data) {
  return request({
    url: '/data/info',
    method: 'post',
    data: data
  })
}

// 修改试验信息主
export function updateInfo(data) {
  return request({
    url: '/data/info',
    method: 'put',
    data: data
  })
}

// 删除试验信息
export function delInfo(experimentIds, projectIds) {
  return request({
    url: '/data/info/' + experimentIds + '/project/' + projectIds,
    method: 'delete'
  })
}

export function getprojectList() {
  return request({
    url: '/data/info/projectList',
    method: 'get'
  })
}

export function getTargetInfos() {
  return request({
    url: '/data/info/targetTypeList',
    method: 'get'
  })
}

export function getExperimentInfos(ExperimentInfoQuery) {
  return request({
    url: '/data/info/experimentInfos',
    method: 'get',
    params: ExperimentInfoQuery
  })
}