/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intermedio;

/**
 *
 * @author jesus
 */
public class I3DUtils {
    
    // TODO Ver como generamos las variables temporales.
    //  Dependiendo de como se generen ( si se registran en la tabla de simbolos o no)
    //  Podremos unificar el tema del uso de variables y constantes.
    //  P.E: Si una constante se inicializa con una operación, la operación genera un valor temporal
    //       que, si se tiene en cuenta en la tabla de simbolos, nos vendrá como un operando normal y corriente.
    //       De otra forma, la variable nos vendría como un entero, provocando que tuvieramos que diferenciar los casos.
    public static InstruccionTresDirecciones crea(InstruccionMaquina instr, Operando source, Operando dest) {
        InstruccionTresDirecciones c3d = null;
        switch(instr) {
            case COPIA:
                c3d = new Copia(source, dest);
                break;
            case GOTO:
                break;
            case ETIQUETA:
                break;
            case LLAMADA:
                break;
            case PREAMBULO:
                break;
            case SUMA:
                break;
            case RESTA:
                break;
            case PRODUCTO:
                break;
            case DIVISION:
                break;
            case GT:
                break;
            case GTE:
                break;
            case LT:
                break;
            case LTE:
                break;
            case AND:
                break;
            case OR:
                break;
                
            // Kept this just for safety reason
            default:
                throw new AssertionError(instr.name());
            
        }
        
        return c3d;
    }
    
}
