package common;

//this class just made for test

public class Score{
	private int score;
	
	public int getScore(){
		return score;
	}
	
	public synchronized void addScore(int value){
		score += value;
	}
	
}
