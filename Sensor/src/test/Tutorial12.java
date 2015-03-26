package test;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import sensor.dataitems.DataItem;
import sensor.rdf.Prop;
import sensor.rdf.Uri4Rdf;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

public class Tutorial12 {
	public static void main(String[] args) throws IOException{
		DataItem di1 = new DataItem("00000001", "CO", "livingroom_1");
		DataItem di2 = new DataItem("00000002", "SO2", "bedroom_1");
		DataItem di3 = new DataItem("00000003", "temp", "bedroom_2");
		DataItem di4 = new DataItem("00000004", "temp", "studyroom_1");
		DataItem di5 = new DataItem("00000005", "temp", "kitchen_1");
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currTime = df.format(new Date());
		String[] curr = currTime.split(" ");
		currTime = curr[0]+"T"+curr[1];

		Model m1 = ModelFactory.createDefaultModel();

//		Resource ssRes = m1.createResource();
//		ssRes.addProperty(new PropertyImpl(Uri4Rdf.predURI + "sensorID"), di.getSensorID());
//		ssRes.addProperty(new PropertyImpl(Uri4Rdf.predURI + "locatedAt"), di.getLocation());
//		m1.add(new ResourceImpl(Uri4Rdf.obsURI+di.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "generatedBy"), ssRes);
		
		m1.add(new ResourceImpl(Uri4Rdf.sensorURI+di1.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "valueType"), di1.getName());
		m1.add(new ResourceImpl(Uri4Rdf.sensorURI+di1.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "hasValue"), m1.createTypedLiteral(di1.generateValue()));
		m1.add(new ResourceImpl(Uri4Rdf.sensorURI+di1.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "samplingTime"), m1.createTypedLiteral(currTime, XSDDatatype.XSDdateTime));
		
		m1.add(new ResourceImpl(Uri4Rdf.roomURI+di1.getLocation()), 
			new PropertyImpl(Uri4Rdf.predURI + "hasSensor"), new ResourceImpl(Uri4Rdf.sensorURI+di1.getSensorID()));
		m1.add(new ResourceImpl(Uri4Rdf.homeURI), 
			new PropertyImpl(Uri4Rdf.predURI + "hasRoom"), new ResourceImpl(Uri4Rdf.roomURI+di1.getLocation()));
		m1.setNsPrefix("p", Uri4Rdf.predURI);
		//m1.write(System.out, "RDF/XML");
		
		//System.out.println("\n------------------------------\n");
		
		Model m2 = ModelFactory.createDefaultModel();
		
		m2.add(new ResourceImpl(Uri4Rdf.sensorURI+di2.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "valueType"), di2.getName());
		m2.add(new ResourceImpl(Uri4Rdf.sensorURI+di2.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "hasValue"), m2.createTypedLiteral(di2.generateValue()));
		m2.add(new ResourceImpl(Uri4Rdf.sensorURI+di2.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "samplingTime"), m2.createTypedLiteral(currTime, XSDDatatype.XSDdateTime));
		
		m2.add(new ResourceImpl(Uri4Rdf.roomURI+di2.getLocation()), 
			new PropertyImpl(Uri4Rdf.predURI + "hasSensor"), new ResourceImpl(Uri4Rdf.sensorURI+di2.getSensorID()));
		m2.add(new ResourceImpl(Uri4Rdf.homeURI), 
			new PropertyImpl(Uri4Rdf.predURI + "hasRoom"), new ResourceImpl(Uri4Rdf.roomURI+di2.getLocation()));
		m2.setNsPrefix("p", Uri4Rdf.predURI);
		//m2.write(System.out, "RDF/XML");
		
		//System.out.println("\n------------------------------------\n");
		
		Model m3 = ModelFactory.createDefaultModel();
		
		m3.add(new ResourceImpl(Uri4Rdf.sensorURI+di3.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "valueType"), di3.getName());
		m3.add(new ResourceImpl(Uri4Rdf.sensorURI+di3.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "hasValue"), m3.createTypedLiteral(di3.generateValue()));
		m3.add(new ResourceImpl(Uri4Rdf.sensorURI+di3.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "samplingTime"), m3.createTypedLiteral(currTime, XSDDatatype.XSDdateTime));
		
		m3.add(new ResourceImpl(Uri4Rdf.roomURI+di3.getLocation()), 
			new PropertyImpl(Uri4Rdf.predURI + "hasSensor"), new ResourceImpl(Uri4Rdf.sensorURI+di3.getSensorID()));
		m3.add(new ResourceImpl(Uri4Rdf.homeURI), 
			new PropertyImpl(Uri4Rdf.predURI + "hasRoom"), new ResourceImpl(Uri4Rdf.roomURI+di3.getLocation()));
		m3.setNsPrefix("p", Uri4Rdf.predURI);
		
		
		
		Model m4 = ModelFactory.createDefaultModel();
		
		m4.add(new ResourceImpl(Uri4Rdf.sensorURI+di4.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "valueType"), di4.getName());
		m4.add(new ResourceImpl(Uri4Rdf.sensorURI+di4.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "hasValue"), m4.createTypedLiteral(di4.generateValue()));
		m4.add(new ResourceImpl(Uri4Rdf.sensorURI+di4.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "samplingTime"), m4.createTypedLiteral(currTime, XSDDatatype.XSDdateTime));
		
		m4.add(new ResourceImpl(Uri4Rdf.roomURI+di4.getLocation()), 
			new PropertyImpl(Uri4Rdf.predURI + "hasSensor"), new ResourceImpl(Uri4Rdf.sensorURI+di4.getSensorID()));
		m4.add(new ResourceImpl(Uri4Rdf.homeURI), 
			new PropertyImpl(Uri4Rdf.predURI + "hasRoom"), new ResourceImpl(Uri4Rdf.roomURI+di4.getLocation()));
		m4.setNsPrefix("p", Uri4Rdf.predURI);
		
		
		
		Model m5 = ModelFactory.createDefaultModel();
		
		m5.add(new ResourceImpl(Uri4Rdf.sensorURI+di5.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "valueType"), di5.getName());
		m5.add(new ResourceImpl(Uri4Rdf.sensorURI+di5.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "hasValue"), m5.createTypedLiteral(di5.generateValue()));
		m5.add(new ResourceImpl(Uri4Rdf.sensorURI+di5.getSensorID()), new PropertyImpl(Uri4Rdf.predURI + "samplingTime"), m5.createTypedLiteral(currTime, XSDDatatype.XSDdateTime));
		
		
		m5.add(new ResourceImpl(Uri4Rdf.roomURI+di5.getLocation()), 
			new PropertyImpl(Uri4Rdf.predURI + "hasSensor"), new ResourceImpl(Uri4Rdf.sensorURI+di5.getSensorID()));
		m5.add(new ResourceImpl(Uri4Rdf.homeURI), 
			new PropertyImpl(Uri4Rdf.predURI + "hasRoom"), new ResourceImpl(Uri4Rdf.roomURI+di5.getLocation()));
		m5.setNsPrefix("p", Uri4Rdf.predURI);
		
		
		Model mAll = m1.union(m2).union(m3).union(m4).union(m5);
		mAll.setNsPrefix("p", Uri4Rdf.predURI);
		mAll.write(System.out,"RDF/XML");
		
		//RDFDataMgr.write(System.out, mAll, RDFFormat.RDFXML);
		
		System.out.println("\n------------------------------------\n");
		
		String t = di2.getLocation();
		t="\""+t+"\"";

		// Create a new query
		String queryString = 
			"PREFIX p:<http://hem.org/predicate#> PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> SELECT ?room ?value WHERE { ?room p:hasSensor ?sensor . ?sensor p:hasValue ?value . ?sensor p:valueType 'CO' . FILTER (?value>=0.0) }";
		
//		String queryString = 
//		"PREFIX p:<http://hem.org/predicate#> "+
//		"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#> "+
//		"SELECT ?room ?type ?value ?time "+
//		"WHERE {"+
//		"?room p:hasSensor ?sensor . "+
//		"?sensor p:hasValue ?value . "+
//		"?sensor p:valueType ?type . "+
//		"?sensor p:samplingTime ?time . "+
//		"FILTER (?time >= '2014-05-26T17:00:00'^^xsd:dateTime) "+
//		"}";
		
		System.out.println(queryString);

		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, mAll);
		
		ResultSet rs = qe.execSelect();
		
//		System.out.println(rs.hasNext());
//		
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		ResultSetFormatter.outputAsCSV(bos, rs);
//		
//		System.out.println(bos.toString());
		
		String re = ResultSetFormatter.asText(rs);
		qe.close();
		    
		System.out.print(re);


//		StmtIterator iter = m1.listStatements(null,Prop.valueType,(RDFNode)null);
//		if (iter.hasNext()) {
//		    while (iter.hasNext()) {
//		        System.out.println(iter.next().getString());
//		    }
//		} else {
//		    System.out.println("No vcards were found in the database");
//		}
		
	}
}
