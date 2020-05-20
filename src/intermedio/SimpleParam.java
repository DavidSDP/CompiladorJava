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
        bI.add(this.primero.load(DataRegister.D0));
        Declaracion valor = this.primero.getValor();
        bI.add(Instruccion.nuevaInstruccion("\tPUSH_PARAM " + valor.getDesplazamiento() + "(A6), #" + valor.getOcupacion() + "\n"));
        return bI.toString();
    }
}
