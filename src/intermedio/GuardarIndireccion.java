package intermedio;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.Size;
import CodigoMaquina.especiales.Contenido;
import CodigoMaquina.especiales.Indireccion;
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
        DeclaracionArray declArray = (DeclaracionArray) segundo.getValor();
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));

        bI.add(segundo.loadStringDescriptorVariable(AddressRegister.A5));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Indireccion.__(4, AddressRegister.A5), AddressRegister.A5));
        bI.add(tercero.load(DataRegister.D1));
        // TODO Add bounds checking
        bI.add(new Instruccion(OpCode.MULU, Literal.__(declArray.getTipoDato().getSize()), DataRegister.D1));
        bI.add(new Instruccion(OpCode.ADD, Size.L, DataRegister.D1, AddressRegister.A5));

        // Este paso lo debemos dar de forma extra para no tener que rehacer htodo la clase Operador
        // debido a que no podemos decirle donde lo tiene que guardar
        bI.add(primero.load(DataRegister.D2));
        // TODO Por ahora solo permitimos arrays de tipos básicos. Si admitieramos arrays de strings
        //  esta implementación no serviría ya que tendríamos que guardar 2 Longs
        bI.add(new Instruccion(OpCode.MOVE, Size.W, DataRegister.D2, Contenido.__(AddressRegister.A5)));
        return bI.toString();
    }
}
