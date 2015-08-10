package br.org.accamargo.cipe.gqe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import br.org.accamargo.cipe.gqe.QueryReader.BaseParser;
import br.org.accamargo.cipe.gqe.QueryReader.ParsedQuery;
import br.org.accamargo.cipe.gqe.QueryReader.ParserFactory;

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
		// set default values here
		String ontocloudOntology = "";
		String ocNS = "http://www.cipe.accamargo.org.br/ontologias/ontocloud2.owl#";
		String domainOntology = "";
		String dmNS = "";
		String query_type = "simple";
		String optimizer = "simple";
		String planner = "simple";
		String exec = "run";
		String query_string = "";
		boolean stats = false;

		Options opts = new Options();
		opts.addOption("query", true, "A string containing the query to be expanded");
		opts.addOption("federation_ontology", true, "The federation ontology file");
		opts.addOption("domain_ontology", true, "The domain ontology file");
		opts.addOption("ontocloud_ns", true, "The federation namespace (default value: "+ocNS+")");
		opts.addOption("domain_ns", true, "The domain namespace The domain namespace (if specified, will be appended before each of the queryied DOMAIN_CLASSes)");
		opts.addOption("help",false,"Shows the help message");
		opts.addOption("query_type", true, "The accepted query type: 'simple' (default) or 'dl' " );
		opts.addOption("optimizer", true, "Execute a query optimizer: 'simple' (default) or 'none'" );
		opts.addOption("planner", true, "Execute a query planner: 'simple' (default) or 'none'" );
		opts.addOption("exec", true, "The query executer: 'run' (default) or 'print'" );		
		opts.addOption("stats", false, "Display statistics for queries" );

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse( opts, args);

		if ( cmd.hasOption("help")) {
			showHelpMessage( "", opts );
			return;
		} 

		if ( cmd.hasOption("federation_ontology") ) {
			ontocloudOntology = cmd.getOptionValue("federation_ontology");
		}

		if ( cmd.hasOption("domain_ontology") ) {
			domainOntology = cmd.getOptionValue("domain_ontology");
		}

		if ( cmd.hasOption("ontocloud_ns") ) {
			ocNS = cmd.getOptionValue("ontocloud_ns");
		}

		if ( cmd.hasOption("domain_ns") ) {
			dmNS = cmd.getOptionValue("domain_ns");
		}

		if ( cmd.hasOption("stats") ) {
			stats = true;
		}

		if ( cmd.hasOption("query_type") ) {
			query_type = cmd.getOptionValue("query_type");
		}

		if ( cmd.hasOption("optimizer") ) {
			if( cmd.getOptionValue("optimizer").equals("simple") ) {
				optimizer = "simple";
			} else if( cmd.getOptionValue("optimizer").equals("none") ) {
				optimizer = "none";
			} else {
				showHelpMessage("Error: unrecognized optimizer '"+cmd.getOptionValue("optimizer")+"'", opts);
				return;
			}
		}		

		if ( cmd.hasOption("planner") ) {
			if( cmd.getOptionValue("planner").equals("simple") ) {
				planner = "simple";
			} else if( cmd.getOptionValue("planner").equals("none") ) {
				planner = "none";
			} else {
				showHelpMessage("Error: unrecognized planner '"+cmd.getOptionValue("planner")+"'", opts);
				return;
			}
		}

		if ( cmd.hasOption("exec") ) {
			if( cmd.getOptionValue("exec").equals("run") ) {
				exec = "run";
			} else if( cmd.getOptionValue("exec").equals("print") ) {
				exec = "print";
			} else {
				showHelpMessage("Error: unrecognized exec option'"+cmd.getOptionValue("exec")+"'", opts);
				return;
			}
		}
		if ( cmd.hasOption("query") ) {
			query_string = cmd.getOptionValue("query");
		}
		// check minimal arguments
		if ( ontocloudOntology.isEmpty() ) {
			showHelpMessage( "Error: specify federation ontology file name", opts );
			return;
		} 
		if ( domainOntology.isEmpty() ) {
			showHelpMessage( "Error: specify domain ontology file name", opts );
			return;
		}

		if ( ocNS.isEmpty() ) {
			showHelpMessage( "Error: specify ontocloud namespace", opts );
			return;
		}
		if( query_string.isEmpty() ) {
			showHelpMessage( "Error: specify the query to be expanded", opts );
		}

		QueryExpander gqe = new QueryExpander(ontocloudOntology, ocNS, domainOntology, dmNS); 

		// create query from the arguments
		// TODO strategy for loading the query (so we can use sparql as well) (github #10)
		
		BaseParser queryParser = ParserFactory.create(query_type);
		
		ParsedQuery parsed_query = queryParser.parse(query_string);
		
		String working_query = gqe.createQueryFromClasses( parsed_query );
		Op op = Algebra.compile(QueryFactory.create(working_query));

		// optimize
		// TODO implement strategy here (github #11)
		if ( optimizer.equals("none") ) {
			// do nothing
		} else
			if ( optimizer.equals("simple") ) {
				QueryOptimizer go = new QueryOptimizer();
				// TODO how many times are enough? 
				for(int i=0; i<1000; i ++ ) 
					op = Transformer.transform(go, op );
				working_query = OpAsQuery.asQuery(op).toString(); 
			}


		// plan execution
		// TODO implement strategy here (github #9)
		if ( planner.equals("none") ) {
			// do nothing
		} else
			if (planner.equals("simple") ) {
				QueryPlanner gp = new QueryPlanner();
				// plan statistics
				StaticCosts.setCosts(gp,dmNS);
				// TODO how many times are enough?
				for (int i=0; i<1000;i ++)
					op = Transformer.transform(gp, op);

				working_query = OpAsQuery.asQuery(op).toString(); 
			}


		// execute query and yield results
		if ( exec.equals("run") ) {
			Query query = QueryFactory.create(working_query) ;
			QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel() ) ;
			try {
				long startTime = System.nanoTime();			
				ResultSet results = qexec.execSelect() ;

				for ( ; results.hasNext() ; )
				{
					QuerySolution soln = results.nextSolution() ;
					RDFNode x = soln.get("pct") ; //  github #8
					System.out.println(x);
				}
				long runningTime = System.nanoTime() - startTime;
				if ( stats ) {
					System.out.println( "Results: " + results.getRowNumber() );
					System.out.println( "Time (ns): " + runningTime );
				}
			} finally { qexec.close() ; }
		} else if ( exec.equals("print") ) {
			System.out.println(working_query);
		}

	}

	private static void showHelpMessage(String string, Options opts) {
		// TODO Auto-generated method stub
		HelpFormatter formatter = new HelpFormatter();
		if ( ! string.isEmpty() )
			System.out.println(string);
		formatter.printHelp("SPARQLFederator [options] <DOMAIN_CLASS> [...]", opts);
		return;
	}


}
