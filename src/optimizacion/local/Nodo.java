package optimizacion.local;

import intermedio.BloqueBasico;

import java.awt.*;

class Nodo {
    private BloqueBasico bloqueBasico;
    private Color color;

    public Nodo(BloqueBasico basico) {
        bloqueBasico = basico;
        color = Color.WHITE;
    }
    
    public BloqueBasico getBloqueBasico() {
    	return this.bloqueBasico;
    }

    public void markUnvisited() {
        this.color = Color.WHITE;
    }

    public void markVisiting() {
        this.color = Color.GRAY;
    }

    public void markVisited() {
        this.color = Color.BLACK;
    }

    public boolean isUnvisited() {
        return Color.WHITE.equals(color);
    }
}