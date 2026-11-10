/**
 * Representa um registro sintetico utilizado nos experimentos
 * da disciplina Estrutura de Dados 2.
 *
 * Esta classe e fornecida pelo professor e nao faz parte da avaliacao
 * das estruturas implementadas pelos alunos.
 */
public class Registro {

    private final int id;
    private final String nome;
    private final String categoria;
    private final int pontuacao;

    public Registro(int id, String nome, String categoria, int pontuacao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.pontuacao = pontuacao;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", pontuacao=" + pontuacao +
                '}';
    }
}
