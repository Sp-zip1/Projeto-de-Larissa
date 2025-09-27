package Ações;

import Atores.Inimigo;
import Atores.Jogador;

import java.util.function.BiConsumer;

public class Efeito {
    public BiConsumer<Jogador, Inimigo> acao;

    public Efeito(BiConsumer<Jogador, Inimigo> acao) {
        this.acao = acao;
    }
    public void aplicar(Jogador jogador, Inimigo inimigo) {
        acao.accept(jogador, inimigo);
    }
    public static Efeito dano(int quantia) {
        return new Efeito((j, i) -> i.setHPInimigo(i.getHPInimigo() - quantia));
    }

    public static Efeito cura(int quantia) {
        return new Efeito((j, i) -> j.setHPPlayer(j.getHPPlayer() + quantia));
    }

}
