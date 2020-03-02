package Ejecucion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Errores.ErrorHandler;
import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
import analisisLexico.Scanner;
import analisisSintactico.parser;
import analisisSintactico.arbol.ArbolSintactico;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

public class Main {
	public static void main(String[] args) {
		Boolean errorGrave = false;
		try {
			
			comienzaEjecucion();
			
			FileReader in = new FileReader(args[0]);
			
			ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();

			System.out.print("Cargando Scanner...");
			Scanner scanner = new Scanner(symbolFactory, in);
			System.out.println(" se ha cargado el Scanner");
			System.out.print("Cargando Parser...");
			parser parser = new parser(scanner, symbolFactory);
			System.out.println(" se ha cargado el Parser");

			System.out.print("Comienza an�lisis del fichero...");
			Symbol topSymbol = parser.parse();
			System.out.println(" el an�lisis ha finalizado");

			System.out.print("Generando �rbol de derivaci�n...");
			ArbolSintactico.generar(topSymbol);
			System.out.println(" se ha generado el �rbol de derivaci�n");
			
		} catch (FileNotFoundException e) {
			ErrorHandler.reportaError("El fichero de entrada '"+args[0]+"' no existe");
			errorGrave = true;
		} catch (IOException e) {
			ErrorHandler.reportaError("Se ha producido un error al procesar el fichero de entrada");
			errorGrave = true;
		} catch (ErrorProcesador e) {
			ErrorHandler.reportaError(e);
			errorGrave = true;
		} catch (Exception e) {
			ErrorHandler.reportaError(e.getLocalizedMessage());
			errorGrave = true;
		}
		terminaEjecucion(errorGrave);
	}

	private static void comienzaEjecucion() throws IOException, ErrorProcesador {
		FicheroTokens.abreFichero();
		ArbolSintactico.abreFichero();
		ErrorHandler.abreFichero();
		FicheroEntornos.abreFichero();
		System.out.println("Ejecuci�n Procesador de Lenguaje...");
	}

	private static void terminaEjecucion(Boolean errorGrave) {
		if(errorGrave) {
			System.out.println();
			System.out.println("Se han encontrado errores, la ejecuci�n se ha interrumpido.");
			ErrorHandler.reportaError("Se han encontrado errores, la ejecuci�n se ha interrumpido.");
			System.out.println("Puede consultar los errores en el fichero /output/Errores.txt");
		}else if(GlobalVariables.hayErrores) {
			System.out.println();
			System.out.println("La ejecuci�n ha finalizado con errores, puede consultarlos en el fichero /output/Errores.txt");
			ErrorHandler.reportaError("Se han encontrado errores, pero la ejecuci�n ha continuado.");
		}else {
			System.out.println();
			System.out.println("La ejecuci�n ha finalizado sin errores.");
			ErrorHandler.reportaError("La ejecuci�n ha finalizado sin errores.");
		}
		System.out.println();
		try {
			FicheroTokens.cierraFichero();
			ArbolSintactico.cierraFichero();
			ErrorHandler.cierraFichero();
			FicheroEntornos.cierraFichero();
		} catch (ErrorProcesador e) {
			System.out.println(e.getMensaje());
		}
	}
	
}
