package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

public class ParsedQuery {

	private List <OntClass> data;
		
	public ParsedQuery() {
		super();
	}

	public void addIntersectionClass(OntClass myClass) {
		data.add(myClass);
	}

	public ArrayList<OntClass> getArrayList() {
		ArrayList<OntClass> result = new ArrayList<OntClass>();
		Iterator<OntClass> i = data.iterator();
		while( i.hasNext() ) {
			result.add( i.next() );
		}
		return result;
	}



}
