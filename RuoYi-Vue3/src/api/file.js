import request from '@/utils/request'

export function submitDownloadTask(paths) {
  return request({
    url: '/api/file/submit',
    method: 'post',
    data: paths
  })
}

export function getDownloadTaskStatus(taskKey) {
  return request({
    url: '/api/file/progress/' + taskKey,
    method: 'get'
  })
}

export function downloadFile(taskKey) {
  return request({
    url: '/api/file/download/' + taskKey,
    method: 'get'
  })
}