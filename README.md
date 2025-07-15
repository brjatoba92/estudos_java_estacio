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

## 📁 **ESTRUTURA ATUAL DO PROJETO**

```
sistema_escolar/
├── 📁 src/                          # Código fonte
│   ├── 📄 Main.java                 # Classe principal com interface gráfica e console
│   ├── 📄 DataImporter.java         # Importador de dados iniciais
│   ├── 📁 gui/                      # Interface gráfica
│   │   ├── 📄 MainFrame.java        # Janela principal do sistema
│   │   ├── 📄 AlunoFrame.java       # Interface de gerenciamento de alunos
│   │   └── 📄 ProfessorFrame.java   # Interface de gerenciamento de professores
│   │   └── 📄 DisciplinaFrame.java  # Interface de gerenciamento de disciplinas
│   │   └── 📄 TurmaFrame.java       # Interface de gerenciamento de turmas
│   ├── 📁 services/                 # Camada de serviços (lógica de negócio)
│   │   ├── 📄 AlunoService.java     # Serviços para alunos
│   │   ├── 📄 ProfessorService.java # Serviços para professores
│   │   ├── 📄 TurmaService.java     # Serviços para turmas
│   │   ├── 📄 DisciplinaService.java # Serviços para disciplinas
│   │   └── 📄 AlunoDBService.java   # Serviços de banco para alunos
│   ├── 📁 dao/                      # Camada de acesso a dados (DAO)
│   │   ├── 📄 AlunoDAO.java         # Acesso a dados de alunos
│   │   ├── 📄 ProfessorDAO.java     # Acesso a dados de professores
│   │   ├── 📄 TurmaDAO.java         # Acesso a dados de turmas
│   │   └── 📄 DisciplinaDAO.java    # Acesso a dados de disciplinas
│   ├── 📁 models/                   # Modelos de dados (entidades)
│   │   ├── 📄 Aluno.java           # Modelo de aluno
│   │   ├── 📄 Professor.java       # Modelo de professor
│   │   ├── 📄 Turma.java           # Modelo de turma
│   │   ├── 📄 Disciplina.java      # Modelo de disciplina
│   │   ├── 📄 Escola.java          # Modelo de escola
│   │   ├── 📄 Nota.java            # Modelo de nota
│   │   └── 📄 Prova.java           # Modelo de prova
│   └── 📁 util/                     # Utilitários
│       ├── 📄 JsonUtil.java        # Utilitário para JSON
│       ├── 📄 Database.java        # Configuração de banco
│       ├── 📄 DatabaseUtil.java    # Utilitários de banco
│       ├── 📄 LocalDateAdapter.java # Adaptador para datas
│       └── 📄 DataUtil.java        # Utilitários de data (vazio)
├── 📁 bin/                          # Arquivos compilados (.class)
│   ├── 📄 Main.class               # Classe principal compilada
│   ├──  DataImporter.class       # Importador compilado
│   ├── 📄 DataImporter$*.class     # Classes internas do importador
│   ├── 📁 gui/                     # Interface gráfica compilada
│   │   ├── 📄 MainFrame.class
│   │   ├── 📄 AlunoFrame.class
│   │   ├── 📄 DisciplinaFrame.class
│   │   └── 📄 ProfessorFrame.class
│   │   └── 📄 TurmaFrame.class
│   ├── 📁 services/                # Serviços compilados
│   │   ├──  AlunoService.class
│   │   ├── 📄 ProfessorService.class
│   │   ├──  TurmaService.class
│   │   ├── 📄 DisciplinaService.class
│   │   └── 📄 AlunoDBService.class
│   ├── 📁 dao/                     # DAOs compilados
│   │   ├──  AlunoDAO.class
│   │   ├──  ProfessorDAO.class
│   │   ├──  TurmaDAO.class
│   │   └── DisciplinaDAO.class
│   ├── 📁 models/                  # Modelos compilados
│   │   ├── Aluno.class
│   │   ├── Professor.class
│   │   ├── Turma.class
│   │   ├── Disciplina.class
│   │   ├── Escola.class
│   │   ├── Nota.class
│   │   └── Prova.class
│   └── 📁 util/                    # Utilitários compilados
│       ├── JsonUtil.class
│       ├── Database.class
│       ├── DatabaseUtil.class
│       └── LocalDateAdapter.class
├── 📁 lib/                         # Bibliotecas externas
│   └── 📄 gson-2.10.1.jar         # Biblioteca Gson para JSON
    └── 📄 sqlite-jdbc-3.50.2.0.jar         # Biblioteca SQLite
├── 📄 compile.sh                   # Script de compilação
└── 📁 .vscode/                     # Configurações do VS Code
```

## 🏗️ **ARQUITETURA DO SISTEMA**

### **Padrão de Arquitetura:**
- **MVC (Model-View-Controller)** com separação clara de responsabilidades
- **DAO Pattern** para acesso a dados
- **Service Layer** para lógica de negócio
- **GUI Layer** para interface gráfica

### **Camadas:**
1. **📱 GUI (View):** Interface gráfica com Swing
2. **⚙️ Services (Controller):** Lógica de negócio e controle
3. **🗄️ DAO (Data Access):** Acesso a dados e persistência
4. **📊 Models (Model):** Entidades e modelos de dados
5. **🔧 Utils:** Utilitários e helpers

### **Funcionalidades:**
- ✅ Interface gráfica funcional
- ✅ Menu de console interativo
- ✅ Persistência em JSON
- ✅ Gerenciamento de alunos, professores, turmas e disciplinas
- ✅ Estrutura organizada em pacotes
- ✅ Compilação com dependências externas

Esta estrutura está pronta para ser copiada e colada no seu README!

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
