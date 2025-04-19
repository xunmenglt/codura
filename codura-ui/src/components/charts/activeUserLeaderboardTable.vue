<template>
  <div class="sticky-table">
    <el-table :data="useInfoList" style="width: 100%">
      <el-table-column prop="userId" label="员工编号" width="180"></el-table-column>
      <el-table-column prop="nickName" label="姓名" width="180"></el-table-column>
      <el-table-column prop="codeCompletionQaTimes" label="AI使用次数" width="180"></el-table-column>
      <el-table-column prop="editorUsageTime" label="开发总时间" width="180">
        <template #default="{ row }">
          {{ _autoFormatTime(row.editorUsageTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="editorActiveTime" label="开发活跃时间" width="180">
        <template #default="{ row }">
          {{ _autoFormatTime(row.editorActiveTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="addedCodeLines" label="新增代码行数" width="180">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.addedCodeLines) }}行
        </template>
      </el-table-column>
      <el-table-column prop="deletedCodeLines" label="回退代码行数" width="180">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.deletedCodeLines) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalKeyboardInputs" label="总键盘输入次数" width="180">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.totalKeyboardInputs) }}次
        </template>
      </el-table-column>
      <el-table-column prop="ctrlCCount" label="CTRL+C次数" width="180">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.ctrlCCount) }}次
        </template>
      </el-table-column>
      <el-table-column prop="ctrlVCount" label="CTRL+V次数" width="180">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.ctrlVCount) }}次
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getActiveUserLeaderboard } from '@/api/chart'
import { autoFormatNumber,autoFormatTime }from '@/utils/xunmeng'
export default {
  name: 'activeUserLeaderboardTable',
  
  data() {
    return {
      useInfoList: [
      ],
      searchParam:{
        pageNum:1,
        pageSize:10,
        orderByColumn:"editor_active_time",
        isAsc:"desc",
        startTime:"",
        endTime:""
      }
    }
  },
  mounted() {
    this.flashData()
  },
  methods: {
    async flashData(){
      const res=await getActiveUserLeaderboard(this.searchParam)
      if(res && res.code==200){
        this.useInfoList=res.rows
      }
    },
    _autoFormatNumber(data){
      return autoFormatNumber(data)
    },
    _autoFormatTime(data){
      return autoFormatTime(data)
    }
  }
}
</script>

<style scoped>
.sticky-table {
  position: sticky;
  top: 0;
  background-color: #f5f5f5;
  z-index: 1;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.el-table th {
  background-color: #4B6A9F;
  color: white;
  font-weight: bold;
}

.el-table tr {
  background: rgba(255, 255, 255, 0.9);
}
</style>