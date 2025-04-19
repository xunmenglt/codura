<template>
    <div class="container">
      <!-- 操作栏 -->
      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus">创建</el-button>
        <el-input
          v-model="searchQuery"
          placeholder="搜索资源名称"
          class="search-input"
          clearable
          suffix-icon="el-icon-search"
        ></el-input>
      </div>
  
      <!-- 表格 -->
      <el-table :data="tableData" border style="width: 100%" class="table">
        <el-table-column prop="name" label="名称" width="180"></el-table-column>
        <el-table-column prop="type" label="资源类型" width="100"></el-table-column>
        <el-table-column prop="size" label="资源大小" width="120"></el-table-column>
        <el-table-column prop="version" label="版本名称" width="100"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column prop="updatedAt" label="修改时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="mini" type="text">预览</el-button>
            <el-button size="mini" type="text">更多</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="100"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="10"
          :current-page="1"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchQuery: '',
        tableData: [
  {
    "name": "市场分析报告",
    "type": "文档",
    "size": "2.1 MB",
    "version": "1.2",
    "createdAt": "2024-04-20 09:30:00",
    "updatedAt": "2024-04-21 15:45:00"
  },
  {
    "name": "财务预算计划",
    "type": "电子表格",
    "size": "512 KB",
    "version": "1.3",
    "createdAt": "2024-05-01 14:45:00",
    "updatedAt": "2024-05-05 10:20:00"
  },
  {
    "name": "产品设计规范",
    "type": "文档",
    "size": "3.5 MB",
    "version": "1.1",
    "createdAt": "2024-03-15 16:00:00",
    "updatedAt": "2024-03-18 11:30:00"
  },
  {
    "name": "用户调研问卷",
    "type": "电子表格",
    "size": "1.8 MB",
    "version": "1.0",
    "createdAt": "2024-06-01 08:00:00",
    "updatedAt": "-"
  },
  {
    "name": "项目进度更新",
    "type": "演示文稿",
    "size": "5.2 MB",
    "version": "1.4",
    "createdAt": "2024-07-10 13:00:00",
    "updatedAt": "2024-07-12 16:45:00"
  },
  {
    "name": "会议记录",
    "type": "文档",
    "size": "976 KB",
    "version": "1.5",
    "createdAt": "2024-08-01 10:30:00",
    "updatedAt": "2024-08-03 14:15:00"
  },
  {
    "name": "年度总结报告",
    "type": "演示文稿",
    "size": "6.3 MB",
    "version": "1.0",
    "createdAt": "2024-01-10 09:00:00",
    "updatedAt": "-"
  },
  {
    "name": "员工手册",
    "type": "文档",
    "size": "2.8 MB",
    "version": "2.1",
    "createdAt": "2024-02-05 15:00:00",
    "updatedAt": "2024-02-10 11:00:00"
  },
  {
    "name": "客户合同模板",
    "type": "文档",
    "size": "1.4 MB",
    "version": "1.2",
    "createdAt": "2024-05-20 14:00:00",
    "updatedAt": "2024-05-22 16:30:00"
  },
  {
    "name": "产品发布计划",
    "type": "电子表格",
    "size": "2.5 MB",
    "version": "1.1",
    "createdAt": "2024-09-01 09:15:00",
    "updatedAt": "2024-09-05 10:45:00"
  }
]
      };
    },
    methods: {
      handleSizeChange(size) {
        console.log(`每页 ${size} 条`);
      },
      handleCurrentChange(page) {
        console.log(`当前页: ${page}`);
      }
    }
  };
  </script>
  
  <style scoped>
  .container {
    padding: 20px;
    background-color: #f5f7fa;
  }
  
  .toolbar {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .search-input {
    margin-left: 20px;
    width: 250px;
  }
  
  .table {
    background-color: #fff;
    border-radius: 8px;
    overflow: hidden;
  }
  
  .pagination {
    margin-top: 20px;
    text-align: right;
  }
  </style>