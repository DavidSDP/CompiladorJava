package optimizacion;

import java.util.ArrayList;

import intermedio.InstruccionTresDirecciones;

public class RetornoOptimizacion {
    private ArrayList<InstruccionTresDirecciones> instrucciones;
    private boolean cambiado;

    public RetornoOptimizacion(ArrayList<InstruccionTresDirecciones> instrucciones, boolean cambiado) {
        this.instrucciones = instrucciones;
        this.cambiado = cambiado;
    }

    public ArrayList<InstruccionTresDirecciones> getInstrucciones() {
        return instrucciones;
    }

    public boolean isCambiado() {
        return cambiado;
    }
}
