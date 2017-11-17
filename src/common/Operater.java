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
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import bullet.Bullet;
import bullet.Bullets;
import enemy.Enemy;
import enemy.Enemys;
import item.Item;
import item.Items;
import player.Player;

/**
 * 사용중인 스레드는 타이머 스케쥴러까지 포함하여 총 5개. 그 중 3개는 게임 구동을 위한 스레드이고
 * 그 외에 두 개는 각각 페이즈 변경을 위한 타이머 테스크, 일시정지를 위한 스레드가 있다.
 * 생성시 매개변수로 JPanel을 받아 그 패널에 그림을 그린다.
 *
 */

public class Operater {

	// 게임 멈추거나 할 때 매개변수로 사용할 상수
	private static final int PAUSE = -1;
	private static final int RESTART = 1;

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
	private int changePhaseInterval = 4000;

	private int phase = 1; // 현재 페이즈/ 1부터 시작
	private int phaseMax = 2; // 최대 진행 페이즈
	private Timer timer = new Timer(); // 페이즈 변환을 위해 사용할 타이머 객체
	private Timer timer2 = new Timer();
	private int remainingTime = changePhaseInterval;
	private int phaseTime = 0;

	// 게임 구동을 위해 사용할 스레드
	private Thread makeEnemy;
	private Thread updateAll;
	private Thread playerAttack;


	// 게임 구동 시 사용할 flag 들
	private boolean isInChanging = false; // 페이즈가 바뀌고 있는지 확인하는 flag
	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private boolean isChangingPhase = false;

	/**
	 * 유일한 생성자로 생성 시, list와 각 객체, 필요한 리스너를 패널에 add한다.
	 * @param panel 이 게임이 사용하는 패널
	 */
	public Operater(JPanel panel){
		this.panel = panel;
		this.width = panel.getWidth();
		this.height = panel.getHeight();

		backgroundImage = new ImageIcon("image/background.gif").getImage();

		try {
			pauseBG = ImageIO.read(new File("image/pauseBG.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bulletList = new ArrayList<Bullet>();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();

		player = new Player(new Point(0, height - 100));
		score = new Score();


		panel.addMouseMotionListener(player);
		panel.addKeyListener(new GamePause());
	}

	/**
	 * 패널에 게임의 내용을 그린다.
	 * @param g 그리는 대상 Graphics2D 객체
	 */
	public void paintAll(Graphics2D g){

		g.drawImage(backgroundImage, 0, 0, null);
		Bullets.paintBullets(bulletList, g);
		Enemys.paintEnemys(enemyList, g);
		Items.paintItems(itemList, g);
		player.drawSelf(g);
	}

	/**
	 * 게임을 실행하기 위한 스레드 3개를 새로 시작한다.
	 * 호출할 때마다 새로운 스레드를 만들어 시작한다.
	 */
	public void startGame(){
		if(!isGameOver){
			makeEnemy = new MakeEnemy();
			updateAll = new UpdateAll();
			playerAttack = new PlayerAttack();

			makeEnemy.start();
			updateAll.start();
			playerAttack.start();

			timer = new Timer();
			timer2 = new Timer();

			timer.schedule(new ChangePhase(), remainingTime, changePhaseInterval);
			timer2.schedule(new CheckPlayerDead(), 0, updateInterval);
		}
	}

	/**
	 * 게임을 일시정지 시키는 메소드 모든 스레드들이 끝날 떄까지 기다린다. join 사용
	 */
	public synchronized void gamePause(){

		isGamePause = true;

		try {
			updateAll.join();
			makeEnemy.join();
			playerAttack.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		timer.cancel();

		remainingTime = changePhaseInterval - (score.getTimeInteger() - phaseTime);
		timer2.cancel();

		if(remainingTime < 0)
			remainingTime = 0;

		((Graphics2D) panel.getGraphics()).drawImage(pauseBG, 0, 0, null);

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
	 * 게임 종료 flag를 true로 하고 화면에 표시
	 */
	private void gameOver(){
		gamePause();
		isGameOver = true;

		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 60);

		Graphics2D g = (Graphics2D) panel.getGraphics();

		TextLayout text = new TextLayout("GAME OVER", font, g.getFontRenderContext());
		Rectangle2D rect = text.getBounds();

		g.setColor(Color.WHITE);
		text.draw(g, (width - (float) rect.getWidth())/2, (height - (float) rect.getHeight())/2);

	}

	/**
	 * 페이즈를 바꾸는 메소드
	 */
	private void changePhase(){
		// 나중에 그림 그리는 기능 추가 예정

		score.changePhase();
		phase++;

		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 60);

		Graphics2D g = (Graphics2D) panel.getGraphics();

		TextLayout text = new TextLayout(score.getPhase(), font, g.getFontRenderContext());
		Rectangle2D rect = text.getBounds();

		g.setColor(Color.WHITE);
		text.draw(g, (width - (float) rect.getWidth())/2, (height - (float) rect.getHeight())/2);
	}

	/**
	 * 적 객체를 생성하는 스레드
	 *
	 */
	class MakeEnemy extends Thread{
		@Override
		public void run() {
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
	 * 삭제할 객체들을 삭제하는 메소드 마지막에 게임이 종료되었는지 확인하고 게임 실행 시간 정보를 갱신한다.
	 * 모든 것이 끝났으면 repaint()를 호출한다.
	 */
	class UpdateAll extends Thread{
		@Override
		public void run() {
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

				score.addTime(updateInterval);
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

	/**
	 * 게임을 일시정지 시키거나 다시 시작시키는 스레드
	 * 생성 시 flag를 받아서 둘 중 어떤 일을 할 것인지 정한다.
	 */
	class ChangeGameState extends Thread{

		private int flag;

		/**
		 *
		 * @param flag 일시정지 시킬 것인지, 다시 시작할 것인지 정하는 flag
		 */
		public ChangeGameState(int flag){
			this.flag = flag;
		}

		@Override
		public void run(){

			if(flag == PAUSE){
				gamePause();

				Font font = new Font(Font.SANS_SERIF, Font.BOLD, 60);

				Graphics2D g = (Graphics2D) panel.getGraphics();

				TextLayout text = new TextLayout("PAUSE", font, g.getFontRenderContext());
				Rectangle2D rect = text.getBounds();

				g.setColor(Color.WHITE);
				text.draw(g, (width - (float) rect.getWidth())/2, (height - (float) rect.getHeight())/2);

			}
			else if(flag == RESTART)
				gameRestart();

			isInChanging = false;
		}
	}

	/**
	 * 일정 시간마다 페이즈를 바꾸는 타임 테스크
	 *
	 */
	class ChangePhase extends TimerTask{

		@Override
		public void run() {

			if(phase < phaseMax) {

				new  Thread(){
					@Override
					public void run(){

						isChangingPhase = true;

						gamePause();
						Enemys.changeEnemyVariety();
						changePhase();

						try {
							Thread.sleep(2000); // 나중에 작업하기 위한 테스트용 sleep
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						gameRestart();
						phaseTime = score.getTimeInteger();
						isChangingPhase = false;
					}
				}.start();
			}
			else
				timer.cancel();
		}

	}

	class CheckPlayerDead extends TimerTask{

		@Override
		public void run(){
			if(player.isDead()){
				gameOver();
				timer2.cancel();
			}
		}
	}

	/**
	 * ESC를 누르면 게임이 멈추게 하기 위한 키어댑터 클래스
	 * ESC를 입력 받았을 때, 일시 정지 중이거나 다시 시작중이면 아무 일도 하지 않는다.
	 */
	class GamePause extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !isInChanging && !isChangingPhase){
				if(!isGamePause){
					isInChanging = true;
					new ChangeGameState(PAUSE).start();
				}
				else {
					isInChanging = true;
					new ChangeGameState(RESTART).start();
				}
			}
		}
	}

}
