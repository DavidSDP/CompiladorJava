package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;

public class Division extends InstruccionTresDirecciones {
    public Division(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.DIVISION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }  

    @Override
    public String toMachineCode() {
        // En el caso del 68k el add directamente guarda la info en el segundo
        // operando, así que necesitamos guardar el valor en el registro para
        // devolver donde toque eso.
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        /*
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le div el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        bI.add(this.primero.load(DataRegister.D0));
        bI.add(this.segundo.load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.DIVS, DataRegister.D1, DataRegister.D0));
        bI.add(this.tercero.save(DataRegister.D0));
        return bI.toString();
    }
}