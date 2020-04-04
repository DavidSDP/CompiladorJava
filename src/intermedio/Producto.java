package intermedio;

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
        
        /**
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le multiplicamos el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        sb.append("mv " + primero.toString() + ", D0 \n");
        sb.append("mul " + segundo.toString() + ", D0 \n");
        sb.append("mv D0, " + segundo.toString());
        
        return sb.toString();
    }
}