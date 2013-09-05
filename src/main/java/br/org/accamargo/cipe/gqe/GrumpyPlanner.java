package br.org.accamargo.cipe.gqe;

import java.util.Iterator;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;
import com.hp.hpl.jena.sparql.algebra.op.OpJoin;
import com.hp.hpl.jena.sparql.algebra.op.OpService;
import com.hp.hpl.jena.sparql.algebra.op.OpUnion;


public class GrumpyPlanner {
	

	private String insideService;

	public int start( Op x ) throws Exception {
		return go( x );
	}
	
	
	private int go( OpJoin x ) throws Exception {
		
		int cost =go( x.getLeft() ) +  go( x.getRight() );
		System.out.println("Visita um join;");
		System.out.println("\tcost:"+cost);
		return cost ;
	}
	
	private int go( OpUnion x ) throws Exception {
		
		int cost =go( x.getLeft() ) +  go( x.getRight() );
		System.out.println("Visita um union;");
		System.out.println("\tcost:"+cost);
		return cost ;
	}	
	
	private int go( OpBGP x ) {
		
		
		Iterator<Triple> i = x.getPattern().iterator();
		
		int cost = 0;
		
		while( i.hasNext() ){
			cost+= go(i.next());
		}

		System.out.println("Visita um bgp;");
		System.out.println("\tcost:"+cost);
		return cost + 0 ;// overhead
	}
	
	private int go( Op x ) throws Exception {

		System.out.println("Visita um op;" + x.getClass() );

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
		
		System.out.println("\tdefault cost");
		return 1;
	}
	
	private int go( OpService o ) throws Exception {
		System.out.println("Visita um service;" );
		
		String url = o.getService().toString();
		
		if ( insideService != null )
			throw new Exception("Can't deal with OpServices within OpServices!");
		
		insideService = url; 
		
		int cost = go( o.getSubOp() ) + 0 ;// overhead 
		
		insideService = null;
		
		return  cost;
	}
	
	private int go( Triple t ) {
		System.out.println("Visita uma tripla;" + t );
		
		if ( insideService != null )
			return 1 + 100 ; // service cost

		return 1;
		
	}
	


}
