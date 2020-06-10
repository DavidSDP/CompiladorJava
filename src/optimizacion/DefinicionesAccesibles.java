package optimizacion;

import Procesador.Declaracion;
import intermedio.BloqueBasico;
import intermedio.Definiciones;
import intermedio.InstruccionTresDirecciones;
import optimizacion.local.Grafo;

import java.util.*;
import java.util.stream.Collectors;


class TuplaUsoDefinicion {

    private Declaracion variable;
    private InstruccionTresDirecciones instruccion;

    public TuplaUsoDefinicion(Declaracion variable, InstruccionTresDirecciones instruccion) {
        this.variable = variable;
        this.instruccion = instruccion;
    }

    @Override
    public int hashCode() {
        return (this.variable.hashCode() + " " + this.instruccion.hashCode()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TuplaUsoDefinicion)) {
            return false;
        }

        TuplaUsoDefinicion tupla = (TuplaUsoDefinicion)obj;
        return tupla.variable.equals(variable) && tupla.instruccion.equals(instruccion);
    }
}

/**
 * La idea de esta clase es utilizarla como motor para calcular las definiciones accesibles de
 * una función.
 *
 * Ojo!, esto lo debemos hacer por función porque tal como estamos haciendo la identificación de los
 * bloques basicos no podemos aplicarla a las clases ( se nos jodería htodo el chiriniguito )
 */
public class DefinicionesAccesibles {
    private List<BloqueBasico> bloques;
    private SecuenciaInstrucciones instrucciones;
    private Grafo grafoBloquesBasicos;

    // Conjuntos necesarios para llevar a cabo el calculo de definiciones accesibles ( al menos
    //  el algoritmo general)
    private HashMap<BloqueBasico, Set<InstruccionTresDirecciones>> g;
    private HashMap<BloqueBasico, Set<InstruccionTresDirecciones>> k;
    private HashMap<BloqueBasico, Set<InstruccionTresDirecciones>> in;
    private HashMap<BloqueBasico, Set<InstruccionTresDirecciones>> out;

    private HashMap<InstruccionTresDirecciones, Set<InstruccionTresDirecciones>> definicionUso;
    private HashMap<TuplaUsoDefinicion, Set<InstruccionTresDirecciones>> usoDefinicion;

    private HashSet<InstruccionTresDirecciones> definicionesAccesibles;


    public DefinicionesAccesibles(List<BloqueBasico> bloques, SecuenciaInstrucciones instrucciones, Grafo grafoBloquesBasicos) {
        this.bloques = bloques;
        this.instrucciones = instrucciones;
        this.grafoBloquesBasicos = grafoBloquesBasicos;
        this.g = new HashMap<>();
        this.k = new HashMap<>();
        this.in = new HashMap<>();
        this.out = new HashMap<>();
        this.definicionUso = new HashMap<>();
        this.usoDefinicion = new HashMap<>();

        this.ejecutar();
    }

    public Set<InstruccionTresDirecciones> getDefiniciones(InstruccionTresDirecciones i, Declaracion variable) {
        return usoDefinicion.get(new TuplaUsoDefinicion(variable, i));
    }

    private void ejecutar() {
        rellenarGK();
        rellenarInOut();
        rellenarDA();
    }

    /**
     * Ultima parte del algoritmo.
     * Aqui se rellenan las definiciones accesibles
     * y además también se rellenan las cadenas:
     *  - uso-definicion
     *  - definicion-uso
     */
    private void rellenarDA() {
        for (InstruccionTresDirecciones definicion : Definiciones.getInstance().getDefiniciones()) {
            definicionUso.put(definicion, new HashSet<>());
        }

        HashSet<InstruccionTresDirecciones> definiciones;
        Set<InstruccionTresDirecciones> definicionUsoVariable;
        BloqueBasico bloque;
        InstruccionTresDirecciones instruccion;
        int cantidadBloques = bloques.size();
        for (int idx = 2; idx < cantidadBloques; idx++) {
            bloque = bloques.get(idx);
            definicionesAccesibles = new HashSet<>(in.get(bloque));
            for (int instruccionIdx = bloque.getInicio(); instruccionIdx <= bloque.getFin(); instruccionIdx++) {
                instruccion = instrucciones.get(instruccionIdx);
                ArrayList<Declaracion> argumentos = instruccion.getArgumentos();
                for (Declaracion argumento : argumentos) {
                    definiciones = new HashSet<>();
                    usoDefinicion.put(new TuplaUsoDefinicion(argumento, instruccion), definiciones);
                    for (InstruccionTresDirecciones definicion : definicionesAccesibles) {
                        if (definicion.getDestino().equals(argumento)) {
                            definicionUsoVariable = definicionUso.get(definicion);
                            definicionUsoVariable.add(instruccion);
                        }
                        definiciones.add(definicion);
                    }
                }
                if (instruccion.esDefinicion()) {
                    definicionesAccesibles.removeAll(getDefiniciones(definicionesAccesibles, instruccion.getDestino()));
                    definicionesAccesibles.add(instruccion);
                }
            }
        }
    }

    /**
     * Fase 1 del algoritmo
     */
    private void rellenarGK() {
        BloqueBasico actual;
        HashSet<InstruccionTresDirecciones> tempG;
        HashSet<InstruccionTresDirecciones> tempK;
        int cantidadBloques = bloques.size();
        // E y S no están dentro del algoritmo, por tanto, los obviamos.
        for (int idx = 2; idx < cantidadBloques; idx++) {
            actual = this.bloques.get(idx);
            // Inicializamos G / K a vacío
            tempG = new HashSet<>();
            g.put(actual, tempG);
            tempK = new HashSet<>();
            k.put(actual, tempK);

            // TODO Utilizar un jodido iterador in here
            for (int instruccionIndex = actual.getInicio(); instruccionIndex <= actual.getFin(); instruccionIndex++) {
                InstruccionTresDirecciones i3d = instrucciones.get(instruccionIndex);
                if(i3d.esDefinicion()) {
                    Set<InstruccionTresDirecciones> definiciones = getDefiniciones(i3d.getDestino());
                    tempK.addAll(definiciones);
                    tempG.removeAll(definiciones);
                    tempG.add(i3d);
                }
            }
        }

        // Si nos fijamos en la segunda fase del algoritmo, el único bloque que excluimos es el de inicio
        // por tanto, a pesar de que hemos excluido S de la inicialización, debemos inizializarlo a vacio
        // Puede que esto nos de problemas, ya lo veremos.
        g.put(bloques.get(1), new HashSet<>());
        k.put(bloques.get(1), new HashSet<>());
    }

    /**
     * Fase 2 del algoritmo
     */
    private void rellenarInOut() {
        HashSet<InstruccionTresDirecciones> tempOut;
        HashSet<InstruccionTresDirecciones> tempIn;
        BloqueBasico actual;

        // Ojo! Solo excluimos E. Se ha tenido que retocar la función rellenarGK debido
        // a esta peculiaridad
        int cantidadBloques = bloques.size();
        for (int idx = 1; idx < cantidadBloques; idx++) {
            actual = bloques.get(idx);
            tempOut = new HashSet<>(g.get(actual));
            out.put(actual, tempOut);
        }

        List<BloqueBasico> predecesores;
        ArrayList<BloqueBasico> pendientes = new ArrayList<>(bloques);
        // Quitamos E
        pendientes.remove(bloques.get(0));
        while(!pendientes.isEmpty()) {
            actual = pendientes.remove(0);
            tempIn = new HashSet<>();
            predecesores = grafoBloquesBasicos.getPredecesores(actual);
            for(BloqueBasico predecesor : predecesores) {
                tempIn.addAll(out.get(predecesor));
            }
            // Reutilizamos tempOut para almecenar el nuevo valor temporal de Out;
            // G(b)
            tempOut = new HashSet<>(g.get(actual));
            // I / K(b)
            tempIn.removeAll(k.get(actual));
            // G(b) U  ( I / K(b) )
            tempOut.addAll(tempIn);

            if(!tempOut.equals(out.get(actual))) {
                out.put(actual, tempOut);
                pendientes.addAll(grafoBloquesBasicos.getSucesores(actual));
            }
        }

        // Una vez rellenado Out, podemos rellenar In.
        // TODO Calcular in
    }

    private Set<InstruccionTresDirecciones> getDefiniciones(Declaracion destino) {
        return getDefiniciones(Definiciones.getInstance().getDefiniciones(), destino);
    }

    private Set<InstruccionTresDirecciones> getDefiniciones(Collection<InstruccionTresDirecciones> instrucciones, Declaracion destino) {
        Set<InstruccionTresDirecciones> definiciones = instrucciones.stream().filter(
                d -> destino.equals(d.getDestino())
        ).collect(Collectors.toSet());
        return definiciones;
    }

}
