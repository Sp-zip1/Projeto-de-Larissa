package Ações;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BuffManager {
    private final List<BuffBase> buffs = new ArrayList<>();
    public static class BuffBase{}
    public static class BuffTemporario extends BuffBase {
        private final BiConsumer<Jogador, Carta> acao;
        private boolean usado = false;

        public BuffTemporario(BiConsumer<Jogador, Carta> acao) {
            this.acao = acao;
        }

        public void aplicar(Jogador jogador, Carta carta) {
            if (!usado) {
                acao.accept(jogador, carta);
                usado = true;
            }
        }

        public boolean jaUsou() {
            return usado;
        }
    }
    public static class BuffDuracao extends BuffBase {
        private final BiConsumer<Jogador, Inimigo> acaoPorTurno;
        private int turnosRestantes;

        public BuffDuracao(BiConsumer<Jogador, Inimigo> acaoPorTurno, int turnos) {
            this.acaoPorTurno = acaoPorTurno;
            this.turnosRestantes = turnos;
        }
        public void aplicarPorTurno(Jogador jogador, Inimigo inimigo) {
            if (turnosRestantes > 0) {
                acaoPorTurno.accept(jogador, inimigo);
                turnosRestantes--;
            }
        }
        public boolean terminou() {
            return turnosRestantes <= 0;
        }
        public int getTurnosRestantes() {
            return turnosRestantes;
        }
    }
public void adicionarBuffTemp(BiConsumer<Jogador, Carta> acao){
    buffs.add(new BuffTemporario(acao));
}
public void executarBuffTemp(Jogador j, Carta c){
    List<BuffBase> ativos = new ArrayList<>(buffs);
    for (BuffBase buff : ativos) {
        if (buff instanceof BuffTemporario) {
            ((BuffTemporario) buff).aplicar(j, c);
        }
    }
    buffs.removeIf(buff -> buff instanceof BuffTemporario && ((BuffTemporario) buff).jaUsou());
}
    public void aplicarBuffsDeTurno(Jogador jogador, Inimigo inimigo) {
        List<BuffDuracao> buffsTurno = new ArrayList<>();
        for (BuffBase buff : buffs) {
            if (buff instanceof BuffDuracao) {
                buffsTurno.add((BuffDuracao) buff);
            }
        }
        for (BuffDuracao buff : buffsTurno) {
            buff.aplicarPorTurno(jogador, inimigo);
        }
        buffs.removeIf(buff -> buff instanceof BuffDuracao && ((BuffDuracao) buff).terminou());
    }
    public void limparBuffs() {
        buffs.clear();
    }
    public int contarBuffs() {
        return buffs.size();
    }
    public List<BuffBase> getBuffs() {
        return new ArrayList<>(buffs);
    }
}