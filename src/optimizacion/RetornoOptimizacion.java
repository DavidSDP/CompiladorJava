package optimizacion;

import java.util.List;

import intermedio.InstruccionTresDirecciones;

public class RetornoOptimizacion {
    private List<InstruccionTresDirecciones> instrucciones;
    private boolean cambiado;

    public RetornoOptimizacion(List<InstruccionTresDirecciones> instrucciones, boolean cambiado) {
        this.instrucciones = instrucciones;
        this.cambiado = cambiado;
    }

    public List<InstruccionTresDirecciones> getInstrucciones() {
        return instrucciones;
    }

    public boolean isCambiado() {
        return cambiado;
    }
}
