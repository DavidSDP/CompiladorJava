package intermedio;

/**
 * Instruccion de 3 direcciones que representa todas las
 * acciones que se tienen que llevar a cabo para poder
 * inicializar la ejecución del programa.
 */
public class EntryPoint extends Llamada {
    public EntryPoint(Operando callee, Operando caller) {
        super(callee, caller, null);
        this.operacion = OperacionTresDirecciones.ENTRY_POINT;
    }

    @Override
    public String toMachineCode() {
        /*
        ¿Como se debe representar el null en el bloque de activación? 0 ?
        Cosas que se deben hacer aqui:
            1- Calcular el primer bloque de activación:
                    * Access link = null
                    * BP anterior = null
            2- Realizar la llamada a la función pertinente ( apuntado por el primer parametro )
         */
        StringBuilder sb = new StringBuilder();
        String machineCodeCall = super.toMachineCode();
        return sb.append(machineCodeCall).toString();
    }
}
