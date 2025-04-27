@echo off

set ITEM_DIR=.\codura-server
set ROOT_DIR=%~dp0\..\..

pushd %ROOT_DIR%

echo Building %ITEM_DIR% project...
cd %ITEM_DIR%

call mvn clean package -Dmaven.test.skip=true

if %ERRORLEVEL% equ 0 (
    echo %ITEM_DIR% build successful!
) else (
    echo %ITEM_DIR% build failed!
    exit /b 1
)

set JAR_FILE=xunmeng-admin\target\*.jar

ren "%JAR_FILE%" codura-server.jar

if %ERRORLEVEL% equ 0 (
    echo JAR file renamed successfully to codura-server.jar
) else (
    echo Failed to rename JAR file.
    exit /b 1
)

cd ..
echo Current Dir is %CD%
echo Original project jar package location: %ITEM_DIR%\xunmeng-admin\target\codura-server.jar
echo Target project jar package location: %CD%\scripts\docker\server\codura-server.jar

move %ITEM_DIR%\xunmeng-admin\target\codura-server.jar "%CD%\scripts\docker\server\codura-server.jar"

if %ERRORLEVEL% equ 0 (
    echo JAR file moved to root directory successfully.
) else (
    echo Failed to move JAR file.
    exit /b 1
)

popd

echo Build process for %ITEM_DIR% completed.
