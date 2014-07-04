package br.org.accamargo.cipe.gqe;

public class StaticCosts {

	public static void setCosts(QueryPlanner gp, String dmNS) {
		// ontocloud ontop
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"PacienteMaiorDe18Anos", new QueryCost(5));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"Quimioterapia", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"AdenocarcinomaInvasivo", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"C50", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T0", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T1", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T2", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T3", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"T4", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N0", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N1", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N2", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"N3", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"M0", new QueryCost(3));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/rhc", dmNS+"M1", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"AdenocarcinomaInvasivo", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore3", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore2", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore1", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"IHQ_HER2_Escore0", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/laudos", dmNS+"ISHRazaoHerCHR17Maior2.2", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Paciente", new QueryCost(104));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"PacienteMaiorDe18Anos", new QueryCost(50));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Paclitaxel", new QueryCost(26));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Docetaxel", new QueryCost(15));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Cetuximab", new QueryCost(13));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Erlotinib", new QueryCost(3));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Trastuzumab", new QueryCost(10));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"Gefitinib", new QueryCost(11));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"TaxanoHaMaisDeQuatroSemanas", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"C50", new QueryCost(102));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T0", new QueryCost(50));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T1", new QueryCost(31));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T2", new QueryCost(13));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T3", new QueryCost(9));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"T4", new QueryCost(5));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N0", new QueryCost(38));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N1", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N2", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"N3", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"M0", new QueryCost(9));
		gp.getCostMap().setServiceCost("http://fisher:8080/openrdf-sesame/repositories/mv_repl", dmNS+"M1", new QueryCost(4));
		
		// ontocloud d2r
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"AdenocarcinomaInvasivo", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore0", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore1", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore2", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore3", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"ISHRazaoHerCHR17Maior2.2", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"C50", new QueryCost(53));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Cetuximab", new QueryCost(26));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Docetaxel", new QueryCost(24));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Erlotinib", new QueryCost(4));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Gefitinib", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"M0", new QueryCost(102));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"M1", new QueryCost(9));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N0", new QueryCost(21));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N1", new QueryCost(7));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N2", new QueryCost(5));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N3", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Paciente", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"PacienteMaiorDe18Anos", new QueryCost(197));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Paclitaxel", new QueryCost(19));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T0", new QueryCost(22));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T1", new QueryCost(6));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T2", new QueryCost(7));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T3", new QueryCost(7));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T4", new QueryCost(4));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"TaxanoHaMaisDeQuatroSemanas", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Trastuzumab", new QueryCost(9));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"AdenocarcinomaInvasivo", new QueryCost(5));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"C50", new QueryCost(3));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"M0", new QueryCost(9));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"M1", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N0", new QueryCost(8));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N1", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N2", new QueryCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N3", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"PacienteMaiorDe18Anos", new QueryCost(17));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"Quimioterapia", new QueryCost(5));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T0", new QueryCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T1", new QueryCost(4));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T2", new QueryCost(3));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T3", new QueryCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T4", new QueryCost(1));
	}
	
	
}
