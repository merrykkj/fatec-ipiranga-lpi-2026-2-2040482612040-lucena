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
		if (qtdLivros >= MAX_LIVROS) {
			System.out.print("[ERRO] O acervo está totalmente cheio!");
			return qtdLivros;
		}

		int codigo;
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

			} catch (InputMismatchException e) {
				System.out.println("[ERRO] Digite apenas números inteiros!");
				sc.nextLine();
			}
		} while (!condicao);
		return qtdLivros;

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

		int opcao;

		do {
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
			case 0:
				System.out.println("Saindo do sistema...");
				break;
			}

		} while (opcao != 0);


		sc.close();
	}
}

