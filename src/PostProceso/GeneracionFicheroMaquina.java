package PostProceso;

import Errores.ErrorProcesador;
import Procesador.GlobalVariables;
import intermedio.InstruccionTresDirecciones;
import intermedio.ProgramaIntermedio;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
El concepto de esta fase es como la del linkado.
En este caso en vez de ser ficheros objetos provinientes de otra compilación ( librería o algo precalculado)
son ficheros ensamblador que contienen todas las funciones de bajo nivel que dan soporte a las operaciones nativas
tales como:
    - Interacción con el módulo de memoria dinámica ( inicialización, obtener/liberar bloque de memoria ... )
    - Comparaciones en plataforma nativa ( en el caso de los strings cometimos el crimen de no hacerlo como un array y
      la comparación tiene que ser ad-hoc )
 */
public class GeneracionFicheroMaquina {

    private static final Path TEMPLATE_FOLDER = Paths.get("scripts");
    private static final Path TEMPLATE = TEMPLATE_FOLDER.resolve("template.X68");

    public static void escribirVersionNoOptimizada(ProgramaIntermedio programa) throws ErrorProcesador {
        /*
        Idea: Obtener los ficheros que se han generado, obtener las plantillas y generar el fichero
        que contiene el código final ( template con librerías + código generado )
         */
        try {
            File file = new File(GlobalVariables.FICHERO_MAQUINA.toString());
            FileWriter fileWriter = new FileWriter(file);
            escribirCodigo(programa, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new ErrorProcesador("Se ha producido un error en la E/S del fichero final no optimizado");
        }
    }

    public static void escribirVersionOptimizada(ProgramaIntermedio programa) throws ErrorProcesador {
        /*
        Idea: Obtener los ficheros que se han generado, obtener las plantillas y generar el fichero
        que contiene el código final ( template con librerías + código generado )
         */
        try {
            File file = new File(GlobalVariables.FICHERO_MAQUINA_OPT.toString());
            FileWriter fileWriter = new FileWriter(file);
            escribirCodigo(programa.optimizado(), fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            throw new ErrorProcesador("Se ha producido un error en la E/S del fichero final optimizado");
        }

    }

    private static void escribirCodigo(Iterable<InstruccionTresDirecciones> instrucciones, FileWriter fileWriter) throws IOException {
        /*
        3 fases:
            1- Escritura de la plantilla antes del código generado
            2- Escritura del código generado
            3- Escritura del final de la plantilla
         */
        BufferedReader readerPlantilla = getReaderPlantilla();
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        // precondicion: Template no vacio y markup no esta en la primera linea
        String line = readerPlantilla.readLine();
        while(line != null && !line.contains(";{Generate Code here}")) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            line = readerPlantilla.readLine();
        }
        // Post condicion: Markup leido del fichero y no introducido en el fichero nuevo

        // Fase 2: Escritura del código generado
        for (InstruccionTresDirecciones instr : instrucciones) {
            bufferedWriter.write(instr.toMachineCode());
        }

        // Fase 3: Escritura del resto de la plantilla
        line = readerPlantilla.readLine();
        while(line != null) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            line = readerPlantilla.readLine();
        }

        // Flush output before returning to avoid closing the file without writing everything in it.
        bufferedWriter.flush();
    }
    private static BufferedReader getReaderPlantilla() throws FileNotFoundException {
        File file = new File(TEMPLATE.toString());
        FileReader fileReader = new FileReader(file);
        return  new BufferedReader(fileReader);
    }
}
