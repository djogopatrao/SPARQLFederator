package br.org.accamargo.cipe.gqe;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.org.accamargo.cipe.gqe.QueryReader.ParsedQuery;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpAsQuery;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;
import com.hp.hpl.jena.sparql.algebra.op.OpJoin;
import com.hp.hpl.jena.sparql.algebra.op.OpSequence;
import com.hp.hpl.jena.sparql.algebra.op.OpService;
import com.hp.hpl.jena.sparql.algebra.op.OpTriple;
import com.hp.hpl.jena.sparql.algebra.op.OpUnion;
import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class QueryExpander {

	OntModel model;
	private String ontocloudOntology;
	private String domainOntology;
	private String ontocloudNS;
	private String domainNS;
	private OntProperty rdfType;
	
	public String getOntocloudOntology() {
		return ontocloudOntology;
	}

	public String getDomainOntology() {
		return domainOntology;
	}

	public OntModel getModel() {
		return model;
	}

	public String getOntocloudNS() {
		return ontocloudNS;
	}

	public String getDomainNS() {
		return domainNS;
	}

	/**
	 * create the instance and initialize the ontology model
	 * 
	 * @TODO the namespace for ontocloud could be hardcoded, so we (1) save one parameter and (2) force the usage of the correct classes
	 * @param ontocloudOntology
	 * @param ocNS
	 * @param domainOntology
	 * @param dmNS
	 */
	public QueryExpander(String ontocloudOntology, String ocNS, String domainOntology, String dmNS ) {
		this.ontocloudOntology = ontocloudOntology;
		this.domainOntology = domainOntology;
		this.ontocloudNS = ocNS;
		this.domainNS = dmNS;
		
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		model.read(this.domainOntology, null);
		model.read(this.ontocloudOntology, null);
		
		// shortcuts
		this.rdfType = model.getOntProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
	}
	
	public String createQueryFromClasses( ParsedQuery list ) throws Exception {
		
		ArrayList<OntClass> resources = getResourcesFromString( list );
		
		// @TODO what is the correct way of treating this error?
		if ( resources == null )
			return "";
				
		return OpAsQuery.asQuery(Algebra.optimize(createPatternFromClasses( resources ))).toString();
	}

	/**
	 * this is the method that expands queries based on domain ontology axioms
	 * and which endpoints are they defined
	 * 
	 * TODO semantics should be on different classes - so it`s easier to add more of it
	 * TODO equivalence should be really easy to implement - however we'll need to check backwards.
	 * that is, if A AND B EQUIVALENT_TO C => when querying C we query A and B, and VICE VERSA,
	 * 
	 * @param resources - a list of classes (assume that this is C1 AND C2 AND ... Cn)
	 * @return OpSequence with SPARQL syntactic nodes corresponding to the desired expanded query
	 * @throws Exception
	 */
	private OpSequence createPatternFromClasses(ArrayList<OntClass> resources) throws Exception {
		OpSequence finalBP = OpSequence.create();
		
		Iterator<OntClass> it = resources.iterator();
		
		while( it.hasNext() ) {
			OntClass ontclass = it.next();
			ArrayList<Resource> endpoints = getEndpointsThatMap(ontclass);
			ArrayList<OntClass> subclasses = getDirectSubClasses(ontclass);

			// create an UNION querying all SERVICEs that implements the current class
			Iterator<Resource> endpointIterator = endpoints.iterator();

			Op currentNode = null;
			while( endpointIterator.hasNext() ) {
				
				OpService service = createServiceNode(endpointIterator.next(),ontclass);
				
				if ( currentNode == null ) {
					currentNode =  service;
				}
				else {
					currentNode = new OpUnion( service, currentNode );
				}
			}
			
			// now the inference semantics
			if ( subclasses != null ) {

				Iterator<OntClass> subclassesIterator = subclasses.iterator();
				
				while( subclassesIterator.hasNext()) {
					OntClass ontSubClass = subclassesIterator.next();					
					OpSequence  subclassSequence;
					
					if( ontSubClass.isIntersectionClass() ) {
						// A AND B subclassof C
						subclassSequence = createPatternFromClasses(getIntersectionClasses(ontSubClass));
					} else 
					if ( ontSubClass.isClass() ) {
						// A  subclassof C
						subclassSequence = createPatternFromClasses(ontSubClass);
					} else
					{
						throw new Exception("Can't deal with this class: "+ontSubClass.getRDFType());
					}
					
					if ( !subclassSequence.getElements().isEmpty() ) {
						if ( currentNode == null )
							currentNode = subclassSequence;
						else
							currentNode = new OpUnion( subclassSequence, currentNode );
					}
					
				}
			}				
			
			if ( currentNode != null )
				finalBP.add(currentNode);
		}
		return finalBP;
		
	}

	/**
	 * returns an OpService referencing specific endpoint with a triple
	 * following the format:
	 *
	 * ?pct rdf:type ontclass 
	 * 
	 * @param endpointURL
	 * @param ontclass
	 * @return
	 */
	private OpService createServiceNode(Resource endpointURL, OntClass ontclass) {
		return new OpService(
				endpointURL.asNode(),
				(Op) new OpTriple(new Triple(
						Var.alloc("pct"),
						this.rdfType.asNode(),
						ontclass.asNode()
						)
				), false );
		}

	/**
	 * return an array of classes comprising the intersection; if it's
	 * not a intersectionClass, returns null
	 * 
	 * @param ontSubClass
	 * @return
	 */
	private ArrayList<OntClass> getIntersectionClasses(OntClass ontSubClass) {
		
		if ( ! ontSubClass.isIntersectionClass() )
			return null;
		
		ExtendedIterator<? extends OntClass> opList = ontSubClass.asIntersectionClass().listOperands();
		
		ArrayList <OntClass> tmp1 = new ArrayList<OntClass>();
		while( opList.hasNext() ) {
			tmp1.add( opList.next() );
		}
		
		return tmp1;
	}

	private OpSequence createPatternFromClasses(OntClass ontSubClass) throws Exception {
		
		ArrayList<OntClass> tmp = new ArrayList<OntClass>();
		tmp.add(ontSubClass);
		
		return createPatternFromClasses(tmp);
		
	}

	/**
	 * return a list of Resources, one for each endpoint implementing
	 * the specified class
	 * 
	 * 
	 * @param ontclass class
	 * @return list of resources mapping that class
	 */
	public ArrayList<Resource> getEndpointsThatMap(OntClass ontclass) {
		String queryString = "SELECT ?system ?endpoint { ?system <"+this.ontocloudNS+"systemImplementsClass> <"+ontclass.getURI()+">; <"+this.ontocloudNS+"systemHasSparqlEndpoint> ?endpoint. }" ;
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,model);
		ResultSet resultset = qexec.execSelect();
		
		ArrayList<Resource> my_result = new ArrayList<Resource>();
		
		while( resultset.hasNext() ) {
			QuerySolution result = resultset.nextSolution();
			
			Resource r1 = result.getResource("system");
			Resource r2 = result.getResource("endpoint");

			my_result.add(r2);
		}	
		return my_result;
	}

	public ArrayList<OntClass> getDirectSubClasses(OntClass ontclass) {
		ExtendedIterator<OntClass> list = ontclass.listSubClasses(true);
		ArrayList<OntClass> tmp = new ArrayList<OntClass>();
		while( list.hasNext() ) {
			tmp.add(list.next());
		}
		return tmp;
		
	}
	

	/**
	 * for each concept URI passed as string, create the corresponding OntClass (if any)
	 * 
	 * @param list 
	 * @return list
	 */
	public ArrayList<OntClass> getResourcesFromString(ParsedQuery list) {
		ArrayList<OntClass> return_values = new ArrayList<OntClass>();
		Iterator<String> it = list.iterator();
		while ( it.hasNext() ) {
			String class_name = it.next();
			OntClass ontClass = model.getOntClass( this.domainNS + class_name );
			if ( ontClass != null )
				return_values.add(ontClass);
		}
		return return_values;
	}
	
	

	

}
