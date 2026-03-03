import request from '@/utils/request';
export function generatePath(name){
    return request({
        url: '/utils/generatePath',
        method: 'post',
        params: { name: name } 
    });
}