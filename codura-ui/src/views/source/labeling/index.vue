<template>
    <el-card class="annotation-container">
      <div slot="header" class="clearfix">
        <span>数据标注</span>
        <el-input
          v-model="searchKeyword"
          placeholder="任务名称"
          suffix-icon="el-icon-search"
          @keyup.enter="searchItems"
          class="search-input"
        ></el-input>
        <el-button type="primary" size="mini" @click="addNewItem">新增</el-button>
      </div>
      <el-table :data="paginatedItems" stripe style="width: 100%">
        <el-table-column prop="taskName" label="任务名称"></el-table-column>
        <el-table-column prop="module" label="所属模块"></el-table-column>
        <el-table-column prop="dataType" label="标注数据类型"></el-table-column>
        <el-table-column label="标注总数">
          <template slot-scope="{row}">
            {{ row.totalCount }} / {{ row.totalCount }}
          </template>
        </el-table-column>
        <el-table-column label="标注进度">
          <template slot-scope="{row}">
            {{ row.progress }}%
          </template>
        </el-table-column>
        <el-table-column label="标注状态">
          <template slot-scope="{row}">
            <el-tag :type="getStatusType(row.status)">{{ row.statusDisplay }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="{row}">
            <div class="button-group">
              <el-button size="mini" type="primary" @click="viewItem(row)">查看</el-button>
              <el-button size="mini" type="warning" @click="editItem(row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="deleteItem(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 30]"
        :page-size="itemsPerPage"
        layout="total, sizes, prev, pager, next"
        :total="filteredItems.length"
      ></el-pagination>
    </el-card>
  </template>
  
  <script>
  import { ref, computed } from 'vue';
  
  export default {
    setup() {
      const items = ref([
        { taskName: 'aaaaa', module: 'pcsk8s', dataType: '图片', totalCount: 10, progress: 0, status: 'completed', statusDisplay: '已完成', createTime: '2024-07-09 15:29:36' },
        { taskName: '图像文字', module: 'pcsk8s', dataType: '图片', totalCount: 10, progress: 100, status: 'failed', statusDisplay: '已失败', createTime: '2024-06-29 10:11:08' },
        { taskName: 'test', module: 'pcsk8s', dataType: '图片', totalCount: 100, progress: 50, status: 'in-progress', statusDisplay: '进行中', createTime: '2024-06-10 14:09:02' },
        // Add more items here...
      ]);
  
      const searchKeyword = ref('');
      const currentPage = ref(1);
      const itemsPerPage = ref(10);
  
      const filteredItems = computed(() => {
        const lowerKeyword = searchKeyword.value.toLowerCase();
        return items.value.filter(item => item.taskName.toLowerCase().includes(lowerKeyword));
      });
  
      const paginatedItems = computed(() => {
          const startIndex = (currentPage.value - 1) * itemsPerPage.value;
          const endIndex = Math.min(startIndex + itemsPerPage.value, filteredItems.value.length);
          return filteredItems.value.slice(startIndex, endIndex);
      })
  
  
      const addNewItem = () => {
        items.value.unshift({ taskName: '新建任务', module: '', dataType: '', totalCount: 0, progress: 0, status: 'in-progress', statusDisplay: '进行中', createTime: new Date().toLocaleString() });
      };
  
      const viewItem = (item) => {
        console.log('查看', item);
      };
  
      const editItem = (item) => {
        console.log('编辑', item);
      };
  
      const deleteItem = (index) => {
        items.value.splice(index, 1);
      };
  
      const searchItems = () => {
        // Add more sophisticated search logic here if needed.
      };
  
      const handleSizeChange = (size) => {
        itemsPerPage.value = size;
      };
  
      const handleCurrentChange = (currentPage) => {
          currentPage.value = currentPage;
      };
  
      const getStatusType = (status) => {
        switch (status) {
          case 'completed': return 'success';
          case 'failed': return 'danger';
          case 'in-progress': return 'info';
          default: return '';
        }
      };
  
      return {
        items,
        searchKeyword,
        filteredItems,
        paginatedItems,
        currentPage,
        itemsPerPage,
        addNewItem,
        viewItem,
        editItem,
        deleteItem,
        searchItems,
        handleSizeChange,
        handleCurrentChange,
        getStatusType,
      };
    },
  };
  </script>
  
  <style scoped>
  .annotation-container {
      width: 90%;
      margin: 20px auto;
  }
  
  .search-input {
      margin-left: 20px;
  }
  
  .button-group {
    display: flex;
    justify-content: space-around; /* 按钮水平分布 */
  }
  </style>