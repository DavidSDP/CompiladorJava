package Ejecucion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Errores.ErrorHandler;
import Errores.ErrorProcesador;
import PostProceso.GeneracionFicheroMaquina;
import Procesador.GlobalVariables;
import analisisLexico.Scanner;
import analisisSintactico.parser;
import analisisSintactico.arbol.ArbolSintactico;
import intermedio.ProgramaIntermedio;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import optimizacion.OptimizacionLocal;
import optimizacion.OptimizacionMirilla;
import optimizacion.Optimizador;

public class Main {
    public static void main(String[] args) throws Exception {
        Boolean errorGrave = false;
        CommandlineParser clparser = new CommandlineParser(args);
        clparser.parse();
        System.out.println("-------------------------------------------------------------");
        System.out.println("Parámetros de la compilación");
        System.out.println(clparser.toString());
        System.out.println("-------------------------------------------------------------");
        try {

            comienzaEjecucion();
            FileReader in = new FileReader(clparser.getFilepath());

            ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();

            System.out.print("Cargando Scanner...");
            Scanner scanner = new Scanner(symbolFactory, in);
            System.out.println(" se ha cargado el Scanner");
            System.out.print("Cargando Parser...");
            parser parser = new parser(scanner, symbolFactory);
            System.out.println(" se ha cargado el Parser");

            System.out.print("Comienza análisis del fichero...");
            Symbol topSymbol = parser.parse();
            System.out.println(" el análisis ha finalizado");

            System.out.print("Generando árbol de derivación...");
            ArbolSintactico.generar(topSymbol);
            System.out.println(" se ha generado el árbol de derivación");

            // TODO James Gosling se está acordando de toda tu familia
            // en estos mismos instantes
            // Además, algún arquitecto de software acaba de morir en India
            if (!GlobalVariables.hayErrores) {
                // Sin optimizaciones
                FicheroIntermedio.escribirInstrucciones(ProgramaIntermedio.getInstance());
                GeneracionFicheroMaquina.escribirVersionNoOptimizada(ProgramaIntermedio.getInstance());
                
                Integer nivelOptimizacion = clparser.getNivelOptimizacion();
                
                List<Optimizador> optimizadores = new ArrayList<>();
                optimizadores.add(new OptimizacionMirilla());
                optimizadores.add(new OptimizacionLocal());
                
                if (nivelOptimizacion > 0) {
                	
                	ProgramaIntermedio.getInstance().desInicializarVariables();
                	
                	for(int i = 0; i < nivelOptimizacion && i < optimizadores.size(); i++) {
                		ProgramaIntermedio.getInstance().addOptimizador(optimizadores.get(i));
                	}
                	
                    ProgramaIntermedio.getInstance().optimizar();
                    // Optimizado
                    FicheroIntermedioOptimizado.escribirInstrucciones(ProgramaIntermedio.getInstance());
                    GeneracionFicheroMaquina.escribirVersionOptimizada(ProgramaIntermedio.getInstance());
                }
            }
        } catch (FileNotFoundException e) {
            ErrorHandler.reportaError("El fichero de entrada '" + clparser.getFilepath() + "' no existe");
            errorGrave = true;
        } catch (IOException e) {
            ErrorHandler.reportaError("Se ha producido un error al procesar el fichero de entrada");
            errorGrave = true;
        } catch (ErrorProcesador e) {
            ErrorHandler.reportaError(e);
            errorGrave = true;
        } catch (Exception e) {
            // Existen cierto mensajes que no se deben mostrar, ya que pueden ser derivados de
            // la recuperación de errores.
            // Por tanto, si estamos debuggeando el compilador, sacamos htodo lo que nos
            // tenga que decir el compilador, de otra forma, los ignoramos.
            if (clparser.isDebugging()) {
                e.printStackTrace();
                ErrorHandler.reportaError(e.toString());
            }
            errorGrave = true;
        }

        terminaEjecucion(errorGrave);
    }

    private static void comienzaEjecucion() throws IOException, ErrorProcesador {
        // Creamos el directorio output. Probablemente nos interesaría
        // que fuera en el mismo directorio que el de la prueba
        // Para eso, coger la ruta de prueba, relativizar el output
        // con respecto a ese fichero ( tal vez 2 arriba )
        // y crear el directorio acorde a eso.
        File outputDir = new File(GlobalVariables.outputDir.toString());
        outputDir.mkdirs();
        FicheroTokens.abreFichero();
        ArbolSintactico.abreFichero();
        ErrorHandler.abreFichero();
        FicheroEntornos.abreFichero();
        FicheroIntermedio.abreFichero();
        FicheroIntermedioOptimizado.abreFichero();
        System.out.println("Ejecución Procesador de Lenguaje...");
    }

    private static void terminaEjecucion(Boolean errorGrave) {
        if (errorGrave) {
            System.out.println();
            System.out.println("Se han encontrado errores, la ejecución se ha interrumpido.");
            ErrorHandler.reportaError("Se han encontrado errores, la ejecución se ha interrumpido.");
            System.out.println("Puede consultar los errores en el fichero /output/Errores.txt");
        } else if (GlobalVariables.hayErrores) {
            System.out.println();
            System.out.println("La ejecución ha finalizado con errores, puede consultarlos en el fichero /output/Errores.txt");
            ErrorHandler.reportaError("Se han encontrado errores, pero la ejecución ha continuado.");
        } else {
            System.out.println();
            System.out.println("La ejecución ha finalizado sin errores.");
            ErrorHandler.reportaError("La ejecución ha finalizado sin errores.");
        }
        System.out.println();
        try {
            FicheroTokens.cierraFichero();
            ArbolSintactico.cierraFichero();
            ErrorHandler.cierraFichero();
            FicheroEntornos.cierraFichero();
            FicheroIntermedio.cierraFichero();
            FicheroIntermedioOptimizado.cierraFichero();
        } catch (ErrorProcesador e) {
            System.out.println(e.getMensaje());
        }
    }

}
