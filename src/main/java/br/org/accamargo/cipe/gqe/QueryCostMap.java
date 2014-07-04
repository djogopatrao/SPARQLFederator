package br.org.accamargo.cipe.gqe;

import java.util.HashMap;

// TODO allow configuration of join e union -> only service allowed!
public class QueryCostMap {
	
	private HashMap <String, HashMap<String,QueryCost > > serviceCost = new HashMap <String, HashMap<String,QueryCost > >();
	private HashMap <String, QueryCost> operationCost = new HashMap <String, QueryCost>(); 

	// 	TODO hardcoded
	private QueryCost defaultCost = new QueryCost(0);
	
	QueryCost getServiceCost( String endpoint, String classname ) {
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
	
	QueryCost setServiceCost( String endpoint, String classname, QueryCost value ) {
		
		if ( !serviceCost.containsKey(endpoint))
			serviceCost.put(endpoint, new HashMap<String,QueryCost>());
		
		return serviceCost.get(endpoint).put(classname, value);
	}
	
	QueryCost getOperationCost( String operation ) {
		if ( ! operationCost.containsKey(operation) ) {
//			System.out.println("default cost for operation "+operation);
			return defaultCost;
		}

		
		return operationCost.get(operation);
	}
	
	QueryCost setOperationCost( String operation, QueryCost value ) {
		
		return operationCost.put(operation, value);
	}
	
	void dump() {
		System.out.println(serviceCost.toString());
		System.out.println(operationCost.toString());
	}
	
}
