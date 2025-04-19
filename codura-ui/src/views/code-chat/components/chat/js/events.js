const EVENT_NAME={
    READY:"chat_ready",
    InitConversation:"init_conversation",
    SetSelectedSnippet:"chat_set_selected_snippet",
    ChatMessage:"chat_message",
    StopGeneration:"stop_generation",
    AddMessage:"add_message",
    OnCompletion:"on_completion",
    OnCompleEnd:"on_end",
    SaveConversation:"save_conversation",
    OnGenerateLimit:"on_generate_limit",
    FlashUserInfo:"flash_user_info",
    GetUserInfo:"get_user_info",
    Login:"login",
    Logout:"logout",
    LogoutFail:"logout_fail",
    LogoutSuccess:"logout_success",
    LoginFail:"login_fail",
    AuthFail:"auth_fail",

}
export const EVENT_TYPES=EVENT_NAME
export default {
    data() {
        return {
            // 选择文本
            selectedText:"",
            // 选择文本长度
            selectedTextLength:0,
            eventHandlers:{}
        }
    },
    methods: {
        initCompletion(){
            this.completion={
                role: "",
                content: "",
                type:"",
                language: {},
                error:"" 
            }
        },
        initHandlers(){
            this.eventHandlers={
                [EVENT_NAME.InitConversation]:this.handleFlashConversation,
                [EVENT_NAME.AddMessage]:this.handleAddMessage,
                [EVENT_NAME.OnLoading]:()=>{},
                [EVENT_NAME.OnCompletion]:this.handleCompletionMessage,
                [EVENT_NAME.OnCompleEnd]:this.handleCompletionEnd,
                [EVENT_NAME.StopGeneration]:()=>{},
                [EVENT_NAME.SetSelectedSnippet]:this.handleSetSelectedSnippet,
                [EVENT_NAME.OnGenerateLimit]:this.handleGenerateLimit
            }
        },
        postMessage(data){
            let msg={}
            msg=this.str2json(data)
            if(window.postIntellijMessage){
                window.postIntellijMessage(msg)
            }
        },
        messageEventHandler(event){
            const eventMsg = event.data
            let handler=this.eventHandlers[eventMsg.type]
            if (handler){
                handler(event.data)
            }
        },

        // 处理请求受限事件
        handleGenerateLimit(event){
            this.initCompletion()
            setTimeout(()=>{
                this.generating=false
                this.$refs.chatInputRef.focus()
            },200)
        },
        // 添加message
        handleAddMessage(event){
            if(event && event.value && event.value.data){
                let message=event
                const content = this.getCompletionContent(message);
                this.conversation.messages=[...(this.conversation.messages || []),{ role: this.USER, content: content }]
                setTimeout(()=>{
                    this.scrollToBottom()
                },200)
            }
        },
        // 刷新对话信息
        handleFlashConversation(event){
            if(event && event.value && event.value.data){
                this.conversation=event.value.data
            }
        },
        // 处理剪贴版信息
        handleSetSelectedSnippet(event){
            if(event){
                if(event.value && event.value.data){
                    this.selectedText=event.value.data.code
                    this.selectedTextLength=this.selectedText?this.selectedText.length:0
                }
            }
        },
        // 处理发送信息
        handleSubmitForm(){
            if(this.inputText){
                let input=this.inputText
                this.generating=true
                this.inputText=''
                this.postMessage({
                    type:EVENT_NAME.ChatMessage,
                    data:[
                        ...(this.conversation.messages||[]),
                        {
                            role:this.USER,
                            content:input
                        }
                    ]
                })
                this.conversation.messages=[...(this.conversation.messages || []),{ role: this.USER, content: input }]
                this.scrollToBottom()
            }
        },
        handleCompletionMessage(event){
            if(event && event.value && event.value.data){
                this.generating=true
                let message=event
                this.completion={
                    role: this.ASSISTANT,
                    content: this.getCompletionContent(message),
                    type: message.value.data.type,
                    language: message.value.data.language,
                    error: message.value.error
                }
                this.scrollToBottom()
            }
        },
        handleCompletionEnd(event){
            if(event && event.value && event.value.data){
                const messages=[
                    ...(this.conversation.messages||[]),
                    {
                        role: this.ASSISTANT,
                        content: this.getCompletionContent(event)
                    }
                ]
                this.conversation.messages=messages
                this.saveLastConversation({
                    ...this.conversation,
                    messages: messages
                })
            }
            this.initCompletion()
            let _this=this
            setTimeout(()=>{
                _this.$refs.chatInputRef.focus()
                _this.generating=false
                _this.scrollToBottom()
            },200)
        },
        handleStopGeneration(){
            this.postMessage({
                type:EVENT_NAME.StopGeneration
            })
        },
        sendReadyMsgToServer(){
            this.postMessage({
                type:EVENT_NAME.READY
            })
        },
        saveLastConversation(conversation){
            this.postMessage({
                type:EVENT_NAME.SaveConversation,
                data:conversation
            })
        },

        addEventHandler(eventType,handler){
            if (eventType && handler){
                this.eventHandlers[eventType]=handler
            }
        }
    },
    created() {
        this.initHandlers()
        // 创建消息监听函数，接受服务器发送过来的消息
        window.addEventListener("message",this.messageEventHandler)
        // 发送准备信息给后端，请求对应的服务
        this.sendReadyMsgToServer()
    },
    destroyed(){
        // 移出消息监听函数
        window.removeEventListener("message",this.messageEventHandler)
    },
    
}