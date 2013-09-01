package br.org.accamargo.cipe.gqe;


import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.AllDifferent;
import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.DataRange;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.EnumeratedClass;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.ontology.Profile;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;
import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		String owlPath = args[0];// "file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/ontocloud2full_ontop.owl";
		String owlPath1 = args[1];//"file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/20130713_estudo_clinico.owl";
		String ecNS = args[2];//"http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#";
		String oc2NS =args[3];// "http://www.cipe.accamargo.org.br/ontologias/ontocloud2.owl#";
		
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		OntDocumentManager dm = model.getDocumentManager();
		
		model.read(owlPath, null);
		model.read(owlPath1, null);


		// listar subclasses imediatas de "Tratamento"
		OntClass tratamento = model.getOntClass(ecNS+"Tratamento");
		Iterator<OntClass> it = tratamento.listSubClasses(true); // somente subclasses imediatas
		while (it.hasNext()) {
			OntClass ontclass = it.next();
			System.out.println(ontclass.getLocalName());
		}
		
		// listar quais endpoints implementam o conceito "C50"
		OntClass c50 = model.getOntClass(ecNS+"C50");
		String queryString = "SELECT ?system ?endpoint { ?system <"+oc2NS+"systemImplementsClass> <"+ecNS+"C50.1>; <"+oc2NS+"systemHasSparqlEndpoint> ?endpoint. }" ;
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query,model);
		ResultSet resultset = qexec.execSelect();
		while( resultset.hasNext() ) {
			QuerySolution result = resultset.nextSolution();
			
			Resource r1 = result.getResource("system");
			Resource r2 = result.getResource("endpoint");
			System.out.println(r1.getURI());
			System.out.println(r2.getURI());
		}

		// listar subclasses imediatas de "CriterioTriagem", cuidando das classes de intersecção
		OntClass criterioTriagem = model.getOntClass(ecNS+"CriterioTriagem");
		Iterator<OntClass> it2 = criterioTriagem.listSubClasses(true); // somente subclasses imediatas
		while (it2.hasNext()) {
			OntClass ontclass = it2.next();
			if ( ontclass.isIntersectionClass() ) {
				ArrayList<String> tmp = new ArrayList<String>();
				IntersectionClass ic = ontclass.asIntersectionClass();
				ExtendedIterator<? extends OntClass> rdftypes = ic.listOperands();
				
				while( rdftypes.hasNext() ) {
					Resource rdftype = rdftypes.next();
					tmp.add(rdftype.getURI());
				}
				System.out.println("Class Expression(" + tmp.toString() + ")");
			}
			else if ( ontclass.isClass() ) {
				System.out.println("Classe Simples: "+ontclass.getLocalName());
			} else {
				throw new Exception("Não sei lidar com isso!" + ontclass.getRDFType(true) );
			}
		}
	}
	
	public static int teste() {
		return 1;
	}

}
