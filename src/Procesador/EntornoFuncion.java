package Procesador;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntornoFuncion extends Entorno{
	
	// Se guardan los Identificadores que son argumentos del Entorno (Sólo funciones)
	private List<String> listaArgumentos;

	public EntornoFuncion(EntornoClase entornoAnterior, Identificador identificador) {
		super(entornoAnterior, identificador);
		this.setListaArgumentos(new ArrayList<>());
	}
	
	////////*	IDENTIFICADORES DE FUNCIONES/ARGUMENTOS		*////////
	
	// Especifica los argumentos de la función
	public void putFuncionArgs(String funcionID, String argumentoID) {
		if(!((EntornoClase)this.getEntornoAnterior()).containsFuncion(funcionID))
			throw new Error("La función con identificador: '"+funcionID+"' no ha sido declarada en la tabla de funciones del entorno");
		
		if(!this.contains(argumentoID))
			throw new Error("El identificador: '"+argumentoID+"' no ha sido declarado en la tabla de identificadores del entorno");
		
		if(this.getListaArgumentos().contains(argumentoID))
			throw new Error("Se ha definido el argumento: '"+argumentoID+"' duplicado para la función '"+funcionID+"'");
		
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
	
	/* Dibujando el Entorno */
	
	public void printEntorno() {
		System.out.println();
		System.out.println(" -> ENTORNO FUNCIÓN "+this.get_identificador_entorno()+", de nivel "+this.getNivel()+" <- ");
		System.out.println();
		
		System.out.println("ID: "+this.getIdentificador().getId()+" , TIPO: "+this.getIdentificador().getTipo());

		System.out.println();
		System.out.println(" VARIABLES: ");
		if(this.getTablaIDs().isEmpty()) {
			System.out.println(" - no hay identificadores declarados - ");
		}else {
			Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				Identificador id = this.getTablaIDs().get(key);
				System.out.println("ID: "+id.getId()+" , TIPO: "+id.getTipo());
			}
		}
		
		System.out.println();
		System.out.println("     -> argumentos: ");
		List<String> argumentos = this.listaArgumentos;
		if(argumentos == null || argumentos.isEmpty()) {
			System.out.println("              -> sin argumentos <-");
		}else {
			for(String arg: argumentos) {
				Identificador idArgumento = this.get(arg);
				System.out.println("             -> id: "+idArgumento.getId()+" , tipo: "+idArgumento.getTipo());
			}
		}
		System.out.println();
		System.out.println("_______________________________________");
		System.out.println();
	}

	public List<String> getListaArgumentos() {
		return listaArgumentos;
	}

	public void setListaArgumentos(List<String> listaArgumentos) {
		this.listaArgumentos = listaArgumentos;
	}

}
