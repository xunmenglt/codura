// config repositories
pluginManagement {
    repositories {
        maven (url="https://maven.aliyun.com/repository/public")
        maven (url="https://maven.aliyun.com/repository/gradle-plugin")
        mavenCentral()
//        maven ("https://maven.aliyun.com/repository/public" )
//        maven ("https://nexus.bsdn.org/content/groups/public/" )
        gradlePluginPortal()
    }
}
rootProject.name = "Codura"
