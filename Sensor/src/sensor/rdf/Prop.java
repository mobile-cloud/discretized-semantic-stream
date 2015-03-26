package sensor.rdf;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;

public class Prop {
	public static PropertyImpl hasValue = new PropertyImpl(Uri4Rdf.predURI+"hasValue");
	
	public static PropertyImpl valueType = new PropertyImpl(Uri4Rdf.predURI+"valueType");
	
	public static PropertyImpl samplingTime = new PropertyImpl(Uri4Rdf.predURI+"samplingTime");
}
