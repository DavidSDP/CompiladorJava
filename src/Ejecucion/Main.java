package Ejecucion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import analisisLexico.Scanner;
import analisisSintactico.parser;
import analisisSintactico.arbol.ArbolSintactico;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

public class Main {
	public static void main(String[] args) {
		try {
			FileReader in = new FileReader(args[0]);
			
			ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
			
			Scanner scanner = new Scanner(symbolFactory, in);
			parser parser = new parser(scanner, symbolFactory);
			
			Symbol topSymbol = parser.parse();
			
			ArbolSintactico.generar(topSymbol);
			
		} catch (FileNotFoundException e) {
			System.err.println("El fitxer d'entrada '"+args[0]+"' no existeix");
		} catch (IOException e) {
			System.err.println("Error processant el fitxer d'entrada");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
