
---

# FATEC IPIRANGA "PASTOR ENÉAS TOGNINI" • LINGUAGEM DE PROGRAMAÇÃO I (2026.2)

## Proposta Técnica de Sistema (E0)

### Documento de Especificação de Requisitos e Planejamento Incremental

**Aluno(a):** Vitória Pereira de Lucena
**RA:** 2040482612040
**Tema Sorteado:** Sistema de Biblioteca

### 1. Apresentação e Contextualização do Tema

O presente documento estabelece a proposta técnica para o desenvolvimento do sistema semestral da disciplina de Linguagem de Programação I. O domínio escolhido para este projeto é o **Sistema de Biblioteca**, estruturado para atender às demandas operacionais de uma biblioteca de pequeno porte, permitindo o controle de seu acervo e dos empréstimos realizados.

O objetivo é automatizar o cadastro e controle dos livros, acompanhar a disponibilidade dos exemplares, registrar o histórico de empréstimos e auxiliar na identificação de livros em atraso, evoluindo de forma incremental ao longo do semestre acadêmico.

O desenvolvimento seguirá o modelo incremental proposto pela disciplina, no qual cada entrega acrescenta novos recursos ao mesmo sistema, acompanhando os conteúdos de programação apresentados durante o semestre. 

### 2. Entidade Central e Atributos Mínimos

A arquitetura de dados orientada a objetos simplificada do sistema baseia-se na entidade central **Livro**, contendo os seguintes atributos fundamentais:

* **codigo (int):** Identificador único do livro no sistema.
* **titulo (String):** Título da obra disponível no acervo.
* **autor (String):** Nome do autor ou autores da obra.
* **quantidadeDisponivel (int):** Quantidade de exemplares disponíveis para empréstimo.
* **status (String):** Estado atual do livro no sistema, como "Disponível" ou "Emprestado".

Esses atributos correspondem à entidade central e aos atributos mínimos definidos para o Tema 4 – Sistema de Biblioteca no projeto da disciplina. 

### 3. Escopo e Diretrizes de Implementação

O sistema será desenvolvido em **linguagem Java**, seguindo progressivamente os conceitos apresentados na disciplina de Linguagem de Programação I.

A implementação será realizada de forma incremental, mantendo uma única base de código que será ampliada a cada entrega, sem reescrever o sistema do zero entre as etapas. Serão utilizados vetores, matrizes, cadeias de caracteres, métodos, modularização e manipulação de arquivos conforme o conteúdo correspondente a cada etapa. 

O projeto também será versionado continuamente por meio do **GitHub**, mantendo o histórico de evolução do código entre as entregas.

### 4. Requisitos Funcionais e Cronograma de Entregas

O sistema será construído de forma modular e incremental, dividindo-se nas cinco etapas previstas no cronograma oficial da disciplina:

| Etapa | Conceito Principal | Funcionalidade Descrita |
|---|---|---|
| **E1** | Vetores (Arrays) | Cadastro de livros, busca por título ou código, identificação do livro mais emprestado por meio de contador simples e listagem dos livros disponíveis. |
| **E2** | Matrizes | Implementação de uma matriz de empréstimos, utilizando as linhas para representar os livros e as colunas para representar os meses, permitindo calcular o total de empréstimos por livro e por mês. |
| **E3** | Strings e Validação | Validação do código dos livros e geração de relatório formatado de livros em atraso, utilizando comparação de datas como strings. |
| **E4** | Métodos e Modularização | Implementação das operações de empréstimo, devolução e busca por meio de métodos com parâmetros e retornos, evitando duplicação de código. |
| **E5** | Manipulação de Arquivos | Persistência do acervo e do histórico de empréstimos em arquivo texto, permitindo que os dados sejam mantidos entre diferentes execuções do sistema. |

### 5. Repositório e Padrão de Versionamento

O projeto será versionado em um **repositório individual e privado no GitHub**, com o docente adicionado como colaborador, seguindo a nomenclatura estabelecida pela disciplina.

**Nome do Repositório:**
`fatec-ipiranga-lpi-2026-2-[RA]-[Sobrenome]`

**Padrão de Arquivos Java:**
`[RA]_[NomeAluno]_Atividade05_Seq[N].java`

O desenvolvimento seguirá o fluxo de versionamento definido para o projeto, com um commit dedicado para cada entrega e uma tag correspondente, permitindo identificar o estado do código em cada etapa. 

As entregas anteriores não serão excluídas, permitindo acompanhar a evolução do sistema ao longo do semestre, conforme exigido pelo projeto incremental. 

### 6. Considerações Finais

Esta proposta técnica apresenta o planejamento inicial do **Sistema de Biblioteca**, definindo sua entidade central, seus atributos fundamentais e as funcionalidades que serão desenvolvidas progressivamente durante as entregas E1 a E5.

O projeto será construído de maneira incremental, incorporando os conceitos de vetores, matrizes, strings, métodos e manipulação de arquivos conforme apresentados na disciplina. Dessa forma, cada entrega representará uma evolução da mesma aplicação, mantendo o histórico do desenvolvimento e permitindo demonstrar a aplicação prática dos conteúdos estudados em Linguagem de Programação I. 

---
