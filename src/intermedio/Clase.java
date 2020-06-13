package intermedio;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.Size;
import CodigoMaquina.especiales.Literal;
import Procesador.DeclaracionClase;
import Procesador.EntornoClase;

public class Clase extends InstruccionTresDirecciones {
    public Clase(Operando primero) {
        super(OperacionTresDirecciones.CLASE);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        EntornoClase entorno = ((DeclaracionClase) primero.getValor()).getEntornoAsociado();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, new OperandoEspecial("STACK_TOP"), AddressRegister.A6));
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(entorno.getTamanoTotalVariables()), AddressRegister.A6));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, new OperandoEspecial("STACK_TOP")));
        return bI.toString();
    }

    @Override
    public boolean esDefinicion() {
        return false;
    }
}
