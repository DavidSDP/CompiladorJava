package intermedio;

import Procesador.Declaracion;
import Procesador.DeclaracionArray;
import Procesador.DeclaracionConstante;

public class Operando implements MachineCodeSerializable {
    protected ModoDireccionamiento modo;

    // Ahora mismo Declaracion puede contener una variable o una constante.
    // Asi que no es necesario diferenciar el tipo de valor que estamos manejando
    // en este momento
    protected  Declaracion valor;

    // El operando refleja que variable/constante se está utilizando en el cálculo
    // y a que profundidad se está usando.
    // Esta profundidad sirve para poder calcular cuantos bloques de activación
    // se tienen que escalar para poder llegar al entorno local de la variable.
    protected int profundidad;

    protected String targetRegister;
    protected String sourceRegister;

    public Operando(Declaracion valor, int profundidad) {
        this.valor = valor;
        this.profundidad = profundidad;
        this.targetRegister = "D0";
        this.sourceRegister = "D1";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Forma un string del estilo [PrC: 10, XXXXXXX]
        sb.append("[PrC: ").append(this.profundidad).append(", ").append(valor.toString()).append("]");
        return sb.toString();
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();

        // Ojo! Esto es una aplicación concreta del access link
        // Declaracion variable
        int profundidadDeclaracion = valor.getProfundidadDeclaracion();
        if (profundidad > profundidadDeclaracion) {
            // Uso de una variable "global"
            sb.append("\tmove.w BP, A6\n");
            for (int distanciaEntornos = profundidad - profundidadDeclaracion; distanciaEntornos > 0; distanciaEntornos--) {
                sb.append("\tmove.w (A6), A6\n");
            }
        } else {
            // Uso de una variable local
            sb.append("\tmove.w BP, A6\n");
        }
        // Le tamano de este move depende del tamano de la variable
        sb.append("\tmove ").append(valor.getDesplazamiento()).append("(A6),").append(targetRegister).append("\n");

        return sb.toString();
    }

    public Declaracion getValor() {
        return valor;
    }

    public int getProfundidad() {
        return profundidad;
    }
}
