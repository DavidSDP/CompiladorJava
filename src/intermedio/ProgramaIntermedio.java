package intermedio;

import java.util.ArrayList;
import java.util.Iterator;

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
        // TODO
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
