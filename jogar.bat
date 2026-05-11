@echo off
title Jogo da Cobrinha - Lançador
echo ==========================================
echo    🐍 INICIANDO O JOGO DA COBRINHA 🐍
echo ==========================================
echo.

:: Verifica se o Java está no PATH
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERRO] Java (JRE/JDK) não encontrado!
    echo.
    echo Para jogar, você precisa do Java instalado.
    echo Baixe em: https://adoptium.net/ (Recomendado: JDK 17 ou superior)
    echo.
    pause
    exit /b
)

:: Verifica se o compilador javac existe (necessário se não houver o .class)
if not exist "JogoCobrinha.class" (
    where javac >nul 2>nul
    if %errorlevel% neq 0 (
        echo [AVISO] Compilador Java (javac) não encontrado e o jogo não está compilado.
        echo Por favor, instale o JDK completo para compilar o código pela primeira vez.
        pause
        exit /b
    )
    echo Compilando o código fonte...
    javac JogoCobrinha.java
    if %errorlevel% neq 0 (
        echo [ERRO] Falha ao compilar o código.
        pause
        exit /b
    )
)

echo Abrindo o jogo...
start /b java JogoCobrinha
echo.
echo Bom jogo!
timeout /t 3 >nul
exit
