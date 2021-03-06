package optimizacion.local;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
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
        vertices = new ArrayList<>();
        predecesores = new HashMap<>();
        sucesores = new HashMap<>();
        bloqueToNodo = new HashMap<>();
    }

    public void addVertice(BloqueBasico origen) {
        Nodo nodo = new Nodo(origen);
        bloqueToNodo.put(origen, nodo);
        vertices.add(nodo);
        predecesores.put(nodo, new ArrayList<>());
        sucesores.put(nodo, new ArrayList<>());
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

    public List<BloqueBasico> getBloquesBasicos() {
        return this.vertices.stream().map(m -> m.getBloqueBasico()).collect(Collectors.toList());
    }

    public List<BloqueBasico> getPredecesores(BloqueBasico bloque) {
        if (bloqueToNodo.get(bloque) != null && this.predecesores.get(bloqueToNodo.get(bloque)) != null) {
            return this.predecesores.get(bloqueToNodo.get(bloque)).stream().map(m -> m.getDestino().getBloqueBasico()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<BloqueBasico> getSucesores(BloqueBasico bloque) {
        if (bloqueToNodo.get(bloque) != null && this.sucesores.get(bloqueToNodo.get(bloque)) != null) {
            return this.sucesores.get(bloqueToNodo.get(bloque)).stream()
                    .map(m -> m.getDestino().getBloqueBasico())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<Arco> getArcos() {
        List<Arco> arcos = new ArrayList<>();
        this.vertices.stream().filter(p -> {
            return ((this.sucesores.get(p) != null) && (!this.sucesores.get(p).isEmpty()));
        }).forEach(v -> {
            this.sucesores.get(v).stream().forEach(s -> arcos.add(new Arco(s.getOrigen().getBloqueBasico(), s.getDestino().getBloqueBasico())));
            ;
        });
        return arcos;
    }

    public void removeArista(BloqueBasico origen, BloqueBasico destino) {
        Nodo nodoOrigen = bloqueToNodo.get(origen);
        Nodo nodoDestino = bloqueToNodo.get(destino);

        Arista arista = sucesores.get(nodoOrigen).stream().filter(ar -> ar.getDestino() == nodoDestino).findFirst().orElse(null);
        assert arista != null;
        sucesores.get(nodoOrigen).remove(arista);

        arista = predecesores.get(nodoDestino).stream().filter(ar -> ar.getDestino() == nodoOrigen).findFirst().orElse(null);
        assert arista != null;
        predecesores.get(nodoDestino).remove(arista);
    }

    public List<BloqueBasico> getVerticesInconnexos() {
        paintGraph();
        return this.vertices.stream().filter(Nodo::isUnvisited).map(Nodo::getBloqueBasico).collect(Collectors.toList());
    }

    public void exportToFile(String filename) throws ErrorProcesador, IOException {
        File file;
        FileWriter fileWriter;
        try {
            file = new File(GlobalVariables.outputDir.resolve(filename).toString());
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            throw new ErrorProcesador("Se ha producido un error al abrir el fichero de código intermedio");
        }

        fileWriter.write(this.getPrintableGraph());

        try {
            System.out.println("Se ha generado el fichero de código intermedio en: "+file.getAbsolutePath());
            fileWriter.close();
        } catch (IOException e) {
            throw new ErrorProcesador("Se ha producido un error al cerrar el fichero de código intermedio");
        }
    }

    private String getPrintableGraph() {

        // Duplicidad de codigo para evitar tener que pasar un función acción y tener que hacer
        // retornos en el algoritmo de coloreado
        for (Nodo vertice : vertices) {
            vertice.markUnvisited();
        }


        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {");
        // Suponemos que el vertice de inicio es el primero #PorqueYoLoValgo
        sb.append(printNode(vertices.get(0)));
        sb.append("}");

        return sb.toString();
    }

    private StringBuilder printNode(Nodo vertice) {
        StringBuilder str = new StringBuilder();
        Nodo legitimoSucesor;
        vertice.markVisiting();
        // Imprimimos el nodo actual
        str.append(vertice.getId())
                .append(" [color=lightgrey,style=filled,label=\"")
                .append(vertice.getName())
                .append("\"]")
                .append(System.lineSeparator());

        // Y Ahora imprimimos las aristas a los sucesores
        for (Arista sucesor : sucesores.get(vertice)) {
            legitimoSucesor = sucesor.getDestino();
            str.append(vertice.getId()).append(" -> ").append(legitimoSucesor.getId()).append(System.lineSeparator());
            if (legitimoSucesor.isUnvisited()) {
                str.append(printNode(legitimoSucesor));
            }
        }
        vertice.markVisited();

        return str;
    }

    private void paintGraph() {
        for (Nodo vertice : vertices) {
            vertice.markUnvisited();
        }
        // Suponemos que el vertice de inicio es el primero #PorqueYoLoValgo
        visit(vertices.get(0));
    }

    private void visit(Nodo vertice) {
        Nodo legitimoSucesor;
        vertice.markVisiting();
        for (Arista sucesor : sucesores.get(vertice)) {
            legitimoSucesor = sucesor.getDestino();
            if (legitimoSucesor.isUnvisited()) {
                visit(legitimoSucesor);
            }
        }
        vertice.markVisited();
    }
}
