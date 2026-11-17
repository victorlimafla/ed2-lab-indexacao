public class TabelaHash implements EstruturaIndexada {
    private final int capacidade;
    private int colisoes;
    private int tamanho;
    private int inspecoesUltimaBusca;
    private int inspecoesUltimaConsulta;
    private final ListaEncadeada[] tabela;

    public TabelaHash(int capacidade){
        this.capacidade = capacidade;
        this.colisoes = 0;
        tabela = new ListaEncadeada[capacidade];
        for(int i = 0; i < capacidade; i++){
            tabela[i] = new ListaEncadeada();
        }
    }

    private int funcaoHash(int id){
        return Math.floorMod(id, capacidade);
    }

    @Override
    public boolean inserir(Registro registro) {
        int indice = funcaoHash(registro.getId());
        ListaEncadeada lista = tabela[indice];

        No atual = lista.getInicio();
        while(atual != null){
            if(atual.getRegistro().getId() == registro.getId()){
                return false;
            }
            atual = atual.getProximoNo();
        }

        if(!lista.isEmpty()){
            colisoes++;
        }

        lista.insert(registro);
        tamanho++;
        return true;
    }

    @Override
    public Registro buscar(int id) {
        int indice = funcaoHash(id);
        ListaEncadeada lista = tabela[indice];
        inspecoesUltimaBusca = 0;

        No atual = lista.getInicio();

        while (atual != null){
            inspecoesUltimaBusca++;
            if(atual.getRegistro().getId() == id){
                return atual.getRegistro();
            }

            atual = atual.getProximoNo();
        }

        return null;
    }

    @Override
    public boolean remover(int id) {
        int indice = funcaoHash(id);
        ListaEncadeada lista = tabela[indice];

        boolean removido = lista.remove(id);
        if(removido){
            tamanho--;
        }

        return removido;
    }

    @Override
    public int tamanho() {
        return tamanho;
    }

    @Override
    public Registro[] consultarIntervalo(int min, int max) {
        inspecoesUltimaConsulta = 0;
        int encontrados = 0;

        for(ListaEncadeada lista : tabela){
            No atual = lista.getInicio();
            while(atual != null){
                inspecoesUltimaConsulta++;
                int id = atual.getRegistro().getId();
                if(id >= min && id <= max){
                    encontrados++;
                }
                atual = atual.getProximoNo();
            }
        }

        Registro[] resultado = new Registro[encontrados];
        int i = 0;
        for(ListaEncadeada lista : tabela){
            No atual = lista.getInicio();
            while (atual != null){
                int id = atual.getRegistro().getId();
                if(id >= min && id <= max){
                    resultado[i] = atual.getRegistro();
                    i++;
                }
                atual = atual.getProximoNo();
            }
        }

        return resultado;
    }

    @Override
    public int getInspecoesUltimaBusca() {
        return inspecoesUltimaBusca;
    }

    @Override
    public int getInspecoesUltimaConsulta() {
        return inspecoesUltimaConsulta;
    }

    @Override
    public void resetarContadores() {
        colisoes = 0;
        inspecoesUltimaBusca = 0;
        inspecoesUltimaConsulta = 0;
    }

    public int getCapacidade(){
        return capacidade;
    }

    public double getFatorCarga(){
        return (double) tamanho / capacidade;
    }

    public int getColisoes(){
        return colisoes;
    }


    //Classe Nó

    private static class No {
        private final Registro registro;
        private No proximoNo;

        public No(Registro registro) {
            this.registro = registro;
            this.proximoNo = null;
        }

        public Registro getRegistro() {
            return registro;
        }

        public No getProximoNo() {
            return proximoNo;
        }

        public void setProximoNo(No proximoNo) {
            this.proximoNo = proximoNo;
        }
    }


    //Classe Lista Encadeada

    private static class ListaEncadeada{
        private No inicio;
        private No fim;

        public ListaEncadeada() {
            this.inicio = null;
            this.fim = null;
        }

        public No getInicio() {
            return inicio;
        }

        public boolean isEmpty(){
            return (inicio == null);
        }

        public void insert(Registro registro){
            No novoNo = new No(registro);

            if(isEmpty()){
                inicio = novoNo;
                fim = novoNo;
            }
            else{
                fim.setProximoNo(novoNo);
                fim = novoNo;
            }
        }

        public boolean remove(int id){
            if(isEmpty()){
                return false;
            }

            No anterior = null;
            No atual = inicio;

            while(atual != null && atual.getRegistro().getId() != id){
                anterior = atual;
                atual = atual.getProximoNo();
            }

            if(atual == null){
                return false;
            }

            if(atual == inicio){
                inicio = inicio.getProximoNo();

                if(inicio == null){
                    fim = null;
                }
            }
            else{
                anterior.setProximoNo(atual.getProximoNo());

                if(atual == fim){
                    fim = anterior;
                }
            }

            return true;
        }
    }
}




