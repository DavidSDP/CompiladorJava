package Errores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Procesador.GlobalVariables;

public class ErrorHandler {
	
	private static FileWriter fileWriter;
	private static File file;
	
	public static void abreFichero() throws ErrorProcesador, IOException {
		try {
			file = new File(GlobalVariables.FICHERO_ERRORES);
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al abrir el fichero de Errores");
		}
		escribeLinea("// ERRORES GENERADOS //");
	}
	
	public static void cierraFichero() throws ErrorProcesador {
		try {
			System.out.println("Se ha generado el fichero de Errores en: "+file.getAbsolutePath());
			fileWriter.close();
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al cerrar el fichero de Tokens");
		}
	}
	
	private static void escribeLinea(String linea) throws IOException {
		fileWriter.write(linea);
		fileWriter.write(System.lineSeparator());
	}
	
	public static void reportaError(ErrorProcesador errorProcesador) {
		try {
			GlobalVariables.hayErrores = true;
			if(errorProcesador instanceof ErrorLexico) {
				escribeLinea("");
				escribeLinea("Error léxico ->");
				escribeLinea("\t" + errorProcesador.getErrorLine());
				escribeLinea("<-");
			}
			if(errorProcesador instanceof ErrorSintactico) {
				escribeLinea("");
				escribeLinea("Error sintáctico ->");
				escribeLinea("\t" + errorProcesador.getErrorLine());
				escribeLinea("<-");
			}
			if(errorProcesador instanceof ErrorSemantico) {
				escribeLinea("");
				escribeLinea("Error semántico ->");
				escribeLinea("\t" + errorProcesador.getErrorLine());
				escribeLinea("<-");
			}
		} catch (IOException e) {
			System.out.println("Error al acceder al fichero de errores");
		}
	}
	
	public static void reportaError(String error) {
		try {
			GlobalVariables.hayErrores = true;
			escribeLinea(error);
		} catch (IOException e) {
			System.out.println("Error al acceder al fichero de errores");
		}
	}

}
