package Atores;

public class AnimacaoVibrar implements AnimacaoInimigo {
    private float tempo = 0f;
    private float intensidade = 2f;
    private float velocidade = 15f;

    public AnimacaoVibrar() {}

    public AnimacaoVibrar(float intensidade, float velocidade) {
        this.intensidade = intensidade;
        this.velocidade = velocidade;
    }

    @Override
    public void atualizar(float delta) {
        tempo += delta;
    }

    @Override
    public float getOffsetX() {
        return intensidade * (float)Math.sin(tempo * velocidade * 1.3);
    }

    @Override
    public float getOffsetY() {
        return intensidade * (float)Math.cos(tempo * velocidade);
    }

    @Override
    public float getEscala() {
        return 1f;
    }

    @Override
    public float getRotacao() {
        return 0f;
    }

    @Override
    public float getAlpha() {
        return 1f;
    }

    @Override
    public void resetar() {
        tempo = 0f;
    }
}
