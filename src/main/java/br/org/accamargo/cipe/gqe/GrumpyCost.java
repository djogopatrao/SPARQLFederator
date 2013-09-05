package br.org.accamargo.cipe.gqe;

public class GrumpyCost {

	private int value;
	
	public int getValue() { return value; }
	public void setValue(int v ) { value=v; }
	
	public GrumpyCost(int value) {
		super();
		this.value = value;
	}
	public boolean isGreaterThan( GrumpyCost c2 ) { return value > c2.value; }
	public boolean isSmallerThan( GrumpyCost c2 ) { return value < c2.value; }
	public boolean isEqualTo( GrumpyCost c2 ) { return value == c2.value; }
	
	
}
