import request from '@/utils/request'

export function listInfo(query) {
  return request({
    url: '/data/info/list',
    method: 'get',
    params: query
  })
}

export function getInfo(id, type) {
  return request({
    url: id ? '/data/info/' + id : '/data/info/',
    method: 'get',
    params: { type }
  })
}

export function addInfo(data) {
  return request({
    url: '/data/info',
    method: 'post',
    data
  })
}

export function updateInfo(id, type, data) {
  return request({
    url: '/data/info/' + id,
    method: 'put',
    params: { type },
    data
  })
}

export function delInfo(id, type) {
  return request({
    url: '/data/info/' + id,
    method: 'delete',
    params: { type }
  })
}

export function delInfoBatch(experimentIds, projectIds) {
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

export function getExperimentInfos(experimentInfoQuery) {
  return request({
    url: '/data/info/experimentInfos',
    method: 'get',
    params: experimentInfoQuery
  })
}
