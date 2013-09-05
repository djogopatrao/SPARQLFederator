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
        GrumpyPlanner gp = new GrumpyPlanner();
        
        // plan statistics
        setCosts(gp,dmNS);
        gp.getCostMap().dump();
        
        System.out.println("Crit<8E>rio Triagem-----------");
        String query2 = gqe.createQueryFromClasses(Arrays.asList("CriterioTriagem"));
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
        
        
        System.out.println("Original query:"+query2.length());
        System.out.println("Optimized query:"+optimized_query.length());
        System.out.println("Planned query:"+planned_query.length());
        
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

	private static void setCosts(GrumpyPlanner gp, String dmNS) {
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"PacienteMaiorDe18Anos", new GrumpyCost(5));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"Quimioterapia", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"AdenocarcinomaInvasivo", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"C50", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T0", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T1", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T2", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T3", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T4", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N0", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N1", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N2", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N3", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"M0", new GrumpyCost(3));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"M1", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"AdenocarcinomaInvasivo", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore3", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore2", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore1", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore0", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"ISHRazaoHerCHR17Maior2.2", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Paciente", new GrumpyCost(104));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"PacienteMaiorDe18Anos", new GrumpyCost(50));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Paclitaxel", new GrumpyCost(26));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Docetaxel", new GrumpyCost(15));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Cetuximab", new GrumpyCost(13));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Erlotinib", new GrumpyCost(3));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Trastuzumab", new GrumpyCost(10));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Gefitinib", new GrumpyCost(11));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"TaxanoHaMaisDeQuatroSemanas", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"C50", new GrumpyCost(102));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T0", new GrumpyCost(50));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T1", new GrumpyCost(31));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T2", new GrumpyCost(13));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T3", new GrumpyCost(9));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T4", new GrumpyCost(5));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N0", new GrumpyCost(38));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N1", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N2", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N3", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"M0", new GrumpyCost(9));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"M1", new GrumpyCost(4));
		
	}

}
