<template>
    <div class="box" :style="styles">
        <mavon-editor v-if="!readOnly" @input="handleInput" ref="md" v-model="value" boxShadowStyle="0px" @imgAdd="imgAdd" @save="saveFun"/>
        <mavon-editor
            v-else
            class="md"
            boxShadowStyle="0px"
            v-model="value"
            :subfield="false"
            :defaultOpen="'preview'"
            :toolbarsFlag="false"
            :editable="false"
            :scrollStyle="true"
            :ishljs="true"
        />
    </div>
</template>

<script>
import {mavonEditor} from 'mavon-editor'
require('mavon-editor/dist/css/index.css')
import {upload} from '@/api/file'
export default {
    name:'XmEditor',
    components:{mavonEditor},
    props:{
        content:{
            type:String,
            default:""
        },
        saveFun:{
            type:Function,
            default:()=>console.log("保存文件")
        },
        /* 高度 */
        height: {
            type: String,
            default: null,
        },
        /* 最小高度 */
        minHeight: {
            type: String,
            default: null,
        },
        /* 只读 */
        readOnly: {
            type: Boolean,
            default: false,
        },
        /* 上传文件大小限制(MB) */
        fileSize: {
            type: Number,
            default: 5,
        },
        isTiming:{
            type:Boolean,
            default:false,
        },
        timingTime:{
            type:Number,
            default:5
        }
    },
    data() {
        return {
            value:'',
            timer:null,
            flag:null,
        }
    },
    watch:{
        content(newValue){
            this.value=newValue+''
            if(!this.flag){
                this.flag=newValue+''
            }
        },
        value(newValue,oldValue){
            clearTimeout(this.timer)
            if(newValue ===this.flag || newValue===oldValue || newValue ===null || newValue===''){
                return
            }
            if(this.isTiming){
                console.log("开始倒计时")   
                this.timer=setTimeout(()=>{
                    console.log("倒计时结束")
                    this.saveFun()
                    clearTimeout(this.timer)
                },this.timingTime*1000)
            }
        }
    },
    computed:{
        styles() {
            let style = {};
            if (this.minHeight) {
                style.minHeight = `${this.minHeight}`;
            }
            if (this.height) {
                style.height = `${this.height}`;
            }
            return style;
        },
    },
    destroyed(){
        clearTimeout(this.timer)
    },
    methods:{
        async imgAdd(pos, $file) {
            let $vm = this.$refs.md
            const formData = new FormData();
            formData.append('file', $file);
            const res= await upload(formData,'notice')
            if (res.code==200){
                $vm.$img2Url(pos, process.env.VUE_APP_BASE_API + res.fileName);
            }
        },
        handleInput(){
            if(!this.readOnly){
                this.$emit("update:content", this.value);
            }
        }
    }
}
</script>

<style lang="less" scoped>
    .box{
        width: 100%;
    }
    /deep/ .v-note-wrapper {
        height: 100% !important;
    }
    /deep/ .v-show-content{
        background: #fff !important;
    }
</style>