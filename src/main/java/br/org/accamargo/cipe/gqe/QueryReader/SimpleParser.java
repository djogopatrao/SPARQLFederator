package br.org.accamargo.cipe.gqe.QueryReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

public class SimpleParser implements BaseParser {
	
	Logger  logger = Logger.getLogger( SimpleParser.class );
	
	@Override
	public ParsedQuery parse( String query, OntModel model, String defaultUri ) throws Exception {
		String[] queryArray = query.split("\\s+");
		if ( queryArray.length == 0 ) {
			throw new Exception("Query is empty!");
		}
		ParsedQuery result = new ParsedQuery();
		for( int i=0; i<queryArray.length; i++ ) {
			String tmp = queryArray[i];
			if ( ! tmp.startsWith("http://") ) {
				if ( defaultUri.isEmpty() ) {
					logger.warn("Absolute class name given ('"+tmp+"'), and no default URI provided!");
				}
				tmp = defaultUri + tmp;
			}
			result.addIntersectionClass( getClass( tmp, model ) );
		}
		return result;
	}
	
	public ParsedQuery parse( String query, OntModel model ) throws Exception {
		return parse( query, model, "" );
	}
	
	/**
	 * for each concept URI passed as string, create the corresponding OntClass (if any)
	 * @param model 
	 * 
	 * @param list 
	 * @return list
	 * @throws Exception 
	 */
	public OntClass getClass(String myClass, OntModel model) throws Exception {
		OntClass ontClass = model.getOntClass( myClass );
		if ( ontClass == null ) {
			throw new Exception("Class '"+myClass+"' not defined in ontology!");
		}
		return ontClass;
	}

}
