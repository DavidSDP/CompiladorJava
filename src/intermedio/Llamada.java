package intermedio;


import CodigoMaquina.*;
import CodigoMaquina.especiales.*;
import Procesador.DeclaracionFuncion;

public class Llamada extends InstruccionTresDirecciones {
    public Llamada(Operando primero, Operando segundo, Operando tercero) {
        super(OperacionTresDirecciones.LLAMADA);
        // Callee
        this.primero = primero;
        // Caller
        this.segundo = segundo;
        // Return container
        this.tercero = tercero;
    }

    @Override
    public String toMachineCode() {
        DeclaracionFuncion callee = (DeclaracionFuncion)this.primero.getValor();
        BloqueInstrucciones bI = new BloqueInstrucciones();

        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.STACK_TOP, AddressRegister.A6));
        if (!callee.hasParams()) {
            // Si la funcion no tiene parametros no se ha movido el STACK_TOP desde la ultima insercion de
            // datos y sigue en el dato anterior, así que aqui lo tenemos que mover
            bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.STACK_TOP, PreDecremento.__(StackPointer.A7)));
            bI.add(new Instruccion(OpCode.ADDQ, Size.L, Literal.__(2), AddressRegister.A6));
        }

        if (callee.getTamanoRetorno() > 0) {
            bI.add(new Instruccion(OpCode.ADD, Size.L, Literal.__(callee.getTamanoRetorno()), AddressRegister.A6));
        }

        if (this.segundo != null) {
            DeclaracionFuncion caller = (DeclaracionFuncion)this.segundo.getValor();
            // Determinar el access link que se tiene que almacenar en el nuevo bloque de activación
            // Ojo! Por facilidad de cálculos al acceder a las variables se almacenará el BP en el access link
            // de esa forma siempre trataremos htodo de la misma manera. Esto hace que a la hora de escalar
            // los bloques de activación tengamos que restar 1 palabra a la posición para usar el siguiente access link
            boolean mismoNivel = callee.declaradaAlMismoNivel(caller);
            if (mismoNivel) {
                // BP hace referencia al bloque de activacion antiguo y el nuevo, que esta en construccion,
                // esta referenciado por el STACK_TOP
                // El access link es el access link que tiene el caller
                bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.BP, AddressRegister.A5));
                bI.add(new Instruccion(OpCode.SUBQ, Size.L, Literal.__(4), AddressRegister.A5));
                bI.add(new Instruccion(OpCode.MOVE, Size.L, Contenido.__(AddressRegister.A5), Contenido.__(AddressRegister.A6)));
                bI.add(new Instruccion(OpCode.ADDQ, Size.L, Literal.__(2), AddressRegister.A6));
                bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, Variables.STACK_TOP));

            } else {
                // Ahora mismo no tenemos llamadas recursivas ni clases anidadas
                // asi que este caso no se puede dar.
                // Dadas 2 funciones A, B ambas estarán siempre declaradas al mismo nivel.
                assert false;
            }

        } else {
            // Si no hay caller estamos gestionando el main y se tiene que hacer de forma diferente:
            // Saltamos el BP actual
            bI.add(new Instruccion(OpCode.ADDQ, Size.L, Literal.__(2), AddressRegister.A6));
            bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.BP, Contenido.__(AddressRegister.A6)));
            bI.add(new Instruccion(OpCode.ADDQ, Size.L, Literal.__(2), AddressRegister.A6));
            bI.add(new Instruccion(OpCode.MOVE, Size.L, AddressRegister.A6, Variables.STACK_TOP));
        }

        // Actualizamos el estado del programa y llamamos a la función
        bI.add(Instruccion.nuevaInstruccion("\tbsr update_bp")); // Actualiza BP y AL
        bI.add(Instruccion.nuevaInstruccion("\tbsr " + callee.getEtiqueta()));

        // Puede que la función tenga retorno y que el que llama no lo esté gestionando
        if (this.tercero != null) {
            bI.add(new Instruccion(OpCode.MOVE, Size.L, Variables.BP, AddressRegister.A6));
            bI.add(new Instruccion(OpCode.SUB, Size.L, Literal.__(4 + callee.getTamanoRetorno()), AddressRegister.A6));
            if (callee.isReturnIsComplexType()) {
                bI.add(new Instruccion(OpCode.MOVE, Size.L, Contenido.__(AddressRegister.A6), AddressRegister.A0));
            } else {
                bI.add(new Instruccion(OpCode.MOVE, Size.W, Contenido.__(AddressRegister.A6), DataRegister.D5));
            }

            // Ojo! no se puede alternar el orden de la restauración del BP y el guardado del retorno!!
            bI.add(Instruccion.nuevaInstruccion("\tbsr restore_bp")); // Actualiza BP y AL

            if (callee.isReturnIsComplexType()) {
                bI.add(this.tercero.saveStringDescriptorConstante(AddressRegister.A0));
            } else {
                bI.add(this.tercero.save(DataRegister.D5));
            }
        } else {
            bI.add(Instruccion.nuevaInstruccion("\tbsr restore_bp"));
        }

        // Esto funciona si los supuestos parametros han añadido el backup de la cima de la pila
        // Una alternativa es guardar el listado de declaraciones de los parametros de las funciones en la
        // declaracion de la funcion para poder calcular el offset de las variables para poder eliminarlas de la pila
        bI.add(new Instruccion(OpCode.MOVE, Size.L, PostIncremento.__(StackPointer.A7), Variables.STACK_TOP));
        return bI.toString();
    }
}
