package intermedio;


import Procesador.Declaracion;
import Procesador.DeclaracionFuncion;

public class Llamada extends InstruccionTresDirecciones {
    public Llamada(Operando primero) {
        super(OperacionTresDirecciones.LLAMADA);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        DeclaracionFuncion declaracionFuncion = (DeclaracionFuncion)this.primero.getValor();
        // TODO Es necesario saber si la funcion tiene retorno o no para poder reservar el espacio oportuno
        //  aun no esta hecho.
        sb.append("\tbsr update_bp\n") // Actualiza BP y Access link
                .append("\tbsr ").append(declaracionFuncion.getEtiqueta()).append("\n")
                // TODO Handle return
                // Esto funciona si los supuestos parametros han añadido el backup de la cima de la pila
                // Una alternativa es guardar el listado de declaraciones de los parametros de las funciones en la
                // declaracion de la funcion para poder calcular el offset de las variables para poder eliminarlas de la pila
                .append("\tmove.w (A7)+, STACK_TOP\n");


        return sb.toString();
    }
}
