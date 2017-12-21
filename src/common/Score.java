package common;

public class Score{
	private int score;
	private int phase = 1;
	private int time;
	private String name;
	
	// �⺻ ��� 0���� �ʱ�ȭ phase�� 1
	public Score(){
		
	}
	
	// ���� �÷��� ������ �Ķ���ͷ� �޾Ƽ� ��ü �����ϴ� ������
	public Score(String name, String score, String time){
		this.name = name;
		this.score = Integer.valueOf(score);
		
		int timeInt = Integer.valueOf(time.substring(0, 2)) * 60000;
		timeInt += Integer.valueOf(time.substring(3)) * 1000;
		
		this.time = timeInt;
	}

	// ���� ������ return
	public String getScore(){
		return String.valueOf(score);
	}
	// ���� ������ �߰�
	public synchronized void addScore(int value){
		score += value;
	}
	// ���� ������ ����
	public void setPhase(int phase){
		this.phase = phase;
	}
	// ���� �ð� ����
	public void addTime(int value){
		time += value;
	}
	// ���� ������ ����
	public void changePhase(){
		phase++;
	}
	// ���� ������ return
	public String getPhase() {
		return new StringBuilder("PHASE ").append(phase).toString();
	}

	// �ð��� 00:00���� ����
	public String getTime(){

		return new StringBuilder(String.format("%02d", time/60000)).append(":").append(String.format("%02d", (time/1000)%60)).toString();
	}
	// ����� �̸� set
	public void setName(String name) {
		this.name = name;
	}
	// ����� �̸� get
	public String getName(){
		return name;
	}
	// �ð��� ms ������ ����
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
