<template>
    <div class="model-repo">
      <el-row :gutter="20" class="search-bar">
        <el-col :span="4">
          <el-button type="primary" @click="importModel">导入模型</el-button>
        </el-col>
        <el-col :span="8">
          <el-input v-model="searchQuery" placeholder="模型名称">
            <template #append>
              <el-button @click="searchModel" icon="el-icon-search">搜索</el-button>
            </template>
          </el-input>
        </el-col>
      </el-row>
  
      <el-table :data="models" style="width: 100%" class="model-table">
        <el-table-column prop="id" label="模型ID" width="180"></el-table-column>
        <el-table-column prop="name" label="模型名称"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="200"></el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button @click="editModel(scope.row)" type="text" icon="el-icon-edit"></el-button>
            <el-button @click="deleteModel(scope.row)" type="text" icon="el-icon-delete"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchQuery: '',
        models: [
          { id: 43, name: 'test0937', createdAt: '2024-03-21 09:38:01' },
          { id: 42, name: 'test1747', createdAt: '2024-03-20 19:11:14' },
          { id: 35, name: 'model', createdAt: '2024-03-20 10:29:23' },
          { id: 32, name: 'test-t-2-2', createdAt: '2024-03-19 19:10:56' },
          { id: 31, name: 'test-training-2-2', createdAt: '2024-03-19 19:10:09' },
          { id: 25, name: 'model-2-1', createdAt: '2024-03-01 14:24:22' },
          { id: 23, name: 'mnist-v2', createdAt: '2024-02-28 11:09:30' },
          { id: 19, name: 'test2-2-2', createdAt: '2024-02-26 02:31:33' }
        ]
      }
    },
    methods: {
      importModel() {
        // 模拟导入模型的操作
        this.$message('导入模型功能待实现');
      },
      searchModel() {
        this.$message(`搜索模型: ${this.searchQuery}`);
        // 模拟搜索功能，根据 searchQuery 过滤数据
      },
      editModel(model) {
        this.$message(`编辑模型: ${model.name}`);
        // 模拟编辑模型功能
      },
      deleteModel(model) {
        this.$confirm(`确定删除模型 ${model.name} 吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          // 模拟删除模型功能
          this.models = this.models.filter(m => m.id !== model.id);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    }
  }
  </script>
  
  <style scoped>
  .model-repo {
    padding: 20px;
  }
  
  .search-bar {
    margin-bottom: 20px;
  }
  
  .model-table {
    margin-top: 20px;
  }
  </style>