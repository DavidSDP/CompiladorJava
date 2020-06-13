package intermedio;

import Procesador.AlmacenVariables;
import Procesador.Declaracion;
import Procesador.Entorno;
import Procesador.GlobalVariables;


public class I3DUtils {

    // TODO Utilizar estos helpers en donde se detectan declaraciones.
    public static InstruccionTresDirecciones creaDeclaracion(OperacionTresDirecciones instr, Declaracion source, Declaracion dest) {
        InstruccionTresDirecciones instruccion = crea(instr, source, dest);
        Definiciones.getInstance().add(instruccion);
        return instruccion;
    }

    public static InstruccionTresDirecciones creaDeclaracion(OperacionTresDirecciones instr, Declaracion primero, Declaracion segundo, Declaracion tercero) {
        InstruccionTresDirecciones instruccion = crea(instr, primero, segundo, tercero);
        Definiciones.getInstance().add(instruccion);
        return instruccion;
    }

    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion source, Declaracion dest) {
        Entorno entorno = GlobalVariables.entornoActual();
        Operando sourceOperator = new Operando(source, entorno.getProfundidad());
        Operando destOperator = new Operando(dest, entorno.getProfundidad());
        return crea(instr, sourceOperator, destOperator, null);
    }
    
    // Solo es utils para generar etiquetas y saltos incondicionales
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, String etiqueta) {
        Operando primerOperando = new OperandoEtiqueta(etiqueta);
        return crea(instr, primerOperando, null, null);
    }

    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion primero) {
        Entorno entorno = GlobalVariables.entornoActual();
        Operando primerOperando = new Operando(primero, entorno.getProfundidad());
        return crea(instr, primerOperando, null, null);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion primero, Declaracion segundo, Declaracion tercero) {
        Entorno entorno = GlobalVariables.entornoActual();
        Operando primerOperando = new Operando(primero, entorno.getProfundidad());
        Operando segundoOperando = new Operando(segundo, entorno.getProfundidad());
        // Probablemente aqui no necesitemos saber la profundidad, sin embargo, para mantener el codigo homogeneo
        // pasamos tambien la profundidad
        Operando tercerOperando = new Operando(tercero, entorno.getProfundidad());
        return crea(instr, primerOperando, segundoOperando, tercerOperando);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion primero, Declaracion segundo, String etiqueta) {
        Entorno entorno = GlobalVariables.entornoActual();
        Operando primerOperando = new Operando(primero, entorno.getProfundidad());
        Operando segundoOperando = new Operando(segundo, entorno.getProfundidad());
        // Las etiquetas no tienen entorno. Asi que no se le pone nada de nivel de profundidad
        Operando tercerOperando = new OperandoEtiqueta(etiqueta);
        return crea(instr, primerOperando, segundoOperando, tercerOperando);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Operando primero, Operando segundo, Operando tercero) {
        return crearInstruccion(instr, primero, segundo, tercero);
    }

    private static InstruccionTresDirecciones crearInstruccion(OperacionTresDirecciones instr, Operando primero, Operando segundo, Operando tercero) {
        InstruccionTresDirecciones i3d;
        switch(instr) {
            case COPIA:
                i3d = new Copia(primero, segundo);
                break;
            case GOTO:
                i3d = new Goto(primero);
                break;
            case ETIQUETA:
                i3d = new Etiqueta(primero);
                break;
            case DECLARAR_INDIRECCION:
                i3d = new DeclaracionIndireccion(primero);
                break;
            case CARGAR_INDIRECCION:
                i3d = new CargarIndireccion(primero, segundo, tercero);
                break;
            case GUARDAR_INDIRECCION:
                i3d = new GuardarIndireccion(primero, segundo, tercero);
                break;
            case PARAM:
                i3d = new SimpleParam(primero);
                break;
            case COMPLEX_PARAM:
                i3d = new ComplexParam(primero);
                break;
            case LLAMADA:
                i3d = new Llamada(primero, segundo, tercero);
                break;
            case PREAMBULO:
                i3d = new Preambulo(primero); // primero es el numero de procedimiento/funcion
                break;
            case RETORNO:
                i3d = new Retorno(primero, segundo); // primero es el numero de procedimiento/funcion
                break;
            case SUMA:
                i3d = new Suma(primero, segundo, tercero);
                break;
            case RESTA:
                i3d = new Resta(primero, segundo, tercero);
                break;
            case PRODUCTO:
                i3d = new Producto(primero, segundo, tercero);
                break;
            case DIVISION:
                i3d = new Division(primero, segundo, tercero);
                break;
            case GT:
                i3d = new GT(primero, segundo, tercero);
                break;
            case GTE:
                i3d = new GTE(primero, segundo, tercero);
                break;
            case LT:
                i3d = new LT(primero, segundo, tercero);
                break;
            case LTE:
                i3d = new LTE(primero, segundo, tercero);
                break;
            case AND:
                i3d = new And(primero, segundo, tercero);
                break;
            case OR:
                i3d = new Or(primero, segundo, tercero);
                break;
            case EQ:
                i3d = new EQ(primero, segundo, tercero);
                break;
            case NE:
                i3d = new NE(primero, segundo, tercero);
                break;
            case CLASE:
                i3d = new Clase(primero);
                break;
            case ENTRY_POINT:
                i3d = new EntryPoint(primero, segundo);
                break;
            default:
                throw new AssertionError(instr.name());
        }

        ProgramaIntermedio.getInstance().addInstruccion(i3d);
        if (i3d.esDefinicion()) {
            Definiciones.getInstance().add(i3d);
        }
        return i3d;
    }

    public static OperacionTresDirecciones getTipoOperacion(String simboloOperacion) throws Exception {
        OperacionTresDirecciones op;
        switch(simboloOperacion) {
            case "<":
                op = OperacionTresDirecciones.LT;
                break;
            case "<=": 
                op = OperacionTresDirecciones.LTE;
                break;
            case ">": 
                op = OperacionTresDirecciones.GT;
                break;
            case ">=":
                op = OperacionTresDirecciones.GTE;
                break;
            case "||":
                op = OperacionTresDirecciones.OR;
                break;
            case "&&":
                op = OperacionTresDirecciones.AND;
                break;
            case "+": 
                op = OperacionTresDirecciones.SUMA;
                break;
            case "*": 
                op = OperacionTresDirecciones.PRODUCTO;
                break;
            case "-": 
                op = OperacionTresDirecciones.RESTA;
                break;
            case "/": 
                op = OperacionTresDirecciones.DIVISION;
                break;
            case "==":
                op = OperacionTresDirecciones.EQ;
                break;
            case "!=":
                op = OperacionTresDirecciones.NE;
                break;
            default:
                throw new Exception("Me cago en tus muelas");
        }
        return op;
    }
    
}
