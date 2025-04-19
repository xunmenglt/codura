<template>
    <div class="app-container">
      <el-row :gutter="20">
        <!--用户数据-->
        <el-col :span="24">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="用户名称" prop="userName">
              <el-input
                v-model="queryParams.userName"
                placeholder="请输入用户名称"
                clearable
                style="width: 240px"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="用户昵称" prop="nickName">
                <el-input
                  v-model="queryParams.nickName"
                  placeholder="请输入用户昵称"
                  clearable
                  style="width: 240px"
                  @keyup.enter.native="handleQuery"
                />
              </el-form-item>
            <el-form-item label="用户邮箱" prop="email">
              <el-input
                v-model="queryParams.email"
                placeholder="请输入用户邮箱"
                clearable
                style="width: 240px"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="用户状态" prop="enabled">
              <el-select
                v-model="queryParams.enabled"
                placeholder="用户状态"
                clearable
                style="width: 240px"
              >
                <el-option
                  v-for="dict in dict.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
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
                v-hasPermi="['system:user:add']"
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
                v-hasPermi="['system:user:edit']"
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
                v-hasPermi="['system:user:delete']"
              >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
          </el-row>
          <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="序号" align="center" type="index" width="50">
              <template slot-scope="scope">
                {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column label="用户名称" align="center" key="userName" prop="userName" v-if="columns[0].visible" :show-overflow-tooltip="true" />
            <el-table-column label="用户昵称" align="center" key="nickName" prop="nickName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
            <el-table-column label="用户邮箱" align="center" key="email" prop="email" v-if="columns[2].visible"  />
            <el-table-column label="状态" align="center" key="status" v-if="columns[3].visible">
              <template slot-scope="scope">
                <el-switch
                  v-model="scope.row.enabled"
                  :active-value="1"
                  :inactive-value="0"
                  @change="handleStatusChange(scope.row)"
                ></el-switch>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" v-if="columns[4].visible" width="160">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              width="160"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope" v-if="scope.row.userName !== 'admin'">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleUpdate(scope.row)"
                  v-hasPermi="['system:user:edit']"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['system:user:delete']"
                >删除</el-button>
                <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:user:resetPwd', 'system:user:edit']">
                  <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                      v-hasPermi="['system:user:resetPwd']">重置密码</el-dropdown-item>
  
                  </el-dropdown-menu>
                </el-dropdown>
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
        </el-col>
      </el-row>
  
      <!-- 添加或修改用户配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="用户名" prop="userName">
                <el-input :disabled="isUpdate" v-model="form.userName" placeholder="请输入用户名" maxlength="30" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户昵称" prop="nickName">
                <el-input v-model="form.nickName" placeholder="请输入用户昵称" maxlength="30" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="!isUpdate" label="用户密码" prop="password">
                <el-input v-model="form.password" placeholder="请输入用户密码" type="password" maxlength="20" show-password/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="状态">
                <el-radio-group v-model="form.enabled">
                  <el-radio
                    v-for="dict in dict.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value"
                  >{{dict.label}}</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色">
                <el-select v-model="form.roleIds" multiple placeholder="请选择角色">
                  <el-option
                    v-for="item in roleOptions"
                    :key="item.roleName"
                    :label="item.roleZhName"
                    :value="item.roleName"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>

    </div>
  </template>
  
  <script>
  import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus } from "@/api/system/user";
  import { getToken } from "@/utils/auth";
  import {SYS_DICT, TOAST_POSITION, TOAST_TYPE} from '@/plugins/Constants'
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import RightToolbar from '@/components/RightToolbar'
  import Pagination from '@/components/Pagination'
  import { showTextMessage } from '@/plugins/toastification';
  
  export default {
    name: "User",
    components: { Treeselect,RightToolbar,Pagination },
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
        // 用户表格数据
        userList: null,
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        deptName: undefined,
        // 默认密码
        initPassword: 'admin123',
        // 日期范围
        dateRange: [],
        // 岗位选项
        postOptions: [],
        // 角色选项
        roleOptions: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/system/user/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          orderByColumn:'create_time',
          userName: undefined,
          nickName: undefined,
          email: undefined,
          enabled: undefined,
        },
        // 列信息
        columns: [
          { key: 1, label: `用户名称`, visible: true },
          { key: 2, label: `用户昵称`, visible: true },
          { key: 4, label: `邮箱`, visible: true },
          { key: 5, label: `状态`, visible: true },
          { key: 6, label: `创建时间`, visible: true }
        ],
        // 表单校验
        rules: {
          userName: [
            { required: true, message: "用户名称不能为空", trigger: "blur" },
            { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
          ],
          nickName: [
            { required: true, message: "用户昵称不能为空", trigger: "blur" }
          ],
          password: [
            { required: true, message: "用户密码不能为空", trigger: "blur" },
            { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' },
            { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
          ],
          email: [
            {
              type: "email",
              message: "请输入正确的邮箱地址",
              trigger: ["blur", "change"]
            }
          ],
          phonenumber: [
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: "请输入正确的手机号码",
              trigger: "blur"
            }
          ]
        },
        // 字典
        dict:SYS_DICT
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.userList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.deptId = data.id;
        this.handleQuery();
      },
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.enabled === 1 ? "启用" : "停用";
        this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function() {
          return changeUserStatus(row.userName, row.enabled);
        }).then(() => {
          this.$modal.msgSuccess(text + "成功");
        }).catch(function() {
          row.enabled = row.enabled === "0" ? "1" : "0";
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          userName: undefined,
          nickName: undefined,
          password: undefined,
          email: undefined,
          status: 0,
          remark: undefined,
          roleIds: []
        };
        this.resetForm("form");
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
        this.ids = selection.map(item => item.userName);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      // 更多操作触发
      handleCommand(command, row) {
        switch (command) {
          case "handleResetPwd":
            this.handleResetPwd(row);
            break;
          case "handleAuthRole":
            this.handleAuthRole(row);
            break;
          default:
            break;
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.isUpdate=false
        getUser().then(response => {
          this.roleOptions = response.roles;
          this.open = true;
          this.title = "添加用户";
          this.form.password = this.initPassword;
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.isUpdate=true
        this.reset();
        const userName = row.userName || this.ids;
        getUser(userName).then(response => {
          this.form = response.data;
          this.roleOptions = response.roles;
          this.$set(this.form, "roleIds", response.roleIds);
          this.open = true;
          this.title = "修改用户";
          this.form.password = undefined;
        })
      },
      /** 重置密码按钮操作 */
      handleResetPwd(row) {
        this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          closeOnClickModal: false,
          inputPattern: /^.{5,20}$/,
          inputErrorMessage: "用户密码长度必须介于 5 和 20 之间",
          inputValidator: (value) => {
            if (/<|>|"|'|\||\\/.test(value)) {
              return "不能包含非法字符：< > \" ' \\\ |"
            }
          },
        }).then(({ value }) => {
            resetUserPwd(row.userName, value).then(response => {
              this.$modal.msgSuccess("修改成功，新密码是：" + value);
            });
          }).catch(() => {});
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.isUpdate) {
              updateUser(this.form).then(response => {
                showTextMessage(TOAST_TYPE.success,'修改成功',TOAST_POSITION.top)
                this.open = false;
                this.getList();
              }).catch(e=>{
                console.log(e)
              });
            } else {
              addUser(this.form).then(response => {
                showTextMessage(TOAST_TYPE.success,'新增成功',TOAST_POSITION.top)
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const userIds = row.userName || this.ids;
        this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
          return delUser(userIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      }
    }
  };
  </script>