import indexJs from './index.js'
import constants from './constants.js'
import events from './events.js'
import utils from './utils.js'
import user from './user.js'

import CodealienImage from '@/assets/images/code-chat-images/logo.svg'
import UserImage from '@/assets/images/code-chat-images/user.svg'
import ScrollBotomImage from '@/assets/images/code-chat-images/scrollBotom.svg'
import StopImage from '@/assets/images/code-chat-images/stop.svg'


export default{
    name:"code-chat",
    mixins:[indexJs,constants,utils,events,user],
    data() {
        return {
            CodealienImage:CodealienImage,
            UserImage:UserImage,
            ScrollBotomImage:ScrollBotomImage,
            StopImage:StopImage
        }
    },
    created() {
        
    },
    methods: {
        toLogin(){
            this.$router.push('/code-chat/login')
        },
        
        toTutorial(){
            this.$router.push('/code-chat/tutorial')
        }
    },
}