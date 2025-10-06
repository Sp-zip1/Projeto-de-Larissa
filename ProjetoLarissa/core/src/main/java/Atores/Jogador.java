package Atores;
//TENHO QUE ORGANIZAR ESSE CODIGO ESTA  MUITO CONFUSO
import Entidades.Carta;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Jogador {
    public Integer HPPlayer;
    public Integer danoEXATK;
    public Integer defesaEXPlayer;
    public Integer mana;
    public ArrayList<Carta> deckPlayer = new ArrayList<>();
    public ArrayList<Carta> descarte = new ArrayList<>();
    public ArrayList<Carta> mãoPlayer = new ArrayList<>();
    private final List<BiConsumer<Jogador, Carta>> buffs = new ArrayList<>();

    public Jogador(Integer HPPlayer, Integer danoEXATK, Integer defesaEXPlayer, Integer mana) {
        this.HPPlayer = HPPlayer;
        this.danoEXATK = danoEXATK;
        this.defesaEXPlayer = defesaEXPlayer;
        this.mana = mana;
    }
    public Integer getHPPlayer() {
        return HPPlayer;
    }

    public void setHPPlayer(Integer HPPlayer) {
        this.HPPlayer = HPPlayer;
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

    public void setMana(Integer mana) {
        this.mana = mana;
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
}
