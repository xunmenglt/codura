'use strict'
const path = require('path')
const { defineConfig } = require('@vue/cli-service')
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')
function resolve(dir) {
  return path.join(__dirname, dir)
}
// 请求基础路径
process.env.VUE_APP_BASE_API="http://localhost:8080"
process.env.VUE_APP_BOFRE_API="http://localhost:8081"
process.env.VUE_APP_NAME="Codura"
module.exports = defineConfig({
  pages:{
    index:{
      entry:'src/main.js',
      title:'Codura · 让每一行代码更聪明'
    }
  },
  devServer:{
    port: 8082,
    // 跨域代理
    proxy:{
      '/api': { // 代理标识符，匹配以 /api 开头的请求
        target: process.env.VUE_APP_BASE_API, // 目标服务器地址
        changeOrigin: true, // 是否跨域
        pathRewrite: { '^/api': '' }, // 将 /api 重写为空
      },
    }
  },
  transpileDependencies: [
    'vuetify'
  ],
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  outputDir: 'dist',
  // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  assetsDir: 'static',
  css: {
    loaderOptions: {
      sass: {
        sassOptions: {outputStyle: "expanded" }
      }
    }
  },
  configureWebpack: {
    plugins: [new NodePolyfillPlugin()],
  },
  chainWebpack(config) {
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    config.when(process.env.NODE_ENV !== 'development', config => {

          config.optimization.splitChunks({
            chunks: 'all',
            cacheGroups: {
              libs: {
                name: 'chunk-libs',
                test: /[\\/]node_modules[\\/]/,
                priority: 10,
                chunks: 'initial' // only package third parties that are initially dependent
              },
              elementUI: {
                name: 'chunk-elementUI', // split elementUI into a single package
                test: /[\\/]node_modules[\\/]_?element-ui(.*)/, // in order to adapt to cnpm
                priority: 20 // the weight needs to be larger than libs and app or it will be packaged into libs or app
              },
              commons: {
                name: 'chunk-commons',
                test: resolve('src/components'), // can customize your rules
                minChunks: 3, //  minimum common number
                priority: 5,
                reuseExistingChunk: true
              }
            }
          })
          config.optimization.runtimeChunk('single')
    })
  },

})