#!/bin/bash

# Diretórios dos módulos
MODULES=("pagamento-aprovado" "pagamento-negado" "server")

# Nome base das imagens Docker
IMAGE_NAME_BASE="couza"

# Loop pelos módulos
for MODULE in "${MODULES[@]}"
do
    echo "Buildando imagem para o modulo $MODULE..."
    cd $MODULE || exit 1

    # Cria a tag da imagem com base no nome do módulo
    IMAGE_NAME="$IMAGE_NAME_BASE-$MODULE"

    # Executa o build da imagem Docker
    docker build -t "$IMAGE_NAME" .

    # Volta para o diretório raiz
    cd ..
done

echo "Build finalizado."
