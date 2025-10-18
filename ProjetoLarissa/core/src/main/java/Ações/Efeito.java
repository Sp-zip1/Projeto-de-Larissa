package Ações;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.Main;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiConsumer;

public class Efeito {
    public BiConsumer<Jogador, Inimigo> acao;
    public static  Random random = new Random();
    public Texture icone;
    public Efeito(BiConsumer<Jogador, Inimigo> acao) {
        this.acao = acao;
    }
    public void aplicar(Jogador jogador, Inimigo inimigo) {
        acao.accept(jogador, inimigo);
    }
    public Texture getIcone() {
        return icone;
    }
    public Efeito setIcone(Texture icone) {
        this.icone = icone;
        return this;
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
        }).setIcone(Main.instance.iconduplication);
    }
    public static Efeito cavarCartas(int quantia){
        return new Efeito((j, i) -> {
            ArrayList<Carta> cartasPuxadas = j.puxarCartasDoDeck(quantia);
            j.mãoPlayer.addAll(cartasPuxadas);
        });
    }
    public static Efeito danoJogador(int quantia){
        return new Efeito(((j, i) -> j.setHPPlayer(j.getHPPlayer() - (quantia+i.getDano())))).setIcone(Main.getInstance().attackicon);
    }
    public static Efeito removerMão() {
        return new Efeito((j, i) -> {
            int index = random.nextInt(j.mãoPlayer.size());
            j.mãoPlayer.remove(index);
        });
    }

        public static Efeito curaIn(int quantia) {
            return new Efeito((j, i) -> i.setHPInimigo(i.getHPInimigo() + quantia));
        }
    public static Efeito danoExtraIn(int quantia){
        return new Efeito((j, i) -> i.setDano(1));
    }
    public static Efeito manaExtra(int quantia){
        return new Efeito(((j, i) -> j.setMana(j.getMana()+1)));
    }

}
