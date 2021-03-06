package intermedio;

import CodigoMaquina.*;
import CodigoMaquina.especiales.*;
import Procesador.DeclaracionArray;

public class CargarIndireccion extends InstruccionTresDirecciones {
    public CargarIndireccion(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.CARGAR_INDIRECCION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        DeclaracionArray declArray = (DeclaracionArray) primero.getValor();

        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        // TODO Esto deberiamos renombrarlo a loadDescriptorVariable
        bI.add(primero.loadStringDescriptorVariable(AddressRegister.A5));
        bI.add(new Instruccion(OpCode.MOVE, Size.W, Contenido.__(AddressRegister.A5), DataRegister.D0));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Indireccion.__(4, AddressRegister.A5), AddressRegister.A5));
        bI.add(segundo.load(DataRegister.D1));
        // TODO Add bounds checking
        bI.add(new Instruccion(OpCode.MULU, Literal.__(declArray.getTipoDato().getSize()), DataRegister.D1));
        bI.add(new Instruccion(OpCode.ADD, Size.L, DataRegister.D1, AddressRegister.A5));
        bI.add(tercero.save(Contenido.__(AddressRegister.A5)));
        return bI.toString();
    }

    @Override
    public boolean esDefinicion() {
        // TODO Comprobar que impacto tendría suponer que la carga de una dirección es una
        //  definición ( que lo es )
        return false;
    }
}
