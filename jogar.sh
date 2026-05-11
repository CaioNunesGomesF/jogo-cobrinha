#!/bin/bash

# Cores para o terminal
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}==========================================${NC}"
echo -e "${GREEN}   🐍 INICIANDO O JOGO DA COBRINHA 🐍    ${NC}"
echo -e "${GREEN}==========================================${NC}"

# Verifica se o Java está instalado
if ! command -v java &> /dev/null
then
    echo -e "${RED}[ERRO] Java não encontrado!${NC}"
    echo "Por favor, instale o Java (JRE/JDK)."
    echo "No Ubuntu/Debian: sudo apt install default-jdk"
    exit
fi

# Compila se necessário
if [ ! -f "JogoCobrinha.class" ]; then
    if ! command -v javac &> /dev/null
    then
        echo -e "${RED}[ERRO] Compilador javac não encontrado!${NC}"
        echo "Instale o JDK para compilar o jogo."
        exit
    fi
    echo "Compilando o código..."
    javac JogoCobrinha.java
fi

echo "Iniciando o jogo..."
java JogoCobrinha &
echo -e "${GREEN}Bom jogo!${NC}"
sleep 2
exit
