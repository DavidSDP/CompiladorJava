package SimbolosNoTerminales;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.Nodo;

/**
 * No hace falta tipo subyacente. Sin embargo hay que comprobar dos cosas:
 * 		1. Como debemos generar el arbol en este caso
 * 		2. El tipo subyacente se utiliza a la hora de pintar el árbol?
 */
public class SimboloDeclaraciones extends Nodo {

	private SimboloDeclaracion declaracion;
	private SimboloDeclaraciones siguiente;

	public SimboloDeclaraciones(SimboloDeclaracion declaracion, SimboloDeclaraciones declaraciones) {
		this.declaracion = declaracion;
		this.siguiente = declaraciones;
	}

	public SimboloDeclaracion getDeclaracion() {
		return declaracion;
	}

	public SimboloDeclaraciones getSiguiente() {
		return siguiente;
	}
}
