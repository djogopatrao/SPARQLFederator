package br.org.accamargo.cipe.gqe;

import java.util.Iterator;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;
import com.hp.hpl.jena.sparql.algebra.op.OpJoin;
import com.hp.hpl.jena.sparql.algebra.op.OpService;
import com.hp.hpl.jena.sparql.algebra.op.OpUnion;


public class GrumpyCostEstimator {

	GrumpyCostMap costMap = new GrumpyCostMap();

	private String insideService;
	
	public GrumpyCostMap getCostMap() { return costMap; }
	

	public int start( Op x ) throws Exception {
		return go( x );
	}
	
	
	private int go( OpJoin x ) throws Exception {
		
		int cost =go( x.getLeft() ) +  go( x.getRight() )  + costMap.getOperationCost("OpJoin").getValue();
//		System.out.println("Visita um join;");
//		System.out.println("\tcost:"+cost);
		return cost ;
	}
	
	private int go( OpUnion x ) throws Exception {
		
		int cost =go( x.getLeft() ) +  go( x.getRight() ) + costMap.getOperationCost("OpUnion").getValue();
//		System.out.println("Visita um union;");
//		System.out.println("\tcost:"+cost);
		return cost ;
	}	
	
	private int go( OpBGP x ) {
		
		
		Iterator<Triple> i = x.getPattern().iterator();
		
		int cost = 0;
		
		while( i.hasNext() ){
			cost+= go(i.next());
		}
		
		cost +=  costMap.getOperationCost("OpBGP").getValue();

//		System.out.println("Visita um bgp;");
//		System.out.println("\tcost:"+cost);
		return cost;
	}
	
	private int go( Op x ) throws Exception {


		// como fazer isso de maneira reflexiva?
		// Class<? extends Op> class1 = x.getClass();
		if ( x.getClass() == OpBGP.class )
			return go( (OpBGP) x );
		else if ( x.getClass() == OpJoin.class )
			return go( (OpJoin) x );
		else if ( x.getClass() == OpUnion.class )
			return go( (OpUnion) x );		
		else if ( x.getClass() == OpService.class )
			return go( (OpService) x );
		
//		System.out.println("Visita um op;" + x.getClass() );
//		System.out.println("\tdefault cost");
		return  costMap.getOperationCost("Op").getValue();
	}
	
	private int go( OpService o ) throws Exception {
		
		String url = o.getService().toString();
		
		if ( insideService != null )
			throw new Exception("Can't deal with OpServices within OpServices!");
		
		insideService = url; 
		
		int cost = go( o.getSubOp() ) +  costMap.getOperationCost("OpService").getValue() ;// overhead 

//		System.out.println("Visita um service;" );
//		System.out.println("\tcost:"+cost);

		insideService = null;
		
		return  cost;
	}
	
	private int go( Triple t ) {
		
		int cost = 0;
		
		if ( insideService != null ) {
			System.out.println(t.getObject().toString());
			cost = costMap.getServiceCost( insideService, t.getObject().toString() ).getValue();
		}
		else cost = costMap.getOperationCost("Triple").getValue();
		
//		System.out.println("Visita uma tripla;" + t );
//		System.out.println("\tcost:"+cost);
		return cost;
	}
	


}
