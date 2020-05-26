package intermedio;


import java.util.ArrayList;
import java.util.Iterator;

import optimizacion.OptimizacionLocal;
import optimizacion.RetornoOptimizacion;
import optimizacion.mirilla.NormalizacionOperandos;
import optimizacion.mirilla.SaltosCondicionales;

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
        RetornoOptimizacion retornoOptimizacion;
        ArrayList<InstruccionTresDirecciones> optimizado = (ArrayList<InstruccionTresDirecciones>)instrucciones.clone();
        boolean cambiado = false;
        retornoOptimizacion = new RetornoOptimizacion(optimizado, cambiado);
        // Mirilla
        do{
            retornoOptimizacion = lanzaAlgoritmosMirilla(retornoOptimizacion);
            if(retornoOptimizacion != null) {
	            cambiado = retornoOptimizacion.isCambiado();
	            optimizado = retornoOptimizacion.getInstrucciones();
            }else {
            	cambiado = false;
            }
        }while(cambiado);
        
        // Local
        cambiado = false;
        do{
            retornoOptimizacion = OptimizacionLocal.optimiza(optimizado);
            if(retornoOptimizacion != null) {
            	optimizado = retornoOptimizacion.getInstrucciones();
                cambiado = cambiado && retornoOptimizacion.isCambiado();
            }else {
            	cambiado = false;
            }
        }while(cambiado);
        instruccionesOptimizadas = optimizado;
    }
    
    public static RetornoOptimizacion lanzaAlgoritmosMirilla(RetornoOptimizacion optimizacion){
    	RetornoOptimizacion retornoOptimizacion = SaltosCondicionales.optimizar(optimizacion);
        retornoOptimizacion = NormalizacionOperandos.optimizar(retornoOptimizacion);
        return retornoOptimizacion;
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
