package intermedio;

import Procesador.Declaracion;
import Procesador.Entorno;
import Procesador.GlobalVariables;


public class I3DUtils {

    // Usado por los retornos de procedimiento ( return void )
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr) {
        return crearInstruccion(instr, null, null, null);
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
        InstruccionTresDirecciones c3d;
        switch(instr) {
            case COPIA:
                c3d = new Copia(primero, segundo);
                break;
            case GOTO:
                c3d = new Goto(primero);
                break;
            case ETIQUETA:
                c3d = new Etiqueta(primero);
                break;
            case CARGAR_INDIRECCION:
                c3d = new CargarIndireccion(primero, segundo, tercero);
                break;
            case GUARDAR_INDIRECCION:
                c3d = new GuardarIndireccion(primero, segundo, tercero);
                break;
            case PARAM:
                c3d = new Param(primero);
                break;
            case LLAMADA:
                c3d = new Llamada(primero); // primero es el numero de procedimiento/funcion
                break;
            case PREAMBULO:
                c3d = new Preambulo(primero); // primero es el numero de procedimiento/funcion
                break;
            case RETORNO:
                c3d = new Retorno(primero, segundo); // primero es el numero de procedimiento/funcion
                break;
            case SUMA:
                c3d = new Suma(primero, segundo, tercero);
                break;
            case RESTA:
                c3d = new Resta(primero, segundo, tercero);
                break;
            case PRODUCTO:
                c3d = new Producto(primero, segundo, tercero);
                break;
            case DIVISION:
                c3d = new Division(primero, segundo, tercero);
                break;
            case GT:
                c3d = new GT(primero, segundo, tercero);
                break;
            case GTE:
                c3d = new GTE(primero, segundo, tercero);
                break;
            case LT:
                c3d = new LT(primero, segundo, tercero);
                break;
            case LTE:
                c3d = new LTE(primero, segundo, tercero);
                break;
            case AND:
                c3d = new And(primero, segundo, tercero);
                break;
            case OR:
                c3d = new Or(primero, segundo, tercero);
                break;
            case EQ:
                c3d = new EQ(primero, segundo, tercero);
                break;
            case NE:
                c3d = new NE(primero, segundo, tercero);
                break;
            default:
                throw new AssertionError(instr.name());
        }

        ProgramaIntermedio.getInstance().addInstruccion(c3d);
        return c3d;
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
