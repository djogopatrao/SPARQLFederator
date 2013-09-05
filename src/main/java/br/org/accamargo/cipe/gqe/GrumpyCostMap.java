package br.org.accamargo.cipe.gqe;

import java.util.HashMap;

// TODO allow configuration of join e union -> only service allowed!
public class GrumpyCostMap {
	
	private HashMap <String, HashMap<String,GrumpyCost > > serviceCost = new HashMap <String, HashMap<String,GrumpyCost > >();
	private HashMap <String, GrumpyCost> operationCost = new HashMap <String, GrumpyCost>(); 

	// 	TODO hardcoded
	private GrumpyCost defaultCost = new GrumpyCost(0);
	
	GrumpyCost getServiceCost( String endpoint, String classname ) {
		if ( ! serviceCost.containsKey(endpoint) ) {
//			System.out.println("default cost for service "+endpoint);
			return defaultCost;
		}

		if ( ! serviceCost.get(endpoint).containsKey(classname) ){
//			System.out.println("default cost for service "+endpoint + "class "+classname);
			return defaultCost;
		}
		
		return serviceCost.get(endpoint).get(classname);
	}
	
	GrumpyCost setServiceCost( String endpoint, String classname, GrumpyCost value ) {
		
		if ( !serviceCost.containsKey(endpoint))
			serviceCost.put(endpoint, new HashMap<String,GrumpyCost>());
		
		return serviceCost.get(endpoint).put(classname, value);
	}
	
	GrumpyCost getOperationCost( String operation ) {
		if ( ! operationCost.containsKey(operation) ) {
//			System.out.println("default cost for operation "+operation);
			return defaultCost;
		}

		
		return operationCost.get(operation);
	}
	
	GrumpyCost setOperationCost( String operation, GrumpyCost value ) {
		
		return operationCost.put(operation, value);
	}
	
	void dump() {
		System.out.println(serviceCost.toString());
		System.out.println(operationCost.toString());
	}
	
}
