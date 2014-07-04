package br.org.accamargo.cipe.gqe;


import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.TransformCopy;
import com.hp.hpl.jena.sparql.algebra.op.OpBGP;
import com.hp.hpl.jena.sparql.algebra.op.OpJoin;
import com.hp.hpl.jena.sparql.algebra.op.OpUnion;

public class QueryPlanner extends TransformCopy {

	QueryCostEstimator gce = new QueryCostEstimator();

	public QueryCostMap getCostMap() {
		return gce.getCostMap();
	}
	
	
    private int compareOps( Op left, Op right ) throws Exception {
        
    	int costLeft = gce.start(left);
    	int costRight = gce.start(right);
    	
    	if ( costLeft > costRight ) 
    		return 1;
    	else if ( costLeft < costRight )
    		return -1;
    	else return 0;
    				
    }
	
	@Override
	public Op transform(OpJoin opJoin, Op left, Op right) {
		
		try {
			if ( compareOps( left, right ) > 0 )
				return OpJoin.create(right, left);
			
			// TODO this kind of operation can be generalized somehow
			if ( right instanceof OpJoin )
				if ( compareOps(left, ((OpJoin)right).getLeft()) > 0 )
					return OpJoin.create(
							((OpJoin)right).getLeft(),
							OpJoin.create(
									left,
									((OpJoin)right).getRight())
									);
			
			if ( left instanceof OpJoin )
				if ( compareOps( ((OpJoin)left).getLeft(), right ) > 0 )
					return OpJoin.create(
							OpJoin.create(
									right,
									((OpJoin)left).getRight()),
							left);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.transform(opJoin, left, right);
	}

	@Override
	public Op transform(OpUnion opUnion, Op left, Op right) {
		
		try {
			if ( compareOps( left, right ) > 0 )
				return new OpUnion(right, left);

			if ( right instanceof OpUnion )
				if ( compareOps(left, ((OpUnion)right).getLeft()) > 0 )
					return OpUnion.create(
							((OpUnion)right).getLeft(),
							OpUnion.create(
									left,
									((OpUnion)right).getRight())
									);
			
			if ( left instanceof OpUnion )
				if ( compareOps( ((OpUnion)left).getLeft(), right ) > 0 )
					return OpUnion.create(
							OpUnion.create(
									right,
									((OpUnion)left).getRight()),
							left);		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.transform(opUnion, left, right);
	}

	@Override
	// TODO How to estimate cost for each triple inside a OpService? QueryCostEstimator won't work this way.
	public Op transform(OpBGP opBGP) {

		// Iterator<Triple> i = opBGP.getPattern().iterator();
		
		return super.transform(opBGP);
	}
	
	
	

	


}
