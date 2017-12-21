package common;

import java.util.Comparator;

public class ScoreComparator implements Comparator<Score>{
	
	//여러 방식에 따라 Score객체를 정렬하기 위한 Comparator
	public static final int BY_SCORE = 0;
	public static final int BY_NAME = 1;
	public static final int BY_TIME = 2;
	
	private int way;
	
	public ScoreComparator(int way){
		this.way = way;
	}
	
	@Override
	public int compare(Score s1, Score s2) {
		int result = 0;
		
		switch(way){
		
		case BY_SCORE:
			result = Integer.valueOf(s1.getScore()) - Integer.valueOf(s2.getScore());
			break;
		case BY_NAME:
			result = s1.getName().compareTo(s2.getName());
			break;
		case BY_TIME:
			result = s1.getTimeInteger() - s2.getTimeInteger();
			break;
		}
		
		return result * -1;
	}

}
