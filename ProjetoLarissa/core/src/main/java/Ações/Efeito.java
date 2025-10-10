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
        return new Efeito((j, i) -> {
            int novoHP = i.getHPInimigo() - (quantia + j.getDanoEXATK());
            i.setHPInimigo(novoHP); // já limita a 0 dentro do setter
        });
    }


    public static Efeito cura(int quantia) {
        return new Efeito((j, i) -> j.setHPPlayer(j.getHPPlayer() + quantia));
    }
    public static Efeito danoExtra(int quantia){
        return new Efeito((j, i) -> j.setDanoEXATK(j.getDanoEXATK()+quantia));
    }
    public static Efeito duplicarProxima() {
        return new Efeito((j, i) -> {
            j.adicionarBuffTemporario((jogador, carta) -> {
                carta.executarEfeitosDireto(jogador, i);
            });
        });
    }
    public static Efeito cavarCartas(int quantia){
        return new Efeito((j, i) -> j.puxarCartasDoDeck(quantia));
    }
    public static Efeito danoJogador(int quantia){
        return new Efeito(((j, i) -> j.setHPPlayer(j.getHPPlayer() - quantia)));
    }

}
