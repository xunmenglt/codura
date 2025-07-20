import request from '@/utils/request'


export function getFimConfigByUserName(userName) {
    return request({
        url: '/fimConfig/user/'+userName,
        method: 'get',
    })
}
export function getChatConfigByUserName(userName) {
    return request({
        url: '/chatConfig/user/'+userName,
        method: 'get',
    })
}

export function addChatConfig(data) {
    return request({
        url: '/chatConfig/add',
        method: 'post',
        data: data
    })
}
export function addFimConfig(data) {
    return request({
        url: '/fimConfig/add',
        method: 'post',
        data: data
    })
}


export function updateFimConfig(data) {
    return request({
        url: '/fimConfig/update',
        method: 'put',
        data: data
    })
}
export function updateChatConfig(data) {
    return request({
        url: '/chatConfig/update',
        method: 'put',
        data: data
    })
}
