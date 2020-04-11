package Procesador;

import java.util.Objects;

import Checkers.Tipo;

public class Declaracion {

    protected Identificador identificador;
    protected Tipo tipo;
    protected int desplazamiento;

    public Declaracion(Identificador identificador, Tipo tipo) {
    	// TODO Esto probablemente es una aberracion de la naturaleza.
		//      Pero lo mantenemos hasta que se hayan migrado todas
		//      las creaciones de declaraciones
        this(identificador, tipo, 0);
    }

	public Declaracion(Identificador identificador, Tipo tipo, int desplazamiento) {
		this.identificador = identificador;
		this.tipo = tipo;
		this.desplazamiento = desplazamiento;
	}

    public Identificador getId() {
        return identificador;
    }

    public void setId(Identificador id) {
        this.identificador = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.identificador == null)
            return false;

        if (!(obj instanceof Declaracion)) {
            return false;
        }
        return this.identificador.equals(((Declaracion) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificador);
    }

    @Override
    public String toString() {
        return tipo.toString() + " " + identificador.toString();
    }

    public int getOcupacion() {
        /*
         * Hipoteticamente aqui deberiamos poder calcular la ocupacion de la variable de la siguiente
         * manera:
         * 	 - tipo.getMemorySize()
         * 	En caso de los arrays sería :
         * 	 - numElementos * tipo.getMemorySize()
         */
        return 0;
    }

	/**
	 * El desplazamiento del elemento actual es:
	 * 		Base pointer + sumatorio_tamano_variables_anteriores
	 *
	 * Por comodidad dicho sumatorio se calcula en el entorno en el momento
	 * de declarar una variable y se le pasa a la variable en cuestion
	 *
	 * TODO Toda esta explicación puede ser del todo incorrecta.
	 * 		Revisitar esta parte cuando se haya implementado la optimizacion
	 * 		ya que la eliminacion de variable se puede cargar htodo el calculo
	 * 		Posibles fixes:
	 * 			- Mantener una lista enlazada de declaraciones y generar el
	 * 			  desplazamiento al final
	 * 			- Generar codigo maquina para calcular el desplazamiento en
	 * 			  runtime. Ahora mismo no se como lo haria ....
	 *
	 * P.D: la h de htodo no es un error, es que si no no me colorea bien el todo :)
	 *
	 * @return El desplazamiento necesario en memoria para referenciar esta variable
	 */
	public int getDesplazamiento() {
        return this.desplazamiento;
    }
}



