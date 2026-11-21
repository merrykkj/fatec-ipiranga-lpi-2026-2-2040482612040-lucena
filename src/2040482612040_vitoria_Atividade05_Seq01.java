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

	static int qtdLivros = 0;

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

	// Retorna a posição do livro ou -1 se não encontrar.
	public static int buscarIndiceLivro(int codigo) {
		for (int i=0; i < qtdLivros; i++) {
			if (codigo == codigoLivros[i]) {
				return i;
			}
		}
		return -1;
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
	public static String lerTextoLivro (String mensagem) {
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
