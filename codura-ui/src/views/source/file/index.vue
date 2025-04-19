<template>
    <div class="file-management">
      <el-row class="toolbar" type="flex" justify="space-between" align="middle">
        <div class="button-group">
          <el-button type="primary" icon="el-icon-upload" size="small">上传</el-button>
          <el-button type="success" icon="el-icon-folder-add" size="small">新建文件夹</el-button>
          <el-button type="success" icon="el-icon-document-add" size="small">新建文件</el-button>
        </div>
        <el-input
          placeholder="当前目录下搜索"
          suffix-icon="el-icon-search"
          v-model="searchQuery"
          size="small"
          class="search-input"
        />
      </el-row>
  
      <el-table :data="fileList" style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="文件名" width="300">
          <template slot-scope="scope">
            <i class="el-icon-folder"></i>
            <span style="margin-left: 8px;">{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="size" label="大小"></el-table-column>
      </el-table>
  
      <div class="pagination">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalFiles"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :current-page="currentPage"
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
        fileList: [
        { name: 'train_dataset', size: '1.2 TB' },
        { name: 'validation_dataset', size: '300 GB' },
        { name: 'test_dataset', size: '150 GB' },
        { name: 'pretrained_embeddings', size: '500 GB' },
        { name: 'tokenizers', size: '2 GB' },
        { name: 'vocabularies', size: '1 GB' },
        { name: 'configuration_files', size: '5 GB' },
        { name: 'checkpoints', size: '750 GB' },
        { name: 'logs', size: '200 GB' },
        { name: 'evaluation_results', size: '10 GB' }
      ],
        totalFiles: 79,
        pageSize: 10,
        currentPage: 1
      };
    },
    methods: {
      handleSizeChange(size) {
        this.pageSize = size;
      },
      handleCurrentChange(page) {
        this.currentPage = page;
      }
    }
  };
  </script>
  
  <style scoped>
  .file-management {
    padding: 20px;
  }
  
  .toolbar {
    margin-bottom: 20px;
  }
  
  .button-group {
    display: flex;
    gap: 10px;
  }
  
  .search-input {
    width: 300px;
  }
  
  .pagination {
    margin-top: 20px;
    text-align: right;
  }
  </style>
  