<template>
    <div id="codealien-app">
        <transition name="fade-transform" mode="out-in">
            <router-view />
        </transition>
    </div>
</template>

<script>
export default {
    
}
</script>

<style lang="less" scoped>
    @import './index.css';
</style>