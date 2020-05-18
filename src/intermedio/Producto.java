package intermedio;

import CodigoMaquina.DataRegister;

public class Producto extends InstruccionTresDirecciones {
    public Producto(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.PRODUCTO);
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
        /*
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le div el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        sb.append(this.primero.load(DataRegister.D0))
                .append(this.segundo.load(DataRegister.D1))
                .append("\tmuls D0, D1\n")
                .append(this.tercero.save(DataRegister.D1));

        return sb.toString();
    }
}