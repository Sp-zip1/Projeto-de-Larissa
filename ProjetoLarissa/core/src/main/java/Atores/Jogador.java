package Atores;

import Entidades.Carta;

import java.util.ArrayList;

public class Jogador {
    public Integer HPPlayer;
    public Integer danoEXATK;
    public Integer defesaEXPlayer;
    public Integer mana;
    public ArrayList<Carta> deckPlayer = new ArrayList<>();
    public ArrayList<Carta> descarte = new ArrayList<>();
    public ArrayList<Carta> mãoPlayer = new ArrayList<>();

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
}
