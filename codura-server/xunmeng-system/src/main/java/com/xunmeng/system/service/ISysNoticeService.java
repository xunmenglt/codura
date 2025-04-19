package com.xunmeng.system.service;

import com.xunmeng.system.pojo.SysNotice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-08
 */
public interface ISysNoticeService extends IService<SysNotice> {

    List<SysNotice> selectNoticeList(SysNotice notice);

    int deleteNoticeByIds(Long[] noticeIds);
}
