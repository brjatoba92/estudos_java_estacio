# 📚 Sistema Escolar em Java

Este é um projeto educacional de um **Sistema Escolar**, desenvolvido com o objetivo de aprender e aplicar os conceitos fundamentais de **Java** e **Programação Orientada a Objetos (POO)**.

---

## 🔧 Funcionalidades já implementadas

- ✅ Cadastro de **alunos**, **professores**, **turmas**, **disciplinas** e **provas**;
- ✅ Associação de **alunos às turmas** e de **professores às turmas**;
- ✅ Registro de **notas** por aluno e prova;
- ✅ Cálculo e exibição de **boletim com média final**;
- ✅ Impressão no console das turmas e alunos.

---

## 🏗️ Estrutura do projeto

sistema_escolar/
├── bin/
│ ├── models/  (Aluno.class, Disciplina.class, ...)
│ ├── Main.class
├── src/
│ ├── models/
│ │ ├── Aluno.java
│ │ ├── Professor.java
│ │ ├── Turma.java
│ │ ├── Disciplina.java
│ │ ├── Prova.java
│ │ └── Nota.java
│ └── main.java
├── compile.sh (executar os comandos)


---

## 📌 Tecnologias e conceitos usados

- ☕ **Java 17+**
- 📦 Programação Orientada a Objetos (POO)
- 🎯 Encapsulamento, composição, listas (`ArrayList`)
- 📆 Manipulação de datas com `LocalDate`

---

## 🚀 Como executar

1. Compile o projeto:

```bash
javac src/models/*.java src/main.java
```

2. Execute
```bash
java -cp src Main
```bash

OBS: Os comandos acima foram inclusos no executavel compile.sh

```bash
chmod +x compile.sh
```bash

```bash
compile.sh
```bash
