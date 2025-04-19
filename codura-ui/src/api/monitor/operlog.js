import request from '@/utils/request'
import { download } from '@/utils/request'

export const listOperlog=(params)=>{
    return request({
        url:'/monitor/operlog/list',
        method:'get',
        params:params
    })
}

// 删除操作日志
export const delOperlog=(operIds)=>{
    return request({
        url:'/monitor/operlog/delete/'+operIds,
        method:'delete'
    })
}

// 清空操作日志
export const cleanOperlog=()=>{
    return request({
        url:'/monitor/operlog/clear',
        method:'delete'
    })
}

export const exportOperLogs=(queryParams)=>{
    download('/monitor/operlog/export', {
        ...queryParams
    }, `operlog_${new Date().getTime()}.xlsx`)
}