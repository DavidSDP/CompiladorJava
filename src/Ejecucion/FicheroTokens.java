package Ejecucion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.ConsoleHandler;

import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
import analisisSintactico.sym;
import java_cup.runtime.Symbol;

public class FicheroTokens {
	
	private static FileWriter fileWriter;
	private static File file;
	
	public static void abreFichero() throws IOException, ErrorProcesador {
		try {
			file = new File(GlobalVariables.FICHERO_TOKENS);
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al abrir el fichero de Tokens");
		}
		escribeLinea("// TOKENS GENERADOS //");
	}
	
	public static void cierraFichero() throws ErrorProcesador {
		try {
			System.out.println("Se ha generado el fichero de Tokens en: "+file.getAbsolutePath());
			fileWriter.close();
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al cerrar el fichero de Tokens");
		}
	}
	
	private static void escribeLinea(String linea) throws IOException {
		fileWriter.write(linea);
		fileWriter.write(System.lineSeparator());
	}
	
	public static void almacenaToken(Symbol symbol) throws IOException {
		if(symbol.sym == sym.EOF) {
			escribeLinea("(" + sym.terminalNames[symbol.sym] + ", " + symbol.value + ")");
		} else {
			escribeLinea("(" + sym.terminalNames[symbol.sym] + ", " + symbol.value + ")");
		}
	}
}
