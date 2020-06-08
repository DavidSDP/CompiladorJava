package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import Procesador.Declaracion;

public class SimpleParam extends Param {

    public SimpleParam(Operando primero) {
        super(OperacionTresDirecciones.PARAM, primero);
    }

    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(primero.load(DataRegister.D0));
        Declaracion valor = primero.getValor();
        // Currently we've got 2 kinds of size handling for variables
        //  - Word
        //  - LongWord
        // We need to use the specific param macro depending on the size
        String macro =  valor.getOcupacion() <= 2 ? "PUSH_WORD" : "PUSH_LONG";
        bI.add(Instruccion.nuevaInstruccion("\t" + macro + " " + valor.getDesplazamiento() + "(A6)\n"));
        return bI.toString();
    }
}
