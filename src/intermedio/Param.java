package intermedio;

import Procesador.Declaracion;

public class Param extends InstruccionTresDirecciones {
    public Param(Operando primero) {
        super(OperacionTresDirecciones.PARAM);
        this.primero = primero;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        Declaracion valor = this.primero.getValor();

        sb.append(super.toMachineCode());

        // TODO Aqui se tiene que mirar si el parametro es ultimo el ultimo o no para
        //  anadir codigo que adelante el BP y que evite solapamientos de memoria

        sb.append(this.primero.load("D0")) // D0 me da igual, solo me interesa A6
                .append("\tPUSH_PARAM ").append(valor.getDesplazamiento()).append("(A6), #").append(valor.getOcupacion()).append("\n");

        return sb.toString();
    }
}
