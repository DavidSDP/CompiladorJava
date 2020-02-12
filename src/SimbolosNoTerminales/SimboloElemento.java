package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.TipoSubyacente;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;

public class SimboloElemento extends Nodo implements TipoSubyacente{
	
	private SimboloAsignacion asignacion;
	private SimboloFuncionDecl funcionDecl;
	private SimboloClase clase;
	
	public SimboloElemento(SimboloAsignacion a) {
		this.asignacion = a;
	}
	
	public SimboloElemento(SimboloFuncionDecl f) {
		this.funcionDecl = f;
	}
	
	public SimboloElemento(SimboloClase c) {
		this.clase = c;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		if(asignacion != null) {
			hijos.add(asignacion);
			return hijos;
		}
		if(funcionDecl != null) {
			hijos.add(funcionDecl);
			return hijos;
		}
		if(clase != null) {
			hijos.add(clase);
			return hijos;
		}
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloElemento";
	}
	
	@Override
	public Tipo getTipoSubyacente() {
		if(clase!=null)
			return Tipo.Class;
		return Tipo.Void;
	}
}
