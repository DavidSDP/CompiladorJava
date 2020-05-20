package intermedio;

import java.util.ArrayList;
import java.util.HashMap;

class Nodo {
    private BloqueBasico bloqueBasico;

    public Nodo(BloqueBasico basico) {
        bloqueBasico = basico;
    }
}

class Arista {
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

}

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

        ArrayList<Arista> _predecesores = sucesores.getOrDefault(nodoDestino, new ArrayList<>());
        Arista backward = new Arista(nodoDestino, nodoOrigen);
        _predecesores.add(backward);
        predecesores.put(nodoOrigen, _predecesores);
    }
}
