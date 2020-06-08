package optimizacion.local;

import intermedio.BloqueBasico;

class Nodo {
    private BloqueBasico bloqueBasico;

    public Nodo(BloqueBasico basico) {
        bloqueBasico = basico;
    }
    
    public BloqueBasico getBloqueBasico() {
    	return this.bloqueBasico;
    }
    
}