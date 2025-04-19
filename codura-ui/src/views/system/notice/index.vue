<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="通知标题" prop="noticeTitle">
        <el-input
          v-model="queryParams.noticeTitle"
          placeholder="请输入通知标题"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通知类型" prop="noticeType">
        <el-input
          v-model="queryParams.noticeType"
          placeholder="请输入通知类型"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:notice:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:notice:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:notice:delete']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noticeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="noticeId" type="index" width="50"/>
      <el-table-column label="通知标题" prop="noticeTitle" :show-overflow-tooltip="true" width="150" />
      <el-table-column label="通知类型" prop="noticeType" :show-overflow-tooltip="true" width="150">
        <template slot-scope="scope">
          <dict-tag :options="dict.notice_type" :value="scope.row.noticeType"/>
        </template>
      </el-table-column>
    
      <el-table-column label="状态" prop="status" :show-overflow-tooltip="true" width="200">
        <template slot-scope="scope">
          <dict-tag :options="dict.sys_notice_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建者" prop="createBy" :show-overflow-tooltip="true" width="200"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.roleName !== 'admin'">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:notice:delete']"
          >删除</el-button>
          <el-button
          size="mini"
          type="text"
          icon="el-icon-view"
          @click="handleView(scope.row)"
          v-hasPermi="['system:notice:query']"
        >详细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
    v-show="total>0"
    :total="total"
    :page.sync="queryParams.pageNum"
    :limit.sync="queryParams.pageSize"
    @pagination="getList"
    />
  </div>
</template>

<script>
import {delNotice, listNotice} from '@/api/system/notice'
import RightToolbar from '@/components/RightToolbar'
import pagination from '@/components/Pagination'
import { SYS_DICT } from '@/plugins/Constants'
import DictTag from '@/components/DictTag'

export default {
  components:{RightToolbar,pagination,DictTag},
  data() {
    return {
      // 是否更新
      isUpdate:false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 表格数据
      noticeList:[],
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      queryParams:{
        pageNum: 1,
        pageSize: 10,
        noticeTitle:undefined,
        noticeType:undefined,
        createBy:undefined
      },
      dict:SYS_DICT
    }
  },
  created(){
    this.getList()
  },
  methods:{
    getList(){
      this.loading = true
      listNotice(this.addDateRange(this.queryParams,this.dateRange)).then(res=>{
        if (res && res.code && res.code===200){
            this.noticeList=res.rows
            this.total = res.total;
        }
        this.loading = false;
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
        this.ids = selection.map(item => item.noticeId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
        const noticeIds = row.noticeId || this.ids;
        this.$modal.confirm('是否确认删除通知编号为"' + noticeIds + '"的数据项？').then(function() {
          return delNotice(noticeIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    handleAdd(){
      this.$router.push('/system/notice-alter/notice/0')
    },
    handleUpdate(row){
        const noticeIds = row.noticeId || this.ids;
        this.$router.push('/system/notice-alter/notice/'+noticeIds)
    },
    handleView(row){
      const noticeIds = row.noticeId
      this.$router.push({path:'/system/notice-alter/notice/'+noticeIds,query:{
        isView:true,
      }})
    }
  }
}
</script>