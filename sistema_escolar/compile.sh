#!/bin/bash

# Caminho do JAR do Gson
GSON_JAR=lib/gson-2.10.1.jar

# Cria a pasta de saída
mkdir -p bin

# Compila os arquivos .java com suporte à biblioteca Gson
javac -cp ".:$GSON_JAR" -d bin src/util/*.java src/models/*.java src/Main.java

# Executa o programa principal com a biblioteca no classpath
cd bin
java -cp ".:../$GSON_JAR" Main
