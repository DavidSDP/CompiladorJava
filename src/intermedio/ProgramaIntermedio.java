package intermedio;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Procesador.Declaracion;
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
    private List<Optimizador> optimizadores;

    public ProgramaIntermedio() {
        instrucciones = new ArrayList<>();
        instruccionesOptimizadas = new ArrayList<>();
        optimizadores = new ArrayList<>();
    }

    public void addInstruccion(InstruccionTresDirecciones instr) {
        instrucciones.add(instr);
    }
    
    public void addOptimizador(Optimizador optimizador) {
    	optimizadores.add(optimizador);
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

    public Iterable<InstruccionTresDirecciones> optimizado() {
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

	public void desInicializarVariables() {
		this.instrucciones.stream().forEach(x->{
			Operando actual = x.getPrimero();
			if(actual != null) {
				Declaracion declaracion = actual.getValor();
				if(declaracion != null) {
					declaracion.desInicializar();
				}
			}
			actual = x.getSegundo();
			if(actual != null) {
				Declaracion declaracion = actual.getValor();
				if(declaracion != null) {
					declaracion.desInicializar();
				}
			}
			actual = x.getTercero();
			if(actual != null) {
				Declaracion declaracion = actual.getValor();
				if(declaracion != null) {
					declaracion.desInicializar();
				}
			}
		});
	}
}
