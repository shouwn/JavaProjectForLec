package common;

public class Points {
	
	public static boolean checkPointInArea(Point check, Point startArea, Point endArea){
		
		if(check.getX() >= startArea.getX() && check.getX() <= endArea.getX())
			if(check.getY() >= startArea.getY() && check.getY() <= endArea.getY())
				return true;
		
		return false;
	}

	public static boolean checkAreaInArea(Point startArea1, Point endArea1, Point startArea2, Point endArea2){
		
		Point point1 = startArea1;
		Point point2 = new Point(endArea1.getX(), startArea1.getY());
		Point point3 = new Point(startArea1.getX(), endArea1.getY());
		Point point4 = endArea1;
		
		if(checkPointInArea(point1, startArea2, endArea2))
			return true;

		if(checkPointInArea(point2, startArea2, endArea2))
			return true;

		if(checkPointInArea(point3, startArea2, endArea2))
			return true;
		
		if(checkPointInArea(point4, startArea2, endArea2))
			return true;
		
		return false;
	}
}
