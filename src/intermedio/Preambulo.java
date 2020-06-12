package intermedio;

import CodigoMaquina.*;
import CodigoMaquina.especiales.Literal;
import Procesador.Declaracion;
import Procesador.DeclaracionFuncion;

import java.util.ArrayList;

public class Preambulo extends InstruccionTresDirecciones {
    public Preambulo(Operando primero) {
        super(OperacionTresDirecciones.PREAMBULO);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        DeclaracionFuncion declaracionFuncion = (DeclaracionFuncion) primero.getValor();
        int memoria = declaracionFuncion.getTamanoMemoriaNecesaria();

        BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.STACK_TOP, AddressRegister.A5));
        bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(memoria), AddressRegister.A5));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A5, Variables.STACK_TOP));

        return bI.toString();
    }

    /**
     * Por norma general el primer y segundo argumentos se consideran no destino.
     * Obviamente, esto depende de la instrucción, así que se tiene que sobreescribir en esos
     * casos donde esta condición no es cierta.
     */
    public ArrayList<Declaracion> getArgumentos() {
        return new ArrayList<>();
    }

    @Override
    public boolean esDefinicion() {
        return false;
    }
}
