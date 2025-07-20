import request from '@/utils/request'

export function getUserAllInfo() {
    return request({
        url: `/user-use-info/info`,
        method: 'get',
    })
}
export function getUserWeekInfo() {
    return request({
        url: `/user-use-info/weekInfo`,
        method: 'get',
    })
}

export function getUserTodayInfo() {
    return request({
        url: `/user-use-info/todayInfo`,
        method: 'get',
    })
}

export function getUserAiUseTime() {
    return request({
        url: `/user-use-info/ai-usage-time`,
        method: 'get',
    })
}


export function getUserAiTokenCount() {
    return request({
        url: `/user-use-info/ai-token-count`,
        method: 'get',
    })
}

export function getUserAiUseTimes() {
    return request({
        url: `/user-use-info/ai-use-times`,
        method: 'get',
    })
}