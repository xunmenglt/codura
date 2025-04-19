# docker运行脚本
通过本教程你可以通过docker快速构建本代码辅助平台的后管系统。
## 模块解释
**（1）devassist-ui**

* 描述：该模块为后管平台的web程序，该模块运行成功之后可为用户提供一个功能丰富其可操作的ui界面。
* 技术点：VUE2、Axios、ElementUI

**（2）devassist-server**

* 描述：该模块为后管平台提供全面的API服务
* 技术点：SpringBoot、Mybatis、SpringSecurity
* 数据存储：MySQL、Redis

## docker镜像构建脚本描述
* 脚本存放目录：```项目根目录/scripts/docker```
* devassist-server镜像构建脚本：```项目根目录/scripts/docker/server```
* devassist-ui镜像构建脚步：```项目根目录/scripts/docker/ui```

## 构建流程
（1）构建后管平台后台服务运行程序
* 执行以下命令进入工作目录

  ```shell
  cd /项目根目录
  ```

* 按如下方式执行脚本文件（注意执行前请确保本机包含Maven以及JDK1.8环境，请参考SpringBoot服务配置）

  * windows

    ```
    .\scripts\docker\server_build.bat
    ```

  * linux

    ```shell
    chmod +x .\scripts\docker\server_build.sh
    .\scripts\docker\server_build.sh
    ```

* 最终可在```项目根目录/scripts/docker/server```目录下可看到```devassist-server.jar```java程序

（2）前台WebUI镜像构建

* 将```项目根目录/devassist-ui```目录移动到```项目根目录/scripts/docker/ui```目录下

（3）一键构建镜像

* 切换到```项目根目录/scripts/docker```目录下执行以下命令即可构建程序并运行程序

```shell
docker-compose -up
```