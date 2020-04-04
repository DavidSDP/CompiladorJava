package Ejecucion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Errores.ErrorProcesador;
import Procesador.GlobalVariables;

public class FicheroEntornos {
	
	private static FileWriter fileWriter;
	private static File file;
	
	public static void abreFichero() throws IOException, ErrorProcesador {
		try {
			file = new File(GlobalVariables.FICHERO_ENTORNOS.toString());
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al abrir el fichero de Entornos");
		}
	}
	
	public static void cierraFichero() throws ErrorProcesador {
		try {
			System.out.println("Se ha generado el fichero de Entornos en: "+file.getAbsolutePath());
			fileWriter.close();
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al cerrar el fichero de Entornos");
		}
	}
	
	private static void escribeLinea(String linea) throws IOException {
		fileWriter.write(linea);
		fileWriter.write(System.lineSeparator());
	}
	
	public static void almacenaEntorno(String print) throws IOException {
		escribeLinea(print);
	}
}
