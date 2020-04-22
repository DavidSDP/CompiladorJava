package intermedio;

import Checkers.Tipo;
import Procesador.Declaracion;
import Procesador.DeclaracionArray;
import Procesador.DeclaracionConstante;

public class Operando {
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
     *
     * Esto NO esta bien hecho. Ahora mismo escalamos por los diferentes bloques de activación
     * que son los inmediatamente superiores en el orden de llamada. Pero esto no refleja
     * los ambitos de ejecución.
     *
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
            DeclaracionConstante constante = (DeclaracionConstante)this.valor;
            // Convertimos el valor ( sea cual sea ) a valor máquina. Ahora mismo los literales son Bool e Integer.
            // Falta por ver como se manejan los strings. De momento los dejo de lado.
            if (constante.getTipo().equals(Tipo.Integer)) {
                Integer numero = Integer.parseInt((String) constante.getValor());
                sb.append("\tmove.w #")
                        .append(numero).append(", ")
                        .append(toRegister)
                        .append("\n");
            } else if(constante.getTipo().equals(Tipo.Boolean)) {
                int valor = mapBooleanValue((String) constante.getValor());
                sb.append("\tmove.w #")
                        .append(valor).append(", ")
                        .append(toRegister)
                        .append("\n");
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
     *     Variable
     *     Posicion de array
     *
     * @param fromRegister
     * @return
     */
    public String save(String fromRegister){
        StringBuilder sb = new StringBuilder();
        sb.append(this.putActivationBlockAddressInRegister())
                .append("\tmove.w ").append(fromRegister).append(", ").append(this.getValor().getDesplazamiento()).append("(A6)\n");
        return sb.toString();
    }

    public String toMachineCode(String targetRegister) {

        StringBuilder sb = new StringBuilder();

        // Ojo! Aqui miro si es constante porque ahora mismo todas las constantes que se gestionan así
        // son literales. Probablemente deberíamos diferenciar entre literal y constante.
        if (this.valor instanceof DeclaracionConstante) {
            DeclaracionConstante constante = (DeclaracionConstante)this.valor;
            // Convertimos el valor ( sea cual sea ) a valor máquina. Ahora mismo los literales son Bool e Integer.
            // Falta por ver como se manejan los strings. De momento los dejo de lado.
            if (constante.getTipo().equals(Tipo.Integer)) {
                Integer numero = Integer.parseInt((String) constante.getValor());
                sb.append("\tmove.w #")
                        .append(numero).append(", ")
                        .append(targetRegister)
                        .append("\n");
            } else if(constante.getTipo().equals(Tipo.Boolean)) {
                int valor = mapBooleanValue((String) constante.getValor());
                sb.append("\tmove.w #")
                        .append(valor).append(", ")
                        .append(targetRegister)
                        .append("\n");
            }
        } else {
            // Si no es una constante es una variable
            sb.append(this.putActivationBlockAddressInRegister())
                    .append("\tmove ")
                    .append(this.valor.getDesplazamiento()).append("(A6), ")
                    .append(targetRegister)
                    .append("\n");
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
        return value.equals("true")? 1 : 0;
    }

}
