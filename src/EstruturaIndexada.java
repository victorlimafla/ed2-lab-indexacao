public interface EstruturaIndexada {

    // Operações principais
    boolean inserir(Registro registro);   // false se o id já existir (não sobrescreve)
    Registro buscar(int id);              // null se não existir
    boolean remover(int id);              // false se o id não existir
    int tamanho();                        // quantidade de registros armazenados

    // Consulta por intervalo (min e max inclusivos)
    Registro[] consultarIntervalo(int min, int max);   // registros com id entre min e max (vetor vazio se nenhum)

    // Métricas
    int getInspecoesUltimaBusca();        // nós/entradas examinados na última chamada a buscar()
    int getInspecoesUltimaConsulta();     // nós/entradas examinados na última chamada a consultarIntervalo()
    void resetarContadores();             // zera todos os contadores
}