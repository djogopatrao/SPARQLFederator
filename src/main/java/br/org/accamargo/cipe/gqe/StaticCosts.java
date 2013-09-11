package br.org.accamargo.cipe.gqe;

public class StaticCosts {

	public static void setCosts(GrumpyPlanner gp, String dmNS) {
		// ontocloud ontop
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
		
		// ontocloud d2r
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"AdenocarcinomaInvasivo", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore0", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore1", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore2", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"IHQ_HER2_Escore3", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2023/sparql", dmNS+"ISHRazaoHerCHR17Maior2.2", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"C50", new GrumpyCost(53));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Cetuximab", new GrumpyCost(26));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Docetaxel", new GrumpyCost(24));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Erlotinib", new GrumpyCost(4));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Gefitinib", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"M0", new GrumpyCost(102));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"M1", new GrumpyCost(9));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N0", new GrumpyCost(21));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N1", new GrumpyCost(7));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N2", new GrumpyCost(5));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"N3", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Paciente", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"PacienteMaiorDe18Anos", new GrumpyCost(197));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Paclitaxel", new GrumpyCost(19));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T0", new GrumpyCost(22));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T1", new GrumpyCost(6));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T2", new GrumpyCost(7));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T3", new GrumpyCost(7));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"T4", new GrumpyCost(4));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"TaxanoHaMaisDeQuatroSemanas", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2020/sparql", dmNS+"Trastuzumab", new GrumpyCost(9));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"AdenocarcinomaInvasivo", new GrumpyCost(5));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"C50", new GrumpyCost(3));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"M0", new GrumpyCost(9));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"M1", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N0", new GrumpyCost(8));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N1", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N2", new GrumpyCost(1));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"N3", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"PacienteMaiorDe18Anos", new GrumpyCost(17));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"Quimioterapia", new GrumpyCost(5));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T0", new GrumpyCost(0));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T1", new GrumpyCost(4));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T2", new GrumpyCost(3));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T3", new GrumpyCost(2));
		gp.getCostMap().setServiceCost("http://fisher:2024/sparql", dmNS+"T4", new GrumpyCost(1));
	}
	
	
}
