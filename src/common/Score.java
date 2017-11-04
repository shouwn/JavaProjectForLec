package common;

import javax.swing.JLabel;

//this class just made for test

public class Score{
	private int score;
	private JLabel label;
	
	public int getScore(){
		return score;
	}
	
	public synchronized void addScore(int value){
		score += value;
	}
	
}
