package br.org.accamargo.cipe.gqe;

import java.util.Arrays;
import java.util.regex.Pattern;

import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.OpAsQuery;
import com.hp.hpl.jena.sparql.algebra.Transformer;

public class SPARQLFederator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
        String ontocloudOntology = args[0];
        String ocNS = args[1];
        String domainOntology = args[2];
        String dmNS = args[3];
        
        System.out.println(ontocloudOntology);
        
        GrumpyQueryExpander gqe = new GrumpyQueryExpander(ontocloudOntology, ocNS, domainOntology, dmNS); 
        GrumpyOptimizer go = new GrumpyOptimizer();
        
        System.out.println("Crit<8E>rio Triagem-----------");
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
