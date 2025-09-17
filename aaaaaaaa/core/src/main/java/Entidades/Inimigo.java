package Entidades;

public class Inimigo {
    int vida;
    int dano;

    public Inimigo(int vida, int dano) {
        this.vida = vida;
        this.dano = dano;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }
}
