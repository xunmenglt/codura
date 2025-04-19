import request from '@/utils/request'

// 获取通知列表
export function listNotice(query) {
  return request({
    url: '/system/notice/list',
    method: 'get',
    params: query
  })
}

//新增通知
export function addNotice(data){
    return request({
        url:'/system/notice/add',
        method: 'post',
        data: data
    })
}


// 修改通知
export function updateNotice(data){
    return request({
        url:'/system/notice/update',
        method:'put',
        data:data
    })
}


//获取通知信息
export function getNotice(noticeId){
    return request({
        url:`/system/notice/${noticeId}`,
        method: 'get',
    })
}


// 删除通知
export function delNotice(noticeIds){
    return request({
        url:"/system/notice/delete/"+noticeIds,
        method:'delete'
    })
}