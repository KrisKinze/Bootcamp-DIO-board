@echo off
echo Iniciando o Sistema de Gerenciamento de Board de Tarefas...
echo.
echo Verificando se o Java está instalado...
java -version
if %errorlevel% neq 0 (
    echo ERRO: Java não encontrado! Por favor, instale o Java 17 ou superior.
    pause
    exit /b 1
)

echo.
echo Compilando e executando a aplicação...
gradlew.bat run

echo.
echo Pressione qualquer tecla para fechar...
pause
