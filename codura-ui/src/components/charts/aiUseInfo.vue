<template>
  <div class="sticky-table">
    <el-table :data="useInfoList" style="width: 100%" :fit="true" border>
      <el-table-column prop="codeCompletionChars" label="代码补全字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeCompletionChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeCompletionTokens" label="代码补全Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeCompletionTokens) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeCompletionQaTimes" label="代码问答次数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeCompletionQaTimes) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeCompletionQaChars" label="代码问答字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeCompletionQaChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeCompletionQaTokens" label="代码问答Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeCompletionQaTokens) }}
        </template>
      </el-table-column>
      <el-table-column prop="testCaseWritingChars" label="测试编写字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.testCaseWritingChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="testCaseWritingTokens" label="测试编写Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.testCaseWritingTokens) }}
        </template>
      </el-table-column>
      <el-table-column prop="variableTypeDeclarationChars" label="变量声明字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.variableTypeDeclarationChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="variableTypeDeclarationTokens" label="变量声明Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.variableTypeDeclarationTokens) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeExplanationChars" label="代码解释字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeExplanationChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeExplanationTokens" label="代码解释Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeExplanationTokens) }}
        </template>
      </el-table-column>
      <el-table-column prop="dcoumentionWritingChars" label="文档编写字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.dcoumentionWritingChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="dcoumentionWritingTokens" label="文档编写Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.dcoumentionWritingTokens) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeRefactoringChars" label="代码重构字符数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeRefactoringChars) }}
        </template>
      </el-table-column>
      <el-table-column prop="codeRefactoringTokens" label="代码重构Token数" align="center">
        <template #default="{ row }">
          {{ _autoFormatNumber(row.codeRefactoringTokens) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import {getUserAllInfo } from '@/api/uselog/userUseInfo';
import { autoFormatNumber,autoFormatTime }from '@/utils/xunmeng'
export default {
  name: 'SingleUserAiUseTable',

  data() {
    return {
      useInfoList: [
      ],

    }
  },
  mounted() {
    this.flashData()
  },
  methods: {
    async flashData(){
      const res=await getUserAllInfo()
      if(res && res.code==200){
        this.useInfoList=[res.data]
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