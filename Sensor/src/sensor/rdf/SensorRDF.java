package sensor.rdf;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import sensor.dataitems.*;

public class SensorRDF {
	public static Model generateRDFModel(DataItem di) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currTime = df.format(new Date());
		
		String newSensorId = di.getSensorID()+"@"+currTime;
		// 生成传感器的RDF model
		Model m = ModelFactory.createDefaultModel();

		m.add(new ResourceImpl(Uri4Rdf.sensorURI+newSensorId), new PropertyImpl(Uri4Rdf.predURI + "valueType"), di.getName());
		m.add(new ResourceImpl(Uri4Rdf.sensorURI+newSensorId), new PropertyImpl(Uri4Rdf.predURI + "hasValue"), m.createTypedLiteral(di.generateValue()));
		m.add(new ResourceImpl(Uri4Rdf.sensorURI+newSensorId), new PropertyImpl(Uri4Rdf.predURI + "samplingTime"), currTime);
		
		m.add(new ResourceImpl(Uri4Rdf.roomURI+di.getLocation()), 
			new PropertyImpl(Uri4Rdf.predURI + "hasSensor"), new ResourceImpl(Uri4Rdf.sensorURI+newSensorId));
		m.add(new ResourceImpl(Uri4Rdf.homeURI), 
			new PropertyImpl(Uri4Rdf.predURI + "hasRoom"), new ResourceImpl(Uri4Rdf.roomURI+di.getLocation()));

		m.setNsPrefix("p", Uri4Rdf.predURI);
		

		return m;
	}
}
