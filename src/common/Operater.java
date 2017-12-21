package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import bullet.Bullet;
import bullet.Bullets;
import enemy.Boss;
import enemy.Enemy;
import enemy.Enemys;
import item.Item;
import item.Items;
import player.Player;
import template.GamePanel;

public class Operater {

	private String information;

	private Boss boss;

	// 배경
	private Image backgroundImage;
	private BufferedImage pauseBG;

	// 그림을 그릴 패널
	private JPanel panel;
	private int width, height; //패널의 넓이와 높이

	// 각 객체들과 객체를 저장할 list
	private List<Bullet> bulletList;
	private List<Enemy> enemyList;
	private List<Item> itemList;
	private Player player;
	private Score score; // 점수와 플레이 시간 현재 페이즈의 정보를 가지고 있는 객체

	// 각 스레드의 간격
	private int makeEnemyInterval = 1000;
	private int updateInterval = 30;
	private int playerAttackInterval = 300;
	private int changePhaseInterval = 30000;

	private int phase = 1; // 현재 페이즈/ 1부터 시작
	private int phaseMax = 4; // 최대 진행 페이즈

	// 게임 구동을 위해 사용할 스레드 4개
	private Thread makeEnemy; // 적을 만드는 스레드
	private Thread updateAll; // 모든 구성요소의 위치와 피격 판정을 하는 스레드
	private Thread playerAttack; // 플레이어의 공격을 위한 스레드
	private Checker checker; // 게임 페이즈가 바뀌었는지, 일시정지인지 게임이 끝났는지 확인하는 스레드

	// 게임 구동 시 사용할 flag 들
	private boolean isInChanging = false; // 페이즈가 바뀌고 있는지 확인하는 flag
	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private boolean isNeedPaintInformation = false; // 일시정지시 상태 정보를 표시할 필요가 있는지 필요한가
	private boolean isBossPhase = false; 

	//공통적으로 사용할 폰트
	private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 60);

	// 게임 내에 사용할 bgm들
	private Sound[] bgms = new Sound[5];
	
	// 생성자, 파라메터로 이 게임을 그릴 패널을 넘겨받음
	public Operater(JPanel panel){
		this.panel = panel;
		this.width = panel.getWidth();
		this.height = panel.getHeight();
		
		// 배경 이미지 로드
		backgroundImage = new ImageIcon("image/background.gif").getImage();

		// 게임 멈췄을 때 이미지 로드
		try {
			pauseBG = ImageIO.read(new File("image/pauseBG.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 사용할 bgms들을 배정
		bgms[0] = new Sound(Sound.STAGECLEAR);
		bgms[1] = new Sound(Sound.MAINTHEME);
		bgms[2] = new Sound(Sound.STAGE2);
		bgms[3] = new Sound(Sound.STAGE3);
		bgms[4] = new Sound(Sound.BOSS);
		
		// 적 생성 갯수를 초기화
		Enemys.resetVariety();
		
		bulletList = new ArrayList<Bullet>();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();

		player = new Player(new Point(0, height - 50));
		score = new Score();
		
		checker = new Checker();
		checker.start();
		
		bgms[phase].playMusic(false);
		
		panel.addMouseMotionListener(player); // 플레이어 움직임을 위한  리스너
		panel.addKeyListener(new GamePause()); // 게임 일시정지를 위한 리스너 - 현재 포커스 문제로 작동하지 않음
	}

	// 패널에 각 게임 구성요소를 그림
	public void paintAll(Graphics2D g){

		g.drawImage(backgroundImage, 0, 0, null);
		Bullets.paintBullets(bulletList, g);
		Enemys.paintEnemys(enemyList, g);
		Items.paintItems(itemList, g);
		player.drawSelf(g);

		// 만약 PAUSE 와 같이 상태 정보를 표시할 필요가 있으면 그림
		if(isNeedPaintInformation) {
			g.drawImage(pauseBG, 0, 0, null);
			TextLayout text = new TextLayout(information, font, g.getFontRenderContext());
			Rectangle2D rect = text.getBounds();

			g.setColor(Color.WHITE);
			text.draw(g, (width - (float) rect.getWidth())/2, (height - (float) rect.getHeight())/2);
			isNeedPaintInformation = false;
		}
	}

	// 게임이 시작할 때 3개의 스레드를 시작시킴
	public void startGame(){
		if(!isGameOver){
			makeEnemy = new MakeEnemy();
			updateAll = new UpdateAll();
			playerAttack = new PlayerAttack();

			updateAll.start();
			playerAttack.start();
			makeEnemy.start();
		}
	}

	// 게임을 일시정지 시키는 메소드 모든 스레드들이 끝날 떄까지 기다린다. join 사용
	public synchronized void gamePause(){

		isGamePause = true;

		try {
			makeEnemy.join();
			updateAll.join();
			playerAttack.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		panel.repaint();
	}

	/**
	 * 게임을 다시 실행시킨다. startGame()을 호출.
	 */
	public synchronized void gameRestart(){

		isGamePause = false;

		startGame();
	}

	/**
	 * score 객체의 get 메소드
	 * @return 게임 진행 정보를 가지고 있는 객체
	 */
	public Score getScore(){
		return score;
	}

	/**
	 * 페이즈를 바꾸는 메소드
	 */
	private void changePhase(){

		// 페이즈 정보를 바꿈
		bgms[phase].stopMusic();
		score.changePhase();
		phase++;
		bgms[phase].playMusic(false);
		information = score.getPhase();

		// 만약 마지막 페이지면 보스를 생성 적 생성 속도를 2배 빠르게 함
		if(phase == phaseMax){
			information = "BOSS";
			boss = Enemys.makeBoss(enemyList, width);
			makeEnemyInterval /= 2;
			isBossPhase = true;
		}

		isNeedPaintInformation = true;

	}

	/**
	 * 적 객체를 생성하는 스레드
	 *
	 */
	class MakeEnemy extends Thread{
		@Override
		public void run() {

			try {
				Thread.sleep(makeEnemyInterval);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			while(!isGamePause) {

				Enemys.makeEnemy(enemyList, panel.getWidth());

				try {
					Thread.sleep(makeEnemyInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 이 게임의 대한 대부분의 정보를 업데이트하는 스레드
	 * 각 객체들의 위치를 이동시키고, 이동 후 충돌 여부와 화면 밖으로 나갔는지 여부를 확인하고
	 * 모든 것이 끝났으면 repaint()를 호출한다.
	 */
	class UpdateAll extends Thread{
		@Override
		public void run() {

			try {
				Thread.sleep(updateInterval);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			while(!isGamePause) {

				Bullets.moveBullets(bulletList);
				Enemys.moveEnemys(enemyList);
				Items.moveItem(itemList);

				Enemys.checkEnemysDamaged(enemyList, bulletList);
				Enemys.checkEnemyAttackedPlayer(enemyList, player);
				Items.checkItemcrashedPlayer(itemList, player);

				Bullets.checkOutOfScreen(bulletList, 0);
				Enemys.checkOutOfScreen(enemyList, height);
				Items.checkOutOfScreen(itemList, height);

				Enemys.deletEnemys(enemyList, itemList, score);
				Bullets.deletBullets(bulletList);
				Items.deletItem(itemList, score);

				panel.repaint();

				try {
					Thread.sleep(updateInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 플레이어가 일정 시간마다 공격을 하게 하는 스레드
	 */
	class PlayerAttack extends Thread{
		@Override
		public void run() {

			while(!isGamePause) {
				Bullets.makeBullet(bulletList, player.getCurrentBulletType());

				try {
					Thread.sleep(playerAttackInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 게임 전체적인 상황을 확인하고 조정하는 스레드
	 */
	class Checker extends Thread{

		public static final int PAUSE = -1;
		public static final int RESTART = 1;
		public static final int NOTHING = 0;

		// 외부에서 이 스레드 객체에 위 세가지 명령을 넣으면 작동하기 위한 변수
		private int order = NOTHING;

		private int remainingTime = changePhaseInterval;

		@Override
		public void run() {

			while(!isGameOver) {
				if(player.isDead()) {
					gamePause();
					isGameOver = true;

					information = "GAME OVER";
					isNeedPaintInformation = true;
					bgms[phase].stopMusic();

					// 게임 종류 후 패널 바꾸는 메소드 실행
					((GamePanel) panel).changeResultPanel();
				}

				// 명령에 따라 작동
				switch(order) {
				case PAUSE: // 일시정지면 일시정지
					gamePause();

					information = "PAUSE";
					isNeedPaintInformation = true;

					break;
				case RESTART: // 재시작이면 재시작
					gameRestart();

					break;
				default: // 확인 작업
					if(!isGamePause) { // 게임이 멈추지 않았으면 시간 증가 후 페이즈 변환을 위한 남은 시간을 감소
						score.addTime(updateInterval);
						remainingTime -= updateInterval;
					}

					// 보스 페이즈면 보스가 죽었는지 확인하고 죽었으면 안내문과 함께 페이즈 변경
					if(isBossPhase && boss.checkDead()){
						gamePause();

						information = "CLEAR";
						isNeedPaintInformation = true;
						isGameOver = true;
						bgms[phase].stopMusic();
						
						((GamePanel) panel).changeResultPanel();
					}

					// 만약 페이즈 변경을 위한 시간이 적을 경우에 변경할 페이즈가 남아 있으면
					if(remainingTime <= 0 && phase < phaseMax) {

						// 페이즈 변경 남은 시간 업데이트
						remainingTime = changePhaseInterval;

						// 후 페이즈 변경
						gamePause();
						Enemys.changeEnemyVariety();
						changePhase();

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						gameRestart();
					}
					break;
				}

				// 변경 종료를 알리고 명령을 아무것도 없음으로 바꿈
				isInChanging = false;
				order = NOTHING;

				try {
					Thread.sleep(updateInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 외부에서 이 스레드 객체에 명령을 보내기 위한 메소드
		public void setOrder(int order) {
			this.order = order;
		}
	}

	/**
	 * ESC를 누르면 게임이 멈추게 하기 위한 키어댑터 클래스
	 * ESC를 입력 받았을 때, 일시 정지 중이거나 다시 시작중이면 아무 일도 하지 않는다.
	 */
	class GamePause extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e){
			// 입력 키가 ESC이고 현재 바뀌는 중이 아니면 일시정지자 재시작을 실행
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !isInChanging){
				if(!isGamePause){
					isInChanging = true;
					checker.setOrder(Checker.PAUSE);
				}
				else {
					isInChanging = true;
					checker.setOrder(Checker.RESTART);
				}
			}
		}
	}

}
