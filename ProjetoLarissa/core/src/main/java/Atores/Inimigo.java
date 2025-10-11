package Atores;

import com.badlogic.gdx.graphics.Texture;

import static com.badlogic.gdx.math.MathUtils.random;

public class Inimigo {
    Integer HPInimigo, maxHP;
    Integer Dano;
    Texture inimigoImg;
    Float OffsetX, OffsetY;
    public Inimigo(Integer HPInimigo, Integer maxHP, Integer dano, Texture inimigoImg, Float offsetX, Float offsetY) {
        this.HPInimigo = HPInimigo;
        this.maxHP = maxHP;
        Dano = dano;
        this.inimigoImg = inimigoImg;
        OffsetX = offsetX;
        OffsetY = offsetY;
    }

    public Integer getHPInimigo() {
        return HPInimigo;
    }

    public void setHPInimigo(Integer HPInimigo) {
        this.HPInimigo = HPInimigo;
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

    public void ExecutarAçãoI(Jogador jogador){
        int escolha = random.nextInt(3);
        switch (escolha) {
            case 1:
                jogador.setHPPlayer(jogador.getHPPlayer() - Dano);
                break;
            case 2:
                setHPInimigo(getHPInimigo() + 3);
                break;
            case 3:
                setDano(getDano() + 5);
                System.out.println(Dano);
                break;
        }
    }
}
