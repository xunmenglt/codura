export default{
    methods: {
        str2json(json_str){
            let json_data={}
            if(!json_str) return json_data
            if(typeof(json_str)!=='object'){
                try{
                    json_data=JSON.parse(json_str)
                }catch(e){
                    console.log("str to json error")
                }
            }else{
                json_data=json_str
            }
            return json_data
        },
        getCompletionContent(message){
            if(message.value.error && message.value.errorMessage){
                return message.value.errorMessage
            }
            return message.value.data.content || this.EMPTY_MESAGE
        }
    },
}