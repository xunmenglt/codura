plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.4"
//    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

group = "com.xunmeng"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies{
    implementation("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("org.projectlombok:lombok")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    annotationProcessor("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("commons-io:commons-io:2.5")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.apache.commons:commons-lang3:3.15.0")
    implementation("com.github.jknack:handlebars:4.4.0")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.3.3")
    type.set("IU") // Target IDE Platform
    plugins.set(listOf("com.intellij.java"))
    
}
tasks.named("test"){
    enabled = false
}
tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }
}
