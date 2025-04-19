<template>
    <div class="all-conatiner">
        <div style="height:400px; width:500px">
            <el-card shadow="never">
                <div class="cropper" style="text-align:center;height:300px;">
                    <vueCropper
                      v-if="option.img"
                      ref="cropper"
                      :img="option.img"
                      :outputSize="option.outputSize"
                      :outputType="option.outputType"
                      :info="option.info"
                      :canScale="option.canScale"
                      :autoCrop="option.autoCrop"
                      :autoCropWidth="option.autoCropWidth"
                      :autoCropHeight="option.autoCropHeight"
                      :fixed="option.fixed"
                      :fixedBox="option.fixedBox"
                      :fixedNumber="option.fixedNumber"
                    ></vueCropper>
                    <div class="tip" v-else>
                        Please Upload A Picture
                    </div>
                  </div>
              </el-card>
              <el-card>
                <div class="btn-container">
                  <el-button type="danger" @click.stop="doCancel">
                      <i class="el-icon-close"></i>
                      cancel
                  </el-button>
                  <el-button type="primary" v-if="option.img" @click.stop="doPreview">
                      <i class="el-icon-view"></i>
                      preview
                  </el-button>
                  <el-button type="primary" v-else @click.stop="doSelect">
                      <i class="el-icon-upload2"></i>
                      select
                  </el-button>
                  <el-button type="success" @click.stop="doConfirm">
                      <i class="el-icon-check"></i>
                      confirm
                  </el-button>
                </div>
              </el-card>
          </div>
          <div class="image-preview">
            <img :src="previewImg"/>
          </div>
    </div>
</template>

<script>
import {selectFile} from '@/utils/fileutil'
import { VueCropper }  from 'vue-cropper' 
import { render } from 'nprogress'
export default {
    props:{
        width:{
            type:Number,
            default:200,
            require:false
        },
        height:{
            type:Number,
            default:200,
            require:false
        }
    },
    name:'PicCropper',
    components: { 
        VueCropper 
    },
    data() {
        return {
            previewImg: null, 
            option: {
                img: '', // 裁剪图片的地址
                info: true, // 裁剪框的大小信息
                outputSize: 1, // 裁剪生成图片的质量
                outputType: 'png', // 裁剪生成图片的格式
                canScale: true, // 图片是否允许滚轮缩放
                autoCrop: true, // 是否默认生成截图框
                autoCropWidth: this.width, // 默认生成截图框宽度
                autoCropHeight: this.height, // 默认生成截图框高度
                fixedBox: false, // 固定截图框大小 不允许改变
                fixed: false, // 是否开启截图框宽高固定比例
                fixedNumber: [1, 1], // 截图框的宽高比例
                full: false, // 是否输出原图比例的截图
                canMoveBox: true, // 截图框能否拖动
                original: true, // 上传图片按照原始比例渲染
                centerBox: false, // 截图框是否被限制在图片里面
                infoTrue: true, // true 为展示真实输出图片宽高 false 展示看到的截图框宽高
                canMove: true,
            },
        }
    },
    methods:{
        doCancel(){
            this.option.img=null,
            this.previewImg=null
            this.$emit('cropperCancel')
        },
        async doSelect(){
            const fileData=await selectFile()

            let reader=new FileReader();

            reader.onload=(e)=>{
                this.option.img=reader.result
            }
            reader.readAsDataURL(fileData);
        },
        doPreview(){
            // 获取截图的 base64 数据
            this.$refs.cropper.getCropData(data => {
                    this.previewImg=data
            })
        },
        doConfirm(){
            this.$refs.cropper.getCropData(data => {
                    //这里写上传给后端的逻辑
                    let that = this
                    if(data){
                        this.$emit('cropperConfirm', that.base64ToFile(data,`${Date.now()}.png`))
                    }else{
                        this.$emit('cropperConfirm',null)
                    }
                    this.option.img=null
                    this.previewImg=null
            })
        },
        //解码base64，以file上传给后端
        base64ToFile(urlData, fileName) {
            let arr = urlData.split(',');
            let mime = arr[0].match(/:(.*?);/)[1];
            let bytes = atob(arr[1]); // 解码base64
            let n = bytes.length
            let ia = new Uint8Array(n);
            while (n--) {
                ia[n] = bytes.charCodeAt(n);
            }
            return new File([ia], fileName, { type: mime });
        },
    }
}
</script>
<style lang="less" scoped>
.all-conatiner{
    display: flex;
    justify-content: center;
    flex-direction: row;
}
.image-preview{
    height: 200px;
    background: #b1b1b1;
    box-shadow: 0 3px 1px -2px rgba(0,0,0,.2),0 2px 2px 0 rgba(0,0,0,.14),0 1px 5px 0 rgba(0,0,0,.12)!important;
    img{
        height: 100%;
    }
    margin-left: 10px;
}
.container{
    width: 300px;
    height: 350px;
}
.btn-container{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: space-around;
    align-items: center;
}
.cropper{
    border-bottom: 1px solid #d4d4d4;
}
.tip{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 30px;
    font-weight: 600;
    color: #d4d4d4;
}
</style>