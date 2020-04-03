package intermedio;

/**
 * Cualquier expresión, sea cual sea, puede representarse por una igualdad
 * tal que:
 *  si Op1 != Op2 goto EtElse
 * Y no importa como se haya calculado el operando 1 y el operando 2
 */
public class SaltoCondicional extends InstruccionTresDirecciones {
    public SaltoCondicional(Operando primero, Operando segundo, Operando etiqueta) {
        // TODO Este super debería pasar algo especifico de un salto condicional
        // De esta forma podremos vincular la comparación de igualdad de las 
        // variables que nos pasen para poder saltar a la etiqueta
        super(InstruccionMaquina.AND);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = etiqueta;
    } 

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
