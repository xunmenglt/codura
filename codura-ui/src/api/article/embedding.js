import request from "@/utils/request";


// 获取文章检索列表
export function getAuditEmbeddingList(params) {
    return request({
      url: `/article/embedding/list`,
      method: 'get',
      params
    })
}

// 是否自动审核开关
export function changeAutoEmbedding(v) {
    return request({
      url: `/article/embedding/auto/${v}`,
      method: 'put',
    })
}

// 获取自动审核开关信息
export function getAutoEmbeddingInfo() {
    return request({
      url: `/article/embedding/auto`,
      method: 'get',
    })
}

// 是否检索
export function dealEmbedding(data) {
    return request({
      url: `/article/embedding/do`,
      method: 'post',
      data
    })
}
