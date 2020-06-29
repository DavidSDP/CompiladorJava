package intermedio;

import Checkers.Tipo;
import CodigoMaquina.*;
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
        // Preparado para Descriptor de String del tipo @.L y #.L
        if(Tipo.String.equals(primero.getValor().getTipo().getTipo())){
            bI.add(primero.loadStringDescriptorVariable(AddressRegister.A0));
            bI.add(segundo.loadStringDescriptorVariable(AddressRegister.A1));
            bI.add(Instruccion.nuevaInstruccion("\t\t\tJSR\tSTREQUALS"));
            bI.add(new Instruccion(OpCode.CMP, Size.L, Literal.__(0), DataRegister.D0));
            bI.add(new Instruccion(OpCode.SEQ, DataRegister.D0));
            bI.add(new Instruccion(OpCode.AND, Size.L, Literal.__(1), DataRegister.D0));
            bI.add(tercero.save(DataRegister.D0));
        } else {
            // Estoy casi convencido de que la comprobacion de igualdad se puede hacer como LT y familiares
            bI.add(primero.load(DataRegister.D0));
            bI.add(segundo.load(DataRegister.D1));
            bI.add(new Instruccion(OpCode.CMP, DataRegister.D1, DataRegister.D0));
            bI.add(new Instruccion(OpCode.SNE, DataRegister.D0));
            bI.add(new Instruccion(OpCode.AND, Literal.__(1), DataRegister.D0));
            bI.add(tercero.save(DataRegister.D0));
        }

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
