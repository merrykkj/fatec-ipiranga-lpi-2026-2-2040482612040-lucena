# Prototipagem do Sistema e Mapeamento de Telas

**Nome:** Vitória Pereira De Lucuna  
**RA:** 2040482612040  
**Tema:** Sistema de Biblioteca

---

## 1. Mapa do Menu Principal e Telas do Sistema
O fluxo textual de navegação do programa estruturado para o console:

### Menu Principal
```text
==================================================
        SISTEMA DE BIBLIOTECA
==================================================
[1] Cadastrar Novo Livro
[2] Registrar Empréstimo
[3] Relatório de Empréstimos em Atraso
[4] Empréstimos Mensais
[0] Sair
==================================================

```
### Tela de Cadastro de Livro
```text
--------------------------------------------------
          CADASTRAR NOVO LIVRO
--------------------------------------------------
Digite o código do livro (Ex: LV-2026-001): lv123
[ERRO] Código inválido! O código deve seguir o padrão LB-AAAA-NNN. Tente novamente.

Digite o código do livro (Ex: LV-2026-001): LV-2026-045
Digite o nome do livro: Engenharia de Software
Quantidade em estoque: 5

[SUCESSO] Livro cadastrado com sucesso!
--------------------------------------------------

```
### Tela de Relatório de Empréstimo

```text
--------------------------------------------------
        RELATÓRIO: EMPRÉSTIMOS EM ATRASO
--------------------------------------------------
CÓDIGO         TÍTULO DO LIVRO            RA DO ALUNO     DIAS ATRASO
--------------------------------------------------
LV-2026-012    Estrutura de Dados         2040482612040   5 dias
LV-2026-033    Banco de Dados Avançado    2040482612099   2 dias
--------------------------------------------------

```

### Tela de Empréstimos Mensais
```text

--------------------------------------------------
              EMPRÉSTIMOS MENSAIS
--------------------------------------------------
Mês       | Total Empréstimos | Devolvidos | Pendentes
--------------------------------------------------
Janeiro   | 15                | 12         | 3
Fevereiro | 22                | 20         | 2
Março     | 18                | 15         | 3
--------------------------------------------------
Pressione qualquer tecla para voltar ao menu...

```
---

## 2. Regras de Validação de Entrada

* **Código do Livro:** Obrigatório seguir estritamente o formato padrão `LV-AAAA-NNN` (onde `LV-` é o prefixo fixo, `AAAA` representa o ano e `NNN` o número sequencial de 3 dígitos). Se o formato não estiver correto, o sistema mostra uma mensagem de erro.

* **Quantidade de Estoque:** Deve ser um valor inteiro maior ou igual a `0`.

* **RA do Aluno:** Deve conter exatamente os dígitos numéricos correspondentes ao padrão institucional (ex: 13 dígitos para o RA acadêmico).
---

## 3. Assinatura dos Métodos

```java
// 1. Valida o formato do código do livro informado
public static boolean validarCodigoLivro(String codigo);

// 2. Cadastra um novo livro nos vetores do acervo
public static void cadastrarLivro(String[] codigos, String[] titulos, int[] estoque, int totalLivros);

// 3. Exibe o relatório formatado de empréstimos em atraso
public static void exibirRelatorioAtrasos(String[] codigosAtraso, String[] titulosAtraso, String[] raAlunos, int[] diasAtraso);
```