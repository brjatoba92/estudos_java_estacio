# 📚 Sistema Escolar em Java

Este é um projeto educacional de um **Sistema Escolar**, desenvolvido com o objetivo de aprender e aplicar os conceitos fundamentais de **Java** e **Programação Orientada a Objetos (POO)**.

---

## 🔧 Funcionalidades implementadas

### ✅ **Gestão de Entidades**
- Cadastro completo de **alunos**, **professores**, **turmas**, **disciplinas** e **provas**
- Associação de **alunos às turmas** e de **professores às turmas**
- Suporte a **múltiplas disciplinas por professor**
- Campo **ementa** para disciplinas com descrição detalhada

### ✅ **Sistema de Notas e Avaliação**
- Registro de **notas** por aluno e prova
- Cálculo e exibição de **boletim com média final**
- Impressão no console das turmas e alunos

### ✅ **Persistência de Dados**
- **Persistência em JSON** para backup e portabilidade
- **Banco de dados SQLite** para armazenamento principal
- **Sincronização automática** entre JSON e SQLite
- **Migração de dados** entre formatos

### ✅ **Gestão Financeira dos Professores**
- Cálculo automático de **ganhos dos professores** baseado em:
  - Valor por hora (`valorHora`)
  - Carga horária das turmas
  - Número de turmas associadas
- **Controle de horas trabalhadas** por disciplina por mês
- **Interface para inserção manual** de horas trabalhadas
- **Relatórios de ganhos** por professor

### ✅ **Interface de Usuário**
- **Interface gráfica completa** com Swing
- **Menu de console interativo** para todas as operações
- **Frames especializados** para cada entidade:
  - `AlunoFrame` - Gestão de alunos
  - `ProfessorFrame` - Gestão de professores
  - `TurmaFrame` - Gestão de turmas
  - `DisciplinaFrame` - Gestão de disciplinas

### ✅ **Operações CRUD Completas**
- **Create**: Cadastro de todas as entidades
- **Read**: Listagem e consulta de dados
- **Update**: Atualização de informações
- **Delete**: Remoção de registros
- **Sincronização** entre JSON e SQLite

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
│   │   ├── 📄 ProfessorFrame.java   # Interface de gerenciamento de professores
│   │   ├── 📄 DisciplinaFrame.java  # Interface de gerenciamento de disciplinas
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
│   │   ├── 📄 Professor.java       # Modelo de professor (com ganhos e horas)
│   │   ├── 📄 Turma.java           # Modelo de turma
│   │   ├── 📄 Disciplina.java      # Modelo de disciplina (com ementa)
│   │   ├── 📄 Escola.java          # Modelo de escola
│   │   ├── 📄 Nota.java            # Modelo de nota
│   │   └── 📄 Prova.java           # Modelo de prova
│   └── 📁 util/                     # Utilitários
│       ├── 📄 JsonUtil.java        # Utilitário para JSON
│       ├── 📄 Database.java        # Configuração de banco
│       ├── 📄 DatabaseUtil.java    # Utilitários de banco
│       ├── 📄 LocalDateAdapter.java # Adaptador para datas
│       └── 📄 DataUtil.java        # Utilitários de data
├── 📁 bin/                          # Arquivos compilados (.class)
│   ├── 📄 Main.class               # Classe principal compilada
│   ├── 📄 DataImporter.class       # Importador compilado
│   ├── 📄 DataImporter$*.class     # Classes internas do importador
│   ├── 📁 gui/                     # Interface gráfica compilada
│   │   ├── 📄 MainFrame.class
│   │   ├── 📄 AlunoFrame.class
│   │   ├── 📄 DisciplinaFrame.class
│   │   ├── 📄 ProfessorFrame.class
│   │   └── 📄 TurmaFrame.class
│   ├── 📁 services/                # Serviços compilados
│   │   ├── 📄 AlunoService.class
│   │   ├── 📄 ProfessorService.class
│   │   ├── 📄 TurmaService.class
│   │   ├── 📄 DisciplinaService.class
│   │   └── 📄 AlunoDBService.class
│   ├── 📁 dao/                     # DAOs compilados
│   │   ├── 📄 AlunoDAO.class
│   │   ├── 📄 ProfessorDAO.class
│   │   ├── 📄 TurmaDAO.class
│   │   └── 📄 DisciplinaDAO.class
│   ├── 📁 models/                  # Modelos compilados
│   │   ├── 📄 Aluno.class
│   │   ├── 📄 Professor.class
│   │   ├── 📄 Turma.class
│   │   ├── 📄 Disciplina.class
│   │   ├── 📄 Escola.class
│   │   ├── 📄 Nota.class
│   │   └── 📄 Prova.class
│   └── 📁 util/                    # Utilitários compilados
│       ├── 📄 JsonUtil.class
│       ├── 📄 Database.class
│       ├── 📄 DatabaseUtil.class
│       └── 📄 LocalDateAdapter.class
├── 📁 lib/                         # Bibliotecas externas
│   ├── 📄 gson-2.10.1.jar         # Biblioteca Gson para JSON
│   └── 📄 sqlite-jdbc-3.50.2.0.jar # Biblioteca SQLite
├── 📄 compile.sh                   # Script de compilação
├── 📄 escola.db                    # Banco de dados SQLite
└── 📁 .vscode/                     # Configurações do VS Code
```

## 🏗️ **ARQUITETURA DO SISTEMA**

### **Padrão de Arquitetura:**
- **MVC (Model-View-Controller)** com separação clara de responsabilidades
- **DAO Pattern** para acesso a dados
- **Service Layer** para lógica de negócio
- **GUI Layer** para interface gráfica
- **Dependency Injection** para serviços

### **Camadas:**
1. **📱 GUI (View):** Interface gráfica com Swing
2. **⚙️ Services (Controller):** Lógica de negócio e controle
3. **🗄️ DAO (Data Access):** Acesso a dados e persistência
4. **📊 Models (Model):** Entidades e modelos de dados
5. **🔧 Utils:** Utilitários e helpers

### **Funcionalidades Avançadas:**
- ✅ Interface gráfica funcional com múltiplas janelas
- ✅ Menu de console interativo completo
- ✅ Persistência dupla (JSON + SQLite)
- ✅ Cálculo automático de ganhos dos professores
- ✅ Controle de horas trabalhadas por disciplina
- ✅ Campo ementa para disciplinas
- ✅ Sincronização automática entre formatos
- ✅ Estrutura organizada em pacotes
- ✅ Compilação com dependências externas
- ✅ Gerenciamento completo de CRUD

---

## 📊 **Modelos de Dados**

### **Professor**
- `matricula`: Identificação única
- `nome`: Nome completo
- `valorHora`: Valor cobrado por hora
- `disciplinas`: Lista de disciplinas que leciona
- `ganhos`: Cálculo automático baseado em turmas e valorHora
- `horasTrabalhadasMes`: Total de horas trabalhadas no mês
- `horasPorDisciplinaMes`: Map com horas por disciplina

### **Disciplina**
- `codigo`: Código único da disciplina
- `nome`: Nome da disciplina
- `ementa`: Descrição detalhada do conteúdo
- `cargaHoraria`: Carga horária total

### **Turma**
- `codigo`: Código único da turma
- `serie`: Série/Período da turma
- `matriculaProfessor`: Professor responsável
- `cargaHoraria`: Carga horária da turma
- `codigoDisciplina`: Disciplina associada

### **Aluno**
- `matricula`: Identificação única
- `nome`: Nome completo
- `dataNascimento`: Data de nascimento
- `turmas`: Lista de turmas matriculadas

---

## 📌 Tecnologias e conceitos usados

- ☕ **Java 17+**
- 📦 **Programação Orientada a Objetos (POO)**
- 🎯 **Encapsulamento, composição, listas (`ArrayList`)**
- 📆 **Manipulação de datas com `LocalDate`**
- 🗄️ **SQLite** para persistência de dados
- 📄 **JSON** para backup e portabilidade
- 🎨 **Swing** para interface gráfica
- 🔧 **Dependency Injection** para serviços
- 📊 **Maps e Collections** para estruturas de dados

---

## 🚀 Como executar

### **1. Compilação**
```bash
chmod +x compile.sh
./compile.sh
```

### **2. Execução**
```bash
java -cp "bin:lib/*" Main
```

### **3. Acesso ao Banco de Dados**
```bash
sqlite3 escola.db
```

### **4. Comandos Úteis do SQLite**
```sql
-- Ver todas as tabelas
.tables

-- Ver estrutura da tabela professores
.schema professores

-- Ver dados dos professores
SELECT * FROM professores;

-- Ver ganhos calculados
SELECT nome, ganhos FROM professores;
```

---

## 🎯 **Funcionalidades Principais**

### **Gestão de Professores**
- Cadastro com valor por hora
- Associação a múltiplas disciplinas
- Cálculo automático de ganhos
- Controle de horas trabalhadas por disciplina

### **Gestão de Disciplinas**
- Cadastro com ementa detalhada
- Associação a professores
- Controle de carga horária

### **Gestão de Turmas**
- Criação de turmas por série/período
- Associação de professores e disciplinas
- Controle de carga horária

### **Gestão de Alunos**
- Cadastro completo
- Matrícula em turmas
- Controle de notas e boletim

### **Relatórios e Consultas**
- Ganhos dos professores
- Horas trabalhadas por disciplina
- Listagem de turmas e alunos
- Boletins com médias

---

## 🔄 **Sincronização de Dados**

O sistema mantém sincronização automática entre:
- **JSON** (backup e portabilidade)
- **SQLite** (banco principal)

Todas as operações CRUD são refletidas em ambos os formatos automaticamente.

---

## 📝 **Notas de Desenvolvimento**

- Sistema desenvolvido para fins educacionais
- Arquitetura escalável e bem estruturada
- Código comentado e organizado
- Pronto para extensões futuras
