package Atores;

import com.badlogic.gdx.graphics.Texture;

import static com.badlogic.gdx.math.MathUtils.random;

public class Inimigo {
    Integer HPInimigo, maxHP;
    Integer Dano;
    Texture inimigoImg;

    public Inimigo(Integer HPInimigo, Integer dano, Texture inimigoImg, Integer maxHP) {
        this.HPInimigo = HPInimigo;
        this.Dano = dano;
        this.inimigoImg = inimigoImg;
        this.maxHP = maxHP;
    }

    public Integer getHPInimigo() {
        return HPInimigo;
    }

    public void setHPInimigo(Integer HPInimigo) {
        this.HPInimigo = Math.max(0, Math.min(HPInimigo, maxHP));
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
