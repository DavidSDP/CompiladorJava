package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.especiales.Literal;

public class LTE extends InstruccionTresDirecciones {
    public LTE(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.LTE);
        this.setPrimero(primero);
        this.setSegundo(segundo);
        this.setTercero(resultado);
    }

    public String generateBranch() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.getPrimero().load(DataRegister.D0));
        bI.add(this.getSegundo().load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, DataRegister.D0, DataRegister.D1));
        bI.add(new Instruccion(OpCode.BLE, new OperandoEspecial(this.getTercero().toString())));
        return bI.toString();
    }

    public String generateOperation() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.getPrimero().load(DataRegister.D0));
        bI.add(this.getSegundo().load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, DataRegister.D1, DataRegister.D0));
        bI.add(new Instruccion(OpCode.SLE, DataRegister.D0));
        bI.add(new Instruccion(OpCode.AND, Literal.__(1), DataRegister.D0));
        bI.add(this.getTercero().save(DataRegister.D0));
        return bI.toString();
    }

    @Override
    public String toMachineCode() {
        if (this.getTercero() instanceof OperandoEtiqueta) {
            return this.generateBranch();
        } else {
            return this.generateOperation();
        }
    }

    public boolean isBranch() {
        return this.getTercero() instanceof OperandoEtiqueta;
    }

    public InstruccionTresDirecciones getComplementario(Goto salto) {
        return new GT(getPrimero(), getSegundo(), salto.getTercero());
    }
}
