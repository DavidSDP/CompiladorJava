package optimizacion;

class RangoInstruccionesFuncion {
    // Indice a la primera y última instrucción de una funcion
    // Ambas instrucciones inclusive
    private int inicio, fin;

    public RangoInstruccionesFuncion(int inicio, int fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFin() {
        return fin;
    }
    
    @Override
    public String toString() {
    	return " funci�n -> inicio:" + inicio + ", fin:" + fin + " <- ";
    }
    
}