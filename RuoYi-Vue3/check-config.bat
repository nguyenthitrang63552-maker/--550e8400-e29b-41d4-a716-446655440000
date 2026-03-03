@echo off
REM 文件管理系统 - 配置检查工具 (Windows 版本)
REM 用途: 验证前后端配置是否正确、安全性检查、部署前验证

setlocal enabledelayedexpansion

set PROJECT_ROOT=%1
if "%PROJECT_ROOT%"=="" set PROJECT_ROOT=.

set BACKEND_FILE=%PROJECT_ROOT%\ruoyi-admin\src\main\java\com\ruoyi\web\controller\system\GetDataController.java
set FRONTEND_FILE=%PROJECT_ROOT%\src\views\testFileOperate\index.vue

set PASS_COUNT=0
set FAIL_COUNT=0
set WARN_COUNT=0

REM ============================================================================
REM 主体检查
REM ============================================================================

cls
echo.
echo ===============================================================================
echo   文件管理系统 - 配置检查工具 (Windows)
echo ===============================================================================
echo.

REM 1. 文件存在性检查
echo [1/9] 文件存在性检查
echo -----------------------------------------------

if exist "%BACKEND_FILE%" (
    echo [✓] 后端文件存在: GetDataController.java
    set /a PASS_COUNT+=1
) else (
    echo [✗] 后端文件不存在: %BACKEND_FILE%
    set /a FAIL_COUNT+=1
)

if exist "%FRONTEND_FILE%" (
    echo [✓] 前端文件存在: testFileOperate/index.vue
    set /a PASS_COUNT+=1
) else (
    echo [✗] 前端文件不存在: %FRONTEND_FILE%
    set /a FAIL_COUNT+=1
)

echo.

REM 2. 后端配置检查
echo [2/9] 后端配置检查
echo -----------------------------------------------

if exist "%BACKEND_FILE%" (
    
    REM 检查 BASE_DIR
    findstr /C:"private static final String BASE_DIR" "%BACKEND_FILE%" >nul
    if !errorlevel! equ 0 (
        echo [✓] 找到 BASE_DIR 定义
        set /a PASS_COUNT+=1
        
        REM 检查是否为默认值
        findstr /C:"C:\Users\nidie\Downloads" "%BACKEND_FILE%" >nul
        if !errorlevel! equ 0 (
            echo [!] 警告: 仍使用默认 Windows 路径
            echo    如果部署到 Linux，请修改为 /home/app/data
            set /a WARN_COUNT+=1
        ) else (
            echo [✓] BASE_DIR 已配置为非默认路径
            set /a PASS_COUNT+=1
        )
    ) else (
        echo [✗] 未找到 BASE_DIR 定义
        set /a FAIL_COUNT+=1
    )
    
    REM 检查 ENABLE_PATH_RESTRICTION
    findstr /C:"ENABLE_PATH_RESTRICTION" "%BACKEND_FILE%" >nul
    if !errorlevel! equ 0 (
        findstr /C:"ENABLE_PATH_RESTRICTION = true" "%BACKEND_FILE%" >nul
        if !errorlevel! equ 0 (
            echo [✓] 路径限制已启用
            set /a PASS_COUNT+=1
        ) else (
            echo [!] 警告: 路径限制已禁用
            set /a WARN_COUNT+=1
        )
    ) else (
        echo [✗] 未找到 ENABLE_PATH_RESTRICTION 定义
        set /a FAIL_COUNT+=1
    )
    
    REM 检查路径验证方法
    findstr /C:"isPathSafe" "%BACKEND_FILE%" >nul
    if !errorlevel! equ 0 (
        echo [✓] 找到路径验证方法: isPathSafe()
        set /a PASS_COUNT+=1
    ) else (
        echo [✗] 缺少路径验证方法
        set /a FAIL_COUNT+=1
    )
)

echo.

REM 3. 前端配置检查
echo [3/9] 前端配置检查
echo -----------------------------------------------

if exist "%FRONTEND_FILE%" (
    
    REM 检查 INITIAL_PATH
    findstr /C:"const INITIAL_PATH" "%FRONTEND_FILE%" >nul
    if !errorlevel! equ 0 (
        echo [✓] 找到 INITIAL_PATH 定义
        set /a PASS_COUNT+=1
    ) else (
        echo [✗] 未找到 INITIAL_PATH 定义
        set /a FAIL_COUNT+=1
    )
    
    REM 检查组件导入
    findstr /C:"ImageViewer" "%FRONTEND_FILE%" >nul
    if !errorlevel! equ 0 (
        echo [✓] 找到组件导入
        set /a PASS_COUNT+=1
    ) else (
        echo [!] 警告: 可能缺少组件导入
        set /a WARN_COUNT+=1
    )
)

echo.

REM 4. 安全特性检查
echo [4/9] 安全特性检查
echo -----------------------------------------------

if exist "%BACKEND_FILE%" (
    findstr /C:"normalize()" "%BACKEND_FILE%" >nul
    if !errorlevel! equ 0 (
        echo [✓] 已实现路径规范化（防目录遍历）
        set /a PASS_COUNT+=1
    ) else (
        echo [!] 警告: 未使用 Path.normalize()
        set /a WARN_COUNT+=1
    )
    
    findstr /C:"URLDecoder" "%BACKEND_FILE%" >nul
    if !errorlevel! equ 0 (
        echo [✓] 已实现 URL 解码
        set /a PASS_COUNT+=1
    ) else (
        echo [!] 警告: 未使用 URLDecoder
        set /a WARN_COUNT+=1
    )
)

echo.

REM 5. 依赖检查
echo [5/9] 依赖检查
echo -----------------------------------------------

if exist "%PROJECT_ROOT%\pom.xml" (
    findstr /C:"commons-io" "%PROJECT_ROOT%\pom.xml" >nul
    if !errorlevel! equ 0 (
        echo [✓] 找到依赖: commons-io
        set /a PASS_COUNT+=1
    ) else (
        echo [!] 警告: 缺少 commons-io
        set /a WARN_COUNT+=1
    )
    
    findstr /C:"poi" "%PROJECT_ROOT%\pom.xml" >nul
    if !errorlevel! equ 0 (
        echo [✓] 找到依赖: apache-poi
        set /a PASS_COUNT+=1
    ) else (
        echo [!] 警告: 缺少 apache-poi
        set /a WARN_COUNT+=1
    )
) else (
    echo [i] 未找到 pom.xml，跳过依赖检查
)

echo.

REM 6. 文档检查
echo [6/9] 文档检查
echo -----------------------------------------------

for %%D in (PATH_SECURITY_CONFIG.md ENVIRONMENT_CONFIG_EXAMPLES.md QUICK_REFERENCE.md CODE_SUMMARY.md) do (
    if exist "%PROJECT_ROOT%\%%D" (
        echo [✓] 找到文档: %%D
        set /a PASS_COUNT+=1
    ) else (
        echo [!] 警告: 缺少文档: %%D
        set /a WARN_COUNT+=1
    )
)

echo.

REM 7. 部署检查清单
echo [7/9] 部署就绪检查
echo -----------------------------------------------

echo 部署前检查清单:
echo   [ ] 后端 BASE_DIR 已配置
echo   [ ] 后端 ENABLE_PATH_RESTRICTION = true
echo   [ ] 前端 INITIAL_PATH 已配置
echo   [ ] 前后端路径一致
echo   [ ] 已建立目标目录结构
echo   [ ] 已设置目录权限
echo   [ ] 已测试路径限制功能
echo   [ ] 已备份原始配置

echo.

REM 8. 总结
echo [8/9] 检查总结
echo -----------------------------------------------

set /a TOTAL=PASS_COUNT+FAIL_COUNT+WARN_COUNT

echo 总检查项: %TOTAL%
echo 通过: %PASS_COUNT%
echo 警告: %WARN_COUNT%
echo 失败: %FAIL_COUNT%

echo.

REM 9. 最终结果
echo [9/9] 最终报告
echo ===============================================================================

if %FAIL_COUNT% equ 0 (
    if %WARN_COUNT% equ 0 (
        echo [✓] 所有检查通过！系统已准备就绪。
        echo.
        exit /b 0
    ) else (
        echo [!] 有 %WARN_COUNT% 个警告，建议检查。
        echo.
        exit /b 0
    )
) else (
    echo [✗] 有 %FAIL_COUNT% 个检查失败，请修复后重试。
    echo.
    exit /b 1
)

endlocal
