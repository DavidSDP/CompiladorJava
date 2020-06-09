package intermedio;

public class BloqueBasico {
    private int id, inicio, fin;
    private Boolean es_E, es_S;

    public BloqueBasico(int id, int inicio, int fin) {
        this(id, inicio);
        this.fin = fin;
    }

    public BloqueBasico(int id) {
        this(id, -1);
    }

    public BloqueBasico(int id, int inicio) {
        this.id = id;
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

    @Override
    public int hashCode() {
        return inicio;
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
        	return "-> E id:"+id+" desde:"+inicio+" hasta:"+fin+" <-";
    	}
    	if(es_S) {
        	return "-> S id:"+id+" desde:"+inicio+" hasta:"+fin+" <-";
    	}
    	return "-> B"+id+" desde:"+inicio+" hasta:"+fin+" <-";
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

}
