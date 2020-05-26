package intermedio;

import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.especiales.Literal;

public class GTE extends InstruccionTresDirecciones {
    public GTE(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.GTE);
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
        bI.add(new Instruccion(OpCode.BGE, new OperandoEspecial(this.getTercero().toString())));
        return bI.toString();
    }

    /**
     * Por lo que se ha podido ver no existe una manera directa de poder calcular el resultado de
     * la comparacion y asignarla a una variable.
     *
     * Segun el manual del 68K para saber si un numero es mayor que otro se debe cumplir que:
     *  V xor N == 0
     *
     * Por tanto para poder hacer el cálculo se siguen los siguientes pasos:
     *   1. Comparamos los números
     *   2. Recuperamos los flags V y N
     *   3. Los desplazamos por separado hasta el LSB
     *   4. Comparamos ambos valores
     *   5. Recuperamos el flag Z
     *   6. Asignamos el valor de Z a la variable de salida
     *
     * Si los dos operandos fueran diferentes Z sería 0 y si fueran iguales Z sería 1
     */
    public String generateOperation() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.getPrimero().load(DataRegister.D0));
        bI.add(this.getSegundo().load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, DataRegister.D1, DataRegister.D0));
        bI.add(new Instruccion(OpCode.SGE, DataRegister.D0));
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
        return new LT(getPrimero(), getSegundo(), salto.getTercero());
    }

}