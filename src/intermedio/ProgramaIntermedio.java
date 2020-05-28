package intermedio;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import optimizacion.OptimizacionLocal;
import optimizacion.OptimizacionMirilla;
import optimizacion.Optimizador;
import optimizacion.RetornoOptimizacion;
import optimizacion.SecuenciaInstrucciones;

/**
 * Un programa intermedio es una secuencia de instrucciones en código de 3
 * direcciones que puede ser traducido a cualquier lenguaje máquina.
 *
 */
public class ProgramaIntermedio implements Iterable<InstruccionTresDirecciones>{

    private static ProgramaIntermedio instance;

    private final ArrayList<InstruccionTresDirecciones> instrucciones;
    private List<InstruccionTresDirecciones> instruccionesOptimizadas;

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
    @SuppressWarnings("unchecked")
	public void optimizar() {
    	
    	List<Optimizador> optimizadores = new ArrayList<>();
    	optimizadores.add(new OptimizacionMirilla());
    	optimizadores.add(new OptimizacionLocal());
    	RetornoOptimizacion retornoActualizado = new RetornoOptimizacion((List<InstruccionTresDirecciones>) instrucciones.clone(), false);
    	Iterator<Optimizador> iterador = optimizadores.iterator();
    	while(iterador.hasNext()) {
    		SecuenciaInstrucciones secuenciaInstrucciones = new SecuenciaInstrucciones(retornoActualizado.getInstrucciones());
    		Optimizador optimizador = iterador.next();
            RetornoOptimizacion retorno = optimizador.optimizar(secuenciaInstrucciones);
            if(retorno != null) {
            	retornoActualizado = retorno;
            }
    	}
    	this.instruccionesOptimizadas = retornoActualizado.getInstrucciones();
    	
    }

    public List<InstruccionTresDirecciones> optimizado() {
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
