package analisisSintactico.arbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Checkers.Tipo;
import Procesador.GlobalVariables;
import SimbolosNoTerminales.SimboloPrograma;
import java_cup.runtime.Symbol;

public class ArbolSintactico {
	
	public static void generar(Symbol topSymbol) {
		generaDotRecursivo((INodo) topSymbol.value);
		cierraFichero();
	}
	
	private static void generaDotRecursivo(INodo nodo) {
		if(nodo.getChildren() != null && !nodo.getChildren().isEmpty()) {
			for(INodo hijo: nodo.getChildren()) {
				escribeLinea(nodo.getIdentificadorUnico() + " -> " + hijo.getIdentificadorUnico());
				generaDotRecursivo(hijo);
			}
			if(nodo instanceof SimboloPrograma) {
				escribeLinea(nodo.getIdentificadorUnico() + " [color=black,style=filled,label=\""+nodo.getName()+"\"]");
			}else {
				escribeLinea(nodo.getIdentificadorUnico() + " [label=\""+nodo.getName()+"\"]");
			}
		}else {
			if(nodo instanceof SimboloTerminal) {
				if(Tipo.Token.equals(((SimboloTerminal)nodo).getTipo())) {
					escribeLinea(nodo.getIdentificadorUnico() + " [color=lightblue,style=filled,label=\""+nodo.getName()+"\"]");
				}else{
					escribeLinea(nodo.getIdentificadorUnico() + " [color=lightgrey,style=filled,label=\""+nodo.getName()+"\"]");
				}
			}
		}
	}

	public static FileWriter fileWriter;
	
	private static void abreFichero() {
		try {
			File newFile = new File(GlobalVariables.FICHERO_ARBOL);
			fileWriter = new FileWriter(newFile);
		} catch (IOException e) {
			throw new Error("Se ha producido un error al abrir el fichero del árbol sintáctico");
		}
		escribeLinea("digraph G {\n");
	}
	
	private static void cierraFichero() {
		try {
			escribeLinea("\n}");
			fileWriter.close();
		} catch (IOException e) {
			new Error("Se ha producido un error al cerrar el fichero del árbol sintáctico");
		}
	}
	
	private static void escribeLinea(String linea) {
		if(fileWriter == null)
			abreFichero();
        try {
			fileWriter.write(linea);
			fileWriter.write(System.lineSeparator());
		} catch (IOException e) {
		}
	}
	
}
