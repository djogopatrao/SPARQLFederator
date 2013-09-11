package br.org.accamargo.cipe.gqe;

import java.util.Arrays;
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

public class SPARQLFederatorRun {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
        String ontocloudOntology = args[0];
        String ocNS = args[1];
        String domainOntology = args[2];
        String dmNS = args[3];
        String class_name = args[4];
        
        System.out.println(ontocloudOntology);
        
        GrumpyQueryExpander gqe = new GrumpyQueryExpander(ontocloudOntology, ocNS, domainOntology, dmNS); 
        GrumpyOptimizer go = new GrumpyOptimizer();
        GrumpyPlanner gp = new GrumpyPlanner();
        
        // plan statistics
        StaticCosts.setCosts(gp,dmNS);

        // create query
        String query2 = gqe.createQueryFromClasses(Arrays.asList(class_name));
        System.out.println(query2);
        Op op = Algebra.compile(QueryFactory.create(query2));
        
        // optimize 
        // TODO how many times are enough? 
        for(int i=0; i<1000; i ++ ) 
                op = Transformer.transform(go, op );
        
        String optimized_query = OpAsQuery.asQuery(op).toString(); 
        System.out.println(optimized_query);
        
        // plan execution
        // TODO how many times are enough? 
        for (int i=0; i<1000;i ++)
        	op = Transformer.transform(gp, op);
        
        String planned_query= OpAsQuery.asQuery(op).toString(); 
        System.out.println(planned_query);
        

        // execute query and yield results
        Query query = QueryFactory.create(planned_query) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel() ) ;
        try {
			long startTime = System.nanoTime();			
			ResultSet results = qexec.execSelect() ;
			
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				RDFNode x = soln.get("pct") ;
				System.out.println(x);
			}
			long runningTime = System.nanoTime() - startTime;
			System.out.println( "Results: " + results.getRowNumber() );
			System.out.println( "Time (ns): " + runningTime );
        } finally { qexec.close() ; }

	}


}
