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

	static int[] codigoLivros = new int[MAX_LIVROS];
	static String[] tituloLivros = new String[MAX_LIVROS];
	static String[] autoresLivros = new String[MAX_LIVROS];
	static int[] categoriasLivros = new int[MAX_LIVROS];
	static int[] anosPublicacao = new int[MAX_LIVROS];
	static int[] totalExemplares = new int[MAX_LIVROS];
	static int[] exemplaresDisponiveis = new int[MAX_LIVROS];
	static int[] contadorEmprestimos = new int[MAX_LIVROS];

	static int qtdLivros = 0;

	public static void cadastrarLivro () {

		// Verifica se o vetor está cheio, senão segue para cadastro
		if (qtdLivros >= MAX_LIVROS) {
			System.out.print("[ERRO] O acervo está totalmente cheio!");
			return;
		}

		int codigo = lerCodigoLivro();
		sc.nextLine();

		String tituloLivro = lerTextoLivro("Digite o título do livro: ");
		String autorLivro = lerTextoLivro("Digite o autor do livro: ");


		for (int i = 0; i < NOMES_CATEGORIAS.length; i++) {
			System.out.println("[" + (i + 1) + "] " + NOMES_CATEGORIAS[i]);
		}

		int categoriaEscolhida = lerInteiroValido(
		                             "Digite a categoria: ", 1, NOMES_CATEGORIAS.length
		                         );

		int anoPublicacao = lerInteiroValido(
		                        "Digite o ano de publicação: ", 1450, ANO_ATUAL
		                    );

		int quantidadeExemplares = lerInteiroValido(
		                               "Digite a quantidade de exemplares: ", 1, 50
		                           );


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
		// Verifica se há livros cadastrados
		if (qtdLivros == 0 ) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;

		} else {
			System.out.println(" ==== LIVROS CADASTRADOS ====");
			// Percorre os livros cadastrados e exibe suas informações na tela.
			for (int i=0; i < qtdLivros; i++) {
				exibirDadosLivro(i);
				System.out.println("------------------------");
			}
		}
	}

	public static void buscarLivroPorCodigo () {
		int buscaCodigo;
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		System.out.print("Digite o código do livro: ");
		buscaCodigo = sc.nextInt();
		int indiceLivro = buscarIndiceLivro(buscaCodigo);

		if (indiceLivro != -1) {
			exibirDadosLivro(indiceLivro);
		}
		else {
			System.out.println("[AVISO] Livro não encontrado.");
		}
	}


	public static int buscarIndiceLivro(int codigo) {
		for (int i=0; i < qtdLivros; i++) {
			if (codigo == codigoLivros[i]) {
				return i;
			}
		}
		return -1;
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

	public static void exibirDadosLivro(int indiceLivro) {


		System.out.println("Categoria: " + NOMES_CATEGORIAS[categoriasLivros[indiceLivro] - 1] +
		                   "\nCódigo do livro: " + codigoLivros[indiceLivro] +
		                   "\nTítulo: " + tituloLivros[indiceLivro] +
		                   "\nAutor: " + autoresLivros[indiceLivro] +
		                   "\nAno de publicação: " + anosPublicacao[indiceLivro] +
		                   "\nTotal de Exemplares: " + totalExemplares[indiceLivro] +
		                   "\nExemplares disponíveis: " + exemplaresDisponiveis[indiceLivro]);
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
				System.out.println("Digite o código do livro: ");
				codigo = sc.nextInt();

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
		do {
			System.out.println(mensagem);
			texto = sc.nextLine().trim();

			if (texto.isEmpty() || texto.contains(";")) {
				System.out.println("[ERRO] O campo não pode ficar vazio nem conter ponto e vírgula (;).");
			}
		} while (texto.isEmpty() || texto.contains(";"));

		return texto;
	}

	public static int lerInteiroValido (String mensagem, int minimo, int maximo) {
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

	public static void adicionarExemplares() {
		int codigo;
		if (qtdLivros == 0) {
			System.out.println("[AVISO] O acervo está vazio.");
			return;
		}
		codigo = lerInteiroValido("Digite o código do livro: ", 100, 9999);
		int indiceLivro = buscarIndiceLivro(codigo);

		if (indiceLivro == -1) {
			System.out.println("[AVISO] Livro não encontrado.");
			return;
		}
		int exemplaresNovos = lerInteiroValido("Digite a quantidade de novos exemplares: ", 1, 50);
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
				System.out.println("[1] Cadastrar Livro");
				System.out.println("[2] Listar Livros");
				System.out.println("[3] Buscar livro por código");
				System.out.println("[4] Adicionar exemplares");
				System.out.println("[5] Listar livros disponíveis");
				System.out.println("[6] Exibir livro mais emprestado");
				System.out.println("[0] Sair");
				System.out.println("==================================================");
				System.out.print("Escolha uma opçao: ");
				opcao = sc.nextInt();

				switch (opcao) {
				case 1:
					cadastrarLivro();
					break;
				case 2:
					listarLivros();
					break;
				case 3:
					buscarLivroPorCodigo();
					break;
				case 4:
					adicionarExemplares();
					break;
				case 5:
					listarLivrosDisponiveis();
					break;
				case 6:
					exibirLivroMaisEmprestado();
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
