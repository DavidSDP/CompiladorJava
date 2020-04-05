package intermedio;

import Procesador.Declaracion;


public class I3DUtils {
    
    public static InstruccionTresDirecciones crea(OperacionTresDirecciones instr, Declaracion source, Declaracion dest) {
        Operando sourceOperator = new Operando(source);
        Operando destOperator = new Operando(dest);
        return crea(instr, sourceOperator, destOperator, null);
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
    
    // TODO Ver como generamos las variables temporales.
    //  Dependiendo de como se generen ( si se registran en la tabla de simbolos o no)
    //  Podremos unificar el tema del uso de variables y constantes.
    //  P.E: Si una constante se inicializa con una operación, la operación genera un valor temporal
    //       que, si se tiene en cuenta en la tabla de simbolos, nos vendrá como un operando normal y corriente.
    //       De otra forma, la variable nos vendría como un entero, provocando que tuvieramos que diferenciar los casos.
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
//                c3d = new Llamada(primero); // primero es el numero de procedimiento/funcion
                break;
            case PREAMBULO:
//                c3d = new Preambulo(primero); // primero es el numero de procedimiento/funcion
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
