package br.org.accamargo.cipe.gqe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpAsQuery;
import com.hp.hpl.jena.sparql.algebra.Transformer;

public class SPARQLFederator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		if ( args.length != 5 ) {
			System.out.println("Usage: SPARQLFederator <federation_ontology.owl> <domain_ontology.owl> <federation namespace> <domain namespace> <Class 1> ...");
			return;
		}

        String ontocloudOntology = args[0];
        String ocNS = args[1];
        String domainOntology = args[2];
        String dmNS = args[3];
        
        List<String> classes_array = new ArrayList<String>();
        for( int i = 4; i<args.length;i++) {
        	classes_array.add(args[i]);
        }
        
        QueryExpander gqe = new QueryExpander(ontocloudOntology, ocNS, domainOntology, dmNS); 
        QueryOptimizer go = new QueryOptimizer();
        QueryPlanner gp = new QueryPlanner();
        
        // plan statistics
        // TODO get it from a file
        // TODO profile it
        StaticCosts.setCosts(gp,dmNS);

        // create query
        String query2 = gqe.createQueryFromClasses(classes_array);
 //       System.out.println(query2);
        Op op = Algebra.compile(QueryFactory.create(query2));
        
        // optimize 
        // TODO how many times are enough? 
        // TODO an option to set optimization
        for(int i=0; i<1000; i ++ ) 
                op = Transformer.transform(go, op );
        
//        String optimized_query = OpAsQuery.asQuery(op).toString(); 
//        System.out.println(optimized_query);
        
        // plan execution
        // TODO how many times are enough? 
        // TODO an option to set planning
        for (int i=0; i<1000;i ++)
        	op = Transformer.transform(gp, op);
        
        String planned_query= OpAsQuery.asQuery(op).toString(); 
        System.out.println(planned_query);
        
        // TODO those are stats - maybe set an option for that
        // System.out.println("Original query:"+query2.length());
        // System.out.println("Optimized query:"+optimized_query.length());
        // System.out.println("Planned query:"+planned_query.length());
        
//        Pattern p = Pattern.compile("SERVICE");
//        java.util.regex.Matcher m_query = p.matcher(query2);
//        java.util.regex.Matcher m_query_opt = p.matcher(optimized_query);
        
//        int ct=0;
//        while( m_query.find()) ct++;
//        System.out.println("Query original SERVICES: " + ct);
//        
//        ct=0;
//        while( m_query_opt.find()) ct++;
//        System.out.println("Query optimizada SERVICES: " + ct);
        

	}

}
