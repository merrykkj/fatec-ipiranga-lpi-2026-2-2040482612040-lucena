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
	// Vetores que guardam as informações dos livros.
	// Os dados de um mesmo livro ficam na mesma posição dos vetores.
	static int[] codigoLivros = new int[MAX_LIVROS];
	static String[] tituloLivros = new String[MAX_LIVROS];
	static String[] autoresLivros = new String[MAX_LIVROS];
	static int[] categoriasLivros = new int[MAX_LIVROS];
	static int[] anosPublicacao = new int[MAX_LIVROS];
	static int[] totalExemplares = new int[MAX_LIVROS];
	static int[] exemplaresDisponiveis = new int[MAX_LIVROS];
	static int[] contadorEmprestimos = new int[MAX_LIVROS];

	// Vetores que guardam as informações dos leitores.
	static int[] codigoLeitores = new int [MAX_LEITORES];
	static String[] nomeLeitores = new String[MAX_LEITORES];
	static int[] tipoLeitores = new int[MAX_LEITORES];
	static int[] emprestimosEmAberto = new int [MAX_LEITORES];

	// Vetores que guardam as informações dos empréstimos.
	static int[] codigosEmprestimos = new int [MAX_EMPRESTIMOS];
	static int[] empCodigoLivro = new int [MAX_EMPRESTIMOS];
	static int[] empCodigoLeitor = new int [MAX_EMPRESTIMOS];
	static int[] empDiaEmprestimo = new int [MAX_EMPRESTIMOS];
	static int[] empDiaPrevisto = new int [MAX_EMPRESTIMOS];
	static int[] empDiaDevolucao = new int [MAX_EMPRESTIMOS];
	static double[] empMulta = new double [MAX_EMPRESTIMOS];
	static int[][] emprestimosTipoCategoria = new int[3][5];

	static int qtdLivros = 0;
	static int qtdLeitores = 0;
	static int qtdEmprestimos = 0;

	// Método de cadastro de novos livros
	public static void cadastrarLivro () {

		// Verifica se o vetor está cheio, senão segue para cadastro
		if (qtdLivros >= MAX_LIVROS) {
			System.out.print("[ERRO] O acervo está totalmente cheio!");
			return;
		}

		int codigo = lerCodigoLivro();
		// Limpa a quebra de linha que ficou no Scanner.
		sc.nextLine();

		// Se o usuário digitar 0, cancela o cadastro e volta para o menu.
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

		// Guarda os dados nos vetores após validar todas as informações.
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

	public static void adicionarExemplares() {
		int codigo;

		// Verifica se existem livros cadastrados no acervo.
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		codigo = lerInteiroValido("Digite o código do livro: ", 100, 9999);

		// Se o usuário digitar 0, cancela a operação e volta para o menu.
		if (codigo == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		// Busca a posição do livro nos vetores através do código informado.
		int indiceLivro = buscarIndiceLivro(codigo);

		// Se retornar -1, significa que o livro não está cadastrado.
		if (indiceLivro == -1) {
			System.out.println("[AVISO] Livro não encontrado.");
			return;
		}

		// A quantidade de novos exemplares deve estar entre 1 e 50.
		int exemplaresNovos = lerInteiroValido("Digite a quantidade de novos exemplares: ", 1, 50);

		// Permite cancelar a operação antes de alterar os vetores.
		if (exemplaresNovos == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		// Soma os novos exemplares ao total e à quantidade disponível,
		// já que os exemplares adicionados ainda não foram emprestados.
		totalExemplares[indiceLivro] += exemplaresNovos;
		exemplaresDisponiveis[indiceLivro] += exemplaresNovos;

		// Mostra as informações do livro com as quantidades atualizadas.
		System.out.println("[SUCESSO] Exemplares adicionados com sucesso.");
		System.out.println("Livro: " + tituloLivros[indiceLivro]);
		System.out.println("Total de exemplares: " + totalExemplares[indiceLivro]);
		System.out.println("Exemplares disponíveis: " + exemplaresDisponiveis[indiceLivro]);
	}

	public static void listarLivros() {
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		// Define o espaçamento das colunas para deixar a tabela organizada e alinhada.
		// Cada número define a quantidade mínima de caracteres de cada coluna.
		// O sinal "-" alinha o conteúdo à esquerda, o "s" representa texto
		// e o %n é a quebra de linha
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

	public static void buscarLivroPorCodigo () {
		int buscaCodigo;

		// Verifica se existem livros cadastrados antes de realizar a busca.
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		buscaCodigo = lerInteiroValido("Digite o código do livro: ", 100, 9999);

		// Permite cancelar a consulta e voltar para o menu.
		if (buscaCodigo == 0) {
			System.out.println("[AVISO] Consulta cancelada.");
			return;
		}

		// Busca a posição do livro nos vetores pelo código informado.
		int indiceLivro = buscarIndiceLivro(buscaCodigo);

		// Se o livro for encontrado, mostra suas informações.
		// Caso contrário, informa que o livro não está cadastrado.
		if (indiceLivro != -1) {
			exibirDadosLivro(indiceLivro);
		}
		else {
			System.out.println("[AVISO] Livro não encontrado.");
		}
	}


	public static void pesquisaPorCategoria() {
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		for (int i=0; i < NOMES_CATEGORIAS.length; i++) {
			// O vetor vai de 0 até 4 por isso adicionamos +1 para que
			// apareça as categorias de 1 até 5
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

		// Percorre os livros e mostra apenas os que pertencem à categoria escolhida.
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


	public static void listarLivrosDisponiveis () {
		boolean livroDisponivel = false;

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		// Verifica os livros disponíveis
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

	// Exibe as informações do livro e calcula quantos exemplares estão emprestados.
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

	// Procura a posição do livro pelo código.
	// Caso não encontre, retorna -1.
	public static int buscarIndiceLivro(int codigo) {
		for (int i=0; i < qtdLivros; i++) {
			if (codigo == codigoLivros[i]) {
				return i;
			}
		}
		return -1;
	}

	public static int lerCodigoLivro () {
		// Cadastro do código do livro
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
				// Erro caso o usuário digite letras ou caracteres inválidos
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		} while (!condicao);
		return codigo;
	}

	public static String lerTexto (String mensagem) {
		String texto;

		// Repete a leitura enquanto o texto digitado for inválido.
		do {
			System.out.println(mensagem + " (/cancelar para sair)");

			// Remove os espaços em branco do início e do final do texto.
			texto = sc.nextLine().trim();

			// Ignora letras maiúsculas e minúsculas ao comparar os textos.
			// Retorna null caso o usuário queira cancelar a operação.
			if (texto.equalsIgnoreCase("/cancelar")) {
				return null;
			}

			// Não permite campos vazios ou com ponto e vírgula,
			// pois esse caractere será usado para separar os dados nos arquivos.
			if (texto.isEmpty() || texto.contains(";")) {
				System.out.println("[ERRO] O campo não pode ficar vazio nem conter ponto e vírgula (;).");
			}

		} while (texto.isEmpty() || texto.contains(";"));

		// Retorna o texto depois que ele for validado.
		return texto;
	}


	public static int lerInteiroValido (String mensagem, int minimo, int maximo) {
		int numero = 0;
		boolean numeroValido = false;

		// Repete a leitura enquanto o usuário não digitar um número válido.
		do {
			try {
				System.out.println(mensagem + " (0 para cancelar)");
				numero = sc.nextInt();

				// Permite cancelar a operação caso o 0 não seja um valor permitido.
				if (numero == 0 && minimo > 0) {
					return 0;
				}

				// Verifica se o número está dentro do intervalo permitido.
				if (numero >= minimo && numero <= maximo) {
					numeroValido = true;
				} else {
					System.out.println("[ERRO] Digite um número entre " + minimo + " e " + maximo);
				}

				// Trata o erro caso o usuário digite algo que não seja um número inteiro.
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");

				// Limpa a entrada inválida para que o usuário possa digitar novamente.
				sc.nextLine();
			}
		} while (!numeroValido);

		// Retorna o número depois que ele for validado.
		return numero;
	}

	// Método de cadastro de novos leitores.
	public static void cadastrarLeitor() {

		// Verifica se o limite de leitores cadastrados foi atingido.
		if (qtdLeitores >= MAX_LEITORES) {
			System.out.println("[ERRO] O limite de leitores foi atingido!");
			return;
		}

		int codigo;
		codigo = lerCodigoLeitor();

		// Limpa a quebra de linha que ficou no Scanner.
		sc.nextLine();

		// Permite cancelar o cadastro e voltar para o menu.
		if (codigo == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		String nomeLeitor = lerTexto("Digite o nome do leitor: ");

		if (nomeLeitor == null) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		// Exibe os tipos de leitores disponíveis.
		// Adiciona 1 ao índice para que as opções comecem em 1.
		for (int i = 0; i < NOMES_TIPOS.length; i++) {
			System.out.println("[" + (i + 1) + "] " + NOMES_TIPOS[i]);
		}

		// Solicita e valida o tipo de leitor escolhido.
		int tipoEscolhido;
		tipoEscolhido = lerInteiroValido("Digite o tipo de leitor: ", 1, NOMES_TIPOS.length);

		if (tipoEscolhido == 0) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		// Guarda os dados nos vetores após validar todas as informações.
		codigoLeitores[qtdLeitores] = codigo;
		nomeLeitores[qtdLeitores] = nomeLeitor;
		tipoLeitores[qtdLeitores] = tipoEscolhido;

		// Todo leitor começa sem nenhum empréstimo em aberto.
		emprestimosEmAberto[qtdLeitores] = 0;

		// Atualiza a quantidade de leitores cadastrados.
		qtdLeitores++;

		// Exibe o tipo, o prazo e o limite de empréstimos do leitor.
		// Subtrai 1 do tipo escolhido porque os índices começam em 0.
		System.out.println("Cadastro concluído com sucesso!");
		System.out.println("Tipo de leitor: " + NOMES_TIPOS[tipoEscolhido - 1]);
		System.out.println("Prazo de devolução: " + PRAZO_POR_TIPO[tipoEscolhido - 1] + " dias");
		System.out.println("Limite de empréstimos: " + LIMITE_POR_TIPO[tipoEscolhido - 1] + " livro(s)");
	}


	// Método que lista todos os leitores cadastrados.
	public static void listarLeitores() {

		// Verifica se existem leitores cadastrados.
		if (qtdLeitores == 0) {
			System.out.println("[AVISO] Não existem leitores cadastrados.");
			return;
		}

		// Define o espaçamento das colunas para deixar a tabela alinhada.
		String formato = "%-8s %-18s %-18s %s%n";

		System.out.println("========== LEITORES CADASTRADOS ==========");
		System.out.printf(formato, "Código", "Nome", "Tipo", "Empréstimos");

		// Percorre todos os leitores cadastrados e exibe suas informações.
		// Mostra a quantidade de empréstimos em aberto e o limite permitido
		// de acordo com o tipo de cada leitor.
		for (int i = 0; i < qtdLeitores; i++) {
			System.out.printf(formato,
			                  codigoLeitores[i],
			                  nomeLeitores[i],
			                  NOMES_TIPOS[tipoLeitores[i] - 1],
			                  emprestimosEmAberto[i] + " de " + LIMITE_POR_TIPO[tipoLeitores[i] - 1]);
		}
	}

	public static int lerCodigoLeitor () {
		// Cadastro do código do leitor
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
				// Erro caso o usuário digite letras ou caracteres inválidos
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		} while (!condicao);
		return codigo;
	}


	// Procura a posição do leitor pelo código.
	// Caso não encontre, retorna -1.
	public static int buscarIndiceLeitor(int codigo) {
		for (int i=0; i < qtdLeitores; i++) {
			if (codigo == codigoLeitores[i]) {
				return i;
			}
		}
		return -1;
	}


	// Método responsável por registrar um novo empréstimo.
	public static void realizarEmprestimo() {

		// Verifica se o limite de empréstimos cadastrados foi atingido.
		if (qtdEmprestimos >= MAX_EMPRESTIMOS) {
			System.out.println("[AVISO] O limite de empréstimos foi atingido.");
			return;
		}

		// Verifica se existem leitores e livros cadastrados.
		if (qtdLeitores == 0) {
			System.out.println("[AVISO] Não existem leitores cadastrados.");
			return;
		}

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		// Solicita o código do leitor e permite cancelar a operação.
		int codigoLeitor = lerInteiroValido("Digite o código do leitor: ", 1, 9999);
		if (codigoLeitor == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		// Busca a posição do leitor nos vetores pelo código informado.
		int indiceLeitor;
		indiceLeitor = buscarIndiceLeitor(codigoLeitor);

		if (indiceLeitor == -1) {
			System.out.println("[ERRO] Leitor não encontrado.");
			return;
		}

		// Solicita e valida o código do livro.
		int codigoLivro;
		codigoLivro = lerInteiroValido("Digite o código do livro: ", 100, 9999);

		if (codigoLivro == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		// Busca a posição do livro nos vetores.
		int indiceLivro;
		indiceLivro = buscarIndiceLivro(codigoLivro);

		if (indiceLivro == -1) {
			System.out.println("[ERRO] Livro não encontrado.");
			return;
		}

		// O dia do empréstimo deve estar entre 1 e 365.
		int diaEmprestimo;
		diaEmprestimo = lerInteiroValido("Digite o dia do empréstimo: ", 1, 365);

		if (diaEmprestimo == 0) {
			System.out.println("[AVISO] Operação cancelada.");
			return;
		}

		// Verifica se o livro possui exemplares disponíveis.
		if (exemplaresDisponiveis[indiceLivro] == 0) {
			System.out.println("[AVISO] Não há exemplares disponíveis.");
			return;
		}

		// Identifica o tipo do leitor e seu limite de empréstimos.
		// Subtrai 1 do tipo porque os índices do vetor começam em 0.
		int tipoLeitor = tipoLeitores[indiceLeitor];
		int limite = LIMITE_POR_TIPO[tipoLeitor - 1];

		if (emprestimosEmAberto[indiceLeitor] >= limite) {
			System.out.println("[AVISO] O leitor atingiu seu limite de empréstimos.");
			return;
		}

		// Impede novos empréstimos caso o leitor possua empréstimos em atraso.
		if (possuiAtraso(codigoLeitor, diaEmprestimo)) {
			System.out.println("[AVISO] O leitor possui empréstimos em atraso.");
			return;
		}

		// Impede que o leitor pegue o mesmo livro duas vezes
		// sem devolver o exemplar anterior.
		if (possuiLivroEmprestado(codigoLeitor, codigoLivro)) {
			System.out.println("[AVISO] O leitor possui empréstimos em aberto com este livro.");
			return;
		}

		// Calcula o dia previsto para devolução de acordo com
		// o prazo permitido para cada tipo de leitor.
		int prazo = PRAZO_POR_TIPO[tipoLeitor - 1];
		int diaPrevisto = diaEmprestimo + prazo;

		// Guarda os dados nos vetores após realizar todas as validações.
		// O código do empréstimo é gerado de forma sequencial.
		codigosEmprestimos[qtdEmprestimos] = qtdEmprestimos + 1;
		empCodigoLivro[qtdEmprestimos] = codigoLivro;
		empCodigoLeitor[qtdEmprestimos] = codigoLeitor;
		empDiaEmprestimo[qtdEmprestimos] = diaEmprestimo;
		empDiaPrevisto[qtdEmprestimos] = diaPrevisto;

		// O empréstimo começa em aberto e sem multa.
		empDiaDevolucao[qtdEmprestimos] = 0;
		empMulta[qtdEmprestimos] = 0.0;

		// Atualiza a disponibilidade do livro e os contadores de empréstimos.
		exemplaresDisponiveis[indiceLivro]--;
		emprestimosEmAberto[indiceLeitor]++;
		contadorEmprestimos[indiceLivro]++;

		// Atualiza a matriz de acordo com o tipo do leitor e a categoria do livro.
		// Cada linha representa um tipo de leitor e cada coluna uma categoria.
		int categoriaLivro = categoriasLivros[indiceLivro];
		emprestimosTipoCategoria[tipoLeitor - 1][categoriaLivro - 1]++;

		// Exibe a confirmação do empréstimo e o dia previsto para devolução.
		System.out.println("[SUCESSO] Empréstimo realizado com sucesso!");
		System.out.println("Código do empréstimo: " + codigosEmprestimos[qtdEmprestimos]);
		System.out.println("Dia previsto para devolução: " + diaPrevisto);

		// Atualiza a quantidade de empréstimos registrados.
		qtdEmprestimos++;
	}

	// Verifica se o leitor já possui um empréstimo em aberto do mesmo livro.
	public static boolean possuiLivroEmprestado(int codigoLeitor, int codigoLivro) {
		for (int i = 0; i < qtdEmprestimos; i++) {

			// O dia da devolução igual a 0 indica que o livro ainda não foi devolvido.
			if (empCodigoLeitor[i] == codigoLeitor && empCodigoLivro[i] == codigoLivro && empDiaDevolucao[i] == 0) {
				return true;
			}
		}

		// Retorna false caso não encontre nenhum empréstimo nessas condições.
		return false;
	}

	// Verifica se o leitor possui algum empréstimo em atraso.
	public static boolean possuiAtraso(int codigoLeitor, int diaEmprestimo) {
		for (int i = 0; i < qtdEmprestimos; i++) {
			// Um empréstimo está atrasado quando ainda não foi devolvido
			// e o dia previsto é menor que o dia do novo empréstimo.
			if (empCodigoLeitor[i] == codigoLeitor && empDiaDevolucao[i] == 0 && empDiaPrevisto[i] < diaEmprestimo) {
				return true;
			}
		}
		// Retorna false caso não encontre nenhum empréstimo atrasado.
		return false;
	}


	// Exibe o relatório de empréstimos por tipo de leitor e categoria.
	public static void relatorioPerfilUso() {
		if (qtdEmprestimos == 0) {
			System.out.println("[AVISO] Nenhum empréstimo registrado.");
			return;
		}

		System.out.println("========== RELATÓRIO DE PERFIL DE USO ==========");

		// Define o espaçamento das colunas para deixar a tabela alinhada.
		String formato = "%-16s %-12s %-10s %-10s %-16s %-10s %s%n";
		System.out.printf(formato, "Tipo de leitor", "Literatura",
		                  "Didático", "Técnico", "Infantojuvenil",
		                  "Outros", "Total");

		int[] totalColunas = new int[5];
		int totalGeral = 0;

		// Percorre a matriz calculando os totais de cada linha, coluna e o total geral.
		// As linhas representam os tipos de leitores e as colunas as categorias.
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

		// Exibe o total de cada categoria e o total geral de empréstimos.
		System.out.printf(formato,
		                  "Total",
		                  totalColunas[0],
		                  totalColunas[1],
		                  totalColunas[2],
		                  totalColunas[3],
		                  totalColunas[4],
		                  totalGeral);

		// Busca a categoria mais emprestada de cada tipo de leitor,
		// utilizando a primeira posição como referência para a comparação.
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

		// Compara os totais das colunas para encontrar a categoria mais emprestada no geral.
		int indiceMaiorGeral = 0;

		for (int i = 1; i < totalColunas.length; i++) {
			if (totalColunas[i] > totalColunas[indiceMaiorGeral]) {
				indiceMaiorGeral = i;
			}
		}

		System.out.println("\nCategoria mais emprestada no geral: "
		                   + NOMES_CATEGORIAS[indiceMaiorGeral]);
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
				System.out.println("[8] Realizar Empréstimo");
				System.out.println("[9] Relatório de perfil de uso");
				System.out.println("[0] Sair");
				System.out.println("==================================================");
				System.out.print("Escolha uma opçao: ");
				opcao = sc.nextInt();

				// Chama o método de acordo com a opção escolhida pelo usuário.
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
