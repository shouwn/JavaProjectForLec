package common;

//this class just made for test

public class Score{
	private int score;
	private int phase = 1;
	private int time;

	public String getScore(){
		return String.valueOf(score);
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

	public void changePhase(){
		phase++;
	}

	public String getPhase() {
		return new StringBuilder("PHASE ").append(phase).toString();
	}

	public String getTime(){

		return new StringBuilder(String.format("%02d", time/60000)).append(":").append(String.format("%02d", (time/1000)%60)).toString();
	}

}
