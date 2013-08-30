package br.org.accamargo.cipe.gqe;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.xerces.impl.xs.identity.Selector.Matcher;
import org.junit.Test;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpAsQuery;
import com.hp.hpl.jena.sparql.algebra.Transformer;
import com.hp.hpl.jena.util.URIref;

public class GQETest {

	private GrumpyQueryExpander gqe;
	
	public GQETest() {
		String ontocloudOntology = "file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/ontocloud2full_ontop.owl";
		String ocNS = "http://www.cipe.accamargo.org.br/ontologias/ontocloud2.owl#";
		String domainOntology = "file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/20130713_estudo_clinico.owl";
		String dmNS = "http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#";
		
		gqe = new GrumpyQueryExpander(ontocloudOntology, ocNS, domainOntology, dmNS);	
	}
	
	//@Test
	public void testConceptCreation() {
		// testa criação de conceitos
		ArrayList<OntClass> actuals = gqe.getResourcesFromString( Arrays.asList( "C50", "Tratamento" ) );
		ArrayList<OntClass> expected = new ArrayList<OntClass>();
		expected.add( gqe.getModel().createClass(gqe.getDomainNS() + "C50" ) );
		expected.add( gqe.getModel().createClass(gqe.getDomainNS() + "Tratamento" ) );
		org.junit.Assert.assertArrayEquals(expected.toArray(), actuals.toArray());
	}
	
	//@Test
	public void testQueryCreation() throws Exception {
		// testa criação de query
		System.out.println("C50 e Quimio-----------");
		String query = gqe.createQueryFromClasses(Arrays.asList("C50","Quimioterapia"));
		System.out.println(query);
		//@TODO teste?
	}
	
	//@Test
	public void testEndpoint() {
		// testa obtenção de endpoints
		ArrayList<Resource> actuals = gqe.getEndpointsThatMap(gqe.getModel().createClass(gqe.getDomainNS() + "C50.1") );
		ArrayList<Resource> expected = new ArrayList<Resource>();
		expected.add(ResourceFactory.createResource("http://fisher:8080/openrdf-sesame/repositories/rhc"));
		expected.add(ResourceFactory.createResource("http://fisher:8080/openrdf-sesame/repositories/mv_repl"));
		org.junit.Assert.assertArrayEquals(expected.toArray(), actuals.toArray());
	}
	
	//@Test
	public void testDirectSubclasses() {
		ArrayList<OntClass> actuals = gqe.getDirectSubClasses(gqe.getModel().createClass(gqe.getDomainNS() + "C50") );
		System.out.println(actuals.toString());
	}
	
	//@Test
	public void testOptimizer() {
		GrumpyOptimizer go = new GrumpyOptimizer();
		System.out.println("Teste optimizer");
		
		Query query = QueryFactory.create("SELECT ?a {" +
				"{ SERVICE <http://1.com/> { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marafo.com/teste1> } }" +
				"UNION " +
				"{ SERVICE <http://3.com/> { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marafo.com/teste2> } }" +
				"UNION " +
				"{ SERVICE <http://2.com/> { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marafo.com/teste3> } }" +
				"UNION " +
				"{ SERVICE <http://2.com/> { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marafo.com/teste4> } }" +
				"UNION " +
				"{ SERVICE <http://3.com/> { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marafo.com/teste5> } }" +
				"UNION " +
				"{ SERVICE <http://1.com/> { ?a <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marafo.com/chatuba> } } }" );

		Op op = Algebra.compile(query);
		System.out.println(op);		
		
		op = Transformer.transform(go, op);
		op = Transformer.transform(go, op);
		System.out.println(op);
		//@TODO test
		
	}
	
	@Test
	public void testEverything() throws Exception {
		
		GrumpyOptimizer go = new GrumpyOptimizer();
		
		System.out.println("Critério Triagem-----------");
		String query2 = gqe.createQueryFromClasses(Arrays.asList("CriterioTriagem"));
		System.out.println(query2);
		Op op = Algebra.compile(QueryFactory.create(query2));
		
		for(int i=0; i<1000; i ++ ) 
			op = Transformer.transform(go, op );
		
		String optimized_query = OpAsQuery.asQuery(op).toString(); 
		System.out.println(optimized_query);
		
		System.out.println("Size query original:"+query2.length());
		System.out.println("Size query otimizada:"+optimized_query.length());
		
		Pattern p = Pattern.compile("SERVICE");
		java.util.regex.Matcher m_query = p.matcher(query2);
		java.util.regex.Matcher m_query_opt = p.matcher(optimized_query);
		
		int ct=0;
		while( m_query.find()) ct++;
		System.out.println("Query original SERVICES: " + ct);
		
		ct=0;
		while( m_query_opt.find()) ct++;
		System.out.println("Query optimizada SERVICES: " + ct);
	}
}
