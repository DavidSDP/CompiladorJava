package intermedio;

public class BloqueBasico {
    private static int ID_SEQ = 0;
    private int id, inicio, fin;
    private Boolean es_E, es_S;

    public BloqueBasico(int inicio, int fin) {
        this(inicio);
        this.fin = fin;
    }

    public BloqueBasico() {
        this(-1);
    }

    public BloqueBasico(int inicio) {
        this.id = ID_SEQ++;
        this.inicio = inicio;
        this.fin = -1;
        this.es_E = false;
        this.es_S = false;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public void aplicarDesplazamiento(int numInstrucciones) {
        this.inicio += numInstrucciones;
        this.fin += numInstrucciones;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(!(obj instanceof BloqueBasico))
    		return false;
    	BloqueBasico b = (BloqueBasico) obj;
    	return (id == b.id) && (inicio == b.inicio) && (fin == b.fin);
    }
    
    @Override
    public String toString() {
    	if(es_E) {
        	return "-> E id:"+id+" ["+inicio+", "+fin+"] <-";
    	}
    	if(es_S) {
        	return "-> S id:"+id+" ["+inicio+", "+fin+"] <-";
    	}
    	return "-> B"+id+" ["+inicio+", "+fin+"] <-";
    }

	public Boolean getEs_E() {
		return es_E;
	}

	public void setEs_E(Boolean es_E) {
		this.es_E = es_E;
	}

	public Boolean getEs_S() {
		return es_S;
	}

	public void setEs_S(Boolean es_S) {
		this.es_S = es_S;
	}

    public int getId() {
        return this.id;
    }
}
