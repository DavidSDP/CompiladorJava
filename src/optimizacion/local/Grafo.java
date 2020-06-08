package optimizacion.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import intermedio.BloqueBasico;
import optimizacion.OptimizacionLocal.Arco;

/**
 * Clase del grafo de bloques basicos
 */
public class Grafo {
    // Los vertices son los bloques basicos
    private ArrayList<Nodo> vertices;
    private HashMap<Nodo, ArrayList<Arista>> predecesores;
    private HashMap<Nodo, ArrayList<Arista>> sucesores;

    // Este hashmap es un helper para encontrar los nodos declarados en el grafo
    private HashMap<BloqueBasico, Nodo> bloqueToNodo;

    public Grafo() {
        vertices =  new ArrayList<>();
        predecesores =  new HashMap<>();
        sucesores =  new HashMap<>();
        bloqueToNodo =  new HashMap<>();
    }

    public void addVertice(BloqueBasico origen) {
        Nodo nodo = new Nodo(origen);
        bloqueToNodo.put(origen, nodo);
        vertices.add(nodo);
    }

    // Los vertices tienen que existir
    public void addArista(BloqueBasico origen, BloqueBasico destino) {
        addArista(origen, destino, false);
    }

    // Los vertices tienen que existir
    public void addArista(BloqueBasico origen, BloqueBasico destino, boolean adyacente) {
        Nodo nodoOrigen = bloqueToNodo.get(origen);
        Nodo nodoDestino = bloqueToNodo.get(destino);

        ArrayList<Arista> _sucesores = sucesores.getOrDefault(nodoOrigen, new ArrayList<>());
        Arista forward = new Arista(nodoOrigen, nodoDestino, adyacente);
        _sucesores.add(forward);
        sucesores.put(nodoOrigen, _sucesores);

        ArrayList<Arista> _predecesores = predecesores.getOrDefault(nodoDestino, new ArrayList<>());
        Arista backward = new Arista(nodoDestino, nodoOrigen);
        _predecesores.add(backward);
        predecesores.put(nodoDestino, _predecesores);
    }
    
    public ArrayList<Nodo> getVertices() {
    	return this.vertices;
    }
    
    public List<BloqueBasico> getBloquesBasicos(){
    	return this.vertices.stream().map(m -> m.getBloqueBasico()).collect(Collectors.toList());
    }
    
    public List<BloqueBasico> getPredecesores(BloqueBasico bloque) {
    	if(bloqueToNodo.get(bloque) != null && this.predecesores.get(bloqueToNodo.get(bloque)) != null) {
    		return this.predecesores.get(bloqueToNodo.get(bloque)).stream().map(m -> m.getOrigen().getBloqueBasico()).collect(Collectors.toList());
    	}
    	return new ArrayList<>();
    }
    
    public List<BloqueBasico> getSucesores(BloqueBasico bloque) {
    	if(bloqueToNodo.get(bloque) != null && this.sucesores.get(bloqueToNodo.get(bloque)) != null) {
    		return this.sucesores.get(bloqueToNodo.get(bloque)).stream().map(m -> m.getDestino().getBloqueBasico()).collect(Collectors.toList());
    	}
    	return new ArrayList<>();
    }

	public List<Arco> getArcos() {
		List<Arco> arcos = new ArrayList<>();
		this.vertices.stream().filter(p -> {
			return ((this.sucesores.get(p) != null) && (!this.sucesores.get(p).isEmpty()));
		}).forEach(v->{
			this.sucesores.get(v).stream().forEach(s -> arcos.add(new Arco(s.getOrigen().getBloqueBasico(), s.getDestino().getBloqueBasico())));;
		});
		return arcos;
	}
    
}
