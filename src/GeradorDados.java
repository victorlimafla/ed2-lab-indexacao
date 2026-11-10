import java.util.Random;

/**
 * Gera conjuntos de dados sinteticos para os experimentos de ED2.
 *
 * Caracteristicas importantes:
 *
 * 1. Os metodos gerarCrescente, gerarDecrescente e gerarAleatorio
 *    geram exatamente os mesmos registros.
 *
 * 2. O que muda entre os metodos e apenas a ordem em que os registros
 *    sao devolvidos.
 *
 * 3. Os dados possuem uma semente fixa. Portanto, todos os alunos
 *    recebem os mesmos registros para uma mesma quantidade.
 *
 * 4. A classe foi preparada para servir apenas como fonte de dados.
 *    O funcionamento interno dela nao faz parte da avaliacao.
 */
public final class GeradorDados {

    private static final long SEMENTE_DADOS = 198507L;
    private static final long SEMENTE_ORDEM = 201307L;

    private static final String[] CATEGORIAS = {
            "Clima",
            "Solo",
            "Hidraulico",
            "Energia",
            "Ambiente",
            "Controle"
    };

    private GeradorDados() {
        // Impede a criacao de objetos desta classe.
    }

    /**
     * Gera os registros em ordem crescente de ID.
     */
    public static Registro[] gerarCrescente(int quantidade) {
        validarQuantidade(quantidade);
        return criarBase(quantidade);
    }

    /**
     * Gera exatamente os mesmos registros, mas em ordem decrescente de ID.
     */
    public static Registro[] gerarDecrescente(int quantidade) {
        validarQuantidade(quantidade);

        Registro[] base = criarBase(quantidade);
        Registro[] dados = new Registro[quantidade];

        for (int i = 0; i < quantidade; i++) {
            dados[i] = base[quantidade - 1 - i];
        }

        return dados;
    }

    /**
     * Gera exatamente os mesmos registros, mas em uma ordem pseudoaleatoria.
     *
     * A ordem tambem usa uma semente fixa, garantindo que todos os alunos
     * recebam a mesma sequencia para uma mesma quantidade.
     */
    public static Registro[] gerarAleatorio(int quantidade) {
        validarQuantidade(quantidade);

        Registro[] dados = criarBase(quantidade);
        Random random = new Random(SEMENTE_ORDEM);

        // Embaralhamento de Fisher-Yates.
        for (int i = dados.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            Registro temp = dados[i];
            dados[i] = dados[j];
            dados[j] = temp;
        }

        return dados;
    }

    /**
     * Cria o conjunto base de registros.
     *
     * Para uma mesma quantidade, os IDs, nomes, categorias e pontuacoes
     * serao sempre os mesmos.
     */
    private static Registro[] criarBase(int quantidade) {
        Registro[] dados = new Registro[quantidade];
        Random random = new Random(SEMENTE_DADOS);

        for (int i = 0; i < quantidade; i++) {
            int id = i + 1;

            // Numero apenas para tornar o nome menos artificial.
            int numeroNome = 100 + random.nextInt(900);
            String nome = "Sensor " + numeroNome;

            String categoria =
                    CATEGORIAS[random.nextInt(CATEGORIAS.length)];

            int pontuacao = random.nextInt(1000);

            dados[i] = new Registro(
                    id,
                    nome,
                    categoria,
                    pontuacao
            );
        }

        return dados;
    }

    private static void validarQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(
                    "A quantidade de registros nao pode ser negativa."
            );
        }
    }
}
