package Ações;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;

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
        return new Efeito((j, i) -> i.setHPInimigo(i.getHPInimigo() - (quantia + j.getDanoEXATK())));
    }

    public static Efeito cura(int quantia) {
        return new Efeito((j, i) -> j.setHPPlayer(j.getHPPlayer() + quantia));
    }
    public static Efeito danoExtra(int quantia){
        return new Efeito((j, i) -> j.setDanoEXATK(j.getDanoEXATK()+quantia));
    }
    public static Efeito duplicar(Carta carta){
        return new Efeito((j, i)->carta.executarEfeitos(j, i));
    }
}
