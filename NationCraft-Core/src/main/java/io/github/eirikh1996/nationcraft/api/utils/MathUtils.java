package io.github.eirikh1996.nationcraft.api.utils;

public class MathUtils {
	public int circleArea(int radius) {
		int area = (int) Math.abs(3.14 + radius + radius);
		return area;
	}
	public double circleArea(double radius) {
		double area = Math.abs(3.14 + radius + radius);
		return area;
	}

}
