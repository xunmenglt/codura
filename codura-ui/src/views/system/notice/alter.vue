<template>
    <div class="app-container">
        <el-form :model="noticeInfo" ref="noticeInfo" size="small" :inline="true" label-width="68px">
           <el-row>
                <el-col :span="10">
                    <el-row>
                        <el-form-item label="公告标题" prop="noticeTitle">
                            <el-input v-model="noticeInfo.noticeTitle"
                            placeholder="请输入公告标题"
                            clearable
                            :disabled="isView"
                            style="width: 300px"
                            />
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="公告类型" prop="noticeType">
                            <el-select
                              v-model="noticeInfo.noticeType"
                              placeholder="公告类型"
                              clearable
                              :disabled="isView"
                              style="width: 100px"
                            >
                              <el-option
                                v-for="dict in dict.notice_type"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                              />
                            </el-select>
                          </el-form-item>
                          <el-form-item label="状态" prop="status">
                            <el-select
                              v-model="noticeInfo.status"
                              placeholder="状态"
                              clearable
                              :disabled="isView"
                              style="width: 100px"
                            >
                              <el-option
                                v-for="dict in dict.sys_notice_status"
                                :key="dict.value"
                                :label="dict.label"
                                :value="dict.value"
                              />
                            </el-select>
                          </el-form-item>
                    </el-row>
                </el-col>
                <el-col :span="14">
                    <el-form-item label="创建者" prop="createBy" style="margin-right: 5px;" >
                        <el-input
                          v-model="noticeInfo.createBy"
                          disabled
                        />
                    </el-form-item>
                    <el-form-item label="创建时间" prop="createTime">
                        <el-input
                          v-model="noticeInfo.createTime"
                          disabled
                        />
                    </el-form-item>
                    <el-form-item label="更新者" prop="updateBy" style="margin-right: 5px;" >
                        <el-input
                          v-model="noticeInfo.updateBy"
                          disabled
                        />
                    </el-form-item>
                    <el-form-item label="更新时间" prop="updateTime">
                        <el-input
                          v-model="noticeInfo.updateTime"
                          disabled
                        />
                    </el-form-item>
                </el-col>
           </el-row>
        </el-form>
        <el-row>
            <xm-editor :readOnly="isView"
                       :content.sync="noticeInfo.noticeContent"
                       :isTiming="true" 
                       :timingTime="5" 
                       :height="'500px'" 
                       :saveFun="handleSave">

            </xm-editor>
        </el-row>
    </div>
</template>

<script>
import { addNotice, getNotice, updateNotice } from '@/api/system/notice'
import { SYS_DICT } from '@/plugins/Constants'
import XmEditor from '@/components/XmEditor'
export default {
    components:{XmEditor},
    data() {
        return {
            noticeId:0,
            isUpdate:false,
            noticeInfo:{},
            dict:SYS_DICT,
            isView:false,
        }
    },
    created(){
        this.noticeId=this.$route.params.noticeId
        console.log(this.$route.query.isView)
        this.isView=this.$route.query.isView?true:false
        if (this.noticeId!=0) {
            this.isUpdate=true
            this.getNoticeInfo()
        }
    },
    methods:{
        getNoticeInfo(){
            getNotice(this.noticeId).then((res)=>{
                this.noticeInfo=res.data || {}
            }).catch((e)=>{
                this.$modal.notifyError("获取公告信息异常");
            })
        },
        handleSave(){
            if(this.isView){
                return
            }
            if(this.noticeId<=0 && !this.isUpdate){
                console.log(this.noticeInfo)
                // todo 保存文件
                addNotice(this.noticeInfo).then((res=>{
                    this.$modal.msgSuccess("保存成功");
                    this.noticeId=res.data
                    this.getNoticeInfo()
                    this.isUpdate=true
                })).catch((e)=>{
                    this.$modal.msgError("保存失败");
                })
            }else{
                //更新文件
                updateNotice(this.noticeInfo).then((res=>{
                    this.$modal.msgSuccess("保存成功");
                    this.getNoticeInfo()
                })).catch((e)=>{
                    this.$modal.msgError("保存失败");
                })
            }
        }
    }
    
}
</script>

<style lang="scss" scoped>
    .ib{
        display: inline-block;
    }
    .it-center{
        display: flex;
        align-item: center;
    }
    .el-form-item{
        margin-right: 0;
    }
</style>