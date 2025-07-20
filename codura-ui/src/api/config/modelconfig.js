import request from '@/utils/request'


//获取当前用户配置信息
export function getConfigByUserName(userName) {
    return request({
        url: '/modelConfig/user/'+userName,
        method: 'get',
    })
}


// 新增配置
export function addConfig(data) {
    return request({
        url: '/modelConfig/add',
        method: 'post',
        data: data
    })
}

// 修改用户
export function updateConfig(data) {
    return request({
        url: '/modelConfig/update',
        method: 'put',
        data: data
    })
}
