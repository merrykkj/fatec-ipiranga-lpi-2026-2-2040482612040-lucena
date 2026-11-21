import java.util.InputMismatchException;
import java.util.Scanner;
class SistemaDeBiblioteca
{
	static final int MAX_LIVROS = 200;
	static final int MAX_LEITORES = 100;
	static final int MAX_EMPRESTIMOS = 500;
	static final int ANO_ATUAL = 2026;

	static final double MULTA_POR_DIA = 0.50;

	static final String[] NOMES_CATEGORIAS = {
		"Literatura", "Didático", "Técnico",

		"Infantojuvenil", "Outros"
	};

	static final String[] NOMES_TIPOS = {
		"Estudante", "Professor", "Comunidade"
	};

	static final int[] PRAZO_POR_TIPO = {7, 14, 7}; // dias
	static final int[] LIMITE_POR_TIPO = {2, 4, 1}; // livros simultâneos

	static Scanner sc = new Scanner(System.in);
	// Os dados de cada livro ficam na mesma posição dos vetores.
	static int[] codigoLivros = new int[MAX_LIVROS];
	static String[] tituloLivros = new String[MAX_LIVROS];
	static String[] autoresLivros = new String[MAX_LIVROS];
	static int[] categoriasLivros = new int[MAX_LIVROS];
	static int[] anosPublicacao = new int[MAX_LIVROS];
	static int[] totalExemplares = new int[MAX_LIVROS];
	static int[] exemplaresDisponiveis = new int[MAX_LIVROS];
	static int[] contadorEmprestimos = new int[MAX_LIVROS];

	// Os dados de cada leitor ficam na mesma posição dos vetores.
	static int[] codigoLeitores = new int [MAX_LEITORES];
	static String[] nomeLeitores = new String[MAX_LEITORES];
	static int[] tipoLeitores = new int[MAX_LEITORES];
	static int[] emprestimosEmAberto = new int [MAX_LEITORES];

	// Guarda os dados de cada empréstimo, incluindo prazo, devolução e multa.
	static int[] codigosEmprestimos = new int [MAX_EMPRESTIMOS];
	static int[] empCodigoLivro = new int [MAX_EMPRESTIMOS];
	static int[] empCodigoLeitor = new int [MAX_EMPRESTIMOS];
	static int[] empDiaEmprestimo = new int [MAX_EMPRESTIMOS];
	static int[] empDiaPrevisto = new int [MAX_EMPRESTIMOS];
	static int[] empDiaDevolucao = new int [MAX_EMPRESTIMOS];
	static double[] empMulta = new double [MAX_EMPRESTIMOS];
	// Matriz que conta os empréstimos por tipo de leitor e categoria.
	static int[][] emprestimosTipoCategoria = new int[3][5];

	static int qtdLivros = 0;
	static int qtdLeitores = 0;
	static int qtdEmprestimos = 0;

	// Cadastra um livro após validar os dados informados.
	public static void cadastrarLivro () {

		if (qtdLivros >= MAX_LIVROS) {
			System.out.print("[ERRO] O acervo está totalmente cheio!");
			return;
		}

		int codigo = lerCodigoLivro();
		sc.nextLine();

		if (codigo == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		String tituloLivro = lerTexto("Digite o título do livro: ");


		if (tituloLivro == null) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		String autorLivro = lerTexto("Digite o autor do livro: ");

		if (autorLivro == null) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}


		for (int i = 0; i < NOMES_CATEGORIAS.length; i++) {
			System.out.println("[" + (i + 1) + "] " + NOMES_CATEGORIAS[i]);
		}

		int categoriaEscolhida = lerInteiroValido(
		                             "Digite a categoria: ", 1, NOMES_CATEGORIAS.length
		                         );

		if (categoriaEscolhida == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		int anoPublicacao = lerInteiroValido(
		                        "Digite o ano de publicação: ", 1450, ANO_ATUAL
		                    );

		if (anoPublicacao == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		int quantidadeExemplares = lerInteiroValido(
		                               "Digite a quantidade de exemplares: ", 1, 50
		                           );

		if (quantidadeExemplares == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		// Salva o livro na próxima posição disponível dos vetores.
		contadorEmprestimos[qtdLivros] = 0;
		codigoLivros[qtdLivros] = codigo;
		tituloLivros[qtdLivros] = tituloLivro;
		autoresLivros[qtdLivros] = autorLivro;
		categoriasLivros[qtdLivros] = categoriaEscolhida;
		anosPublicacao[qtdLivros] = anoPublicacao;
		totalExemplares[qtdLivros] = quantidadeExemplares;
		exemplaresDisponiveis[qtdLivros] = quantidadeExemplares;

		qtdLivros++;
		System.out.println("Cadastro concluído com sucesso!");

	}

	// Adiciona exemplares a um livro já cadastrado.
	public static void adicionarExemplares() {
		int codigo;

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		codigo = lerInteiroValido("Digite o código do livro: ", 100, 9999);

		if (codigo == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		int indiceLivro = buscarIndiceLivro(codigo);

		if (indiceLivro == -1) {
			System.out.println("[AVISO] Livro não encontrado.");
			return;
		}

		int exemplaresNovos = lerInteiroValido("Digite a quantidade de novos exemplares: ", 1, 50);

		if (exemplaresNovos == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		totalExemplares[indiceLivro] += exemplaresNovos;
		exemplaresDisponiveis[indiceLivro] += exemplaresNovos;

		System.out.println("[SUCESSO] Exemplares adicionados com sucesso.");
		System.out.println("Livro: " + tituloLivros[indiceLivro]);
		System.out.println("Total de exemplares: " + totalExemplares[indiceLivro]);
		System.out.println("Exemplares disponíveis: " + exemplaresDisponiveis[indiceLivro]);
	}

	// Lista os livros cadastrados e o total de exemplares.
	public static void listarLivros() {
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		// Define a largura das colunas para alinhar a tabela.
		String formato = "%-8s %-18s %-18s %-16s %-6s %-6s %s%n";
		System.out.println("========== ACERVO DA BIBLIOTECA ==========");
		System.out.printf(formato, "Código", "Título", "Autor",
		                  "Categoria", "Ano", "Total", "Disp.");

		int somaExemplares = 0;
		for (int i = 0; i < qtdLivros; i++) {
			System.out.printf(formato,
			                  codigoLivros[i],
			                  tituloLivros[i],
			                  autoresLivros[i],
			                  NOMES_CATEGORIAS[categoriasLivros[i] - 1],
			                  anosPublicacao[i],
			                  totalExemplares[i],
			                  exemplaresDisponiveis[i]);

			somaExemplares = somaExemplares + totalExemplares[i];
		}
		System.out.println("\nTotal de títulos: " + qtdLivros);
		System.out.println("Total de exemplares: " + somaExemplares);
	}

	// Consulta os dados de um livro pelo código.
	public static void buscarLivroPorCodigo () {
		int buscaCodigo;

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		buscaCodigo = lerInteiroValido("Digite o código do livro: ", 100, 9999);

		if (buscaCodigo == 0) {
			System.out.println("[AVISO] Consulta cancelada.");
			return;
		}

		int indiceLivro = buscarIndiceLivro(buscaCodigo);

		if (indiceLivro != -1) {
			exibirDadosLivro(indiceLivro);
		}
		else {
			System.out.println("[AVISO] Livro não encontrado.");
		}
	}


	// Mostra apenas os livros da categoria escolhida.
	public static void pesquisaPorCategoria() {
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		for (int i=0; i < NOMES_CATEGORIAS.length; i++) {
			System.out.println("[" + (i + 1) + "] " + NOMES_CATEGORIAS[i]);
		}

		int categoriaEscolhida = lerInteiroValido (
		                             "Digite a categoria desejada: ", 1, NOMES_CATEGORIAS.length);

		if (categoriaEscolhida == 0) {
			System.out.println("[AVISO] Pesquisa cancelada.");
			return;
		}


		String formato = "%-8s %-18s %-18s %-16s %-6s %-6s %s%n";
		System.out.println("========== ACERVO DA BIBLIOTECA ==========");
		System.out.printf(formato, "Código", "Título", "Autor",
		                  "Categoria", "Ano", "Total", "Disp.");

		int livrosEncontrados = 0;

		for (int i = 0; i < qtdLivros; i++) {
			if (categoriasLivros[i] == categoriaEscolhida) {
				System.out.printf(formato,
				                  codigoLivros[i],
				                  tituloLivros[i],
				                  autoresLivros[i],
				                  NOMES_CATEGORIAS[categoriasLivros[i] - 1],
				                  anosPublicacao[i],
				                  totalExemplares[i],
				                  exemplaresDisponiveis[i]);

				livrosEncontrados++;
			}
		}

		if (livrosEncontrados == 0) {
			System.out.println("[AVISO] Nenhum livro nesta categoria.");
		} else {
			System.out.println("\nTotal de livros encontrados: " + livrosEncontrados);
		}
	}


	// Exibe somente os livros com exemplares disponíveis.
	public static void listarLivrosDisponiveis () {
		boolean livroDisponivel = false;

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		for (int i=0; i < qtdLivros; i++) {
			if (exemplaresDisponiveis[i] > 0) {
				exibirDadosLivro(i);
				livroDisponivel = true;
			}
		}

		if (!livroDisponivel) {
			System.out.println("[AVISO] Nenhum livro disponível.");
		}
	}

	// Identifica o livro com mais empréstimos registrados.
	public static void exibirLivroMaisEmprestado() {


		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}


		int emprestimoMaior = contadorEmprestimos[0];
		int indiceMaiorEmprestimo = 0;


		for (int i=1; i< qtdLivros; i++) {
			if (contadorEmprestimos[i] > emprestimoMaior) {
				indiceMaiorEmprestimo = i;
				emprestimoMaior = contadorEmprestimos[i];
			}
		}

		if (emprestimoMaior == 0) {
			System.out.println("[AVISO] Nenhum empréstimo registrado.");
			return;
		}
		System.out.println("Titulo: " + tituloLivros[indiceMaiorEmprestimo] + "\nQuantidade de empréstimos: " + emprestimoMaior);

	}

	// Calcula os exemplares emprestados e mostra os dados do livro.
	public static void exibirDadosLivro(int indiceLivro) {
		int totalEmprestado;
		totalEmprestado = totalExemplares[indiceLivro] - exemplaresDisponiveis[indiceLivro];

		System.out.println("Categoria: " + NOMES_CATEGORIAS[categoriasLivros[indiceLivro] - 1] +
		                   "\nCódigo do livro: " + codigoLivros[indiceLivro] +
		                   "\nTítulo: " + tituloLivros[indiceLivro] +
		                   "\nAutor: " + autoresLivros[indiceLivro] +
		                   "\nAno de publicação: " + anosPublicacao[indiceLivro] +
		                   "\nTotal de Exemplares: " + totalExemplares[indiceLivro] +
		                   "\nExemplares disponíveis: " + exemplaresDisponiveis[indiceLivro] +
		                   "\nExemplares emprestados: " + totalEmprestado);
	}

	// Retorna a posição do livro ou -1 se não encontrar.
	public static int buscarIndiceLivro(int codigo) {
		for (int i=0; i < qtdLivros; i++) {
			if (codigo == codigoLivros[i]) {
				return i;
			}
		}
		return -1;
	}

	// Valida o código e impede o cadastro de livros repetidos.
	public static int lerCodigoLivro () {
		int codigo = 0;
		boolean condicao = false;
		do {
			try {
				System.out.println("Digite o código do livro (0 para cancelar): ");
				codigo = sc.nextInt();


				if (codigo == 0) {
					return 0;
				}

				if (codigo > 9999 || codigo < 100) {
					System.out.println("[ERRO] O código deve estar entre 100 e 9999!");
				} else {
					if (buscarIndiceLivro(codigo) == -1) {
						condicao = true;
					} else {
						System.out.println("[ERRO] O código já existe!");
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		} while (!condicao);
		return codigo;
	}

	// Não aceita texto vazio ou ponto e vírgula, usado na separação dos arquivos.
	public static String lerTexto (String mensagem) {
		String texto;

		do {
			System.out.println(mensagem + " (/cancelar para sair)");

			texto = sc.nextLine().trim();

			if (texto.equalsIgnoreCase("/cancelar")) {
				return null;
			}

			if (texto.isEmpty() || texto.contains(";")) {
				System.out.println("[ERRO] O campo não pode ficar vazio nem conter ponto e vírgula (;).");
			}

		} while (texto.isEmpty() || texto.contains(";"));

		return texto;
	}


	// Valida números dentro de um intervalo e permite cancelar com 0.
	public static int lerInteiroValido (String mensagem, int minimo, int maximo) {
		int numero = 0;
		boolean numeroValido = false;

		do {
			try {
				System.out.println(mensagem + " (0 para cancelar)");
				numero = sc.nextInt();

				if (numero == 0 && minimo > 0) {
					return 0;
				}

				if (numero >= minimo && numero <= maximo) {
					numeroValido = true;
				} else {
					System.out.println("[ERRO] Digite um número entre " + minimo + " e " + maximo);
				}

			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");

				sc.nextLine();
			}
		} while (!numeroValido);

		return numero;
	}

	// Cadastra um leitor e define seu tipo de empréstimo.
	public static void cadastrarLeitor() {

		if (qtdLeitores >= MAX_LEITORES) {
			System.out.println("[ERRO] O limite de leitores foi atingido!");
			return;
		}

		int codigo;
		codigo = lerCodigoLeitor();

		sc.nextLine();

		if (codigo == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		String nomeLeitor = lerTexto("Digite o nome do leitor: ");

		if (nomeLeitor == null) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		for (int i = 0; i < NOMES_TIPOS.length; i++) {
			System.out.println("[" + (i + 1) + "] " + NOMES_TIPOS[i]);
		}

		int tipoEscolhido;
		tipoEscolhido = lerInteiroValido("Digite o tipo de leitor: ", 1, NOMES_TIPOS.length);

		if (tipoEscolhido == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		codigoLeitores[qtdLeitores] = codigo;
		nomeLeitores[qtdLeitores] = nomeLeitor;
		tipoLeitores[qtdLeitores] = tipoEscolhido;

		emprestimosEmAberto[qtdLeitores] = 0;

		qtdLeitores++;

		System.out.println("Cadastro concluído com sucesso!");
		System.out.println("Tipo de leitor: " + NOMES_TIPOS[tipoEscolhido - 1]);
		System.out.println("Prazo de devolução: " + PRAZO_POR_TIPO[tipoEscolhido - 1] + " dias");
		System.out.println("Limite de empréstimos: " + LIMITE_POR_TIPO[tipoEscolhido - 1] + " livro(s)");
	}


	// Mostra os leitores e seus empréstimos em aberto.
	public static void listarLeitores() {

		if (qtdLeitores == 0) {
			System.out.println("[AVISO] Não existem leitores cadastrados.");
			return;
		}

		String formato = "%-8s %-18s %-18s %s%n";

		System.out.println("========== LEITORES CADASTRADOS ==========");
		System.out.printf(formato, "Código", "Nome", "Tipo", "Empréstimos");

		for (int i = 0; i < qtdLeitores; i++) {
			System.out.printf(formato,
			                  codigoLeitores[i],
			                  nomeLeitores[i],
			                  NOMES_TIPOS[tipoLeitores[i] - 1],
			                  emprestimosEmAberto[i] + " de " + LIMITE_POR_TIPO[tipoLeitores[i] - 1]);
		}
	}

	// Valida o código e impede o cadastro de leitores repetidos.
	public static int lerCodigoLeitor () {
		int codigo = 0;
		boolean condicao = false;
		do {
			try {
				System.out.println("Digite o código do leitor (0 para cancelar): ");
				codigo = sc.nextInt();


				if (codigo == 0) {
					return 0;
				}

				if (codigo > 9999 || codigo < 1) {
					System.out.println("[ERRO] O código deve estar entre 1 e 9999!");
				} else {
					if (buscarIndiceLeitor(codigo) == -1) {
						condicao = true;
					} else {
						System.out.println("[ERRO] O código já existe!");
					}
				}
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		} while (!condicao);
		return codigo;
	}


	// Retorna a posição do leitor ou -1 se não encontrar.
	public static int buscarIndiceLeitor(int codigo) {
		for (int i=0; i < qtdLeitores; i++) {
			if (codigo == codigoLeitores[i]) {
				return i;
			}
		}
		return -1;
	}


	// Registra um empréstimo após verificar as regras da biblioteca.
	public static void realizarEmprestimo() {

		if (qtdEmprestimos >= MAX_EMPRESTIMOS) {
			System.out.println("[AVISO] O limite de empréstimos foi atingido.");
			return;
		}

		if (qtdLeitores == 0) {
			System.out.println("[AVISO] Não existem leitores cadastrados.");
			return;
		}

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		int codigoLeitor = lerInteiroValido("Digite o código do leitor: ", 1, 9999);
		if (codigoLeitor == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		int indiceLeitor;
		indiceLeitor = buscarIndiceLeitor(codigoLeitor);

		if (indiceLeitor == -1) {
			System.out.println("[ERRO] Leitor não encontrado.");
			return;
		}

		int codigoLivro;
		codigoLivro = lerInteiroValido("Digite o código do livro: ", 100, 9999);

		if (codigoLivro == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		int indiceLivro;
		indiceLivro = buscarIndiceLivro(codigoLivro);

		if (indiceLivro == -1) {
			System.out.println("[ERRO] Livro não encontrado.");
			return;
		}

		int diaEmprestimo;
		diaEmprestimo = lerInteiroValido("Digite o dia do empréstimo: ", 1, 365);

		if (diaEmprestimo == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		if (exemplaresDisponiveis[indiceLivro] == 0) {
			System.out.println("[AVISO] Não há exemplares disponíveis.");
			return;
		}

		int tipoLeitor = tipoLeitores[indiceLeitor];
		int limite = LIMITE_POR_TIPO[tipoLeitor - 1];

		if (emprestimosEmAberto[indiceLeitor] >= limite) {
			System.out.println("[AVISO] O leitor atingiu seu limite de empréstimos.");
			return;
		}

		// Leitores com empréstimos atrasados não podem pegar novos livros.
		if (possuiAtraso(codigoLeitor, diaEmprestimo)) {
			System.out.println("[AVISO] O leitor possui empréstimos em atraso.");
			return;
		}

		// Impede dois empréstimos em aberto do mesmo livro para o mesmo leitor.
		if (possuiLivroEmprestado(codigoLeitor, codigoLivro)) {
			System.out.println("[AVISO] O leitor possui empréstimos em aberto com este livro.");
			return;
		}

		int prazo = PRAZO_POR_TIPO[tipoLeitor - 1];
		// O prazo de devolução depende do tipo de leitor.
		int diaPrevisto = diaEmprestimo + prazo;

		// O código do empréstimo é gerado em sequência.
		codigosEmprestimos[qtdEmprestimos] = qtdEmprestimos + 1;
		empCodigoLivro[qtdEmprestimos] = codigoLivro;
		empCodigoLeitor[qtdEmprestimos] = codigoLeitor;
		empDiaEmprestimo[qtdEmprestimos] = diaEmprestimo;
		empDiaPrevisto[qtdEmprestimos] = diaPrevisto;

		// O valor 0 indica que o empréstimo ainda não foi devolvido.
		empDiaDevolucao[qtdEmprestimos] = 0;
		empMulta[qtdEmprestimos] = 0.0;

		// Atualiza os exemplares e os contadores após o empréstimo.
		exemplaresDisponiveis[indiceLivro]--;
		emprestimosEmAberto[indiceLeitor]++;
		contadorEmprestimos[indiceLivro]++;

		int categoriaLivro = categoriasLivros[indiceLivro];
		// Registra o empréstimo na matriz de tipo de leitor e categoria.
		emprestimosTipoCategoria[tipoLeitor - 1][categoriaLivro - 1]++;

		System.out.println("[SUCESSO] Empréstimo realizado com sucesso!");
		System.out.println("Código do empréstimo: " + codigosEmprestimos[qtdEmprestimos]);
		System.out.println("Dia previsto para devolução: " + diaPrevisto);

		qtdEmprestimos++;
	}

	// Verifica se o leitor já está com esse livro emprestado.
	public static boolean possuiLivroEmprestado(int codigoLeitor, int codigoLivro) {
		for (int i = 0; i < qtdEmprestimos; i++) {

			if (empCodigoLeitor[i] == codigoLeitor && empCodigoLivro[i] == codigoLivro && empDiaDevolucao[i] == 0) {
				return true;
			}
		}

		return false;
	}

	// Verifica se há empréstimos em aberto com prazo vencido.
	public static boolean possuiAtraso(int codigoLeitor, int diaEmprestimo) {
		for (int i = 0; i < qtdEmprestimos; i++) {
			if (empCodigoLeitor[i] == codigoLeitor && empDiaDevolucao[i] == 0 && empDiaPrevisto[i] < diaEmprestimo) {
				return true;
			}
		}
		return false;
	}


	// Mostra os empréstimos por tipo de leitor e categoria.
	public static void relatorioPerfilUso() {
		if (qtdEmprestimos == 0) {
			System.out.println("[AVISO] Nenhum empréstimo registrado.");
			return;
		}

		System.out.println("========== RELATÓRIO DE PERFIL DE USO ==========");

		String formato = "%-16s %-12s %-10s %-10s %-16s %-10s %s%n";
		System.out.printf(formato, "Tipo de leitor", "Literatura",
		                  "Didático", "Técnico", "Infantojuvenil",
		                  "Outros", "Total");

		// Guarda os totais de cada categoria e o total geral.
		int[] totalColunas = new int[5];
		int totalGeral = 0;

		// Soma os empréstimos de cada tipo de leitor e categoria.
		for (int i = 0; i < emprestimosTipoCategoria.length; i++) {
			int totalLinha = 0;

			for (int j = 0; j < emprestimosTipoCategoria[i].length; j++) {
				totalLinha = totalLinha + emprestimosTipoCategoria[i][j];
				totalColunas[j] = emprestimosTipoCategoria[i][j] + totalColunas[j];
				totalGeral = totalGeral + emprestimosTipoCategoria[i][j];
			}

			System.out.printf(formato,
			                  NOMES_TIPOS[i],
			                  emprestimosTipoCategoria[i][0],
			                  emprestimosTipoCategoria[i][1],
			                  emprestimosTipoCategoria[i][2],
			                  emprestimosTipoCategoria[i][3],
			                  emprestimosTipoCategoria[i][4],
			                  totalLinha);
		}

		System.out.printf(formato,
		                  "Total",
		                  totalColunas[0],
		                  totalColunas[1],
		                  totalColunas[2],
		                  totalColunas[3],
		                  totalColunas[4],
		                  totalGeral);

		for (int i = 0; i < emprestimosTipoCategoria.length; i++) {
			int indiceMaiorCategoria = 0;

			for (int j = 1; j < emprestimosTipoCategoria[i].length; j++) {
				if (emprestimosTipoCategoria[i][j] > emprestimosTipoCategoria[i][indiceMaiorCategoria]) {
					indiceMaiorCategoria = j;
				}
			}

			if (emprestimosTipoCategoria[i][indiceMaiorCategoria] == 0) {
				System.out.println(NOMES_TIPOS[i] + ": Nenhum empréstimo registrado");
			} else {
				System.out.println(NOMES_TIPOS[i] + ": " + NOMES_CATEGORIAS[indiceMaiorCategoria]);
			}
		}

		// Encontra a categoria mais emprestada no geral.
		int indiceMaiorGeral = 0;

		for (int i = 1; i < totalColunas.length; i++) {
			if (totalColunas[i] > totalColunas[indiceMaiorGeral]) {
				indiceMaiorGeral = i;
			}
		}

		System.out.println("\nCategoria mais emprestada no geral: "
		                   + NOMES_CATEGORIAS[indiceMaiorGeral]);
	}

	// Registra a devolução de um livro emprestado.
	public static void realizarDevolucao() {
		if (qtdEmprestimos == 0) {
			System.out.println("[AVISO] Nenhum empréstimo registrado.");
			return;
		}

		int codigo = lerInteiroValido("Digite o código do empréstimo: ", 1, qtdEmprestimos);

		if (codigo == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		int emprestimoIndice = localizarEmprestimo(codigo);
		if ( emprestimoIndice == -1) {
			System.out.println("[AVISO] Empréstimo não encontrado.");
			return;
		}
		// Impede que o mesmo empréstimo seja devolvido duas vezes.
		if(empDiaDevolucao[emprestimoIndice] != 0 ) {
			System.out.println("[AVISO] Este empréstimo já foi devolvido.");
			return;
		}

		// A devolução não pode acontecer antes do dia do empréstimo.
		int diaDevolucao;
		do {
			diaDevolucao = lerInteiroValido("Digite o dia de devolução: ", 1, 365);
			if (diaDevolucao == 0) {
				System.out.println("[AVISO] Operação cancelada.");
				return;
			}

			if (diaDevolucao < empDiaEmprestimo[emprestimoIndice]) {
				System.out.println("[AVISO] Data inválida.");
			}
		} while (diaDevolucao < empDiaEmprestimo[emprestimoIndice]);

		// Calcula a multa somente quando a devolução está atrasada.
		int diasEmAtraso = 0;
		double valorMulta = 0;
		if (diaDevolucao > empDiaPrevisto[emprestimoIndice]) {
			diasEmAtraso = diaDevolucao - empDiaPrevisto[emprestimoIndice];
			valorMulta = diasEmAtraso * MULTA_POR_DIA;
		}

		// Salva a devolução e atualiza os exemplares e empréstimos em aberto.
		empDiaDevolucao[emprestimoIndice] = diaDevolucao;
		empMulta[emprestimoIndice] = valorMulta;
		int livroCodigo = empCodigoLivro[emprestimoIndice];
		int livroIndice = buscarIndiceLivro(livroCodigo);
		exemplaresDisponiveis[livroIndice]++;
		int leitorCodigo = empCodigoLeitor[emprestimoIndice];
		int leitorIndice = buscarIndiceLeitor(leitorCodigo);
		emprestimosEmAberto[leitorIndice]--;

		System.out.println("[SUCESSO] Devolução realizada com sucesso!");
		System.out.println("Dias de atraso: " + diasEmAtraso);
		System.out.printf("Valor da multa: R$ %.2f%n", valorMulta);

	}

	// Retorna a posição do empréstimo ou -1 se não encontrar.
	public static int localizarEmprestimo(int codigoEmprestimo) {
		for (int i=0; i < qtdEmprestimos; i++) {
			if (codigoEmprestimo == codigosEmprestimos[i]) {
				return i;
			}
		}
		return -1;
	}

	// Mostra os empréstimos em aberto e verifica se estão atrasados.
	public static void relatorioEmprestimosEmAberto () {
		if (qtdEmprestimos == 0) {
			System.out.println("[AVISO] Nenhum empréstimo registrado.");
			return;
		}

		int diaAtual = lerInteiroValido ("Digite o dia atual: ", 1, 365);
		if (diaAtual == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}


		boolean possuiEmprestimoAberto = false;

		// Percorre os empréstimos e mostra apenas os que não foram devolvidos.
		for (int i=0; i < qtdEmprestimos; i++) {
			if (empDiaDevolucao[i] == 0) {

				int indiceLivro  = buscarIndiceLivro(empCodigoLivro[i]);
				int indiceLeitor = buscarIndiceLeitor(empCodigoLeitor[i]);

				possuiEmprestimoAberto = true;

				System.out.println("Código do empréstimo: " + codigosEmprestimos[i]);
				System.out.println("Livro: " + tituloLivros[indiceLivro]);
				System.out.println("Leitor: " + nomeLeitores[indiceLeitor]);
				System.out.println("Dia do empréstimo: " + empDiaEmprestimo[i]);
				System.out.println("Devolução prevista: " + empDiaPrevisto[i]);

				if (diaAtual > empDiaPrevisto[i]) {
					System.out.println("[AVISO] O empréstimo está atrasado.");
				} else {
					System.out.println("[SUCESSO] O empréstimo está dentro do prazo.");
				}
				System.out.println("----------------------------------------");
			}
		}
		// Informa quando todos os empréstimos já foram devolvidos.
		if (possuiEmprestimoAberto == false) {
			System.out.println("[AVISO] Nenhum empréstimo em aberto.");
			return;
		}
	}

	// Apresenta a disponibilidade dos exemplares e o resumo geral do acervo.
	public static void relatorioDisponibilidadeLivros() {
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		int somaExemplares = 0;
		int totalDisponiveis = 0;
		int totalEmprestados = 0;

		// Calcula os totais de exemplares disponíveis e emprestados.
		for (int i=0; i<qtdLivros; i++) {
			somaExemplares = somaExemplares + totalExemplares[i];
			totalDisponiveis = totalDisponiveis + exemplaresDisponiveis[i];
			totalEmprestados = totalEmprestados + (totalExemplares[i] - exemplaresDisponiveis[i]);

		}

		double porcentagemEmprestada;
		porcentagemEmprestada = (totalEmprestados * 100.00) / somaExemplares;

		System.out.println("\n========== DISPONIBILIDADE DOS LIVROS ==========");

		// Exibe a situação individual de cada livro cadastrado.
		for (int i=0; i < qtdLivros; i++ ) {
			System.out.printf("Código: %d%n", codigoLivros[i]);
			System.out.printf("Título: %s%n", tituloLivros[i]);
			System.out.printf("Total de exemplares: %d%n", totalExemplares[i]);
			System.out.printf("Disponíveis: %d%n", exemplaresDisponiveis[i]);
			System.out.printf("Emprestados: %d%n", totalExemplares[i] - exemplaresDisponiveis[i]);
			System.out.println("----------------------------------------");
		}
		System.out.println("\n========== RESUMO GERAL ==========");
		System.out.printf("Total de exemplares: %d%n", somaExemplares);
		System.out.printf("Total de disponíveis: %d%n", totalDisponiveis);
		System.out.printf("Total de emprestados: %d%n", totalEmprestados);
		System.out.printf("Percentual emprestado: %.2f%%%n", porcentagemEmprestada);
		System.out.println("----------------------------------------");

		boolean livrosIndisponiveis = false;

		System.out.println("\n========== LIVROS INDISPONÍVEIS ==========");

		// Identifica os títulos que não possuem exemplares disponíveis.
		for (int i=0; i < qtdLivros; i++) {
			if (exemplaresDisponiveis[i] == 0) {
				System.out.println("Título: " + tituloLivros[i]);
				livrosIndisponiveis = true;
			}
		}

		if (livrosIndisponiveis == false) {
			System.out.println("[AVISO] Todos os livros possuem exemplares disponíveis.");
			return;
		}

	}

	public static void main(String[] args) {
		int opcao = -1;

		do {
			try {
				System.out.println("==================================================");
				System.out.println("              SISTEMA DE BIBLIOTECA");
				System.out.println("==================================================");
				System.out.println("[1] Cadastrar livro");
				System.out.println("[2] Adicionar exemplares");
				System.out.println("[3] Listar acervo");
				System.out.println("[4] Consultar livro por código");
				System.out.println("[5] Pesquisar livros por categoria");
				System.out.println("[6] Cadastrar leitor");
				System.out.println("[7] Listar leitores");
				System.out.println("[8] Realizar empréstimo");
				System.out.println("[9] Realizar devolução");
				System.out.println("[10] Relatório de emprestimos em aberto");
				System.out.println("[11] Relatório de disponibilidade de livros");
				System.out.println("[13] Relatório de perfil de uso");
				System.out.println("[0] Sair");
				System.out.println("==================================================");
				System.out.print("Escolha uma opçao: ");
				opcao = sc.nextInt();

				switch (opcao) {
				case 1:
					cadastrarLivro();
					break;
				case 2:
					adicionarExemplares();
					break;
				case 3:
					listarLivros();
					break;
				case 4:
					buscarLivroPorCodigo();
					break;
				case 5:
					pesquisaPorCategoria();
					break;
				case 6:
					cadastrarLeitor();
					break;
				case 7:
					listarLeitores();
					break;
				case 8:
					realizarEmprestimo();
					break;
				case 9:
					realizarDevolucao();
					break;
				case 10:
					relatorioEmprestimosEmAberto();
					break;
				case 11:
					relatorioDisponibilidadeLivros();
					break;
				case 13:
					relatorioPerfilUso();
					break;
				case 0:
					System.out.println("Saindo do sistema...");
					break;

				default:
					System.out.println("[ERRO] Opção inválida!");
					break;
				}

			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas numeros ");
				sc.nextLine();
			}
		} while (opcao != 0);


		sc.close();
	}
}
