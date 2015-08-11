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
		OntProperty ontprop;
		
		while( it.hasNext() ) {
			OntClass ontclass = it.next();
			ontprop = this.rdfType;
			Op currentNode = null;
			
			ArrayList<OntClass> subclasses = getDirectSubClasses(ontclass);

			if ( ontclass.isRestriction() ) {
				ontprop = ((Restriction)ontclass).getOnProperty();
				ArrayList<Resource> endpoints = getEndpointsThatMap((OntClass) ((SomeValuesFromRestriction)ontclass).getSomeValuesFrom());
				
				// create an UNION querying all SERVICEs that implements the current class
				Iterator<Resource> endpointIterator = endpoints.iterator();
	
				while( endpointIterator.hasNext() ) {
					Resource i = endpointIterator.next();
					
					OpService service = createServiceNodeFromRestriction( i, (SomeValuesFromRestriction)ontclass );
					
					if ( currentNode == null ) {
						currentNode =  service;
					}
					else {
						currentNode = new OpUnion( service, currentNode );
					}
				}
				
			} else if ( ontclass.isClass() ) {
				ArrayList<Resource> endpoints = getEndpointsThatMap(ontclass);
	
				// create an UNION querying all SERVICEs that implements the current class
				Iterator<Resource> endpointIterator = endpoints.iterator();
	
				while( endpointIterator.hasNext() ) {
					Resource i = endpointIterator.next();
					
					OpService service = createServiceNode( i, ontclass, ontprop );
					
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
						System.out.println(">>>" + ontSubClass.getURI());
						OpSequence  subclassSequence;
						
						if( ontSubClass.isRestriction() ) {
							// (prop some C) subclassof C
							subclassSequence = createPatternFromRestriction((SomeValuesFromRestriction)ontSubClass);
						} else 
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
						
					}// end while
				}// end if subclasses
			}// end if negócio
			if ( currentNode != null ) {
				finalBP.add(currentNode);
			}
			
		}
		return finalBP;
		
	}

	private OpSequence createPatternFromRestriction(SomeValuesFromRestriction ontSubClass) {
		BasicPattern b = new BasicPattern();

		Var tmp = Var.alloc(createTemporaryVariableName());
		
		b.add( (new Triple(
				Var.alloc("pct"),
				ontSubClass.getOnProperty().asNode(),
				tmp)
		));
		
		Op glump = new OpTriple( new Triple(
				tmp,
				this.rdfType.asNode(),
				ontSubClass.getSomeValuesFrom().asNode()
				));
		
		OntClass x= (OntClass) ontSubClass.getSomeValuesFrom();
		Iterator<OntClass> subclasses = getDirectSubClasses(x).iterator();
		while( subclasses.hasNext() ) {
			glump = new OpUnion( glump, new OpTriple(new Triple(
					tmp,
					this.rdfType.asNode(),
					subclasses.next().asNode()
					)) );			
		}
		
		OpSequence seq = OpSequence.create();
		seq.add(new OpBGP(b));
		seq.add( glump );
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
	 * @return
	 */
	private OpService createServiceNode(Resource endpointURL, OntClass ontclass, OntProperty prop) {
		return new OpService(
				endpointURL.asNode(),
				(Op) new OpTriple(new Triple(
						Var.alloc("pct"),
						prop.asNode(),
						ontclass.asNode()
						)
				), false );
		}
	
	private OpService createServiceNodeFromRestriction( Resource endpointURL, SomeValuesFromRestriction ontSubClass ) {
		return new OpService(
				endpointURL.asNode(),
				createPatternFromRestriction(ontSubClass),
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
