package Ações;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BuffManager {
    private final List<BuffBase> buffs = new ArrayList<>();
    public static class BuffBase{
        protected Texture icone;

        public Texture getIcone() {
            return icone;
        }

        public void setIcone(Texture icone) {
            this.icone = icone;
        }
    }
    public static class BuffTemporario extends BuffBase {
        private final BiConsumer<Jogador, Carta> acao;
        private boolean usado = false;
        public BuffTemporario(BiConsumer<Jogador, Carta> acao, Texture icone) {
            this.acao = acao;
            this.icone = icone;
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
        public BuffDuracao(BiConsumer<Jogador, Inimigo> acaoPorTurno, int turnos, Texture icone) {
            this.acaoPorTurno = acaoPorTurno;
            this.turnosRestantes = turnos;
            this.icone = icone;
        }

        public Texture getIcone() {
            return icone;
        }

        public void setIcone(Texture icone) {
            this.icone = icone;
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
public void adicionarBuffTemp(BiConsumer<Jogador, Carta> acao, Texture icone){
    buffs.add(new BuffTemporario(acao, icone));
}
    public void adicionarBuffDuracao(BiConsumer<Jogador, Inimigo> acaoPorTurno, int turnos, Texture icone) {
        buffs.add(new BuffDuracao(acaoPorTurno, turnos, icone));
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