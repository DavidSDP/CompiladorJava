package analisisSintactico.arbol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Checkers.Tipo;
import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
import SimbolosNoTerminales.SimboloPrograma;
import java_cup.runtime.Symbol;

public class ArbolSintactico {
	
	public static void generar(Symbol topSymbol) throws ErrorProcesador, IOException {
		generaDotRecursivo((INodo) topSymbol.value);
		escribeLinea("\n}");
		cierraFichero();
	}
	
	private static void generaDotRecursivo(INodo nodo) throws ErrorProcesador, IOException {
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
	private static File file;
	
	public static void abreFichero() throws ErrorProcesador, IOException {
		try {
			file = new File(GlobalVariables.FICHERO_ARBOL.toString());
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al abrir el fichero del árbol sintáctico");
		}
		escribeLinea("digraph G {\n");
	}
	
	public static void cierraFichero() throws ErrorProcesador {
		try {
			System.out.println("Se ha generado el Árbol Sintáctico en: "+file.getAbsolutePath());
			fileWriter.close();
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al cerrar el fichero del árbol sintáctico");
		}
	}
	
	private static void escribeLinea(String linea) throws IOException {
		fileWriter.write(linea);
		fileWriter.write(System.lineSeparator());
	}
	
}
