package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;

public class ParsedQuery {

	private List <OntClass> data;
		
	public ParsedQuery() {
		super();
		data = new ArrayList<OntClass>();
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
	
	public String toString() {
		Iterator<OntClass> array = data.iterator();
		ArrayList <String> a = new ArrayList<String>();
		while( array.hasNext() ) {
			OntClass i = array.next();
			if ( i.isRestriction() ) {
				SomeValuesFromRestriction r = (SomeValuesFromRestriction)i;
				System.out.println(r.getOnProperty().getURI());
				System.out.println( "=>  some " );
				System.out.println( r.getSomeValuesFrom().getURI());
				a.add( r.getOnProperty().getURI() + " some " + r.getSomeValuesFrom().getURI() );
			} else if ( i.isClass() ) {
				System.out.println("=> class "+i.getURI());
				a.add(i.getURI());
			} else {
				System.out.println("=> cant parse!");
				a.add("<Cant parse this expression!>");
			}
		}
		return StringUtils.join( a.iterator(), " and ");
	}



}
