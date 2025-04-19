<template>
  <div class="comment-container">
    <el-drawer
      title="评论"
      :visible.sync="isShow"
      direction="rtl"
      :before-close="handleClose">
      <div class="drawer-container">
        <div class="parnet-input-container">
          <div class="commenter-avater">
            <img :src="avatar?avatar:DefaultHead"/>
          </div>
          <div class="input-container">
              <el-input
                type="textarea"
                placeholder="请输入评论"
                v-model="inputContent"
                maxlength="500"
                show-word-limit
                :rows="3"
                resize="none"
              >
            </el-input>
            <div class="input-btn">
              <el-button type="primary" round size="mini" @click="doComment(commnetData.comments,articleId,0,allerId)">评论</el-button>
            </div>
          </div>
        </div>
        <div class="comment-container">
          <div class="parent-contaienr">
            <div class="parent-item comment-item" v-for="(parentItem) in commnetData.comments" :key="parentItem.commentId">
              <div class="commenter-avater">
                <img :src="parentItem.fromAvatar?(backEndBaseUrl+ parentItem.fromAvatar):DefaultHead"/>
              </div>
              <div class="lef-container">
                <div class="head-container head">
                  <div class="head-desc">{{parentItem.fromName}} {{formDate(parentItem.createTime)}}</div>
                  <div class="hufu" @click="currentComment=parentItem.commentId"><i class="iconfont icon-pingluncishu"></i> 回复</div>
                </div>
                <div class="parent-content comment-content">
                  {{ parentItem.content }}
                </div>
                <div class="input-container" v-if="currentComment==parentItem.commentId">
                  <el-input
                    type="textarea"
                    placeholder="请输入评论"
                    v-model="finputContent"
                    maxlength="500"
                    show-word-limit
                    :rows="3"
                    resize="none"
                  >
                </el-input>
                <div class="input-btn">
                  <el-button type="primary" round size="mini" @click="doComment(parentItem.comments,articleId,parentItem.commentId,parentItem.fromId)">评论</el-button>
                </div>
              </div>
                <div class="children-container">
                  <div class="children-item comment-item" v-for="(childrenItem) in parentItem.comments" :key="childrenItem.commentId">
                    <div class="commenter-avater">
                      <img :src="childrenItem.fromAvatar?backEndBaseUrl+ childrenItem.fromAvatar:DefaultHead"/>
                    </div>
                    <div class="lef-container">
                      <div class="head-container head">
                        <div class="head-desc">{{childrenItem.fromName}} 回复 {{childrenItem.toName}} {{ formDate(childrenItem.createTime) }}</div>
                        <div class="hufu" @click="currentComment=childrenItem.commentId"><i class="iconfont icon-pingluncishu"></i> 回复</div>
                      </div>
                      <div class="parent-content">
                        {{ childrenItem.content }}
                      </div>
                      <div class="input-container" v-if="currentComment==childrenItem.commentId">
                        <el-input
                          type="textarea"
                          placeholder="请输入评论"
                          v-model="finputContent"
                          maxlength="500"
                          show-word-limit
                          :rows="3"
                          resize="none"
                        >
                      </el-input>
                      <div class="input-btn">
                        <el-button type="primary" round size="mini" @click="doComment(parentItem.comments,articleId,parentItem.commentId,childrenItem.fromId)">评论</el-button>
                      </div>
                    </div>
                    </div>
                  </div>
                  <div 
                    class="lookmore"
                    v-show="parentItem.pageNum*parentItem.pageSize<parentItem.total" 
                    @click="handleMoreComment(parentItem)">
                    <div class="more">查看更多评论</div>
                  </div>
              </div>
              </div>
            </div>
          </div>
  
          <div 
            class="lookmore"
            v-show="commnetData.pageNum*commnetData.pageSize<commnetData.total" >
            <div class="more" @click="handleMoreComment(commnetData)">查看更多评论</div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {listComment,addComment} from '@/api/article/comment.js'
import DefaultHead from '@/assets/logo/logo-head.png';
export default {
  props:{
    articleId:{
      type:String|Number,
      default:""
    },
    isShow:{
      type:Boolean,
      default:false
    },
    allerId:{
      type:String|Number,
      default:""
    },
  },
  data() {
    return {
      commnetData:{
        pageNum:1,
        pageSize:5,
        currentParentId:0,
        total:0,
        articleId:this.articleId,
        comments:[
          
        ]
      },
      currentComment:"",
      backEndBaseUrl:process.env.VUE_APP_BASE_API,
      userName:"",
      nickName:"",
      avatar:"",
      inputContent:"",
      finputContent:"",
      DefaultHead:DefaultHead
    }
  },
  watch:{
    articleId(newValue){
      this.commnetData.articleId=newValue
    }
  },
  created(){
    if(this.$store && this.$store.getters && this.$store.getters.name){
      this.userName=this.$store.getters.name
      this.avatar=this.$store.getters.avatar
      this.nickName=this.$store.getters.nickName
    }
  },
  mounted(){
    setTimeout(()=>{
      this.initCommnetList()
    },200)
  },
  methods:{
    handleClose(){
      this.$emit("change-drawer",!this.isShow)
    },
    async initCommnetList(){
      let {pageNum,pageSize,currentParentId,articleId}=this.commnetData
      const res=await listComment({
        pageNum,pageSize,parentId:currentParentId,articleId
      })
      if(res && res.code===200){
        this.commnetData.total=res.total
        this.commnetData.comments=res.rows
        let size= this.commnetData.comments.length
        for (let i=0;i<size;i+=1){
          const comment=this.commnetData.comments[i]
          comment.pageNum=1
          comment.pageSize=5
          comment.total=0
          comment.currentParentId=comment.commentId
          const r= await listComment({
            pageNum:comment.pageNum,
            pageSize:comment.pageSize,
            articleId:comment.articleId,
            parentId:comment.currentParentId,
          })
          if(r && r.code===200){
            comment.comments=r.rows
            comment.total=r.total
          }
        }
      }
      this.commnetData=JSON.parse(JSON.stringify(this.commnetData))
    },
    async handleMoreComment(commentParent){
      commentParent.pageNum+=1
      let {pageNum,pageSize,currentParentId,articleId}=commentParent
      const res=await listComment({
        pageNum,pageSize,parentId:currentParentId,articleId
      })
      if(res && res.code===200){
        commentParent.total=res.total
        commentParent.comments=[...commentParent.comments,...res.rows]
      }
    },
    async doComment(commentList,articleId,parentId,toId){
      let content=this.inputContent
      if (toId!==this.allerId){
        content=this.finputContent
      }
      if (!content){
        return
      }
      const res=await addComment({
        articleId,parentId,toId,content
      })
      if(res &&res.code === 200){
        let item=res.data
        commentList.unshift(item)
        console.log(commentList)
        this.$modal.msgSuccess("评论成功")
      }
      this.finputContent=""
      this.inputContent=""
    },
    formDate(strdata){
      if(strdata){
        return strdata.split(" ")[0]
      }
      let currentDate=new Date()
      let defaultTime = currentDate.getFullYear()+"-"+(currentDate.getMonth()+1)+"-"+currentDate.getDate()
      return defaultTime      
    }
  }
}
</script>

<style lang="less" scoped>

/deep/ .el-textarea__inner::-webkit-scrollbar{
  width: 6px !important;
  height: 6px !important;
}
/deep/ .el-textarea__inner::-webkit-scrollbar-thumb {
  border-radius: 3px !important;
   -moz-border-radius: 3px !important;
   -webkit-border-radius: 3px !important;
   background-color: #c3c3c3 !important;
}
/deep/ .el-textarea__inner::-webkit-scrollbar-track {
 background-color: transparent !important;
}


/deep/ .el-drawer__body::-webkit-scrollbar{
  width: 6px !important;
  height: 6px !important;
}
/deep/ .el-drawer__body::-webkit-scrollbar-thumb {
  border-radius: 3px !important;
   -moz-border-radius: 3px !important;
   -webkit-border-radius: 3px !important;
   background-color: #c3c3c3 !important;
}
/deep/ .el-drawer__body::-webkit-scrollbar-track {
 background-color: transparent !important;
}

.parnet-input-container{
  width: 100%;
  display: flex;
  margin-bottom: 10px;
  .input-container{
    flex: 1;
    .input-btn{
      display: flex;
      justify-content: end;
      margin-top: 5px;
    }
  }
}
.commenter-avater{
  width: 30px;
  height: 30px;
  margin-right: 8px;
  border-radius: 50%;
  img{
    width: 30px;
    height: 30px;
    border-radius: 50%;
  }
}
.drawer-container{
  width: 100%;
  padding: 0 10px;
}
.head-container{
  flex: 1;
}
.comment-container{
  font-size: 14px;
}
.head{
  width: 100%;
  color: #777888;
  margin-right: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: flex;
  justify-content: space-between;
}
.head-desc{
  flex: 1;
}
.hufu{
  color: var(--hufi-color);
  cursor: pointer;
}
.hufu:hover{
  color: #53bdff;
}
.comment-item{
  margin-bottom: 10px;
  display: inline-flex;
  width: 100%;

}
.comment-item:hover{
  cursor: pointer;
  --hufi-color:#777888;
}
.comment-content{
    font-size: 14px;
    color: #222226;
    line-height: 22px;
    word-break: break-word;
    margin-bottom: 10px;
}
.lef-container{
  flex: 1;
}
.input-btn{
  display: flex;
  justify-content: end;
  margin-top: 10px;
}
.lookmore{
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

.more{
  color: #c3c3c3;
  cursor: pointer;
}
.more:hover{
  color: #53bdff;
}
</style>