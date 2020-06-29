package intermedio;

import CodigoMaquina.*;
import CodigoMaquina.especiales.Contenido;
import CodigoMaquina.especiales.Indireccion;
import CodigoMaquina.especiales.Literal;
import Procesador.Declaracion;
import Procesador.DeclaracionFuncion;
import Procesador.GlobalVariables;

import javax.xml.crypto.Data;

import java.util.ArrayList;

import static CodigoMaquina.Variables.STACK_TOP;

public class Retorno extends InstruccionTresDirecciones {
    public Retorno(Operando primero, Operando segundo) {
        super(OperacionTresDirecciones.RETORNO);
        // Primero es la declaracion de la funcion
        this.primero = primero;
        // Segundo es el parametro que se tiene que retornar en caso de que exista un retorno.
        this.segundo = segundo;
    }

    @Override
    public String toMachineCode() {
        BloqueInstrucciones bI = new BloqueInstrucciones();

        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        if (segundo != null) {
            // El offset se calcula desplazando desde BP el access link + el tamano del
            // tipo que retornamos
            int returnOffset = segundo.getValor().getOcupacion() + 4;

            bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.BP, AddressRegister.A5));
            bI.add(new Instruccion(OpCode.SUB, Size.L, Literal.__(returnOffset), AddressRegister.A5));
            if (GlobalVariables.isComplexParam(segundo.getValor())) {
                bI.add(segundo.loadStringDescriptorVariable(AddressRegister.A4));
                bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A4, Contenido.__(AddressRegister.A5)));
            } else {
                bI.add(segundo.load(DataRegister.D0));
                bI.add(new Instruccion(OpCode.MOVE, Size.W, DataRegister.D0, Contenido.__(AddressRegister.A5)));
            }

        }

        DeclaracionFuncion decl = (DeclaracionFuncion) primero.getValor();
        int memoriaReservada = decl.getTamanoMemoriaNecesaria();
        if (memoriaReservada > 0) {
            // Antes de poder liberar la memoria tenemos que liberar todos las variables locales que han reservado memoria
            // dinámica.
            for (Declaracion declDinamica: decl.getVariablesDinamicas()) {
                actualizarConteoReferencia(bI, declDinamica);
            }

            // Si hemos reservado memoria para variables locales, se tiene que liberar.
            bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.STACK_TOP, AddressRegister.A6));
            bI.add(new Instruccion(OpCode.SUB, Size.L, Literal.__(memoriaReservada), AddressRegister.A6));
            bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, Variables.STACK_TOP));
        }
        bI.add(new Instruccion(OpCode.RTS));

        return bI.toString();
    }

    public ArrayList<Declaracion> getArgumentos() {
        return new ArrayList<>();
    }

    @Override
    public boolean esDefinicion() {
        return false;
    }

    public void actualizarConteoReferencia(BloqueInstrucciones bI, Declaracion decl) {
        /*
         * 1- Obtenemos el número de referencias que apuntan al string.
         * 2- Decrementamos el número de referencias
         */
        DeclaracionFuncion funcion = (DeclaracionFuncion)this.primero.getValor();
        // Utilizamos operando para utilizar el mismo mecanismo de calculo de profundidad
        Operando operando = new Operando(decl, funcion.getEntorno().getProfundidad());
        bI.add(operando.loadStringDescriptorVariable(AddressRegister.A0));
        bI.add(new Instruccion(OpCode.CMP, Size.L, Literal.__(0), AddressRegister.A0));
        String localLabel = Instruccion.getLocalLabel();
        bI.add(new Instruccion(OpCode.BEQ, new OperandoEspecial(localLabel)));
        bI.add(Instruccion.nuevaInstruccion("\tjsr DECREASEREF"));
        bI.add(Instruccion.nuevaInstruccion(localLabel + ":"));
    }
}
