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

    public String getName() {
        return bloqueBasico.toString();
    }

    public String toString() {
        return bloqueBasico.toString() + " - " + color.toString();
    }

    /*
     * Como este ID de momento solo es para proppositos de impresion podemos utilizar el id del bloque basico.
     * No es del todo correcto, pero bueno, tampoco está horriblemente mal
     */
    public int getId() {
        return bloqueBasico.getId();
    }
}