#!/bin/bash

# 设置变量
ITEM_DIR="./devassist-server"
ROOT_DIR="$(dirname "$(pwd)")/.."

# 进入根目录
cd "$ROOT_DIR" || exit 1

echo "Building $ITEM_DIR project..."

# 进入项目目录
cd "$ITEM_DIR" || exit 1

# 执行 Maven 构建命令
mvn clean package -Dmaven.test.skip=true

# 检查 Maven 构建结果
if [ $? -eq 0 ]; then
    echo "$ITEM_DIR build successful!"
else
    echo "$ITEM_DIR build failed!"
    exit 1
fi

# 设置 JAR 文件路径
JAR_FILE="xunmeng-admin/target/*.jar"

# 重命名 JAR 文件
mv "$JAR_FILE" "devassist-server.jar"

# 检查重命名结果
if [ $? -eq 0 ]; then
    echo "JAR file renamed successfully to devassist-server.jar"
else
    echo "Failed to rename JAR file."
    exit 1
fi

# 返回根目录
cd .. || exit 1

# 输出当前目录信息
echo "Current Dir is $(pwd)"
echo "Original project jar package location: $ITEM_DIR/xunmeng-admin/target/devassist-server.jar"
echo "Target project jar package location: $(pwd)/devassist-server.jar"

# 移动 JAR 文件到根目录
mv "$ITEM_DIR/xunmeng-admin/target/devassist-server.jar" "$(pwd)/devassist-server.jar"

# 检查文件移动结果
if [ $? -eq 0 ]; then
    echo "JAR file moved to root directory successfully."
else
    echo "Failed to move JAR file."
    exit 1
fi

echo "Build process for $ITEM_DIR completed."
