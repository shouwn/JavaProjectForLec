package common;

public class Score{
	private int score;
	private int phase = 1;
	private int time;
	private String name;
	
	// 기본 모두 0으로 초기화 phase는 1
	public Score(){
		
	}
	
	// 현재 플레이 정보를 파라메터로 받아서 객체 생성하는 생성자
	public Score(String name, String score, String time){
		this.name = name;
		this.score = Integer.valueOf(score);
		
		int timeInt = Integer.valueOf(time.substring(0, 2)) * 60000;
		timeInt += Integer.valueOf(time.substring(3)) * 1000;
		
		this.time = timeInt;
	}

	// 현재 점수를 return
	public String getScore(){
		return String.valueOf(score);
	}
	// 현재 점수를 추가
	public synchronized void addScore(int value){
		score += value;
	}
	// 현재 페이즈 변경
	public void setPhase(int phase){
		this.phase = phase;
	}
	// 현재 시간 증가
	public void addTime(int value){
		time += value;
	}
	// 현재 페이즈 변경
	public void changePhase(){
		phase++;
	}
	// 현재 페이즈 return
	public String getPhase() {
		return new StringBuilder("PHASE ").append(phase).toString();
	}

	// 시간을 00:00으로 받음
	public String getTime(){

		return new StringBuilder(String.format("%02d", time/60000)).append(":").append(String.format("%02d", (time/1000)%60)).toString();
	}
	// 사용자 이름 set
	public void setName(String name) {
		this.name = name;
	}
	// 사용자 이름 get
	public String getName(){
		return name;
	}
	// 시간을 ms 단위로 받음
	public int getTimeInteger(){

		return time;
	}
	@Override
	public String toString(){
		return new StringBuilder(name)
				.append(" ").append(getScore()).append(" ").append(getTime())
				.toString();
	}
}
