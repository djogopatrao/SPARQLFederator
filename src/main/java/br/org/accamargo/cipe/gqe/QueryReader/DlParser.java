package br.org.accamargo.cipe.gqe.QueryReader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;

public class DlParser implements BaseParser {

	Logger  logger = Logger.getLogger( DlParser.class );
	private OntModel model;

	@Override
	public ParsedQuery parse(String query, OntModel model) throws Exception {
		return parse(query,model,"");
	}

	@Override
	public ParsedQuery parse(String query, OntModel model, String defaultUri)
			throws Exception {
		
		setModel(model);
		
		Iterator<String> queryElements = Arrays.asList(query.split("\\s+")).iterator();
	
		String state = "parseStateInit"; // accept class expression
		
		ParsedQuery result = new ParsedQuery();
		while( queryElements.hasNext() ) {

			Class [] param = new Class[2];
			param[0] = Iterator.class;
			param[1] = ParsedQuery.class;
			System.out.println("Parser in state "+state);
			Method stateMethod = this.getClass().getDeclaredMethod(state, param);
			state = (String) stateMethod.invoke(this, queryElements, result);
				
		}
		if ( state != "parseStateInit" && state != "parseStateLogicalOperator" ) {
			throw new Exception("Parser ended on '"+state+"' state - incomplete input");
		}
		return result;
	}
	
	private void setModel(OntModel model) {
		this.model = model;
		
	}
	
	private String parseStateInit(Iterator<String> queryElements, ParsedQuery result ) throws Exception {
		String tmp = queryElements.next();
		String state = "";
		System.out.println("Evaluating token '"+tmp+"'");
		if ( isClass(tmp) ) {
			System.out.println("Class: '"+tmp+"' => adding to results");
			result.addIntersectionClass( getClass( tmp ) );
			System.out.println("Done");
			state = "parseStateLogicalOperator";
		} else
			if ( isObjectProperty(tmp) )
		{
			parseProperty( tmp, queryElements, result );
			state = "parseStateLogicalOperator";
		} else {
			throw new Exception("Don't know what the token "+tmp+" is");
		}
		return state;
	}

	private void parseProperty(String tmp, Iterator<String> queryElements,
			ParsedQuery result) throws Exception {
		System.out.println("Property: '"+tmp+"' ");
		String modifier = queryElements.next();
		ObjectProperty property = getObjectProperty(tmp);
		if ( modifier.equalsIgnoreCase("some") ) {
			// expects a class
			String className = queryElements.next();
			if ( !isClass(className) ) {
				throw new Exception("Expecting a class name after "+tmp+" "+modifier);
			}
			OntClass myClass = getClass(className);
			SomeValuesFromRestriction s = model.createSomeValuesFromRestriction(null, property, myClass);
			result.addIntersectionClass(s);
		} else {
			throw new Exception("Unimplemented modifier '"+modifier+"'");
		}
	}

	private String parseStateLogicalOperator(Iterator<String> queryElements, ParsedQuery result ) throws Exception {
		String tmp = queryElements.next();
		String state = "";
		if ( tmp.equalsIgnoreCase("and") ) {
			state = "parseStateInit";
		} else {
			throw new Exception("Unimplemented operator '"+tmp+"'");
		}
		return state;
	}

	/**
	 * for each concept URI passed as string, create the corresponding OntClass (if any)
	 * @param model 
	 * 
	 * @param list 
	 * @return list
	 * @throws Exception 
	 */
	private OntClass getClass(String myClass) throws Exception {
		OntClass ontClass = model.getOntClass( myClass );
		if ( ontClass == null ) {
			throw new Exception("Class '"+myClass+"' not defined in ontology!");
		}
		System.out.println("getClass:"+ontClass.toString());
		return ontClass;
	}
	
	private boolean isClass( String myClass ){
		return ( model.getOntClass( myClass ) != null );
	}
	
	private boolean isObjectProperty( String myProperty ) {
		return model.getObjectProperty(myProperty) != null;
	}

	private ObjectProperty getObjectProperty( String myProperty ) throws Exception {
		ObjectProperty prop = model.getObjectProperty(myProperty);
		if ( prop == null ) {
			throw new Exception("Property '"+myProperty+"' is not defined");
		}
		return prop;
	}

}
