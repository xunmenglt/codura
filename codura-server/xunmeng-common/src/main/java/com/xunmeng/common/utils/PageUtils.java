package com.xunmeng.common.utils;

import com.github.pagehelper.PageHelper;
import com.xunmeng.common.core.page.PageDomain;
import com.xunmeng.common.core.page.TableSupport;
import com.xunmeng.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author xunmeng
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum(); // 开始页
        Integer pageSize = pageDomain.getPageSize(); // 页面大小
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy()); // 排序方式
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
