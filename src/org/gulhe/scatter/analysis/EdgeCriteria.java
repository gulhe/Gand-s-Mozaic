package org.gulhe.scatter.analysis;

public class EdgeCriteria implements Criteria  {
	private int rChecked;
	private int gChecked;
	private int bChecked;
	private Criteria parentCrit;

	public EdgeCriteria(int rChecked, int gChecked, int bChecked,
			Criteria parentCrit) {
		this.rChecked = rChecked;
		this.gChecked = gChecked;
		this.bChecked = bChecked;
		this.parentCrit = parentCrit;
	}

	@Override
	public boolean check(Integer r, Integer g, Integer b) {
		if((Math.abs(r)<rChecked)&&(Math.abs(g)<gChecked)&&(Math.abs(b)<bChecked)){
			return false;
		}
		if(parentCrit == null){
			return true;
		}
		return parentCrit.check(r, g, b);
	}
}
