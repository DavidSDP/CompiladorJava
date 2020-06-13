package optimizacion.local;

public class Arista {
    private boolean adyacente;
    private Nodo origen, destino;

    public Arista(Nodo origen, Nodo destino) {
        this(origen, destino, false);
    }

    public Arista(Nodo origen, Nodo destino, boolean adyacente) {
        this.origen = origen;
        this.destino = destino;
        this.adyacente = adyacente;
    }
    
    public Boolean isAdyacente() {
    	return this.adyacente;
    }
    
    public Nodo getOrigen() {
    	return this.origen;
    }
    
    public Nodo getDestino() {
    	return this.destino;
    }

}