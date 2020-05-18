package intermedio;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.Size;
import CodigoMaquina.especiales.Contenido;
import CodigoMaquina.especiales.Literal;
import Procesador.DeclaracionArray;

public class CargarIndireccion extends InstruccionTresDirecciones {
    public CargarIndireccion(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.CARGAR_INDIRECCION);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = tercero;
    }

    /**
     * La estrategia para calcular la and es la siguiente:
     * 1. Obtener la direccion del vector referenciado por primero ( A6 + Desplazamiento de la variable)
     * 2. Calcular el desplazamiento hasta el componente
     *      A) Si indice es un literal podemos realizar el cálculo en tiempo de compilación
     *      B) Si indice es una variable tenemos que generar el código para acceder al sitio adecuado
     * 3. Copiamos el valor a la memoria de salida.
     *
     * Por ahora tanto literales como variables generan el código necesario para calcular la indexacion
     * en runtime.
     *
     * Teniendo en cuenta que A6 contiene la direccion del bloque de activacion que se tiene que acceder en cada momento
     * Ejemplo:
     *  move A6, A5     // preservar la direccion base del bloque de activación
     *  add DESPL, A5   // Nos situamos encima del vector
     *  move X(A6), D1  // Cargamos el valor del indice en D1
     *  mulu TAM, D1    // Calculamos el desplazamiento dentro del vector
     *  add D1, A5      // Nos situamos encima del elemento del vector
     *  move (A5),(A6)  // Copiamos el contenido de la posicion i-esima del vector a la variable de salida (A6)
     */
    @Override
    public String toMachineCode() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
        StringBuilder sb = new StringBuilder();
        DeclaracionArray declArray = (DeclaracionArray)this.primero.getValor();

        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(this.primero.load(DataRegister.D0)); // El guardar al registro es totalmente dummy, lo que nos interesa
        bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, AddressRegister.A5));            // es la direccion que se deja en A6
        bI.add(new Instruccion(OpCode.ADD, Size.W, Literal.__(declArray.getDesplazamiento()), AddressRegister.A5));
        bI.add(this.segundo.load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.MULU, Literal.__(declArray.getTipoDato().getSize()), DataRegister.D1));
        bI.add(new Instruccion(OpCode.ADD, DataRegister.D1, AddressRegister.A5));
                // Juas lo siguiente es hack hack porque hemos permitido meter strings a pelo jajajajaja
        bI.add(this.tercero.save(Contenido.__(AddressRegister.A5)));
        return sb.toString();
    }
}
