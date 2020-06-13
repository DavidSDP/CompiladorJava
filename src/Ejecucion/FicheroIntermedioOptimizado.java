package Ejecucion;

import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
import intermedio.InstruccionTresDirecciones;
import intermedio.ProgramaIntermedio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FicheroIntermedioOptimizado {
	
	private static FileWriter fileWriter;
	private static File file;
	
	public static void abreFichero() throws IOException, ErrorProcesador {
		try {
			file = new File(GlobalVariables.FICHERO_INTERMEDIO_OPT.toString());
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al abrir el fichero de código intermedio");
		}
	}
	
	public static void cierraFichero() throws ErrorProcesador {
		try {
			System.out.println("Se ha generado el fichero de código intermedio en: "+file.getAbsolutePath());
			fileWriter.close();
		} catch (IOException e) {
			throw new ErrorProcesador("Se ha producido un error al cerrar el fichero de código intermedio");
		}
	}
	
    public static void escribirInstrucciones(ProgramaIntermedio programa) throws IOException {
            for(InstruccionTresDirecciones instr : programa.optimizado()) {
                fileWriter.write(instr.toString());
                fileWriter.write(System.lineSeparator());
            }
    }
}
