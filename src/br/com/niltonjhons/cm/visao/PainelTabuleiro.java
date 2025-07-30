package br.com.niltonjhons.cm.visao;
import br.com.niltonjhons.cm.modelo.Tabuleiro;
import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        int total = tabuleiro.getLinhas() * tabuleiro.getColunas();

        tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
                if(e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "❤ Vitória ❤");
                } else {
                    JOptionPane.showMessageDialog(this, "💀 Explosão 💀");
                }

                tabuleiro.reiniciar();

            });
        });
    }
}