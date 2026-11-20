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

	static int qtdLivros = 0;

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

		String tituloLivro = lerTextoLivro("Digite o título do livro: ");


		if (tituloLivro == null) {
			System.out.println("[AVISO] Cadastro cancelado.");
			return;
		}

		String autorLivro = lerTextoLivro("Digite o autor do livro: ");

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

	public static String lerTextoLivro (String mensagem) {
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
