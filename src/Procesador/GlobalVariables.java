package Procesador;

import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;

import Checkers.Tipo;
import Checkers.TipoObject;
import Errores.ErrorSemantico;
import SimbolosNoTerminales.SimboloArgDecl;
import SimbolosNoTerminales.SimboloArgs;
import SimbolosNoTerminales.SimboloArray;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobalVariables {

    // Cuidado! El path se resuelve aqui. Asi que si en algun momento
    // se cambia el directorio de la ejecucion probablemente no sea correcto
    // donde se estan guardando los ficheros
    public static final Path outputDir = Paths.get("output");
    public static final Path FICHERO_TOKENS = outputDir.resolve("FicheroTokens.txt");
    public static final Path FICHERO_ARBOL = outputDir.resolve("ArbolSintactico.dot");
    public static final Path FICHERO_ERRORES = outputDir.resolve("Errores.txt");
    public static final Path FICHERO_ENTORNOS = outputDir.resolve("Entornos.txt");
    public static final Path FICHERO_INTERMEDIO = outputDir.resolve("codigo_intermedio.txt");
    public static final Path FICHERO_INTERMEDIO_OPT = outputDir.resolve("codigo_intermedio_opt.txt");
    public static final Path FICHERO_MAQUINA = outputDir.resolve("codigo_maquina.txt");
    public static final Path FICHERO_MAQUINA_OPT = outputDir.resolve("codigo_maquina_opt.txt");

    public static Boolean DEBUG_MODE = true;
    public static Boolean hayErrores = false;

    private static Integer SecuenciaIdEtiqueta = 0;
    private static Integer SecuenciaIdLiteral = 0;

    private static Integer _idnodoIncremental = 0;
    private static Integer CONTADOR = 1;
    private static Stack<Entorno> pilaEntornos = new Stack<>();

    public static Entorno entornoActual() {
        try {
            return pilaEntornos.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public static Entorno entornoFuncionActual() {
        Entorno entorno;
        try {
            entorno = pilaEntornos.peek();
            return entorno.getEntornoFuncionSuperior();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    public static void declaraBuiltInFunctions(EntornoClase raiz) throws ErrorSemantico, IOException {
        asignaBuiltInFuncionID("read", Tipo.getTipo(Tipo.String), null);
        asignaBuiltInFuncionID("write", Tipo.getTipo(Tipo.Void), new SimboloArgs(new SimboloArgDecl("input", Tipo.getTipo(Tipo.String), null), null, true));
        asignaBuiltInFuncionID("integerToString", Tipo.getTipo(Tipo.String), new SimboloArgs(new SimboloArgDecl("numero", Tipo.getTipo(Tipo.Integer), null), null, true));
        asignaBuiltInFuncionID("stringToInteger", Tipo.getTipo(Tipo.Integer), new SimboloArgs(new SimboloArgDecl("string", Tipo.getTipo(Tipo.String), null), null, true));
    }

    private static void asignaBuiltInFuncionID(String idFuncion, TipoObject tipoRetorno, SimboloArgs args) throws ErrorSemantico, IOException {
        DeclaracionFuncion declaracion = asignaFuncionID(idFuncion, tipoRetorno);
        entraBloqueFuncion(declaracion);

        asignaEntornoFuncionID(idFuncion);
        if (args != null) {
            for (SimboloArgs a = args; a != null; a = a.getNextArg()) {
                GlobalVariables.asignaID(a.getId(), a.getTipo());
            }
            asignaFuncionArgs(idFuncion, args);
        }
        saleBloqueFuncion(true);
    }

    public static Declaracion asignaID(String id, String tipo) throws ErrorSemantico {
        return asignaID(id, Tipo.getTipo(tipo));
    }

    public static Declaracion asignaID(String id, TipoObject tipo) throws ErrorSemantico {
        Entorno top = entornoActual();
        return top.put(tipo, id);
    }

    public static DeclaracionArray asignaArray(String id, String tipo, SimboloArray simboloArrayDef) throws ErrorSemantico {
        Entorno top = entornoActual();
        return top.putArray(id, tipo, simboloArrayDef.getNumero());
    }

    public static DeclaracionConstante asignaIDConstante(String id, String tipo, Object valor) throws ErrorSemantico {
        Entorno top = entornoActual();
        return top.putConstante(Tipo.getTipo(tipo), id, valor);
    }

    public static DeclaracionFuncion asignaFuncionID(String idFuncion, String tipo) throws ErrorSemantico {
        return asignaFuncionID(idFuncion, Tipo.getTipo(tipo));
    }

    public static DeclaracionFuncion asignaFuncionID(String idFuncion, TipoObject tipo) throws ErrorSemantico {
        EntornoClase top = (EntornoClase) entornoActual();
        String etiqueta = GlobalVariables.generarEtiqueta();
        return top.putFuncion(tipo, idFuncion, etiqueta);
    }

    // Llamar una vez dentro del entorno de la funciÃ³n
    public static void asignaEntornoFuncionID(String idFuncion) throws ErrorSemantico {
        EntornoFuncion top = (EntornoFuncion) entornoActual();
        ((EntornoClase) top.getEntornoPadre()).putFuncionEntorno(idFuncion, top);
    }

    public static void asignaFuncionArg(String nombre, String tipoString) throws ErrorSemantico {
        TipoObject tipo = Tipo.getTipo(tipoString);
        EntornoFuncion top = (EntornoFuncion) entornoActual();
        top.putFuncionArg(nombre, tipo);
    }

    public static void asignaFuncionArgArray(String id, String tipo, SimboloArray simboloArrayDef) throws ErrorSemantico {
        EntornoFuncion top = (EntornoFuncion)entornoActual();
        // Aqui hay un leak gigante.
        // Pasamos un número, pero realmente en los parametros no podemos especificarlo (almenos no tal cual lo tenemos)
        // por tanto o permitimos que pongan el tamano en la declaracion o directamente utilizamos el heap para manejar todo esto
        // P.D: Usar el heap probablemente nos hará subir por encima del 9 y aumentar el aprobado
        top.putFuncionArrayArg(id, tipo, simboloArrayDef.getNumero());
    }


    public static void asignaFuncionArgs(String idFuncion, SimboloArgs args) throws ErrorSemantico {
        EntornoFuncion top = (EntornoFuncion) entornoActual();
        for (SimboloArgs a = args; a != null; a = a.getNextArg()) {
            top.putFuncionArgs(idFuncion, a.getId());
        }
    }

    public static DeclaracionClase asignaClaseID(String id) throws ErrorSemantico {
        EntornoClase top = (EntornoClase) entornoActual();
        return top.putClase(id);
    }

    public static Declaracion compruebaID(String id) throws ErrorSemantico {
        Entorno top = entornoActual();
        Declaracion i = top.fullGet(id);
        if (i == null)
            throw new ErrorSemantico("El id " + id + " no es un sÃ­mbolo declarado en el entorno");

        return i;
    }

    public static DeclaracionArray compruebaIDArray(String id) throws ErrorSemantico {
        Entorno top = entornoActual();
        Declaracion d = top.fullGet(id);
        if (!(d instanceof DeclaracionArray))
            throw new ErrorSemantico("El id " + id + " no es un símbolo de array declarado en el entorno");
        return (DeclaracionArray)d;
    }

    public static void compruebaAsignacionPermitida(String id) throws ErrorSemantico {
        Entorno top = entornoActual();
        Declaracion i = top.fullGet(id);
        if (i instanceof DeclaracionConstante)
            throw new ErrorSemantico("El valor del identificador " + id + " no puede ser modificado al tener el atributo FINAL");
    }

    public static DeclaracionFuncion compruebaFuncionID(String id) throws ErrorSemantico {
        Entorno top = entornoActual();
        DeclaracionFuncion i = top.fullGetFuncion(id);
        if (i == null)
            throw new ErrorSemantico("El id " + id + " no es un sÃ­mbolo de funciÃ³n declarado en el entorno");

        return i;
    }

    public static void compruebaClaseID(String id) throws ErrorSemantico {
        EntornoClase top = (EntornoClase) entornoActual();
        Declaracion i = top.fullGetClase(id);
        if (i == null)
            throw new ErrorSemantico("El id " + id + " no es un sÃ­mbolo de clase declarado en el entorno");
    }

    public static void entraBloqueClase(DeclaracionClase identificadorClase) {
        EntornoClase e = new EntornoClase(entornoActual(), identificadorClase);
        // El entorno global no tiene declaracion
        if (identificadorClase != null) {
            identificadorClase.setEntornoAsociado(e);
        }
        pilaEntornos.push(e);
    }

    public static void saleBloqueClase() throws IOException {
        EntornoClase popped = (EntornoClase) pilaEntornos.pop();
        if (DEBUG_MODE) {
            popped.printEntorno();
        }
    }

    public static void entraBloqueFuncion(Declaracion identificadorFuncion) {
        EntornoFuncion e = new EntornoFuncion((EntornoClase) entornoActual(), identificadorFuncion);
        pilaEntornos.push(e);
        DeclaracionFuncion declaracionFuncion = (DeclaracionFuncion)identificadorFuncion;
        declaracionFuncion.setEntornoDependiente(e);
    }

    public static void saleBloqueFuncion(Boolean isBuiltIn) throws IOException {
        EntornoFuncion popped = (EntornoFuncion) pilaEntornos.pop();
        if (DEBUG_MODE && !isBuiltIn) {
            popped.printEntorno();
        }
    }

    public static Integer getIdentificador() {
        return CONTADOR++;
    }

    public static Integer getNodoIdentificadorUnico() {
        return _idnodoIncremental++;
    }

    public static DeclaracionConstante crearVariableTemporal(TipoObject tipo, Object valor) throws ErrorSemantico {
        // Ojo, esto de momento solo sirve para crear literales!
        // En caso de utilizar esta parte para crear una constante ( cosa que no deberías, ya que es una variables más )
        // habrá que utilizar el entorno adaptandolo para que nos facilite la creación de literales
        String name = generarNombreLiteral();
        return new DeclaracionConstante(new Identificador(name, name), tipo, valor, -1);
//        return entornoActual().putConstante(tipo, null, valor);
    }

    public static Declaracion crearVariableTemporal(TipoObject tipo) throws ErrorSemantico {
        return entornoActual().put(tipo, null);
    }

    public static String generarEtiqueta() {
        return "e" + SecuenciaIdEtiqueta++;
    }

    public static String generarNombreLiteral() {
        return "l" + SecuenciaIdLiteral++;
    }

    public static DeclaracionFuncion getMainFunction() throws ErrorSemantico {
        EntornoClase entorno = (EntornoClase)entornoActual();
        DeclaracionFuncion declaracionMain = entorno.getFuncion("main");

        if(declaracionMain == null) throw new ErrorSemantico("Falta la funcion de entrada: main");
        return declaracionMain;
    }
}