package com.xunmeng.system.mapper;

import com.xunmeng.system.pojo.SysNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-08
 */
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    List<SysNotice> selectNoticeList(SysNotice notice);

    int deleteNoticeByIds(Long[] noticeIds);
}
