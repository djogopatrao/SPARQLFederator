package br.org.accamargo.cipe.gqe;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transformer;

import br.org.accamargo.cipe.gqe.GrumpyOptimizer;
import br.org.accamargo.cipe.gqe.GrumpyQueryExpander;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
//    public void testApp() throws Exception
//    {
//    	GrumpyQueryExpander geq = new GrumpyQueryExpander(
//    			"file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/ontocloud2full_ontop_sem_subC50.owl",
//    			"http://www.cipe.accamargo.org.br/ontologias/ontocloud2.owl#",
//    			"file:///Users/diogopatrao/Dropbox/doutorado/ontocloud_ontop_package/20130713_estudo_clinico.owl",
//    			"http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#"
//    			);
//    	String q1 = geq.createQueryFromClasses( Arrays.asList( "UndefinedClass") );
//    	System.out.println(q1);
//
//    	String realQuery = geq.createQueryFromClasses(Arrays.asList("C50_ECIIIb"));
//    	Op realOp = Algebra.compile(QueryFactory.create(realQuery));
//    	System.out.println(realQuery);
// 
//    	GrumpyOptimizer go = new GrumpyOptimizer();
//    	for( int i=1; i<50; i++)
//    		realOp = Transformer.transform( go, realOp );
//    	System.out.println(realOp);
//    
//    	String realQuery1 = geq.createQueryFromClasses(Arrays.asList("DoencaLocalAvancada","DoencaMetastatica"));
//    	Op realOp1 = Algebra.compile(QueryFactory.create(realQuery1));
//    	System.out.println(realQuery1);
// 
//    	for( int i=1; i<50; i++)
//    		realOp1 = Transformer.transform( go, realOp1 );
//    	System.out.println(realOp1);
//    
//    	String realQuery11 = geq.createQueryFromClasses(Arrays.asList("CriterioTriagem"));
//    	Op realOp11 = Algebra.compile(QueryFactory.create(realQuery11));
//    	System.out.println(realQuery11);
// 
//    	for( int i=1; i<50; i++)
//    		realOp11 = Transformer.transform( go, realOp11 );
//    	System.out.println(realOp11);
//    
//    }
    
    
    public void testPlanner() throws Exception {
    	
    	GrumpyPlanner x = new GrumpyPlanner();

    	x.getCostMap().setServiceCost("http://teste1/", "http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#DoencaLocalAvancada", new GrumpyCost(100));
    	x.getCostMap().setServiceCost("http://teste2/", "http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#DoencaLocalAvancada", new GrumpyCost(132));
    	x.getCostMap().setOperationCost("OpJoin", new GrumpyCost(1));
    	x.getCostMap().setOperationCost("OpUnion", new GrumpyCost(2));
    	x.getCostMap().setOperationCost("OpService", new GrumpyCost(1));
    	x.getCostMap().setOperationCost("OpBGP", new GrumpyCost(0));
    	x.getCostMap().setOperationCost("Op", new GrumpyCost(0));
    	x.getCostMap().setOperationCost("Triple", new GrumpyCost(0));
    	    	
    	Op op1 = Algebra.compile(QueryFactory.create("SELECT * { SERVICE <http://teste1/> { ?a a <http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#DoencaLocalAvancada> } } "));   	
    	assertEquals( 101, x.start(op1) );
    	
    	Op op2 = Algebra.compile(QueryFactory.create("SELECT * {" +
    			"{ SERVICE <http://teste1/> " +
    			"{ ?a a <http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#DoencaLocalAvancada> }" +
    			"} UNION " +
    			"{ SERVICE <http://teste2/> " +
    			"{ ?a a <http://www.cipe.accamargo.org.br/ontologias/estudo_clinico.owl#DoencaLocalAvancada> }" +
    			" } } "));   	
    	assertEquals( 236, x.start(op2) );
    	
    }
}