package TEST;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import analisisLexico.Scanner;
import analisisSintactico.sym;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

public class TestScanner {

	public static void main(String[] args) {
		
		if(args == null || args.length == 0) {
			System.out.println();
		}
		if (args.length < 1) {
			System.err.println("Indica un fitxer amb les dades d'entrada");
			System.exit(0);
		}
		try {
			FileReader in = new FileReader(args[0]);
			ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
			Scanner scanner = new Scanner(symbolFactory, in);
			Symbol symbol = scanner.next_token();
			while(symbol != null && symbol.sym != sym.EOF) {
				System.out.println("(" + sym.terminalNames[symbol.sym] + ", " + symbol.value + ")");
				symbol = scanner.next_token();
			}
			System.out.println("(" + sym.terminalNames[symbol.sym] + ", " + symbol.value + ")");
			
		} catch (FileNotFoundException e) {
			System.err.println("El fitxer d'entrada '"+args[0]+"' no existeix");
		} catch (IOException e) {
			System.err.println("Error processant el fitxer d'entrada");
		}
	}
}
