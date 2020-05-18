package intermedio;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.Size;
import CodigoMaquina.especiales.Contenido;
import CodigoMaquina.especiales.Literal;
import Procesador.DeclaracionArray;

public class GuardarIndireccion extends InstruccionTresDirecciones {
    public GuardarIndireccion(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.GUARDAR_INDIRECCION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    /**
     *
     */
    @Override
    public String toMachineCode() {
        DeclaracionArray declArray = (DeclaracionArray) this.segundo.getValor();
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.segundo.load(DataRegister.D0));
        bI.add(new Instruccion(OpCode.MOVE, AddressRegister.A6, AddressRegister.A5));
        bI.add(new Instruccion(OpCode.ADD, Size.W, Literal.__(declArray.getDesplazamiento()), AddressRegister.A5));
        bI.add(this.tercero.load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.MULU, Literal.__(declArray.getTipoDato().getSize()), DataRegister.D1));
        bI.add(new Instruccion(OpCode.ADD, DataRegister.D1, AddressRegister.A5));
        
                // Este paso lo debemos dar de forma extra para no tener que rehacer htodo la clase Operador
                // debido a que no podemos decirle donde lo tiene que guardar
        bI.add(this.tercero.load(DataRegister.D2));
        bI.add(new Instruccion(OpCode.MOVE, Size.W, DataRegister.D2, Contenido.__(AddressRegister.A5)));
        return bI.toString();
        
//        sb.append(putActivationBlockAddressInRegister(this.segundo))
//                .append("\tmove A6, A5\n")
//                .append("\tadd.w #").append(declArray.getDesplazamiento()).append(", A5\n")
//                .append(putActivationBlockAddressInRegister(this.tercero))
//                .append("\tmove ").append(this.tercero.getValor().getDesplazamiento()).append("(A6), D1\n")
//                .append("\tmulu #").append(declArray.getTipoDato().getSize()).append(", D1\n")
//                .append("\tadd D1, A5\n")
//                .append(putActivationBlockAddressInRegister(this.primero))
//                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), (A5)\n");
        
    }
}
