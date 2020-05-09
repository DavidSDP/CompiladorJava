package intermedio;

import Checkers.Tipo;
import Procesador.Declaracion;
import Procesador.DeclaracionConstante;


public class Operando {
    // Ahora mismo Declaracion puede contener una variable o una constante.
    // Asi que no es necesario diferenciar el tipo de valor que estamos manejando
    // en este momento
    protected Declaracion valor;

    // El operando refleja que variable/constante se está utilizando en el cálculo
    // y a que profundidad se está usando.
    // Esta profundidad sirve para poder calcular cuantos bloques de activación
    // se tienen que escalar para poder llegar al entorno local de la variable.
    protected int profundidad;

    public Operando(Declaracion valor, int profundidad) {
        this.valor = valor;
        this.profundidad = profundidad;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Forma un string del estilo [PrC: 10, XXXXXXX]
        sb.append("[PrC: ").append(this.profundidad).append(", ").append(valor.toString()).append("]");
        return sb.toString();
    }

    /**
     * Utilidad para generar el código relacionado con la busqueda de las variables a traves de los
     * bloques de activación
     * <p>
     * Esto NO esta bien hecho. Ahora mismo escalamos por los diferentes bloques de activación
     * que son los inmediatamente superiores en el orden de llamada. Pero esto no refleja
     * los ambitos de ejecución.
     * <p>
     * Para arreglarlo, realmente se tiene que escalar por los access links que realmente contienen
     * el puntero al entorno contenedor ( no tiene porque ser el bloque de activacion anterior )
     */
    public String putActivationBlockAddressInRegister() {
        StringBuilder sb = new StringBuilder();
        int profundidadLlamada = this.getProfundidad();
        int profundidadDeclaracion = this.getValor().getProfundidadDeclaracion();
        if (profundidadLlamada > profundidadDeclaracion) {
            // Uso de una variable "global"
            sb.append("\tmove.w BP, A6\n");
            for (int distanciaEntornos = profundidadLlamada - profundidadDeclaracion; distanciaEntornos > 0; distanciaEntornos--) {
                sb.append("\tsubq.w #2, A6\n");
                sb.append("\tmove.w (A6), A6\n");
            }
        } else {
            // Uso de una variable local
            sb.append("\tmove.w BP, A6\n");
        }
        return sb.toString();
    }

    public String load(String toRegister) {
        StringBuilder sb = new StringBuilder();
        if (this.valor instanceof DeclaracionConstante) {
            DeclaracionConstante constante = (DeclaracionConstante) this.valor;
            // Convertimos el valor ( sea cual sea ) a valor máquina. Ahora mismo los literales son Bool e Integer.
            // Falta por ver como se manejan los strings. De momento los dejo de lado.
            if (Tipo.Integer.equals(constante.getTipo().getTipo())) {
                Integer numero = Integer.parseInt((String) constante.getValor());
                sb.append("\tmove.w #")
                        .append(numero).append(", ")
                        .append(toRegister)
                        .append("\n");
            } else if (Tipo.Boolean.equals(constante.getTipo().getTipo())) {
                int valor = mapBooleanValue((String) constante.getValor());
                sb.append("\tmove.w #")
                        .append(valor).append(", ")
                        .append(toRegister)
                        .append("\n");
            } else if (Tipo.String.equals(constante.getTipo().getTipo())) {
                /**
                 * 1. Reservar memoria dinámica
                 * 2. Poner contenido en la memoria
                 * 3. Contar numero de caracteres
                 * 4. Rellenar metainformación en registro
                 *      - En offset 0 está la dirección
                 *      - En offset 16 está el tamaño
                 */
                // guardar A0
                String text = (String)constante.getValor();
                int size = text.length();

                sb.append("\tmovem.l A0/D3, -(A7)\n")
                        // Reservar espacio en memoria dinamica y crear descriptor
                        // Ojo, las rutinas del DMM están demasiado lejos para usar bsr
                        .append("\tjsr DMMALLOC\n")
                        .append("\tclr.l ").append(toRegister).append("\n")
                        .append("\tmove.w #").append(size).append(", ").append(toRegister).append("\n")
                        .append("\tmoveq #16, D3\n")
                        .append("\tlsl.l D3, ").append(toRegister).append("\n")
                        .append("\tmove.w A0, ").append(toRegister).append("\n");

                // Rellenar memoria dinamica
                for (int idx = 0; idx < size; idx++) {
                    sb.append("\tmove.w #").append((int)text.charAt(idx)).append(", (A0)+\n");
                }
                sb.append("\tmovem.l (A7)+, A0/D3\n");
            }
        } else {
            // Si no es una constante es una variable
            sb.append(this.putActivationBlockAddressInRegister())
                    .append("\tmove ")
                    .append(this.valor.getDesplazamiento()).append("(A6), ")
                    .append(toRegister)
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * Posibilidades:
     * Variable
     * Posicion de array
     *
     * @param fromRegister
     * @return
     */
    public String save(String fromRegister) {
        StringBuilder sb = new StringBuilder();
        // Estos dos son descriptores de variables dinámicas
        if (Tipo.String.equals(this.valor.getTipo().getTipo()) || Tipo.Array.equals(this.valor.getTipo().getTipo())) {
            sb.append(this.putActivationBlockAddressInRegister())
                    .append("\tmove.l ").append(fromRegister).append(", ").append(this.getValor().getDesplazamiento()).append("(A6)\n");
        } else {
            sb.append(this.putActivationBlockAddressInRegister())
                    .append("\tmove.w ").append(fromRegister).append(", ").append(this.getValor().getDesplazamiento()).append("(A6)\n");
        }
        return sb.toString();
    }

    public Declaracion getValor() {
        return valor;
    }

    public int getProfundidad() {
        return profundidad;
    }

    private int mapBooleanValue(String value) {
        return value.equals("true") ? 1 : 0;
    }

}
