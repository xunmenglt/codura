import request from "@/utils/request";


// 获取用户详细信息
export function upload(fromData,savedir="default") {
    return request({
      url: `/file/upload/${savedir}`,
      method: 'post',
      headers: { 'Content-Type': 'multipart/form-data' },
      data:fromData
    })
}