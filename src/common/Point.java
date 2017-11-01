package common;

public class Point {
	private float x;
	private float y;
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(float x, float y) {
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public Point(Point obj) {
		this.x = obj.x;
		this.y = obj.y;
	}
	
	public Point add(Point other) {
		this.x += other.x;
		this.y += other.y;
		
		return this;
	}
	
	public Point add(float x, float y) {
		this.x += x;
		this.y += y;
		
		return this;
	}
	
	public Point add(int x, int y) {
		this.x += x;
		this.y += y;
		
		return this;
	}
	
	public Point move(int varX, int varY) {
		this.x += varX;
		this.y -= varY;
		
		return this;
	}
	
	public Point move(float varX, float varY) {
		this.x += varX;
		this.y -= varY;
		
		return this;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setEqually(Point other) {
		this.x = other.x;
		this.y = other.y;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public float getDistanceTo(Point other) {
		return (float) Math.sqrt(Math.pow(this.x - other.x, 2)+ Math.pow(this.y - other.y, 2));
	}
	
	public float getDistanceTo(int x, int y) {
		return (float) Math.sqrt(Math.pow(this.x - x, 2)+ Math.pow(this.y - y, 2));
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	
}
