package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.especiales.Literal;
import Procesador.Declaracion;

public class NE extends InstruccionTresDirecciones {
    public NE(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.NE);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    }

    public String generateBranch() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(primero.load(DataRegister.D0));
        bI.add(segundo.load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, DataRegister.D0, DataRegister.D1));
        bI.add(new Instruccion(OpCode.BNE, new OperandoEspecial(tercero.toString())));
        return bI.toString();
    }

    public String generateOperation() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(primero.load(DataRegister.D0));
        bI.add(segundo.load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, DataRegister.D1, DataRegister.D0));
        bI.add(new Instruccion(OpCode.SNE, DataRegister.D0));
        bI.add(new Instruccion(OpCode.AND, Literal.__(1), DataRegister.D0));
        bI.add(tercero.save(DataRegister.D0));
        return bI.toString();
    }

    @Override
    public String toMachineCode() {
        if (tercero instanceof OperandoEtiqueta) {
            return this.generateBranch();
        } else {
            return this.generateOperation();
        }
    }

    public boolean isBranch() {
        return tercero instanceof OperandoEtiqueta;
    }

    public InstruccionTresDirecciones getComplementario(Goto salto) {
        return new EQ(primero, segundo, salto.tercero);
    }

    @Override
    public boolean esDefinicion() {
        return !(this.tercero instanceof OperandoEtiqueta);
    }

}
