package intermedio;

import CodigoMaquina.DataRegister;

public class Suma extends InstruccionTresDirecciones {
    public Suma(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.SUMA);
        this.setPrimero(primero);
        this.setSegundo(segundo);
        this.setTercero(tercero);
    }

    @Override
    public String toMachineCode() {
        // En el caso del 68k el add directamente guarda la info en el segundo
        // operando, así que necesitamos guardar el valor en el registro para
        // devolver donde toque eso.
        StringBuilder sb = new StringBuilder();

        sb.append(super.toMachineCode());
        sb.append(this.getPrimero().load(DataRegister.D0))
                .append(this.getSegundo().load(DataRegister.D1))
                .append("\tadd.w D0, D1\n")
                .append(this.getTercero().save(DataRegister.D1));

        return sb.toString();
    }
}