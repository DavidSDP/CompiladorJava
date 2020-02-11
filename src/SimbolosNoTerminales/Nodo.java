package SimbolosNoTerminales;

import java.util.List;

public interface Nodo {
//	Nodo getParent();
	List<Nodo> getChildren();
	String getName();
}
