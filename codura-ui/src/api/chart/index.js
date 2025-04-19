import request from "@/utils/request";

// 获取总用户数
export function getTotalUsers() {
    return request({
      url: '/index/totalUsers',
      method: 'get'
    })
}


// AI总使用次数
export function getTotalAiUseTimes() {
  return request({
    url: '/index/totalAiUseTimes',
    method: 'get'
  })
}

// 总辅助代码量
export function getTotalCodeHelpCount() {
  return request({
    url: '/index/totalCodeHelpCount',
    method: 'get'
  })
}

// 用户使用总时间
export function getTotalPlugInUseTime() {
  return request({
    url: '/index/totalPlugInUseTime',
    method: 'get'
  })
}

// 用户跃总时间
export function getTotalPlugInActivateUseTime() {
  return request({
    url: '/index/totalPlugInActivateUseTime',
    method: 'get'
  })
}

// 每天活跃情况柱状图
export function getDayUsePlugInStatusInMonth() {
  return request({
    url: '/index/dayUsePlugInStatusInMonth',
    method: 'get'
  })
}

// 各类插件功能使用占比
export function getPlugInUsagePercentage() {
  return request({
    url: '/index/plugInUsagePercentage',
    method: 'get'
  })
}

// 每天活跃情况柱状图
export function getActiveUserLeaderboard(params) {
  return request({
    url: '/index/activeUserLeaderboard',
    method: 'get',
    params: params
  })
}