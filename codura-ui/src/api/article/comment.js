import request from "@/utils/request";


// 获取评论列表
export function getAuditCommentList(params) {
    return request({
      url: `/article/comment/list`,
      method: 'get',
      params
    })
}

// 是否自动审核开关
export function changeAutoAuditComment(v) {
    return request({
      url: `/article/comment/auto/${v}`,
      method: 'put',
    })
}

// 获取自动审核开关信息
export function getAutoAuditCommentInfo() {
    return request({
      url: `/article/comment/auto`,
      method: 'get',
    })
}

// 审核评论
export function auditComment(data) {
    return request({
      url: `/article/comment/do`,
      method: 'post',
      data
    })
}

// 获取文章详细信息
export function getCommentInfo(commentId) {
    return request({
      url: `/article/comment/info/${commentId}`,
      method: 'get'
    })
}

// 获取评论树
export function getCommentTree(commentId,params) {
    return request({
      url: `/article/comment/tree/${commentId}`,
      method: 'get',
      params
    })
}