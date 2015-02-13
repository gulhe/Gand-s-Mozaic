package org.gulhe.scatter.analysis;

import org.gulhe.scatter.properties.PropertyHelper;
import org.gulhe.scatter.properties.SelfAwareProperty;

public class Point {
	private int r, g, b;

	private int count = 0;
	private SelfAwareProperty props;

	public Point(SelfAwareProperty sap) {
		this(new Integer(sap.getProperty(PropertyHelper.AVERAGE_RED)),
				new Integer(sap.getProperty(PropertyHelper.AVERAGE_GREEN)),
				new Integer(sap.getProperty(PropertyHelper.AVERAGE_BLUE)));
		props = sap;
	}

	public Point(int red, int green, int blue) {
		r = red;
		g = green;
		b = blue;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int getCount() {
		return count;
	}

	public SelfAwareProperty getProps() {
		return props;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((props == null) ? 0 : props.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (props == null) {
			if (other.props != null)
				return false;
		} else if (!props.equals(other.props))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [rgb[=" + r + "," + g + "," + b + "], count=" + count
				+ ", props=" + props + "]";
	}

	public void incrementCount() {
		this.count++;
	}

	
	public Double distance(Point o){
		int dR = Math.abs(r-o.r);
		int dG = Math.abs(g-o.g);
		int dB = Math.abs(b-o.b);
		
		return Math.sqrt(Math.pow(dR, 2)+Math.pow(dG, 2)+Math.pow(dB, 2));
	}
}