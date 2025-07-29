package br.com.niltonjhons.cm.modelo;
import java.util.ArrayList;
import java.util.List;

public class Campo {
    private final int linha;
    private final int coluna;

    private boolean minado;
    private boolean aberto;
    private boolean marcado;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObservador observador) {
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento) {
        observadores.forEach(o -> o.eventoOcorreu(this, evento));
    }

    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = this.linha != vizinho.linha;
        boolean colunaDiferente = this.coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(this.linha - vizinho.linha);
        int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;


        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else { // Não é vizinho válido
            return false;
        }
    }

    boolean abrir() {
        if (!aberto && !marcado) {
            if (minado) {
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);

            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    boolean vizinhancaSegura() {
        return vizinhos.stream()
                .noneMatch(v -> v.minado);
    }

    void minar() {
        minado = true;
    }

    void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;
        }

        if (marcado) {
            notificarObservadores(CampoEvento.MARCAR);
        } else {
            notificarObservadores(CampoEvento.DESMARCAR);
        }
    }

    long minasNaVizinhanca() {
        return vizinhos.stream()
                .filter(v -> v.minado)
                .count();
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
    }

    public boolean isMinado() {
        return minado;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public boolean isAberto() {
        return aberto;
    }
    public boolean isFechado() {
        return !isAberto();
    }

    public int getLinha() {
        return linha;
    }
    public int getColuna() {
        return coluna;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
        if (aberto) {
            notificarObservadores(CampoEvento.ABRIR);
        }
    }
}