package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.Instruccion;

public class DeclaracionIndireccion extends InstruccionTresDirecciones {
    
    public DeclaracionIndireccion(Operando array) {
        super(OperacionTresDirecciones.DECLARAR_INDIRECCION);
        this.primero = array;
    }

    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.primero.assignDynamicMemory());
        return bI.toString();
    }
}
