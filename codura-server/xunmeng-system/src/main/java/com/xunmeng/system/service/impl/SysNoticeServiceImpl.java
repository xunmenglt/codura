package com.xunmeng.system.service.impl;

import com.xunmeng.system.pojo.SysNotice;
import com.xunmeng.system.mapper.SysNoticeMapper;
import com.xunmeng.system.service.ISysNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiuTeng
 * @since 2024-04-08
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    
    @Autowired
    private SysNoticeMapper noticeMapper;
    
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        
        return noticeMapper.selectNoticeList(notice);
        
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }
}
