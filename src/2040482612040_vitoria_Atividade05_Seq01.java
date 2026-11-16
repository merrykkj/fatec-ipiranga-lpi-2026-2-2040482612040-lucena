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

	public static int cadastrarLivro (int [] codigoLivro, String[] tituloLivros, String[] autoresLivros, int[] categoriasLivros,
	                                  int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int qtdLivros, Scanner sc, int[] contadorEmprestimos)  {

		// Verifica se o vetor está cheio, senão segue para cadastro
		if (qtdLivros >= MAX_LIVROS) {
			System.out.print("[ERRO] O acervo está totalmente cheio!");
			return qtdLivros;
		}

		int codigo = lerCodigoLivro(codigoLivro, qtdLivros, sc);
		sc.nextLine();

		String tituloLivro = lerTextoLivro(sc, "Digite o título do livro: ");
		String autorLivro = lerTextoLivro(sc, "Digite o autor do livro: ");


		for (int i = 0; i < NOMES_CATEGORIAS.length; i++) {
			System.out.println("[" + (i + 1) + "] " + NOMES_CATEGORIAS[i]);
		}

		int categoriaEscolhida = lerInteiroValido(
		                             sc, "Digite a categoria: ", 1, NOMES_CATEGORIAS.length
		                         );


		int anoPublicacao = lerInteiroValido(
		                        sc, "Digite o ano de publicação: ", 1450, ANO_ATUAL
		                    );


		int quantidadeExemplares = lerInteiroValido(
		                               sc, "Digite a quantidade de exemplares: ", 1, 50
		                           );
		contadorEmprestimos[qtdLivros] = 0;
		codigoLivro[qtdLivros] = codigo;
		tituloLivros[qtdLivros] = tituloLivro;
		autoresLivros[qtdLivros] = autorLivro;
		categoriasLivros[qtdLivros] = categoriaEscolhida;
		anosPublicacao[qtdLivros] = anoPublicacao;
		totalExemplares[qtdLivros] = quantidadeExemplares;
		exemplaresDisponiveis[qtdLivros] = quantidadeExemplares;

		qtdLivros++;
		System.out.println("Cadastro concluído com sucesso!");
		return qtdLivros;

	}

	public static void listarLivros(int [] codigoLivro, String[] tituloLivros, String[] autoresLivros, int[] categoriasLivros,
	                                int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int qtdLivros) {

		// Verifica se há livros cadastrados
		if (qtdLivros == 0 ) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;

		} else {
			System.out.println(" ==== LIVROS CADASTRADOS ====");
			// Percorre os livros cadastrados e exibe suas informações na tela.
			for (int i=0; i < qtdLivros; i++) {
				exibirDadosLivro(codigoLivro, tituloLivros, autoresLivros,
				                 categoriasLivros, anosPublicacao, totalExemplares,
				                 exemplaresDisponiveis, i);
				System.out.println("------------------------");
			}
		}
	}

	public static void buscarLivroPorCodigo (int [] codigoLivro, String[] tituloLivros, String[] autoresLivros, int[] categoriasLivros,
	        int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int qtdLivros, Scanner sc) {
		int buscaCodigo;
		boolean livroEncontrado = false;
		int indiceLivro = -1;

		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		System.out.print("Digite o código do livro: ");
		buscaCodigo = sc.nextInt();

		// Laço que percorre os livros e encontra o índice do livro solicitado
		for (int i=0; i < qtdLivros; i++) {
			if (buscaCodigo == codigoLivro[i]) {
				livroEncontrado = true;
				indiceLivro = i;
				break;
			}
		}

		if (livroEncontrado) {
			exibirDadosLivro(codigoLivro, tituloLivros, autoresLivros,
			                 categoriasLivros, anosPublicacao, totalExemplares,
			                 exemplaresDisponiveis, indiceLivro);
		}
		else {
			System.out.println("[AVISO] Livro não encontrado.");
		}
	}

	public static void buscarLivroPorTitulo (int [] codigoLivro, String[] tituloLivros, String[] autoresLivros, int[] categoriasLivros,
	        int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int qtdLivros, Scanner sc) {
		String nomeLivro;
		boolean livroEncontrado = false;
		int indiceLivro = -1;

		if (qtdLivros == 0 ) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		sc.nextLine();

		System.out.print("Digite o título do livro: ");
		nomeLivro = sc.nextLine().trim();

		for (int i=0; i < qtdLivros; i++) {
			if (nomeLivro.equalsIgnoreCase(tituloLivros[i])) {
				livroEncontrado = true;
				indiceLivro = i;
				break;
			}
		}

		if (livroEncontrado) {
			exibirDadosLivro(codigoLivro, tituloLivros, autoresLivros,
			                 categoriasLivros, anosPublicacao, totalExemplares,
			                 exemplaresDisponiveis, indiceLivro);
		}
		else {
			System.out.println("[AVISO] Livro não encontrado.");
		}

	}

	public static void listarLivrosDisponiveis (int [] codigoLivro, String[] tituloLivros, String[] autoresLivros, int[] categoriasLivros,
	        int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int qtdLivros) {
		boolean livroDisponivel = false;



		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}

		// Verifica os livros disponíveis
		for (int i=0; i < qtdLivros; i++) {
			if (exemplaresDisponiveis[i] > 0) {
				exibirDadosLivro(codigoLivro, tituloLivros, autoresLivros,
				                 categoriasLivros, anosPublicacao, totalExemplares,
				                 exemplaresDisponiveis, i);
				livroDisponivel = true;
			}
		}

		if (!livroDisponivel) {
			System.out.println("[AVISO] Nenhum livro disponível.");
		}
	}

	public static void exibirDadosLivro(int [] codigoLivro, String[] tituloLivros, String[] autoresLivros, int[] categoriasLivros,
	                                    int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int indiceLivro) {


		System.out.println("Categoria: " + NOMES_CATEGORIAS[categoriasLivros[indiceLivro] - 1] +
		                   "\nCódigo do livro: " + codigoLivro[indiceLivro] +
		                   "\nTítulo: " + tituloLivros[indiceLivro] +
		                   "\nAutor: " + autoresLivros[indiceLivro] +
		                   "\nAno de publicação: " + anosPublicacao[indiceLivro] +
		                   "\nTotal de Exemplares: " + totalExemplares[indiceLivro] +
		                   "\nExemplares disponíveis: " + exemplaresDisponiveis[indiceLivro]);
	}

	public static void exibirLivroMaisEmprestado(String[] tituloLivros, int[] contadorEmprestimos, int qtdLivros ) {


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


	public static int lerCodigoLivro (int[] codigoLivro, int qtdLivros, Scanner sc) {
		// Cadastro do código do livro
		int codigo = 0;
		boolean condicao = false;
		do {
			try {
				System.out.println("Digite o código do livro: ");
				codigo = sc.nextInt();

				if (codigo > 9999 || codigo < 100) {
					System.out.println("[ERRO] O código deve estar entre 100 e 9999!");
				} else {
					boolean codigoRepetido = false;
					for (int i=0; i < qtdLivros; i++) {
						if (codigo == codigoLivro[i] ) {
							System.out.println("[ERRO] O código já existe!");
							codigoRepetido = true;

							break;
						}

					}
					if (!codigoRepetido) {
						condicao = true;
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

	public static String lerTextoLivro (Scanner sc, String mensagem) {
		String texto;
		do {
			System.out.println(mensagem);
			texto = sc.nextLine().trim();

			if (texto.isEmpty() || texto.contains(";")) {
				System.out.println("[ERRO] O campo não pode ficar vazio nem conter ponto e vírgula (;).");
			}
		} while (texto.isEmpty() || texto.contains(";"));

		return texto;
	}

	public static int lerInteiroValido (Scanner sc, String mensagem, int minimo, int maximo) {
		int numero = 0;
		boolean numeroValido = false;
		do {
			try {
				System.out.println(mensagem);
				numero = sc.nextInt();

				if (numero >= minimo && numero <= maximo) {
					numeroValido = true;
				} else {
					System.out.println("[ERRO] Digite um número entre " + minimo + " e " + maximo);
				}
			} catch (InputMismatchException e ) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		}	while (!numeroValido);
		return numero;
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		int[] codigoLivros = new int[MAX_LIVROS];
		String[] tituloLivros = new String [MAX_LIVROS];
		String[] autoresLivros = new String [MAX_LIVROS];
		int[] categoriasLivros = new int[MAX_LIVROS];
		int[] anosPublicacao = new int[MAX_LIVROS];
		int[] totalExemplares = new int[MAX_LIVROS];
		int[] exemplaresDisponiveis = new int[MAX_LIVROS];
		int[] contadorEmprestimos = new int[MAX_LIVROS];
		int qtdLivros = 0;

		int opcao = -1;

		do {
			try {
				System.out.println("==================================================");
				System.out.println("              SISTEMA DE BIBLIOTECA");
				System.out.println("==================================================");
				System.out.println("[1] Cadastrar Livro");
				System.out.println("[2] Listar Livros");
				System.out.println("[3] Buscar livro por código");
				System.out.println("[4] Buscar livro por título");
				System.out.println("[5] Listar livros disponíveis");
				System.out.println("[6] Exibir livro mais emprestado");
				System.out.println("[0] Sair");
				System.out.println("==================================================");
				System.out.print("Escolha uma opçao: ");
				opcao = sc.nextInt();

				switch (opcao) {
				case 1:
					qtdLivros = cadastrarLivro(codigoLivros, tituloLivros, autoresLivros,
					                           categoriasLivros, anosPublicacao, totalExemplares,
					                           exemplaresDisponiveis, qtdLivros, sc, contadorEmprestimos);
					break;
				case 2:
					listarLivros(codigoLivros, tituloLivros, autoresLivros,
					             categoriasLivros, anosPublicacao, totalExemplares,
					             exemplaresDisponiveis, qtdLivros);
					break;
				case 3:
					buscarLivroPorCodigo(codigoLivros, tituloLivros, autoresLivros,
					                     categoriasLivros, anosPublicacao, totalExemplares,
					                     exemplaresDisponiveis, qtdLivros, sc);
					break;
				case 4:
					buscarLivroPorTitulo(codigoLivros, tituloLivros, autoresLivros,
					                     categoriasLivros, anosPublicacao, totalExemplares,
					                     exemplaresDisponiveis, qtdLivros, sc);
					break;
				case 5:
					listarLivrosDisponiveis(codigoLivros, tituloLivros, autoresLivros,
					                        categoriasLivros, anosPublicacao, totalExemplares,
					                        exemplaresDisponiveis, qtdLivros);
					break;
				case 6:
					exibirLivroMaisEmprestado(tituloLivros, contadorEmprestimos, qtdLivros);
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
