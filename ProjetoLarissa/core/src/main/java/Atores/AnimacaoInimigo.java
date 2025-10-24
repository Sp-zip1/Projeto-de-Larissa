package Atores;
public interface AnimacaoInimigo {
    void atualizar(float delta);
    float getOffsetX();
    float getOffsetY();
    float getEscala();
    float getRotacao();
    float getAlpha();
    void resetar();
}
