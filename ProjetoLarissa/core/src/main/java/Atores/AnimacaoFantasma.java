package Atores;

public class AnimacaoFantasma implements AnimacaoInimigo {
    private float tempo = 0f;
    private float velocidade = 1.5f;
    private float alphaMin = 0.6f;

    public AnimacaoFantasma() {}

    public AnimacaoFantasma(float velocidade, float alphaMin) {
        this.velocidade = velocidade;
        this.alphaMin = alphaMin;
    }

    @Override
    public void atualizar(float delta) {
        tempo += delta;
    }

    @Override
    public float getOffsetX() {
        return 0;
    }

    @Override
    public float getOffsetY() {
        return 0;
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
        return alphaMin + (1f - alphaMin) * (0.5f + 0.5f * (float)Math.sin(tempo * velocidade));
    }

    @Override
    public void resetar() {
        tempo = 0f;
    }
}
