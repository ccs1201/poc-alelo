@echo off
setlocal enabledelayedexpansion

REM Build do maven
REM mvn package -DskipTests=true

REM Diretórios dos módulos
set MODULES=pagamento-aprovado pagamento-negado pagamento-notification server

REM Nome base das imagens Docker
set IMAGE_NAME_BASE=alelo

for %%M in (%MODULES%) do (
    echo Buildando imagem para o modulo %%M...
    cd %%M

    REM Cria a tag da imagem com base no nome do módulo
    set IMAGE_NAME=!IMAGE_NAME_BASE!-%%M

    docker rmi !IMAGE_NAME!:latest

    REM Executa o build da imagem Docker
    docker build -t !IMAGE_NAME! .

    REM Volta para o diretório raiz
    cd ..
)
mvn clean
echo Build finalizado.