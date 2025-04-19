import Vue from 'vue'

import {TOAST_POSITION,TOAST_TYPE } from './Constants'



export const showTextMessage=(type,content,position=undefined)=>{
    const option= {
        position: position || "top-center",
        timeout: 2000,
        closeOnClick: true,
        pauseOnFocusLoss: true,
        pauseOnHover: true,
        draggable: true,
        draggablePercent: 0.6,
        showCloseButtonOnHover: true,
        hideProgressBar: true,
        closeButton: false,
        icon: true,
        rtl: false
    }
    if(type===TOAST_TYPE.success){
        Vue.$toast.success(content,option)
    }else if(type===TOAST_TYPE.info){
        Vue.$toast.info(content,option)
    }else if(type===TOAST_TYPE.warning){
        Vue.$toast.warning(content,option)
    }else if(type===TOAST_TYPE.error){
        Vue.$toast.error(content,option)
    }else{
        Vue.$toast(content,option)
    }
}
