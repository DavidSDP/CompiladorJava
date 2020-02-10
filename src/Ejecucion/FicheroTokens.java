package Ejecucion;

import java.io.FileWriter;
import java.io.IOException;

import Procesador.GlobalVariables;
import analisisSintactico.sym;
import java_cup.runtime.Symbol;

public class FicheroTokens {
	
	public static FileWriter fileWriter;
	
	private static void abreFichero() {
		try {
			fileWriter = new FileWriter(GlobalVariables.FICHERO_TOKENS);
		} catch (IOException e) {
			new Error("Se ha producido un error al abrir el fichero de Tokens");
		}
		escribeLinea("// TOKENS GENERADOS //");
	}
	
	private static void cierraFichero() {
		try {
			fileWriter.close();
		} catch (IOException e) {
			new Error("Se ha producido un error al cerrar el fichero de Tokens");
		}
	}
	
	private static void escribeLinea(String linea) {
		if(fileWriter == null)
			abreFichero();
        try {
			fileWriter.write(linea);
			fileWriter.write(System.lineSeparator());
		} catch (IOException e) {
			new Error("Se ha producido un error al introducir la línea "+linea+" en el fichero de Tokens");
		}
	}
	
	public static void almacenaToken(Symbol symbol) {
		if(symbol != null) {
			if(symbol.sym == sym.EOF) {
				System.out.println("(" + sym.terminalNames[symbol.sym] + ", " + symbol.value + ")");
				cierraFichero();
			} else {
				System.out.println("(" + sym.terminalNames[symbol.sym] + ", " + symbol.value + ")");
			}
		}else {
			cierraFichero();
		}
	}
}
