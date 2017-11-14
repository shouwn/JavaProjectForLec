package common;

//this class just made for test

public class Score{
	private int score;
	private int phase;
	private int time;

	public int getScore(){
		return score;
	}

	public synchronized void addScore(int value){
		score += value;
	}

	public void setPhase(int phase){
		this.phase = phase;
	}

	public void addTime(int value){
		time += value;
	}

	public int getPhase() {
		return phase;
	}

}
