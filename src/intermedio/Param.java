package intermedio;

import Procesador.Declaracion;

public class Param extends InstruccionTresDirecciones {

    private boolean isLast;

    public Param(Operando primero) {
        super(OperacionTresDirecciones.PARAM);
        this.primero = primero;
    }

    public void markLastParam() {
        this.isLast = true;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();
        Declaracion valor = this.primero.getValor();

        sb.append(super.toMachineCode());

        if (this.isLast) {
            sb.append("; Aqui va el codigo necesario para adelantar BP\n");
        }

        sb.append(this.primero.load("D0")) // D0 me da igual, solo me interesa A6
                .append("\tPUSH_PARAM ").append(valor.getDesplazamiento()).append("(A6), #").append(valor.getOcupacion()).append("\n");

        return sb.toString();
    }
}
