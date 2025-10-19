package Atores;

import Ações.Efeito;
import com.badlogic.gdx.graphics.Texture;
import io.github.some_example_name.Main;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Inimigo {
    Integer HPInimigo, maxHP;
    Integer Dano;
    Texture inimigoImg, inimigoDan, inimigoIdle;
    Float OffsetX, OffsetY;
    ArrayList<Efeito> efeitosIn;
    Efeito escolhido;
    Integer turnosInvuneravel=0;
    public Inimigo(Integer HPInimigo, Integer maxHP, Integer dano, Texture inimigoImg, Texture inimigoDan, Texture inimigoIdle, Float offsetX, Float offsetY, ArrayList<Efeito> efeitosIn) {
        this.HPInimigo = HPInimigo;
        this.maxHP = maxHP;
        Dano = dano;
        this.inimigoImg = inimigoImg;
        this.inimigoDan = inimigoDan;
        this.inimigoIdle = inimigoIdle;
        OffsetX = offsetX;
        OffsetY = offsetY;
        this.efeitosIn = efeitosIn;
    }

    public Integer getHPInimigo() {
        return HPInimigo;
    }

    public void setHPInimigo(Integer HPInimigo) {
        if(turnosInvuneravel <=0) {
            this.HPInimigo = Math.min(Math.max(HPInimigo, 0), maxHP);
        }
    }
    public void atualizarTurno() {
        if (turnosInvuneravel > 0) {
            turnosInvuneravel--;
        }
    }
    public Integer getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(Integer maxHP) {
        this.maxHP = maxHP;
    }

    public Integer getDano() {
        return Dano;
    }

    public void setDano(Integer dano) {
        Dano = dano;
    }

    public Texture getInimigoImg() {
        return inimigoImg;
    }

    public void setInimigoImg(Texture inimigoImg) {
        this.inimigoImg = inimigoImg;
    }

    public Texture getInimigoDan() {
        return inimigoDan;
    }

    public void setInimigoDan(Texture inimigoDan) {
        this.inimigoDan = inimigoDan;
    }

    public Texture getInimigoIdle() {
        return inimigoIdle;
    }

    public void setInimigoIdle(Texture inimigoIdle) {
        this.inimigoIdle = inimigoIdle;
    }

    public Float getOffsetX() {
        return OffsetX;
    }

    public void setOffsetX(Float offsetX) {
        OffsetX = offsetX;
    }

    public Float getOffsetY() {
        return OffsetY;
    }

    public void setOffsetY(Float offsetY) {
        OffsetY = offsetY;
    }

    public ArrayList<Efeito> getEfeitosIn() {
        return efeitosIn;
    }

    public void setEfeitosIn(ArrayList<Efeito> efeitosIn) {
        this.efeitosIn = efeitosIn;
    }

    public Efeito getEscolhido() {
        return escolhido;
    }

    public void setEscolhido(Efeito escolhido) {
        this.escolhido = escolhido;
    }

    public Integer getTurnosInvuneravel() {
        return turnosInvuneravel;
    }

    public void setTurnosInvuneravel(Integer turnosInvuneravel) {
        this.turnosInvuneravel = turnosInvuneravel;
    }

    public void ExecutarAçãoI(Jogador jogador){
        escolhido.aplicar(jogador, this);
    }
    public void Escolheração(){
        if (efeitosIn == null || efeitosIn.isEmpty()) {
            System.err.println("ERRO: Lista de efeitos do inimigo está vazia!");
            return;
        }
        int escolha = random.nextInt(efeitosIn.size());
        escolhido = efeitosIn.get(escolha);
        System.out.println("Inimigo escolheu ação: " + escolhido);
    }
}
