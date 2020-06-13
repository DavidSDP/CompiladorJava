package intermedio;

import CodigoMaquina.DataRegister;

public class Suma extends InstruccionTresDirecciones {
    public Suma(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.SUMA);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
        // En el caso del 68k el add directamente guarda la info en el segundo
        // operando, así que necesitamos guardar el valor en el registro para
        // devolver donde toque eso.
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(primero.load(DataRegister.D0))
                .append(segundo.load(DataRegister.D1))
                .append("\tadd.w D0, D1\n")
                .append(tercero.save(DataRegister.D1));

        return sb.toString();
    }

    @Override
    public boolean esDefinicion() {
        return true;
    }
}