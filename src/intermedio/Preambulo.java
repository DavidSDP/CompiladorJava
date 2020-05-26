package intermedio;

import CodigoMaquina.*;
import CodigoMaquina.especiales.Literal;
import Procesador.DeclaracionFuncion;

public class Preambulo extends InstruccionTresDirecciones {
    public Preambulo(Operando primero) {
        super(OperacionTresDirecciones.PREAMBULO);
        this.setPrimero(primero);
    }

    @Override
    public String toMachineCode() {
        DeclaracionFuncion declaracionFuncion = (DeclaracionFuncion)this.getPrimero().getValor();
        int memoria = declaracionFuncion.getTamanoMemoriaNecesaria();

        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.STACK_TOP, AddressRegister.A5));
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(memoria), AddressRegister.A5));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A5, Variables.STACK_TOP));

        return bI.toString();
    }
}
