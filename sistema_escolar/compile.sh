#!/bin/bash
mkdir -p bin
javac -d bin src/models/*.java src/Main.java
cd bin
java Main
