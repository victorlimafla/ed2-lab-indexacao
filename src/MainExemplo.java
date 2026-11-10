/**
 * Exemplo de uso do GeradorDados.
 *
 * Este programa NAO implementa Hash, BST ou AVL.
 * Ele apenas demonstra que:
 *
 * - os registros possuem ID, nome, categoria e pontuacao;
 * - os mesmos registros podem ser obtidos em ordens diferentes.
 */
public class MainExemplo {

    public static void main(String[] args) {

        int quantidade = 8;

        Registro[] crescente =
                GeradorDados.gerarCrescente(quantidade);

        Registro[] decrescente =
                GeradorDados.gerarDecrescente(quantidade);

        Registro[] aleatorio =
                GeradorDados.gerarAleatorio(quantidade);

        System.out.println("=== ORDEM CRESCENTE ===");
        exibir(crescente);

        System.out.println("\n=== ORDEM DECRESCENTE ===");
        exibir(decrescente);

        System.out.println("\n=== ORDEM ALEATORIA ===");
        exibir(aleatorio);
    }

    private static void exibir(Registro[] dados) {
        for (Registro registro : dados) {
            System.out.printf(
                    "ID: %d | Nome: %-12s | Categoria: %-10s | Pontuacao: %d%n",
                    registro.getId(),
                    registro.getNome(),
                    registro.getCategoria(),
                    registro.getPontuacao()
            );
        }
    }
}
