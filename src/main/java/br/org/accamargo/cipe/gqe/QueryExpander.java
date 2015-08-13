package br.org.accamargo.cipe.gqe;


import java.util.ArrayList;
import java.util.Iterator;

import br.org.accamargo.cipe.gqe.QueryReader.BaseParser;
import br.org.accamargo.cipe.gqe.QueryReader.ParsedQuery;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
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
	
	public QueryExpander( OntModel model, String ocNS, String dmNS ) {
		this.model = model;
		this.ontocloudNS = ocNS;
		this.domainNS = dmNS;

		// shortcuts
		this.rdfType = model.getOntProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
	}
	
	public String createQueryFromClasses( BaseParser queryParser, String queryString ) throws Exception {
		
		ParsedQuery parsedQuery = queryParser.parse(queryString,model);
		ArrayList<OntClass> resources = parsedQuery.getArrayList();
		
		// @TODO what is the correct way of treating this error?
		if ( resources == null )
			return "";
				
		return OpAsQuery.asQuery(Algebra.optimize(createPatternFromClasses( resources ))).toString();
	}
	private OpSequence createPatternFromClasses(ArrayList<OntClass> resources ) throws Exception {
		return createPatternFromClasses( resources, Var.alloc("pct") );
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
	 * @param baseVariable 
	 * @return OpSequence with SPARQL syntactic nodes corresponding to the desired expanded query
	 * @throws Exception
	 */
	private OpSequence createPatternFromClasses(ArrayList<OntClass> resources, Var baseVariable) throws Exception {
		OpSequence finalBP = OpSequence.create();
		
		Iterator<OntClass> it = resources.iterator();
		OntProperty ontprop;
		
		System.out.println("Base variable "+baseVariable.toString());
		
		while( it.hasNext() ) {
			OntClass ontclass = it.next();
			ontprop = this.rdfType;
			Op currentNode = null;
			Var variable = baseVariable;

			ArrayList<OntClass> subclasses = getDirectSubClasses(ontclass);

			if ( ontclass.isRestriction() ) {
				
				Var auxVariable = Var.alloc(createTemporaryVariableName());

				ontprop = ((Restriction)ontclass).getOnProperty();
				ArrayList<Resource> endpoints = getEndpointsThatMap((OntClass) ((SomeValuesFromRestriction)ontclass).getSomeValuesFrom());
				
				// create an UNION querying all SERVICEs that implements the current class
				Iterator<Resource> endpointIterator = endpoints.iterator();
	
				while( endpointIterator.hasNext() ) {
					Resource i = endpointIterator.next();
					
					OpService service = createServiceNodeFromRestriction( i, (SomeValuesFromRestriction)ontclass, variable, auxVariable );
					
					if ( currentNode == null ) {
						currentNode =  service;
					}
					else {
						currentNode = new OpUnion( service, currentNode );
					}
				}
				ontclass = (OntClass)((SomeValuesFromRestriction)ontclass).getSomeValuesFrom();

				finalBP.add( currentNode );
				finalBP.add( createPatternFromClasses( ontclass, auxVariable ) );
				
			} else {
			
				System.out.println("Variable is "+variable.toString());
				
				ArrayList<Resource> endpoints = getEndpointsThatMap(ontclass);
	
				// create an UNION querying all SERVICEs that implements the current class
				Iterator<Resource> endpointIterator = endpoints.iterator();
	
				System.out.println("Getting endpoints for "+ontclass.getURI());
				while( endpointIterator.hasNext() ) {
					Resource i = endpointIterator.next();
					
					OpService service = createServiceNode( i, ontclass, ontprop, variable );
					
					if ( currentNode == null ) {
						currentNode =  service;
					}
					else {
						currentNode = new OpUnion( service, currentNode );
					}
				}
				
				// now the inference semantics
				System.out.println("Getting subclasses of "+ontclass.getURI());
				if ( subclasses != null ) {
	
					Iterator<OntClass> subclassesIterator = subclasses.iterator();
					
					while( subclassesIterator.hasNext()) {
						OntClass ontSubClass = subclassesIterator.next();
						System.out.println(">>>" + ontSubClass.getURI());
						OpSequence  subclassSequence;
	
	// rethink
	//					if( ontSubClass.isRestriction() ) {
	//						// (prop some C) subclassof C
	//						subclassSequence = createPatternFromRestriction((SomeValuesFromRestriction)ontSubClass,variable);
	//					} else 					
						if( ontSubClass.isIntersectionClass() ) {
							// A AND B subclassof C
							subclassSequence = createPatternFromClasses(getIntersectionClasses(ontSubClass),variable);
						} else 
						if ( ontSubClass.isClass() ) {
							// A  subclassof C
							subclassSequence = createPatternFromClasses(ontSubClass,variable);
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
						
					}// end while
				}// end if subclasses
				if ( currentNode != null ) {
					finalBP.add(currentNode);
				}
			}
			
		}
		return finalBP;
		
	}

	private OpSequence createPatternFromRestriction(SomeValuesFromRestriction ontSubClass, Var variable, Var auxVariable) {
		BasicPattern b = new BasicPattern();

		b.add( (new Triple(
				variable,
				ontSubClass.getOnProperty().asNode(),
				auxVariable)
		));
		
		OpSequence seq = OpSequence.create();
		seq.add(new OpBGP(b));
		return seq;
	}
	
	private int temporaryVariableCount =0;
	private String createTemporaryVariableName() {
		return "tmp"+this.temporaryVariableCount++;
	}

	/**
	 * returns an OpService referencing specific endpoint with a triple
	 * following the format:
	 *
	 * ?pct rdf:type ontclass 
	 * 
	 * @param endpointURL
	 * @param ontclass
	 * @param variable 
	 * @return
	 */
	private OpService createServiceNode(Resource endpointURL, OntClass ontclass, OntProperty prop, Var variable) {
		return new OpService(
				endpointURL.asNode(),
				(Op) new OpTriple(new Triple(
						variable,
						prop.asNode(),
						ontclass.asNode()
						)
				), false );
		}
	
	private OpService createServiceNodeFromRestriction( Resource endpointURL, SomeValuesFromRestriction ontSubClass, Var variable, Var auxVariable ) {
		return new OpService(
				endpointURL.asNode(),
				createPatternFromRestriction(ontSubClass, variable, auxVariable),
				false );
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

	private OpSequence createPatternFromClasses(OntClass ontSubClass, Var baseVariable) throws Exception {
		
		ArrayList<OntClass> tmp = new ArrayList<OntClass>();
		tmp.add(ontSubClass);
		
		return createPatternFromClasses(tmp,baseVariable);
		
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

	/**
	 * bug #14 - it should be getting subclasses of ( someValuesFrom G ) 
	 * @param ontclass
	 * @return
	 */
	public ArrayList<OntClass> getDirectSubClasses(OntClass ontclass) {
		ExtendedIterator<OntClass> list = ontclass.listSubClasses(false);
		ArrayList<OntClass> tmp = new ArrayList<OntClass>();
		System.out.println("getting subclasses for " + ontclass.getClass().toString() );
		while( list.hasNext() ) {
			OntClass t = list.next();
			System.out.println("\t" + t.toString() );
			tmp.add(t);
		}
		return tmp;
		
	}	

}
