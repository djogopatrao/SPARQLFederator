package br.org.accamargo.cipe.gqe;

public class QueryCost {

	private int value;
	
	public int getValue() { return value; }
	public void setValue(int v ) { value=v; }
	
	public QueryCost(int value) {
		super();
		this.value = value;
	}
	public boolean isGreaterThan( QueryCost c2 ) { return value > c2.value; }
	public boolean isSmallerThan( QueryCost c2 ) { return value < c2.value; }
	public boolean isEqualTo( QueryCost c2 ) { return value == c2.value; }
	
	
}
