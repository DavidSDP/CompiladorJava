package optimizacion;

import intermedio.BloqueBasico;
import intermedio.InstruccionTresDirecciones;

class Invariante {
    private InstruccionTresDirecciones instruccion;
    private BloqueBasico bloque;

    public Invariante(InstruccionTresDirecciones instruccion, BloqueBasico bloque) {
        this.instruccion = instruccion;
        this.bloque = bloque;
    }

    public InstruccionTresDirecciones getInstruccion() {
        return this.instruccion;
    }

    public BloqueBasico getBloque() {
        return this.bloque;
    }
}