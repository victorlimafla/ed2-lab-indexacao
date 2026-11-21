import java.util.ArrayList;
import java.util.List;

public class AVL implements EstruturaIndexada {
    private static class No {
        Registro registro;
        No esquerdo, direito;
        int altura; // Folha = 0; filho nulo = -1.
        No(Registro registro) { this.registro = registro; }
    }

    private No raiz;
    private int quantidade;
    private long inspecoesInsercao, inspecoesBusca, inspecoesRemocao, inspecoesIntervalo;
    private long ultimasInspecoes, rotacoes;
    private Registro removido;
    private int inspecoesUltimaBusca, inspecoesUltimaConsulta;

    private int altura(No no) { return no == null ? -1 : no.altura; }
    private int fatorDeBalanceamento(No no) {
        return no == null ? 0 : altura(no.esquerdo) - altura(no.direito);
    }
    private void atualizarAltura(No no) {
        no.altura = Math.max(altura(no.esquerdo), altura(no.direito)) + 1;
    }
    private No rotacaoDireita(No y) {
        No x = y.esquerdo;
        y.esquerdo = x.direito;
        x.direito = y;
        atualizarAltura(y);
        atualizarAltura(x);
        rotacoes++;
        return x;
    }
    private No rotacaoEsquerda(No x) {
        No y = x.direito;
        x.direito = y.esquerdo;
        y.esquerdo = x;
        atualizarAltura(x);
        atualizarAltura(y);
        rotacoes++;
        return y;
    }
    private No balancear(No no) {
        atualizarAltura(no);
        int fator = fatorDeBalanceamento(no);
        if (fator > 1) {
            if (fatorDeBalanceamento(no.esquerdo) < 0)
                no.esquerdo = rotacaoEsquerda(no.esquerdo);
            return rotacaoDireita(no);
        }
        if (fator < -1) {
            if (fatorDeBalanceamento(no.direito) > 0)
                no.direito = rotacaoDireita(no.direito);
            return rotacaoEsquerda(no);
        }
        return no;
    }

    // IDs repetidos sao ignorados: o registro previamente inserido permanece.
    @Override
    public boolean inserir(Registro registro) {
        if (registro == null) throw new IllegalArgumentException("registro nulo");
        ultimasInspecoes = 0;
        int quantidadeAnterior = quantidade;
        raiz = inserir(raiz, registro);
        inspecoesInsercao += ultimasInspecoes;
        return quantidade > quantidadeAnterior;
    }
    private No inserir(No no, Registro registro) {
        if (no == null) {
            quantidade++;
            return new No(registro);
        }
        ultimasInspecoes++;
        if (registro.getId() < no.registro.getId())
            no.esquerdo = inserir(no.esquerdo, registro);
        else if (registro.getId() > no.registro.getId())
            no.direito = inserir(no.direito, registro);
        else return no;
        return balancear(no);
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
        removido = null;
        raiz = remover(raiz, id);
        inspecoesRemocao += ultimasInspecoes;
        boolean resultado = removido != null;
        removido = null;
        return resultado;
    }
    private No remover(No no, int id) {
        if (no == null) return null;
        ultimasInspecoes++;
        if (id < no.registro.getId()) no.esquerdo = remover(no.esquerdo, id);
        else if (id > no.registro.getId()) no.direito = remover(no.direito, id);
        else {
            removido = no.registro;
            quantidade--;
            if (no.esquerdo == null) return no.direito;
            if (no.direito == null) return no.esquerdo;
            no.direito = removerMinimo(no.direito, no);
        }
        return balancear(no);
    }
    private No removerMinimo(No no, No destino) {
        ultimasInspecoes++;
        if (no.esquerdo == null) {
            destino.registro = no.registro;
            return no.direito;
        }
        no.esquerdo = removerMinimo(no.esquerdo, destino);
        return balancear(no);
    }

    public List<Registro> emOrdem() {
        List<Registro> resultado = new ArrayList<>();
        emOrdem(raiz, resultado);
        return resultado;
    }
    private void emOrdem(No no, List<Registro> resultado) {
        if (no == null) return;
        emOrdem(no.esquerdo, resultado);
        resultado.add(no.registro);
        emOrdem(no.direito, resultado);
    }
    @Override
    public Registro[] consultarIntervalo(int inicio, int fim) {
        List<Registro> resultado = new ArrayList<>();
        ultimasInspecoes = 0;
        if (inicio <= fim) consultarIntervalo(raiz, inicio, fim, resultado);
        inspecoesIntervalo += ultimasInspecoes;
        inspecoesUltimaConsulta = (int) ultimasInspecoes;
        return resultado.toArray(new Registro[0]);
    }
    private void consultarIntervalo(No no, int inicio, int fim, List<Registro> resultado) {
        if (no == null) return;
        ultimasInspecoes++;
        int id = no.registro.getId();
        if (id > inicio) consultarIntervalo(no.esquerdo, inicio, fim, resultado);
        if (id >= inicio && id <= fim) resultado.add(no.registro);
        if (id < fim) consultarIntervalo(no.direito, inicio, fim, resultado);
    }
    public int altura() { return altura(raiz); }
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
    public long getRotacoes() { return rotacoes; }
    public void zerarContadores() {
        ultimasInspecoes = inspecoesInsercao = inspecoesBusca = inspecoesRemocao = inspecoesIntervalo = rotacoes = 0;
        inspecoesUltimaBusca = inspecoesUltimaConsulta = 0;
    }
    @Override
    public void resetarContadores() { zerarContadores(); }
}
