import request from "@/utils/request";


// 获取文章列表
export function getAuditArticleList(params) {
    return request({
      url: `/article/audit/list`,
      method: 'get',
      params
    })
}

// 是否自动审核开关
export function changeAutoAudit(v) {
    return request({
      url: `/article/audit/auto/${v}`,
      method: 'put',
    })
}

// 获取自动审核开关信息
export function getAutoAuditInfo() {
    return request({
      url: `/article/audit/auto`,
      method: 'get',
    })
}

// 审核文章
export function auditArticle(data) {
    return request({
      url: `/article/audit/do`,
      method: 'post',
      data
    })
}

// 获取文章详细信息
export function getAritcleInfo(articleId) {
    return request({
      url: `/article/audit/info/${articleId}`,
      method: 'get'
    })
}