import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class BST implements EstruturaIndexada {
    private static class No {
        Registro registro;
        No esquerdo, direito;

        No(Registro registro) { this.registro = registro; }
    }

    private No raiz;
    private int quantidade;
    private long inspecoesInsercao, inspecoesBusca, inspecoesRemocao, inspecoesIntervalo;
    private long ultimasInspecoes;
    private int inspecoesUltimaBusca, inspecoesUltimaConsulta;

    // IDs repetidos sao ignorados: o registro previamente inserido permanece.
    @Override
    public boolean inserir(Registro registro) {
        if (registro == null) throw new IllegalArgumentException("registro nulo");
        ultimasInspecoes = 0;
        if (raiz == null) {
            raiz = new No(registro);
            quantidade++;
            return true;
        }
        No atual = raiz;
        while (true) {
            ultimasInspecoes++;
            int id = registro.getId();
            if (id == atual.registro.getId()) {
                inspecoesInsercao += ultimasInspecoes;
                return false;
            }
            if (id < atual.registro.getId()) {
                if (atual.esquerdo == null) {
                    atual.esquerdo = new No(registro);
                    quantidade++;
                    break;
                }
                atual = atual.esquerdo;
            } else {
                if (atual.direito == null) {
                    atual.direito = new No(registro);
                    quantidade++;
                    break;
                }
                atual = atual.direito;
            }
        }
        inspecoesInsercao += ultimasInspecoes;
        return true;
    }

    @Override
    public Registro buscar(int id) {
        ultimasInspecoes = 0;
        No atual = raiz;
        while (atual != null) {
            ultimasInspecoes++;
            if (id == atual.registro.getId()) break;
            atual = id < atual.registro.getId() ? atual.esquerdo : atual.direito;
        }
        inspecoesBusca += ultimasInspecoes;
        inspecoesUltimaBusca = (int) ultimasInspecoes;
        return atual == null ? null : atual.registro;
    }

    @Override
    public boolean remover(int id) {
        ultimasInspecoes = 0;
        No pai = null, atual = raiz;
        while (atual != null) {
            ultimasInspecoes++;
            if (id == atual.registro.getId()) break;
            pai = atual;
            atual = id < atual.registro.getId() ? atual.esquerdo : atual.direito;
        }
        if (atual == null) {
            inspecoesRemocao += ultimasInspecoes;
            return false;
        }
        if (atual.esquerdo != null && atual.direito != null) {
            No paiSucessor = atual, sucessor = atual.direito;
            while (true) {
                ultimasInspecoes++;
                if (sucessor.esquerdo == null) break;
                paiSucessor = sucessor;
                sucessor = sucessor.esquerdo;
            }
            atual.registro = sucessor.registro;
            pai = paiSucessor;
            atual = sucessor;
        }
        No filho = atual.esquerdo != null ? atual.esquerdo : atual.direito;
        if (pai == null) raiz = filho;
        else if (pai.esquerdo == atual) pai.esquerdo = filho;
        else pai.direito = filho;
        quantidade--;
        inspecoesRemocao += ultimasInspecoes;
        return true;
    }

    public List<Registro> emOrdem() {
        List<Registro> resultado = new ArrayList<>();
        Deque<No> pilha = new ArrayDeque<>();
        No atual = raiz;
        while (atual != null || !pilha.isEmpty()) {
            while (atual != null) {
                pilha.push(atual);
                atual = atual.esquerdo;
            }
            atual = pilha.pop();
            resultado.add(atual.registro);
            atual = atual.direito;
        }
        return resultado;
    }

    @Override
    public Registro[] consultarIntervalo(int inicio, int fim) {
        List<Registro> resultado = new ArrayList<>();
        ultimasInspecoes = 0;
        if (inicio <= fim) {
            Deque<No> pilha = new ArrayDeque<>();
            No atual = raiz;
            while (atual != null || !pilha.isEmpty()) {
                while (atual != null) {
                    ultimasInspecoes++;
                    pilha.push(atual);
                    atual = atual.registro.getId() > inicio ? atual.esquerdo : null;
                }
                atual = pilha.pop();
                int id = atual.registro.getId();
                if (id > fim) break;
                if (id >= inicio) resultado.add(atual.registro);
                atual = id < fim ? atual.direito : null;
            }
        }
        inspecoesIntervalo += ultimasInspecoes;
        inspecoesUltimaConsulta = (int) ultimasInspecoes;
        return resultado.toArray(new Registro[0]);
    }

    // Altura em arestas: arvore vazia = -1; folha = 0.
    public int altura() {
        if (raiz == null) return -1;
        Deque<No> fila = new ArrayDeque<>();
        fila.add(raiz);
        int altura = -1;
        while (!fila.isEmpty()) {
            int nivel = fila.size();
            altura++;
            for (int i = 0; i < nivel; i++) {
                No no = fila.remove();
                if (no.esquerdo != null) fila.add(no.esquerdo);
                if (no.direito != null) fila.add(no.direito);
            }
        }
        return altura;
    }

    public int getQuantidade() { return quantidade; }
    @Override
    public int tamanho() { return quantidade; }
    @Override
    public int getInspecoesUltimaBusca() { return inspecoesUltimaBusca; }
    @Override
    public int getInspecoesUltimaConsulta() { return inspecoesUltimaConsulta; }
    public long getUltimasInspecoes() { return ultimasInspecoes; }
    public long getInspecoesInsercao() { return inspecoesInsercao; }
    public long getInspecoesBusca() { return inspecoesBusca; }
    public long getInspecoesRemocao() { return inspecoesRemocao; }
    public long getInspecoesIntervalo() { return inspecoesIntervalo; }

    public void zerarContadores() {
        ultimasInspecoes = inspecoesInsercao = inspecoesBusca = inspecoesRemocao = inspecoesIntervalo = 0;
        inspecoesUltimaBusca = inspecoesUltimaConsulta = 0;
    }
    @Override
    public void resetarContadores() { zerarContadores(); }
}
