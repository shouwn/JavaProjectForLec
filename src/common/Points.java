package common;

public class Points {
	
	public static boolean checkPointInArea(Point check, Point startArea, Point endArea){
		
		if(check.getX() >= startArea.getX() && check.getX() <= endArea.getX())
			if(check.getY() >= startArea.getY() && check.getY() <= endArea.getY())
				return true;
		
		return false;
	}

	public static boolean checkAreaInArea(Point startArea1, Point endArea1, Point startArea2, Point endArea2){
		
		if(startArea1.getX() < endArea2.getX() && endArea1.getX() > startArea2.getX()
				&& startArea1.getY() < endArea2.getY() && endArea1.getY() > startArea2.getY())
			return true;
		
		return false;
	}
}
