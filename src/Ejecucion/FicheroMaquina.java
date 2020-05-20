package Ejecucion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
import intermedio.InstruccionTresDirecciones;
import intermedio.ProgramaIntermedio;

public class FicheroMaquina {

    private static File file;
    protected static FileWriter fileWriter;

    protected static final String FICHERO = GlobalVariables.FICHERO_MAQUINA.toString();

    public static void abreFichero() throws IOException, ErrorProcesador {
        try {
            file = new File(FICHERO);
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            throw new ErrorProcesador("Se ha producido un error al abrir el fichero de código intermedio");
        }
    }

    public static void cierraFichero() throws ErrorProcesador {
        try {
            System.out.println("Se ha generado el fichero de código intermedio en: " + file.getAbsolutePath());
            fileWriter.close();
        } catch (IOException e) {
            throw new ErrorProcesador("Se ha producido un error al cerrar el fichero de código intermedio");
        }
    }


    public static void escribirInstrucciones(ProgramaIntermedio programa) throws IOException {
        for (InstruccionTresDirecciones instr : programa) {
            fileWriter.write(instr.toMachineCode());
        }
    }
}
