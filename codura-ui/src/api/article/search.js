import request from "@/utils/request";


// 获取文章检索列表
export function getAuditSearchList(params) {
    return request({
      url: `/article/search/list`,
      method: 'get',
      params
    })
}

// 是否自动审核开关
export function changeAutoSearch(v) {
    return request({
      url: `/article/search/auto/${v}`,
      method: 'put',
    })
}

// 获取自动审核开关信息
export function getAutoSearchInfo() {
    return request({
      url: `/article/search/auto`,
      method: 'get',
    })
}

// 是否检索
export function dealSearch(data) {
    return request({
      url: `/article/search/do`,
      method: 'post',
      data
    })
}
