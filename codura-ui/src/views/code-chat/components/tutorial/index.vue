<template>
  <div class="main">
    <h1 style="text-align: center;">DevAssist操作说明书</h1>
    <h1>1.简介</h1>
    <p style="text-indent: 2em; margin-bottom: 30px;">DevAssist是一款高效的智能开发辅助插件平台，它基于强大的大语言模型，为开发者提供了一系列AI智能代码辅助功能。这些功能不仅包括代码补全，同时还为用户提供了代码问答（代码重构、用例编写、代码解释）等辅助功能。DevAssist通过上下文感知的智能分析，帮助开发者在编码过程中快速解决问题，提高代码质量，促进团队协作。它还支持多语言和跨平台使用，确保了广泛的适用性和一致的用户体验。同时DevAssist致力于通过个性数据私有化数据，为开发者提供一个全面、高效且安全的编程环境。</p>
    <h1>2.系统配置</h1>
    <h2>2.1.安装所需的计算机软硬件要求</h2>
    <p>CPU：Intel 奔腾1.0GHZ 及以上</p>
    <p>内存：8GB 及以上</p>
    <p>硬盘可用空间：1GB及以上</p>
    <p>彩色显示器：屏幕分辨率 1024 * 768 及以上</p>
    <p>操作系统：MicorSoft Windows 98 及以上windows 操作系统</p>
    <h2>2.2.软件编程构架</h2>
    <p>Vue版本：2.0</p>
    <p>node版本：16.14.0及以上</p>
    <p>编辑器：IntelliJ IDEA 2022.3.3、Visual Studio Code 18.0.5</p>
    <p>Python版本：3.9.16及以上</p>
    <p>JDK版本：17.0.12</p>
    <h1>3.软件安装与配置</h1>
    <h2>3.1.编译安装包</h2>
    <p>通过IntelliJ IDEA编辑器对源代码进行编译，然后对编译后的安装包进行本地安装</p>
    <p>(1) 编译步骤（以提供编译好的安装包，可跳过此步骤）：</p>
    <ul>
      <li>打开terminal窗口，进行命令行打包操纵</li>
      <li>打开项目根目录DevAssist-Code，如：cd DevAssist-Code </li>
      <li>首先点击IDEA界面的Gradle源刷新按钮进行项目依赖安装</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/3.1.png"></li>
      <li>当依赖安装完成之后，点击intellij buildPlugin按钮进行项目打包</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/3.2.png"></li>
      <li>打包成功之后，可在根目录下的build/distributions文件中查看打包好的安装包devassist-v1.0.0.jzip</li>
    </ul>
    <p>(2) 安装步骤：</p>
    <ul>
      <li>打开IntelliJ IDEA 2022.3.x编辑器，点击文件>设置>插件后，即可查看插件安装界面</li>
      <li>点击插件安装界面的设置按钮，点击从磁盘安装插件</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/3.3.png"></li>
      <li>选中（1）中编译好的安装包，上传安装，上传之后会IDE重启按钮，点击重启IDE</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/3.4.png"></li>
      <li>重启成功之后，查看IntelliJ IDEA工具栏处是否有DevAssist按钮，点击DevAssis按钮，即可查看插件首页，如果上述步骤全部尝试成功，则代表本插件安装完成。</li>
    </ul>
    <h1>4.软件运行功能界面介绍</h1>
    <h2>4.1.平台用户欢迎界面</h2>
    <ul>
      <li>DevAssist·智能开发辅助平台启动之后，IntelliJ IDEA代码工具栏区域显示如下本平台欢迎页面。如下图所示：</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/4.1.png"></li>
      <li>平台欢迎页面主要用于提示用户是否成功安装本平台，以便用户后续对本平台相关功能的使用。</li>
    </ul>
    <h2>4.2.平台模型配置界面</h2>
    <ul>
      <li>点击IntelliJ IDEA软件的文件>设置后在弹出功能界面继续点击工具>DevAssist Config>Model Privider按钮即可查看到本插件平台模型配置界面配置界面，如下图所示：</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/4.2.png"></li>
    </ul>
    <p>其中该页面中有两大配置区域，第一个是聊天模型服务配置，第二个是代码补全模型服务配置。各配置的参数及详细解释如下：</p>
    <p>(1) 聊天模型服务配置</p>
    <ul>
      <li>名称：模型名称，可按照用户的意愿来完成聊天模型的命名。</li>
      <li>业务类型：默认为chat，已强制不能修改。</li>
      <li>模型名称：该参数为调用本地模型的模型名称，目前可选CodeQwen1.5-7B-Chat和Qwen2-Instruct-7B两个开源模型。</li>
      <li>服务协议：可根据接口的协议类型进行选择，其中包含http和https，默认为http</li>
      <li>服务端口：根据本地模型接口进行选择，默认为80</li>
      <li>服务路径：接口的请求路径，默认为/v1/chat/completions</li>
      <li>API-KEY：接口的请求密钥，如果接口为设置密钥，请设置为空</li>
    </ul>
    <p>(2) 代码补全模型服务配置</p>
    <ul>
      <li>名称：模型名称，可按照用户的意愿来完成代码补全模型的命名。</li>
      <li>业务类型：默认为fim，已强制不能修改。</li>
      <li>填充类型：该参数为代码补全提示模板，目前可选择codeqwen-fim和codellama-fim两个选项。</li>
      <li>模型名称：该参数为调用本地模型的模型名称，目前可选CodeQwen1.5-7B和CodeLlama-7B-hf两个开源代码补全模型。</li>
      <li>服务协议：可根据接口的协议类型进行选择，其中包含http和https，默认为http</li>
      <li>服务端口：根据本地模型接口进行选择，默认为80</li>
      <li>服务路径：接口的请求路径，默认为/v1/completions</li>
      <li>API-KEY：接口的请求密钥，如果接口为设置密钥，请设置为空</li>
    </ul>
    <p>按照以上参数要求，对参数进行配置，配置成功后，点击界面右下角OK按钮对参数配置进行保存。</p>
    <h2>4.3.代码补全模型配置界面</h2>
    <ul>
      <li>代码补全模型配置界面是用来给代码补全时所用的模型进行参数配置，以便为用户提供更好的代码补全服务和体验，为用户打造灵活的自定义功能。</li>
      <li style="list-style: none;"><img src="../../../../assets/images/tutorial/4.3.png"></li>
    </ul>
    <p>按如下要求，对参数进行动态的自定义配置：</p>
    <ul>
      <li>最大生成文本数：该参数可以控制代码补全时为用户提供最大的代码补全行数，默认为100。</li>
      <li>最大生成token数：该参数可以控制代码补全时为用户提供最大的代码补全token数，该值越大，表示代码补全会尽可能输出更多的代码补全内容。</li>
      <li>温度（temperature）：该参数越大，对同一次补全的内容会越不相同，默认为0.75</li>
      <li>开启文件上下文：如果开启该功能，在编写代码时，本平台可对之前编辑内容进行记忆，为下一次补全内容提供更充分的参考。</li>
      <li>开启连续补全：如果开启该功能，本平台在生成一次补全内容后会继续生成下一次补全内容。</li>
      <li>开启补全缓存：如果开启该功能，本平台在生成一次补全后，本平台会记住该补全内容，当用户回退代码时，触发补全后，本平台会采取上一次补全内容给用户参考。</li>
      <li>开启多行补全：如果开启该功能，本平台可以实现多行补全功能</li>
      <li>开启补全：如果关闭该功能，本平台的代码补全功能将失效。</li>
    </ul>
    <p>按照以上参数要求，对参数进行配置，配置成功后，点击界面右下角OK按钮对参数配置进行保存。</p>
    <h2>4.4.聊天模型配置界面</h2>
    <img src="../../../../assets/images/tutorial/4.4.png">
    <p>按如下要求，对参数进行动态的自定义配置：</p>
    <ul>
      <li>最大生成文本数：该参数可以控制聊天问答时模型为用户提供最大的代码补全行数，默认为100。</li>
      <li>最大生成token数：该参数可以控制聊天问答时模型为用户提供最大的代码补全token数，该值越大，表示聊天问答时模型会尽可能输出更多的回复内容。</li>
      <li>最大历史对话数（history message length）：该参数可以控制模型记录聊天过程中的问答历史上下文记录，参数越大，记住的聊天内容就越多。</li>
    </ul>
    <p>按照以上参数要求，对参数进行配置，配置成功后，点击界面右下角OK按钮对参数配置进行保存。</p>
    <h2>4.5.代码补全界面</h2>
    <p>补全触发快捷键：Alt+/</p>
    <p>(1)根据注释编写代码</p>
    <p>通过编写代码注释，本平台可以通过理解代码注释，为用户构建与之相应的代码内容，如下图所示：</p>
    <img src="../../../../assets/images/tutorial/4.5.png">
    <p>其中灰色代码部分为本平台根据用户编写的”编写一个可执行的冒泡排序“注释，生成的相应冒泡排序代码补全内容。按Tab键即可采取本平台生成的补全内容。效果如下图所示：</p>
    <img src="../../../../assets/images/tutorial/4.6.png">
    <p>(2)单行代码补全</p>
    <p>单行代码补全功能，为用户提供智能的代码补全服务，当用户编写代码时，且鼠标指针处于代码的约束括号内或其他可触发单行补全位置时，本平台会为用户生成单行补全功能。效果如下图所示：</p>
    <img src="../../../../assets/images/tutorial/4.7.png">
    <p>其中for循环中灰色部分为本平台生成的单行补全内容，按Tab键可进行采取补全操作。</p>
    <p>(3)多行代码补全</p>
    <p>多行代码补全功能，为用户提供智能的代码补全服务，当用户编写代码时，且鼠标指针处于代码的函数体或其他可触发多行补全位置时，本平台会为用户生成多行补全功能。效果如下图所示：</p>
    <img src="../../../../assets/images/tutorial/4.8.png">
    <p>其中bubbleSort函数后面灰色部分为本平台生成的多行补全内容，按Tab键可进行采取补全操作。</p>
    <h2>4.6.代码问答界面</h2>
    <p>(1)普通问答</p>
    <p>普通问答功能，用户可以在文本框中输入编程或其他问题，点击提交后，平台会返回相应的回答，效果如下图所示:</p>
    <img src="../../../../assets/images/tutorial/4.9.png">
    <p>(2)代码问答</p>
    <p>代码问答功能为用户提供代码解释等功能，用户可以在左边打开一个代码文件，鼠标选中要查询的代码，在右边的输入框中输入问题，本平台会结合代码和用户的问题，生成最终的答案。效果如下图所示：</p>
    <img src='../../../../assets/images/tutorial/4.11.png'>
    <p>同时，在代码问答的界面下方可以看到选中代码的字符总数。</p>
    <p>(3)编写测试用例</p>
    <p>编写测试用例是当用户选中代码片段后，右击选择编写测试用例，系统会根据选中的代码编写单元测试，会给出测试使用的方法，测试代码和详细的注释信息。效果如下图所示：</p>
    <img src='../../../../assets/images/tutorial/4.12.png'>
    <p>(4)变量类型声明</p>
    <p>变量类型声明是平台会根据用户代码对其中涉及的变量声明其类型，用户选中代码片段，右击选择变量类型声明，平台会重新生成更完整的代码。效果如下图所示：</p>
    <img src='../../../../assets/images/tutorial/4.13.png'>
    <p>(5)代码内容解释</p>
    <p>代码内容解释功能用来帮助用户理解特定代码段的功能和实现细节，用户选中代码后右击选择代码内容解释，平台会详细解释代码，包括代码执行的功能、算法逻辑、流程步骤（如每个函数或方法的调用顺序、条件判断、循环结构等）、定义的方法和参数信息。效果如下图所示：</p>
    <img src='../../../../assets/images/tutorial/4.14.png'>
    <p>(6)编写开发文档</p>
    <p>编写开发文档功能能够帮助用户自动化地生成代码段的开发文档，用户选中代码片段，右击选择编写开发文档，平台会生成一篇规范的开发文档，文档内容包括但不限于代码段的描述、功能、参数、返回值、异常处理等。效果如下图所示：</p>
    <img src='../../../../assets/images/tutorial/4.15.png'>
    <p>(7)重构代码内容</p>
    <p>重构代码内容功能，用户选中想要重构的代码片段，右击选择重构代码内容，平台会进行重构操作，这些操作可能包括重命名变量、提取方法、内联变量、移动或复制代码块等，系统会展示原始代码和经过重构后的代码，以便用户可以直观地比较和验证重构的结果。效果如下图所示：</p>
    <img src='../../../../assets/images/tutorial/4.16.png'>
    
  </div>
</template>

<script>

export default {
  data() {
    return {
  
    }
  },
  computed: {
    
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

body {
  background-color: #f9f9f9;
  color: #333;
  line-height: 1.7;
}

.main {
  width: 100%;
  max-width: 960px;
  margin: 0 auto;
  padding: 30px 20px;
  background-color: #fff;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.05);
  border-radius: 10px;
}

/* 标题优化 */
h1 {
  margin-bottom: 30px;
  font-size: 28px;
  color: #2c3e50;
  border-left: 6px solid #409EFF;
  padding-left: 12px;
}

h2 {
  margin-bottom: 20px;
  font-size: 22px;
  color: #34495e;
  border-left: 4px solid #67C23A;
  padding-left: 10px;
}

h3 {
  margin-bottom: 15px;
  font-size: 18px;
  color: #555;
}

/* 段落优化 */
p {
  margin-bottom: 20px;
  text-indent: 2em;
  font-size: 16px;
}

/* 列表优化 */
ul {
  margin-bottom: 20px;
  padding-left: 40px;
  list-style: disc;
}

li {
  margin-bottom: 10px;
  font-size: 15px;
  line-height: 1.6;
}

/* 图片优化 */
img {
  display: block;
  width: 70%;
  max-width: 700px;
  margin: 20px auto;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #ddd;
}

/* 响应式支持 */
@media (max-width: 768px) {
  .main {
    padding: 20px 10px;
  }

  img {
    width: 95%;
  }

  h1 {
    font-size: 24px;
  }

  h2 {
    font-size: 20px;
  }

  p, li {
    font-size: 14px;
  }
}

</style>