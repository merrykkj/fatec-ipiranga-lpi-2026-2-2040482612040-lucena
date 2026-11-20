import java.util.Scanner;
import java.util.InputMismatchException;
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
	                                  int[] anosPublicacao, int[] totalExemplares, int[] exemplaresDisponiveis, int qtdLivros, Scanner sc)  {

		// Verifica se o vetor está cheio, senão segue para cadastro
		if (qtdLivros >= MAX_LIVROS) {
			System.out.print("[ERRO] O acervo está totalmente cheio!");
			return qtdLivros;
		}

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
					if (codigoRepetido == false) {
						condicao = true;
					}
				}

				// Erro caso o usuário digite letras ou caracteres inválidos
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		} while (!condicao);

		sc.nextLine();

		//Cadastra o titulo do livro
		String tituloLivro;

		do {
			System.out.println("Digite o título do livro: ");
			tituloLivro = sc.nextLine().trim();

			if (tituloLivro.isEmpty() || tituloLivro.contains(";")) {
				System.out.println("[ERRO] O campo não pode ficar vazio nem conter ponto e vírgula (;).");
			}

		} while (tituloLivro.isEmpty() || tituloLivro.contains(";"));


		// Cadastra o autor do autor do livro
		String autorLivro;
		do {
			System.out.println("Digite o autor do livro: ");
			autorLivro = sc.nextLine().trim();

			if (autorLivro.isEmpty() || autorLivro.contains(";")) {
				System.out.println("[ERRO] O campo não pode ficar vazio nem conter ponto e vírgula (;).");
			}

		} while (autorLivro.isEmpty() || autorLivro.contains(";"));


		// Cadastra a categoria do livro
		int categoriaEscolhida = 0;
		boolean condicaoValida = false;
		do {

			try {
				for (int i=0; i < NOMES_CATEGORIAS.length; i++) {
					System.out.println("[" + (i + 1) + "] " + NOMES_CATEGORIAS[i]);
				}
				System.out.println("Digite a categoria: ");
				categoriaEscolhida = sc.nextInt();

				if (categoriaEscolhida > 5 || categoriaEscolhida < 1) {
					System.out.println("[ERRO] Deve ser um número de 1 a 5");
				} else {
					condicaoValida = true;

				}
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números (1 a 5)");
				sc.nextLine();
			}
		} while (!condicaoValida);


		// Cadastra ano de publicação
		int anoPublicacao = 0;
		boolean anoValido = false;
		do {
			try {

				System.out.print("Digite o ano de publicação: ");
				anoPublicacao = sc.nextInt();

				if (anoPublicacao < 1450 || anoPublicacao > ANO_ATUAL) {
					System.out.println("[ERRO] Digite um ano de publicação válido");
				} else {
					anoValido = true;
				}
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números");
				sc.nextLine();
			}
		} while (!anoValido);


		// Cadastra quantidade de exemplares
		int quantidadeExemplares = 0;
		boolean numeroValido = false;

		do {
			try {
				System.out.print("Digite a quantidade de exemplares: ");
				quantidadeExemplares = sc.nextInt();

				if (quantidadeExemplares < 1 || quantidadeExemplares > 50) {
					System.out.println("[ERRO] A quantidade de exemplares deve estar entre 1 e 50");
				} else  {
					numeroValido = true;
				}
			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números");
				sc.nextLine();
			}
		} while (!numeroValido);

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

		} else {
			System.out.println(" ==== LIVROS CADASTRADOS ====");
			// Percorre os livros cadastrados e exibe suas informações na tela.
			for (int i=0; i < qtdLivros; i++) {
				System.out.println("Categoria: " + NOMES_CATEGORIAS[categoriasLivros[i] - 1] +
				                   "\nCódigo do livro: " + codigoLivro[i] + "\nTítulo:" +
				                   tituloLivros[i] + "\nAutor: " + autoresLivros[i] +
				                   "\nAno de publicação: " + anosPublicacao[i] + "\nTotal de Exemplares: " + totalExemplares[i] + "\nExemplares disponíveis: " + exemplaresDisponiveis[i]);
				System.out.println("------------------------");
			}
		}
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
		int qtdLivros = 0;

		int opcao = -1;

		do {
			try {
				System.out.println("==================================================");
				System.out.println("              SISTEMA DE BIBLIOTECA");
				System.out.println("==================================================");
				System.out.println("[1] Cadastrar Livro");
				System.out.println("[2] Listar Livros");
				System.out.println("[3] Relatório de Empréstimos em Atraso");
				System.out.println("[4] Empréstimos Mensais");
				System.out.println("[0] Sair");
				System.out.println("==================================================");
				System.out.print("Escolha uma opção: ");
				opcao = sc.nextInt();

				switch (opcao) {
				case 1:
					qtdLivros = cadastrarLivro(codigoLivros, tituloLivros, autoresLivros,
					                           categoriasLivros, anosPublicacao, totalExemplares,
					                           exemplaresDisponiveis, qtdLivros, sc);
					break;
				case 2:
					listarLivros(codigoLivros, tituloLivros, autoresLivros,
					             categoriasLivros, anosPublicacao, totalExemplares,
					             exemplaresDisponiveis, qtdLivros);
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
