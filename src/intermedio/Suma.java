package intermedio;

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
        
        /**
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le sumamos el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        sb.append("move " + primero.toString() + ", D0 \n");
        sb.append("add " + segundo.toString() + ", D0 \n");
        sb.append("move D0, " + tercero.toString());
        
        return sb.toString();
    }
}