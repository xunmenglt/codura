# 使用官方 Node.js 作为基础镜像
FROM node:18.19 AS build-stage

# 设置工作目录
WORKDIR /var/src/app

# 复制项目文件到工作目录
COPY ./codura-ui ./

# 接收环境变量
ARG VUE_APP_BASE_API

# 设置国内镜像源以加速依赖安装
RUN npm config set registry https://registry.npmmirror.com

# 安装依赖
RUN npm install

## 设置环境变量
RUN VUE_APP_BASE_API=$VUE_APP_BASE_API

# 构建 Vue 应用
RUN npm run build

# 使用 Nginx 作为生产环境的基础镜像
FROM ccr.ccs.tencentyun.com/qcloud/nginx:1.9 AS production-stage

# 复制构建后的文件到 Nginx 容器中
COPY --from=build-stage /var/src/app/dist /usr/share/nginx/html

# 暴露容器端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
