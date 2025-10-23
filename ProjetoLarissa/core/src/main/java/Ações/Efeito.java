package Ações;

import Atores.Inimigo;
import Atores.Jogador;
import Entidades.Carta;
import Entidades.TipoC;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.Main;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class Efeito {
    public BiConsumer<Jogador, Inimigo> acao;
    public static  Random random = new Random();
    public int dano;
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
            j.getBuffManager().adicionarBuffTemp((jogador, carta) -> {
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
            Carta cartaRemovida = j.mãoPlayer.get(index);
            j.mãoPlayer.remove(index);
            j.exaustas.add(cartaRemovida);
        });
    }

        public static Efeito curaIn(int quantia) {
            return new Efeito((j, i) -> i.setHPInimigo(i.getHPInimigo() + quantia));
        }
    public static Efeito danoExtraIn(int quantia){
        return new Efeito((j, i) -> i.setDano(i.getDano()+1));
    }
    public static Efeito manaExtra(int quantia){
        return new Efeito(((j, i) -> j.setMana(j.getMana()+1)));
    }
    public static Efeito nullPointerSlash(int quantia){
        return new Efeito((j, i) -> {
            int index = random.nextInt(1, 10);
            if(index > 5){
                i.setHPInimigo(i.getHPInimigo()-quantia);
            }
            else{
                j.setHPPlayer(j.getHPPlayer()-quantia);
            }
        });
    }
    public static Efeito systemOverclock(){
        return new Efeito((j, i)->{
            j.setDanoEXATK(j.getDanoEXATK()+(j.getDanoEXATK()*2));
        });
    }
    public static Efeito imunedano(int quantia){
        return new Efeito((j, i)->{
           i.setTurnosInvuneravel(quantia);
        });
    }
    public static Efeito imunedanoJ(int quantia){
        return new Efeito((j, i)->{
            j.setTurnosInvulneravel(quantia);
        });
    }
    public static Efeito adaptiveAI() {
        return new Efeito((j, i) -> {
            float hpRatio = (float) j.getHPPlayer() / 100f;
            if (hpRatio < 0.4f) {
                j.setHPPlayer(j.getHPPlayer() + 20);
                j.setTurnosInvulneravel(1);
            } else {
                int dano = 15 + j.getDanoEXATK();
                i.setHPInimigo(i.getHPInimigo() - dano);
            }
        });
    }
    public static Efeito forcaCrescente(int bonusInicial, int incremento, int turnos) {
        return new Efeito((j, i) -> {
            final int[] bonusAtual = {bonusInicial};
            j.getBuffManager().adicionarBuffDuracao((jogador, inimigo) -> {
                jogador.setDanoEXATK(jogador.getDanoEXATK() + bonusAtual[0]);
                System.out.println("Força crescente: +" + bonusAtual[0] + " dano");
                bonusAtual[0] += incremento;
            }, turnos);
        });
    }
    public static Efeito addCurse(){
        return new Efeito((j, i)->{
            j.deckPlayer.add(Main.getInstance().fabricasCartas.get("erro404").criarCarta());
        });
    }
    public static Efeito danoContinuoJogador(int danoI, int turnos) {
        return new Efeito((j, i) -> {
            AtomicInteger danoAtual = new AtomicInteger(danoI);
            i.getBuffManager().adicionarBuffDuracao((jogador, inimigo) -> {
                int dano = danoAtual.getAndDecrement();
                jogador.setHPPlayer(jogador.getHPPlayer() - dano);
            }, turnos);
        });
    }
    public static Efeito curaError(int cura){
        return new Efeito((j, i)->{
            for (Carta c: Main.getInstance().jogador.deckPlayer){
                if (c.tipoC == TipoC.CUR){
                    i.setHPInimigo(i.getHPInimigo()+cura);
                }
            }
        });
    }
    public static Efeito danoContinuoInimigo(int danoI, int turnos) {
        return new Efeito((j, i) -> {
            AtomicInteger danoAtual = new AtomicInteger(danoI);
            j.getBuffManager().adicionarBuffDuracao((jogador, inimigo) -> {
                int dano = danoAtual.getAndIncrement();
               i.setHPInimigo(i.getHPInimigo() - dano);
            }, turnos);
        });
    }
    public static Efeito pingOfDeath() {
        return new Efeito((j, i) -> {
            int dano = 5 + j.getDanoEXATK();
            int hpAntes = i.getHPInimigo();
            i.setHPInimigo(i.getHPInimigo() - dano);

            if(i.getHPInimigo() <= 0 && hpAntes > 0) {
                j.setHPPlayer(j.getHPPlayer() + 10);
            }
        });
}
    public static Efeito regeneracao(int curaPorTurno, int turnos) {
        return new Efeito((j, i) -> {
            j.getBuffManager().adicionarBuffDuracao((jogador, inimigo) -> {
                jogador.setHPPlayer(j.getHPPlayer()+curaPorTurno);
            }, turnos);
        });
    }
   public static Efeito systemCorruption() {
        return new Efeito((j, i) -> {
            j.setMana(j.getMana()-1);
        });
    }
public  static Efeito overclockProtocol() {
        return new Efeito((j, i) -> {
            i.getBuffManager().adicionarBuffDuracao((jogador, inimigo) -> {
                inimigo.setDano(inimigo.getDano() * 2);
                Efeito.dano(5);
            }, 2);
        });
    }
    public  static Efeito CompressDan() {
        return new Efeito((j, i) -> {
           Efeito.dano(2 + j.deckPlayer.size());
           System.out.println("efeitodano");
        });
    }
}
