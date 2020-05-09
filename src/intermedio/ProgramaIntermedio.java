package intermedio;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class OptimizationReturn {
    private ArrayList<InstruccionTresDirecciones> instrucciones;
    private boolean cambiado;

    public OptimizationReturn(ArrayList<InstruccionTresDirecciones> instrucciones, boolean cambiado) {
        this.instrucciones = instrucciones;
        this.cambiado = cambiado;
    }

    public ArrayList<InstruccionTresDirecciones> getInstrucciones() {
        return instrucciones;
    }

    public boolean isCambiado() {
        return cambiado;
    }
}

class RangoInstruccionesFuncion {
    // Indice a la primera y última instrucción de una funcion
    // Ambas instrucciones inclusive
    private int inicio, fin;

    public RangoInstruccionesFuncion(int inicio, int fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFin() {
        return fin;
    }
}


/**
 * Un programa intermedio es una secuencia de instrucciones en código de 3
 * direcciones que puede ser traducido a cualquier lenguaje máquina.
 *
 */
public class ProgramaIntermedio implements Iterable<InstruccionTresDirecciones>{

    private static ProgramaIntermedio instance;

    private final ArrayList<InstruccionTresDirecciones> instrucciones;
    private ArrayList<InstruccionTresDirecciones> instruccionesOptimizadas;

    public ProgramaIntermedio() {
        instrucciones = new ArrayList<>();
        instruccionesOptimizadas = new ArrayList<>();
    }

    public void addInstruccion(InstruccionTresDirecciones instr) {
        instrucciones.add(instr);
    }
    
    /**
     * Esta funcion realizará modificaciones sobre las instrucciones 
     * para evitar código muerto autogenerado, saltos innecesarios y 
     * optimización de bucles.
     * 
     * Se puede enfocar tanto como una función como un procedimiento que 
     * modifique el estado del propio programa.
     */
    public void optimizar() {
        OptimizationReturn retornoOptimizacion;
        ArrayList<InstruccionTresDirecciones> optimizado = (ArrayList<InstruccionTresDirecciones>)instrucciones.clone();
        boolean cambiado = true;

        // Mirilla
        while (cambiado) {
            retornoOptimizacion = optimizarSaltosCondicionales(optimizado);
            cambiado = retornoOptimizacion.isCambiado();
            optimizado = retornoOptimizacion.getInstrucciones();

        }

        // Local
        cambiado = true;
        while (cambiado) {
            retornoOptimizacion = optimizacionLocal(optimizado);
            cambiado = cambiado && retornoOptimizacion.isCambiado();
            optimizado = retornoOptimizacion.getInstrucciones();
        }
        instruccionesOptimizadas = optimizado;
    }

    protected OptimizationReturn optimizarSaltosCondicionales(ArrayList<InstruccionTresDirecciones> instrucciones) {
        int i = 0;
        boolean cambiado = false;
        int numElementos = instrucciones.size();
        ArrayList<InstruccionTresDirecciones> nuevas = new ArrayList<>();

        InstruccionTresDirecciones instruccion, siguienteInstruccion, complementario;
        while(i < numElementos) {
            instruccion = instrucciones.get(i);
            switch (instruccion.getOperacion()) {
                case GT:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((GT)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case GTE:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((GTE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case LT:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((LT)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case LTE:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((LTE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case EQ:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((EQ)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case NE:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((NE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                default:
                    nuevas.add(instruccion);
            }
            i++;
        }
        return new OptimizationReturn(nuevas, cambiado);
    }

    protected Grafo getGrafoFlujoFuncion(ArrayList<InstruccionTresDirecciones> instrucciones, RangoInstruccionesFuncion rango) {
        int idBloque = 0;
        boolean finales;
        BloqueBasico e, s, bloque, siguienteBloque;
        InstruccionTresDirecciones instruccion, siguiente;
        Grafo grafoFlujoBloquesBasicos = new Grafo();
        ArrayList<BloqueBasico> bloques = new ArrayList<>();
        HashMap<OperandoEtiqueta, BloqueBasico> etiquetaToLider = new HashMap<>();
        OperandoEtiqueta etiqueta;

        e = new BloqueBasico(++idBloque);
        s = new BloqueBasico(++idBloque);
        bloques.add(e);
        bloques.add(s);
        grafoFlujoBloquesBasicos.addVertice(e);
        grafoFlujoBloquesBasicos.addVertice(s);

        // Identificacion de lideres
        for (int i = rango.getInicio(); i <= rango.getFin(); i++) {
            instruccion = instrucciones.get(i);

            if (isEtiqueta(instruccion)) {
                bloque = new BloqueBasico(++idBloque, i);
                grafoFlujoBloquesBasicos.addVertice(bloque);
                bloques.add(bloque);
                etiqueta = (OperandoEtiqueta)instruccion.getPrimero();
                etiquetaToLider.put(etiqueta, bloque);
            } else if (isConditionalBranch(instruccion)) {
                siguiente = getSiguiente(instrucciones, i + 1);
                finales = isBranch(siguiente) || isRetorno(siguiente) || isEtiqueta(siguiente);
                if (!finales) {
                    bloque = new BloqueBasico(++idBloque, i + 1);
                    grafoFlujoBloquesBasicos.addVertice(bloque);
                    bloques.add(bloque);
                }
            }
        }

        bloque = bloques.get(2);
        grafoFlujoBloquesBasicos.addArista(e, bloque);

        int indiceInstruccion;
        // Identificacion de finales
        for (int b = 2; b < bloques.size(); b++) {
            bloque = bloques.get(b);
            indiceInstruccion = bloque.getInicio();
            instruccion = instrucciones.get(indiceInstruccion);
            finales = isBranch(instruccion) || isRetorno(instruccion) || isEtiqueta(instruccion);
            while(!finales || esEtiquetaBloquePropio(instruccion, bloque, etiquetaToLider)) {
                instruccion = instrucciones.get(++indiceInstruccion);
                finales = isBranch(instruccion) || isRetorno(instruccion) || isEtiqueta(instruccion);
            }

            while(isConditionalBranch(instruccion)) {
                etiqueta = (OperandoEtiqueta)instruccion.getTercero();
                siguienteBloque = etiquetaToLider.get(etiqueta);
                grafoFlujoBloquesBasicos.addArista(bloque, siguienteBloque);
                instruccion = instrucciones.get(++indiceInstruccion);
            }

            if (isUnconditionalBranch(instruccion)) {
                bloque.setFin(indiceInstruccion);
                etiqueta = (OperandoEtiqueta)instruccion.getTercero();
                siguienteBloque = etiquetaToLider.get(etiqueta);
                grafoFlujoBloquesBasicos.addArista(bloque, siguienteBloque);
            } else if (isRetorno(instruccion)) {
                // En todos los retornos lo que tenemos es que "finaliza" la ejecución actual, por
                // tanto es como si estuvieramos saliendo de un programa
                bloque.setFin(indiceInstruccion);
                grafoFlujoBloquesBasicos.addArista(bloque, s);
            } else {
                // Ojo! Este caso es especial, ya que el bloque basico no acaba
                // con un salto ( ya sea retorno o goto ), si no que el flujo puede
                // "caer" al siguiente bloque y por tanto, debemos marcar que este
                // orden no se puede alterar
                bloque.setFin(indiceInstruccion - 1);
                siguienteBloque = bloques.get(b + 1);
                grafoFlujoBloquesBasicos.addArista(bloque, siguienteBloque, true);
            }
        }

        return grafoFlujoBloquesBasicos;
    }

    protected RangoInstruccionesFuncion getSiguienteFuncion(ArrayList<InstruccionTresDirecciones> instrucciones, int inicioBusqueda) {
        int inicio, fin, actual;
        boolean encontradoFin = false;
        InstruccionTresDirecciones instruccion;
        inicio = fin = -1;
        for (actual = inicioBusqueda; actual < instrucciones.size() && !encontradoFin; actual++) {
            instruccion = instrucciones.get(actual);
            if (instruccion instanceof Preambulo && inicio == -1) {
                // La primera "instruccion" de una funcion es su etiqueta, pero es más fácil identificar preambulos.
                inicio = actual - 1;
            } else if (instruccion instanceof Preambulo) {
                // Ha encontrado el preambulo de otra función, por tanto, debemos retroceder 2 instrucciones
                // (preambulo + etiqueta) para encontrar la última instrucción de la funcion
                fin = actual - 2;
                encontradoFin = true;
            } else if (instruccion instanceof Clase) {
                // Hemos encontrado la declaración de la clase, por tanto, también se ha finalizado la función
                // actual
                fin = actual - 2;
                encontradoFin = true;
            } else if (instruccion instanceof Retorno) {
                // Hemos encontrado un posible final, pero no podemos asegurar que no haya más instrucciones despues
                // por tanto, seguimos buscando hasta que encontremos la declaración de una funcion
                // o hasta que acabemos las instrucciones
                fin = actual;
            }
        }

        if (inicio > -1) {
            fin = encontradoFin ? fin : actual - 1;
            return new RangoInstruccionesFuncion(inicio, fin);
        } else {
            // No hemos encontrado más funciones
            return null;
        }
    }

    /**
     * Las optimizaciones locales solo se aplican sobre las funciones. Así que, para cada una de las funciones
     * se genera un grafo independiente sobre el cual se puede optimizar el código
     * @param instrucciones
     * @return
     */
    protected OptimizationReturn optimizacionLocal(ArrayList<InstruccionTresDirecciones> instrucciones) {
        ArrayList<RangoInstruccionesFuncion> funciones = new ArrayList<>();
        HashMap<RangoInstruccionesFuncion, Grafo> grafosFunciones = new HashMap<>();
        Grafo grafoFuncion;
        RangoInstruccionesFuncion rangoInstrucciones = getSiguienteFuncion(instrucciones, 0);
        while (rangoInstrucciones != null) {
            grafoFuncion = getGrafoFlujoFuncion(instrucciones, rangoInstrucciones);
            funciones.add(rangoInstrucciones);
            grafosFunciones.put(rangoInstrucciones, grafoFuncion);
            rangoInstrucciones = getSiguienteFuncion(instrucciones, rangoInstrucciones.getFin() + 1);
        }
        return null;
    }

    protected boolean esEtiquetaBloquePropio(InstruccionTresDirecciones instruccion, BloqueBasico bloque, HashMap<OperandoEtiqueta, BloqueBasico> etiquetaToLider) {
        if (isEtiqueta(instruccion)) {
            OperandoEtiqueta etiqueta = (OperandoEtiqueta)instruccion.getPrimero();
            return bloque == etiquetaToLider.get(etiqueta);
        }
        return false;
    }

    protected boolean isConditionalBranch(InstruccionTresDirecciones instruccion) {
        if (instruccion instanceof GT) {
            return ((GT)instruccion).isBranch();
        } else if (instruccion instanceof GTE) {
            return ((GTE)instruccion).isBranch();
        } else if (instruccion instanceof LT) {
            return ((LT)instruccion).isBranch();
        } else if (instruccion instanceof LTE) {
            return ((LTE)instruccion).isBranch();
        } else if (instruccion instanceof NE) {
            return ((NE)instruccion).isBranch();
        } else if (instruccion instanceof EQ) {
            return ((EQ)instruccion).isBranch();
        }
        return false;
    }

    protected boolean isUnconditionalBranch(InstruccionTresDirecciones instruccion) {
        return instruccion instanceof Goto;
    }

    protected boolean isBranch(InstruccionTresDirecciones instruccion) {
        return isUnconditionalBranch(instruccion) || isConditionalBranch(instruccion);
    }

    protected boolean isRetorno(InstruccionTresDirecciones instruccion) {
        return instruccion instanceof Retorno;
    }

    protected boolean isEtiqueta(InstruccionTresDirecciones instruccion) {
        return instruccion instanceof Etiqueta;
    }

    protected InstruccionTresDirecciones getSiguiente(ArrayList<InstruccionTresDirecciones> instrucciones, int idx) {
        int numElementos = instrucciones.size();
        if (idx < numElementos) {
            return instrucciones.get(idx + 1);
        }
        return null;
    }

    public ArrayList<InstruccionTresDirecciones> optimizado() {
        return this.instruccionesOptimizadas;
    }

    @Override
    public Iterator<InstruccionTresDirecciones> iterator() {
        return this.instrucciones.iterator();
    }

    public static ProgramaIntermedio getInstance() {
        if (instance == null)
            instance = new ProgramaIntermedio();
        
        return instance;
    }
}
