# 使用 OpenJDK 1.8 作为基础镜像
FROM anolis-registry.cn-zhangjiakou.cr.aliyuncs.com/openanolis/openjdk:8-8.6

# 设置工作目录
WORKDIR /var

# 设置环境变量，确保容器使用 UTF-8 编码
ENV LANG C.UTF-8
ENV LC_ALL C.UTF-8

# 将 Spring Boot JAR 文件复制到容器中
COPY ./*.jar /var/app.jar

# 将本机的 application.yml 配置文件复制到容器中的指定目录
COPY ./config/application.yml /var/config/application.yml
COPY ./config/application-druid.yml /var/config/application-druid.yml

# 构建挂载点
VOLUME ["/var/config","/var/data"]

# 暴露容器的 8080 端口
EXPOSE 8080

# 设置环境变量，指定配置文件路径
ENV SPRING_CONFIG_LOCATION=/var/config/application.yml

# 启动 Spring Boot 应用时指定配置文件位置
CMD ["java", "-jar", "/var/app.jar", "--spring.config.location=${SPRING_CONFIG_LOCATION}","--xunmeng.profile=/var/data/uploadPath"]
