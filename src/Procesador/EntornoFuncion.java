package Procesador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

public class EntornoFuncion extends Entorno {
	
	// Se guardan los Identificadores que son argumentos del Entorno (Sólo funciones)
	private List<String> listaArgumentos;

	// Esta variable se utiliza para poder calcular el número de saltos en el access link
	// Cada uso de una variable está vinculada a dos profundidades.
	// La profundidad a la que ha sido declarada y la profundidad a la que se usa.
	private int profundidad;

	public EntornoFuncion(EntornoClase entornoAnterior, Declaracion identificador) {
		super(entornoAnterior, identificador);
		this.setListaArgumentos(new ArrayList<>());
		this.profundidad = entornoAnterior.getProfundidad() + 1;
	}
	
	////////*	IDENTIFICADORES DE FUNCIONES/ARGUMENTOS		*////////
	
	// Especifica los argumentos de la función
	public void putFuncionArgs(String funcionID, String argumentoID) throws ErrorSemantico {
		if(!((EntornoClase)this.getEntornoPadre()).containsFuncion(funcionID))
			throw new ErrorSemantico("La función con identificador: '"+funcionID+"' no ha sido declarada en la tabla de funciones del entorno");
		
		if(!this.containsSoloPropioEntorno(argumentoID))
			throw new ErrorSemantico("El identificador: '"+argumentoID+"' no ha sido declarado en la tabla de identificadores del entorno");
		
		if(this.getListaArgumentos().contains(argumentoID))
			throw new ErrorSemantico("Se ha definido el argumento: '"+argumentoID+"' duplicado para la función '"+funcionID+"'");
		
		this.getListaArgumentos().add(argumentoID);
	}
	
	// Devuelve true si el ID es un argumento de función especificada. Mismo entorno.
	public Boolean containsArgs(String argumentoID) {
		if(!this.getListaArgumentos().contains(argumentoID))
			return false; 
		return true;
	}
	
	// Devuelve la lista de Argumentos para la función especificada. Mismo entorno.
	public List<String> getArgs() {
		return this.getListaArgumentos();
	}
	
	public List<String> getListaArgumentos() {
		return listaArgumentos;
	}

	public void setListaArgumentos(List<String> listaArgumentos) {
		this.listaArgumentos = listaArgumentos;
	}

	@Override
	public int getProfundidad() {
		return this.profundidad;
	}
	
	/* Dibujando el Entorno */
	
	public void printEntorno() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(" -> ENTORNO FUNCIÓN "+this.get_identificador_entorno()+", de nivel "+this.getNivel()+" <- ");
		sb.append("\n");
		sb.append("\n");
		
		sb.append("		ID FUNCION: "+this.getIdentificador().getId().getId()+" , TIPO: "+this.getIdentificador().getTipo());

		sb.append("\n");
		sb.append(" VARIABLES: ");
		sb.append("\n");
		sb.append("\n");
		if(this.getTablaIDs().isEmpty()) {
			sb.append("\n");
			sb.append(" 	- no hay identificadores declarados - ");
			sb.append("\n");
			sb.append("\n");
		}else {
			Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Declaracion id = this.getTablaIDs().get(key);
				sb.append("\n");
				if(id instanceof DeclaracionConstante) {
					sb.append("CONSTANTE "+"ID: "+id.getId().getId()+" , TIPO: "+id.getTipo()+"");
				}else {
					sb.append("VARIABLE "+"ID: "+id.getId().getId()+" , TIPO: "+id.getTipo()+"");
				}
				sb.append("\n");
				sb.append("\n");
			}
		}

		sb.append("\n");
		sb.append("     -> argumentos: ");
		sb.append("\n");
		sb.append("\n");
		List<String> argumentos = this.listaArgumentos;
		if(argumentos == null || argumentos.isEmpty()) {
			sb.append("\n");
			sb.append("              -> sin argumentos <-");
			sb.append("\n");
		}else {
			for(String arg: argumentos) {
				Declaracion idArgumento = this.get(arg);
				sb.append("\n");
				sb.append("             -> id: "+idArgumento.getId().getId()+" , tipo: "+idArgumento.getTipo());
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("_______________________________________");
		sb.append("\n");
		sb.append("\n");
		FicheroEntornos.almacenaEntorno(sb.toString());
	}

	public int getTamanoMemoriaNecesaria() {
		int tamano = 0;

		for (Declaracion decl : ids)
			tamano += decl.getOcupacion();

		for (Entorno hijo: entornosHijo)
			tamano += hijo.getTamanoTotalVariables();

		return tamano;
	}

}
