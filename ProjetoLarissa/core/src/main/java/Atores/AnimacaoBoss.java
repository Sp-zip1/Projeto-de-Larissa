package Atores;

public class AnimacaoBoss implements AnimacaoInimigo {
    private float tempo = 0f;

    @Override
    public void atualizar(float delta) {
        tempo += delta;
    }

    @Override
    public float getOffsetX() {
        return 3f * (float)Math.sin(tempo * 0.5);
    }

    @Override
    public float getOffsetY() {
        if (tempo < 2f) {
            return 100f * (1f - tempo / 2f);
        } else {
            return 8f * (float)Math.sin((tempo - 2f) * 1.0);
        }
    }

    @Override
    public float getEscala() {
        if (tempo < 2f) {
            return 0.5f + (tempo / 2f) * 0.5f;
        } else {
            return 1f + 0.05f * (float)Math.sin((tempo - 2f) * 2.5);
        }
    }

    @Override
    public float getRotacao() {
        return 0;
    }

    @Override
    public float getAlpha() {
        return 0;
    }

    @Override
    public void resetar() {

    }

}