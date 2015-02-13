package org.gulhe.scatter.analysis;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BroadRGBSearchStructure {

	private Map<Integer, Map<Integer, Map<Integer, Collection<Point>>>> matrix;

	public BroadRGBSearchStructure add(Point e) {
		if (matrix == null) {
			matrix = new HashMap<>();
		}
		Map<Integer, Map<Integer, Collection<Point>>> redSubPart = matrix.get(e
				.getR());
		if (redSubPart == null) {
			redSubPart = new HashMap<>();
			matrix.put(e.getR(), redSubPart);
		}
		Map<Integer, Collection<Point>> greenSubPart = redSubPart.get(e.getG());
		if (greenSubPart == null) {
			greenSubPart = new HashMap<>();
			redSubPart.put(e.getG(), greenSubPart);
		}
		Collection<Point> blueSubPart = greenSubPart.get(e.getB());
		if (blueSubPart == null) {
			blueSubPart = new HashSet<>();
			greenSubPart.put(e.getB(), blueSubPart);
		}
		blueSubPart.add(e);
		return this;
	}

	public BroadRGBSearchStructure addAll(Collection<Point> c) {
		for (Point point : c) {
			this.add(point);
		}
		return this;
	}

	public Collection<Point> get(Integer r, Integer g, Integer b) {
		Map<Integer, Map<Integer, Collection<Point>>> redC = this.matrix.get(r);
		if (redC == null) {
			return null;
		}
		Map<Integer, Collection<Point>> greenC = redC.get(g);
		if (greenC == null) {
			return null;
		}
		return greenC.get(b);
	}

	public Collection<Point> yield(Color c,
			int imprecision) {
		return yield(c.getRed(), c.getGreen(), c.getBlue(),imprecision);
	}
	public Collection<Point> yield(Integer r, Integer g, Integer b,
			int imprecision) {
		return yield(r, imprecision, g, imprecision, b, imprecision, null);
	}

	public Collection<Point> yield(Integer r, Integer g, Integer b,
			int imprecision, Criteria crit) {
		return yield(r, imprecision, g, imprecision, b, imprecision, crit);
	}

	public Collection<Point> yield(Integer r, int rImprecision, Integer g,
			int gImprecision, Integer b, int bImprecision, Criteria crit) {
		Collection<Point> result = new HashSet<>();
		for (int i = -rImprecision; i <= rImprecision; i++) {
			for (int j = -gImprecision; j <= gImprecision; j++) {
				for (int k = -bImprecision; k <= bImprecision; k++) {
					if (crit == null || crit.check(i, j, k)) {
						Collection<Point> gotten = this.get(r + i, g + j, b + k);
						if(gotten != null){
							result.addAll(gotten);
						}
					}
				}
			}
		}
		return result;
	}
	public Collection<Point> getClosest(Integer r, int rImprecision, Integer g,
			int gImprecision, Integer b, int bImprecision, final Criteria crit) {
		Integer maxImprecision = Math.max(rImprecision, Math.max(gImprecision, bImprecision));
		Collection<Point> result = new HashSet<>();
		for (int i = 0; i <= maxImprecision; i++) {
			int rChecked = Math.min(rImprecision, i);
			int gChecked = Math.min(gImprecision, i);
			int bChecked = Math.min(bImprecision, i);
			Criteria crit2 = new EdgeCriteria(rChecked, gChecked , bChecked, crit);
			Collection<Point> yield = yield(r, rChecked, g, gChecked, b, bChecked, crit2);
			if((yield != null) && (yield.size()!=0)){
				return yield;
			}
		}
		return result;
	}

	public Collection<Point> getClosest(Color c, int i) {
		return getClosest(c.getRed(), c.getGreen(), c.getBlue(),i);
	}

	public Collection<Point> getClosest(int red, int green, int blue, int i) {
		return getClosest(red, i, green, i, blue, i, null);
	}

}
