#!/bin/bash

# Caminho do JAR do Gson
GSON_JAR=lib/gson-2.10.1.jar
SQLITE_JAR=lib/sqlite-jdbc-3.50.2.0.jar

# Cria a pasta de saída
mkdir -p bin

# Compila os arquivos .java com suporte à biblioteca Gson
javac -cp ".:$GSON_JAR:$SQLITE_JAR" -d bin src/util/*.java src/models/*.java src/services/*.java src/dao/*.java  src/gui/*.java src/Main.java

# Executa o programa principal com a biblioteca no classpath
cd bin
java -cp ".:../$GSON_JAR:../$SQLITE_JAR" Main
