package Atores;
//TENHO QUE ORGANIZAR ESSE CODIGO ESTA  MUITO CONFUSO
import Entidades.Carta;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class Jogador {
    public Integer HPPlayer;
    public Integer danoEXATK;
    public Integer defesaEXPlayer;
    public Integer mana;
    public Texture ImgPlayer;
    public Sound som;
    public ArrayList<Carta> deckPlayer = new ArrayList<>();
    public ArrayList<Carta> descarte = new ArrayList<>();
    public ArrayList<Carta> mãoPlayer = new ArrayList<>();
    public ArrayList<Carta> exaustas = new ArrayList<>();
    private final List<BiConsumer<Jogador, Carta>> buffs = new ArrayList<>();
    public Integer turnosInvulneravel=0;

    public Jogador(Integer HPPlayer, Integer danoEXATK, Integer defesaEXPlayer, Integer mana, Texture imgPlayer, Sound som) {
        this.HPPlayer = HPPlayer;
        this.danoEXATK = danoEXATK;
        this.defesaEXPlayer = defesaEXPlayer;
        this.mana = mana;
        this.ImgPlayer = imgPlayer;
        this.som = som;
    }
    public Integer getHPPlayer() {
        return HPPlayer;
    }

    public void setHPPlayer(Integer HPPlayer) {
        if(turnosInvulneravel == 0){
        this.HPPlayer = Math.min(Math.max(HPPlayer, 0), 100);}
    }

    public Integer getDanoEXATK() {
        return danoEXATK;
    }

    public void setDanoEXATK(Integer danoEXATK) {
        this.danoEXATK = danoEXATK;
    }

    public Integer getDefesaEXPlayer() {
        return defesaEXPlayer;
    }

    public void setDefesaEXPlayer(Integer defesaEXPlayer) {
        this.defesaEXPlayer = defesaEXPlayer;
    }

    public Integer getMana() {
        return mana;
    }

    public Texture getImgPlayer() {
        return ImgPlayer;
    }

    public void setImgPlayer(Texture imgPlayer) {
        ImgPlayer = imgPlayer;
    }

    public void setMana(Integer mana) {
        this.mana = mana;
    }

    public List<BiConsumer<Jogador, Carta>> getBuffs() {
        return buffs;
    }
    public void atualizarTurno() {
        if (turnosInvulneravel > 0) {
            turnosInvulneravel--;
        }
        aplicarBuffsDeTurno();
    }

    public Integer getTurnosInvulneravel() {
        return turnosInvulneravel;
    }

    public void setTurnosInvulneravel(Integer turnosInvulneravel) {
        this.turnosInvulneravel = turnosInvulneravel;
    }

    //CLASSE INTERNA DE SUPORTE
    //VOU BOTAR SEPARADO DPS???
    private static class BuffTemporario implements BiConsumer<Jogador, Carta> {
        private final BiConsumer<Jogador, Carta> acao;
        private boolean usado = false;

        public BuffTemporario(BiConsumer<Jogador, Carta> acao) {
            this.acao = acao;
        }

        @Override
        public void accept(Jogador jogador, Carta carta) {
            if (!usado) {
                acao.accept(jogador, carta);
                usado = true;
            }
        }

        public boolean jaUsou() {
            return usado;
        }
    }
    private static class BuffDuracao implements BiConsumer<Jogador, Carta> {
        private final BiConsumer<Jogador, Carta> acaoPorTurno;
        private int turnosRestantes;

        public BuffDuracao(BiConsumer<Jogador, Carta> acaoPorTurno, int turnos) {
            this.acaoPorTurno = acaoPorTurno;
            this.turnosRestantes = turnos;
        }

        @Override
        public void accept(Jogador jogador, Carta carta) {

        }

        public void aplicarPorTurno(Jogador jogador) {
            if (turnosRestantes > 0) {
                acaoPorTurno.accept(jogador, null);
                turnosRestantes--;
            }
        }

        public boolean terminou() {
            return turnosRestantes <= 0;
        }
    }

    public void adicionarBuffDuracao(BiConsumer<Jogador, Carta> acao, int turnos) {
        buffs.add(new BuffDuracao(acao, turnos));
    }

    public void aplicarBuffsDeTurno() {
        List<BuffDuracao> buffsTurno = new ArrayList<>();

        for (BiConsumer<Jogador, Carta> buff : buffs) {
            if (buff instanceof BuffDuracao) {
                buffsTurno.add((BuffDuracao) buff);
            }
        }

        for (BuffDuracao buff : buffsTurno) {
            buff.aplicarPorTurno(this);
        }

        // Remove buffs expirados
        buffs.removeIf(buff -> buff instanceof BuffDuracao && ((BuffDuracao) buff).terminou());
    }

    public void adicionarBuff(BiConsumer<Jogador, Carta> buff) {
        buffs.add(buff);
    }
    public void adicionarBuffTemporario(BiConsumer<Jogador, Carta> acao) {
        buffs.add(new BuffTemporario(acao));
    }
    //tive que mudar o codigo pois jogar uma carta de duplicação atras da outra causava crash
    public void executarBuffs(Carta carta) {
        List<BiConsumer<Jogador, Carta>> ativos = new ArrayList<>(buffs);

        for (BiConsumer<Jogador, Carta> buff : ativos) {
            buff.accept(this, carta);
        }

        // Remove buffs que já foram usados (temporários)
        buffs.removeIf(buff -> buff instanceof BuffTemporario && ((BuffTemporario) buff).jaUsou());
    }
    public ArrayList<Carta> puxarCartasDoDeck(int quantidade) {
        ArrayList<Carta> resultado = new ArrayList<>();
        while (resultado.size() < quantidade) {
            if (deckPlayer.isEmpty()) {
                if (descarte.isEmpty()) {
                    break;
                }
                deckPlayer.addAll(descarte);
                descarte.clear();
                Collections.shuffle(deckPlayer, new Random());
            }
            resultado.add(deckPlayer.remove(0));
        }
        return resultado;
    }
    public void loopInicioBatalha(){
        if (!exaustas.isEmpty()) {
            System.out.println("Restaurando " + exaustas.size() + " cartas exaustas para o deck");
            deckPlayer.addAll(exaustas);
            exaustas.clear();
        }
        deckPlayer.addAll(descarte);
        deckPlayer.addAll(mãoPlayer);
        descarte.clear();
        mãoPlayer.clear();
        mana = 3;
        danoEXATK = 0;
        turnosInvulneravel = 0;
    }
}

