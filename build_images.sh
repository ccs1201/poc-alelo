#!/bin/bash

mvn clean package -DskipTests=true

# Diretórios dos módulos
MODULES=("pagamento-aprovado" "pagamento-negado" "pagamento-notification" "server")

# Nome base das imagens Docker
IMAGE_NAME_BASE="alelo"

# Loop pelos módulos
for MODULE in "${MODULES[@]}"
do
    echo "Buildando imagem para o modulo $MODULE..."
    cd $MODULE || exit 1

    # Cria a tag da imagem com base no nome do módulo
    IMAGE_NAME="$IMAGE_NAME_BASE-$MODULE"

    # Remove a imagem existente, se houver
    docker rmi "$IMAGE_NAME":latest > /dev/null 2>&1

    # Executa o build da imagem Docker
    docker build -t "$IMAGE_NAME" .

    # Volta para o diretório raiz
    cd ..
done

mvn clean

echo "Build finalizado."
