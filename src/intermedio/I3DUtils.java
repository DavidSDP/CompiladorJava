package intermedio;

import Procesador.Declaracion;


public class I3DUtils {
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion source, Declaracion dest) {
        Operando sourceOperator = new Operando(source);
        Operando destOperator = new Operando(dest);
        return crea(instr, sourceOperator, destOperator, null);
    }
    
    // Solo es utils para generar etiquetas y saltos incondicionales
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, String etiqueta) {
        Operando primerOperando = new OperandoEtiqueta(etiqueta);
        return crea(instr, primerOperando, null, null);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion primero) {
        Operando primerOperando = new Operando(primero);
        return crea(instr, primerOperando, null, null);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion primero, Declaracion segundo, Declaracion tercero) {
        Operando primerOperando = new Operando(primero);
        Operando segundoOperando = new Operando(segundo);
        Operando tercerOperando = new Operando(tercero);
        return crea(instr, primerOperando, segundoOperando, tercerOperando);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion primero, Declaracion segundo, String etiqueta) {
        Operando primerOperando = new Operando(primero);
        Operando segundoOperando = new Operando(segundo);
        Operando tercerOperando = new OperandoEtiqueta(etiqueta);
        return crea(instr, primerOperando, segundoOperando, tercerOperando);
    }
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Operando primero, Operando segundo, Operando tercero) {
        InstruccionTresDirecciones c3d = null;
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
            case LLAMADA:
                c3d = new Llamada(primero); // primero es el numero de procedimiento/funcion
                break;
            case PREAMBULO:
                c3d = new Preambulo(primero); // primero es el numero de procedimiento/funcion
                break;
            case RETORNO:
                c3d = new Retorno(primero); // primero es el numero de procedimiento/funcion
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
        OperacionTresDirecciones op = null;
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
                op = OperacionTresDirecciones.LT;
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
