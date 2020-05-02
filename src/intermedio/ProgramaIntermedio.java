package intermedio;


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


/**
 * Un programa intermedio es una secuencia de instrucciones en código de 3
 * direcciones que puede ser traducido a cualquier lenguaje máquina.
 *
 */
public class ProgramaIntermedio implements Iterable<InstruccionTresDirecciones>{

    private static ProgramaIntermedio instance;

    /**
     * Llegado el momento esta lista de instrucciones se tendrá que 
     * iterar para reordenar/eliminar instrucciones (fase de optimización)
     */
    private final ArrayList<InstruccionTresDirecciones> instrucciones;

    public ProgramaIntermedio() {
        instrucciones = new ArrayList<>();
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
                        cambiado = false;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((GT)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    }
                    break;
                case GTE:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = false;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((GTE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    }
                    break;
                case LT:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = false;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((LT)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    }
                    break;
                case LTE:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = false;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((LTE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    }
                    break;
                case EQ:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = false;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((EQ)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    }
                    break;
                case NE:
                    siguienteInstruccion = this.getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = false;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((NE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    }
                    break;
                default:
                    nuevas.add(instruccion);
            }
            i++;
        }
        return new OptimizationReturn(nuevas, cambiado);
    }

    protected OptimizationReturn optimizacionLocal(ArrayList<InstruccionTresDirecciones> instrucciones) {

        int inicio, fin, idBloque = 0;
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

        inicio = fin = -1;

        // Identificacion de lideres
        int numElementos = instrucciones.size();
        for (int i = 0; i < numElementos; i++) {
            instruccion = instrucciones.get(i);

            if (isEtiqueta(instruccion)) {
                bloque = new BloqueBasico(++idBloque, inicio);
                grafoFlujoBloquesBasicos.addVertice(bloque);
                bloques.add(bloque);
                etiqueta = (OperandoEtiqueta)instruccion.getTercero();
                etiquetaToLider.put(etiqueta, bloque);
            } else if (isConditionalBranch(instruccion)) {
                siguiente = getSiguiente(instrucciones, i + 1);
                finales = isBranch(siguiente) || isRetorno(siguiente) || isEtiqueta(siguiente);
                if (!finales) {
                    bloque = new BloqueBasico(++idBloque, inicio);
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
            while(!finales) {
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


        return null;
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
