<!-- DocumentExtraction.vue -->
<template>
    <el-card class="document-extraction">
      <div slot="header" class="clearfix">
        <span class="title">文档抽取</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="resetForm">重置</el-button>
      </div>
      
      <el-row :gutter="20" class="main-content">
        <el-col :span="12" class="left-panel">
          <el-form :model="form" label-position="top">
            <!-- 文件上传 -->
            <el-form-item label="上传文档">
              <el-upload
                class="upload-demo"
                drag
                action="#"

                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"
                :before-upload="beforeUpload"
                :auto-upload="false"
                :file-list="fileList"
                multiple>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              </el-upload>
            </el-form-item>
  
            <!-- Prompt输入 -->
            <el-form-item label="Prompt">
              <el-input
                type="textarea"
                :rows="4"
                placeholder="请输入prompt"
                v-model="form.prompt">
              </el-input>
            </el-form-item>
  
            <!-- 模型参数设置 -->
            <el-form-item label="模型参数">
              <el-form-item label="温度">
                <el-slider v-model="form.modelParams.temperature" :min="0" :max="1" :step="0.1"></el-slider>
              </el-form-item>
              <el-form-item label="最大长度">
                <el-input-number v-model="form.modelParams.maxLength" :min="1" :max="1000"></el-input-number>
              </el-form-item>
              <el-form-item label="Top P">
                <el-slider v-model="form.modelParams.topP" :min="0" :max="1" :step="0.1"></el-slider>
              </el-form-item>
              <el-form-item label="频率惩罚">
                <el-slider v-model="form.modelParams.frequencyPenalty" :min="0" :max="2" :step="0.1"></el-slider>
              </el-form-item>
            </el-form-item>
  
            <!-- 提交按钮 -->
            <el-form-item>
              <el-button type="primary" @click="submitExtraction" :loading="loading">开始抽取</el-button>
            </el-form-item>
          </el-form>
        </el-col>
  
        <el-col :span="12" class="right-panel">
          <!-- 抽取结果展示 -->
          <div class="extraction-results">
            <h3>抽取结果</h3>
            <el-table v-if="extractionResults.length" :data="extractionResults" height="400" style="width: 100%">
              <el-table-column prop="question" label="问题">
              </el-table-column>
              <el-table-column prop="answer"  label="答案">
              </el-table-column>
            </el-table>
            <el-empty v-else description="暂无抽取结果"></el-empty>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </template>
  
  <script>
  // Script 部分保持不变
  export default {
    name: 'DocumentExtraction',
    data() {
      return {
        form: {
          prompt: '',
          modelParams: {
            temperature: 0.7,
            maxLength: 100,
            topP: 0.9,
            frequencyPenalty: 0.0
          },
          fileList: []
        },
        loading: false,
        extractionResults: []
      }
    },
    methods: {
        submitExtraction() {
    //   if (!this.form.fileList.length) {
    //     this.$message.warning('请先上传文件');
    //     return;
    //   }
      this.loading = true;
      // 模拟API调用
      setTimeout(() => {
        this.extractionResults = [
    {
      "question": "中国传统文化中，有哪些重要的节日？",
      "answer": "中国传统文化中重要的节日有很多，例如春节、清明节、端午节、中秋节等。春节是农历新年，是中国人最重要的节日，象征着新年的开始和团圆。清明节是祭祖和缅怀先人的节日，也是踏青赏景的好时机。端午节是为了纪念屈原，有吃粽子、赛龙舟等习俗。中秋节是阖家团圆的节日，人们赏月、吃月饼，寄托着对团圆的美好愿望。"
    },
    {
      "question": "什么是中国传统文化中的‘孝’？",
      "answer": "中国传统文化中的‘孝’是指子女对父母的尊敬和赡养。它涵盖了各种方面，包括：尊敬父母的权威，服从父母的教诲；关心父母的健康和生活；为父母分忧解难；尽力满足父母的需求；以及在精神上给予父母支持和安慰。孝不仅仅是一种行为，更是一种情感和责任，是中华民族传统美德的重要组成部分，也是家庭和社会和谐的重要基石。"
    },
    {
      "question": "中国有哪些著名的传统节日习俗？",
      "answer": "中国有很多著名的传统节日习俗，例如春节的贴春联、放鞭炮、吃饺子；元宵节的赏花灯、吃汤圆；清明节的扫墓、踏青；端午节的赛龙舟、吃粽子；中秋节的赏月、吃月饼。这些习俗，不仅传承了中华民族的文化基因，也体现了人们对美好生活的向往和追求。"
    },
    {
      "question": "中国传统文化中的礼仪有哪些？",
      "answer": "中国传统文化中的礼仪非常丰富，涵盖了人际交往的方方面面。例如，日常生活中，有对长辈的尊敬、对晚辈的关爱、对朋友的礼让；在社交场合，有待客之道、用餐礼仪、交谈礼仪等等。这些礼仪，体现了中国人注重和谐、注重人际关系的文化特点，也体现着中华民族的文明素养。"
    },
    {
      "question": "请描述一下中国传统建筑的特色？",
      "answer": "中国传统建筑的特色主要体现在其独特的审美观和文化内涵。例如，建筑的布局通常讲究对称和均衡，体现了中国传统文化中追求和谐统一的思想。屋顶的造型多样，如歇山顶、硬山顶等，既有实用性，也具有很强的装饰性，体现了中国建筑的精湛工艺。此外，中国传统建筑还注重与自然环境的融合，例如巧妙地利用庭院、山水等元素，营造出诗情画意的意境。"
    },
    {
      "question": "中国传统服饰有哪些代表性款式？",
      "answer": "中国传统服饰种类繁多，具有鲜明的时代特征和地域特色。代表性款式包括汉服、旗袍、唐装等。汉服是中国古代最普遍的服饰，款式多样，体现了不同朝代的审美特征。旗袍是近代中国女性的代表性服饰，以其优雅的剪裁和独特的韵味而闻名。唐装是唐朝的代表性服饰，以宽松的廓形和华丽的装饰而著称。"
    },
    {
      "question": "中国传统文化中，有哪些重要的哲学思想？",
      "answer": "中国传统文化中重要的哲学思想包括儒家思想、道家思想、墨家思想等。儒家思想强调仁义礼智信，提倡社会和谐，强调人与人之间的和谐关系；道家思想强调道法自然，追求与自然的和谐统一，强调人与自然的和谐关系；墨家思想强调兼爱非攻，提倡社会公平正义。"
    }
  ];
        this.loading = false;
        this.$message.success('文档抽取完成');
      }, 2000);
    },
    resetForm() {
      this.form = {
        prompt: '',
        modelParams: {
          temperature: 0.7,
          maxLength: 100,
          topP: 0.9,
          frequencyPenalty: 0.0
        },
        fileList: []
      };
      this.extractionResults = [];
    },
    handleUploadSuccess(response, file, fileList) {
      this.$message.success(`${file.name} 上传成功`);
      this.form.fileList = fileList;
    },
    handleUploadError(err, file, fileList) {
      this.$message.error(`${file.name} 上传失败`);
    },
    beforeUpload(file) {
    //   const isJPG = file.type === 'image/jpeg';
    //   const isPNG = file.type === 'image/png';
    //   const isLt500K = file.size / 1024 < 500;

    //   if (!isJPG && !isPNG) {
    //     this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
    //   }
    //   if (!isLt500K) {
    //     this.$message.error('上传头像图片大小不能超过 500KB!');
    //   }
    //   return (isJPG || isPNG) && isLt500K;
    return true
    }
    }
  }
  </script>
  
  <style scoped>
  .document-extraction {
    max-width: 95%;
    margin: 0 auto;
    margin-top: 10px;
  }
  .title {
    font-size: 20px;
    font-weight: bold;
  }
  .main-content {
    display: flex;
    height: calc(100vh - 200px);
    min-height: 500px;
  }
  .left-panel, .right-panel {
    height: 100%;
    overflow-y: auto;
  }
  .extraction-results {
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  .extraction-results h3 {
    margin-top: 0;
  }
  .el-table {
    flex: 1;
  }
  .el-form-item {
    margin-bottom: 18px;
  }
  </style>