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
 * ������� ������� Ÿ�̸� �����췯���� �����Ͽ� �� 5��. �� �� 3���� ���� ������ ���� �������̰�
 * �� �ܿ� �� ���� ���� ������ ������ ���� Ÿ�̸� �׽�ũ, �Ͻ������� ���� �����尡 �ִ�.
 * ������ �Ű������� JPanel�� �޾� �� �гο� �׸��� �׸���.
 *
 */

public class Operater {

	// ���� ���߰ų� �� �� �Ű������� ����� ���
	private static final int PAUSE = -1;
	private static final int RESTART = 1;

	// ���
	private Image backgroundImage;
	private BufferedImage pauseBG;

	// �׸��� �׸� �г�
	private JPanel panel;
	private int width, height; //�г��� ���̿� ����

	// �� ��ü��� ��ü�� ������ list
	private List<Bullet> bulletList;
	private List<Enemy> enemyList;
	private List<Item> itemList;
	private Player player;
	private Score score; // ������ �÷��� �ð� ���� �������� ������ ������ �ִ� ��ü

	// �� �������� ����
	private int makeEnemyInterval = 1000;
	private int updateInterval = 30;
	private int playerAttackInterval = 300;
	private int changePhaseInterval = 4000;

	private int phase = 1; // ���� ������/ 1���� ����
	private int phaseMax = 2; // �ִ� ���� ������
	private Timer timer = new Timer(); // ������ ��ȯ�� ���� ����� Ÿ�̸� ��ü
	private Timer timer2 = new Timer();
	private int remainingTime = changePhaseInterval;
	private int phaseTime = 0;

	// ���� ������ ���� ����� ������
	private Thread makeEnemy;
	private Thread updateAll;
	private Thread playerAttack;


	// ���� ���� �� ����� flag ��
	private boolean isInChanging = false; // ����� �ٲ�� �ִ��� Ȯ���ϴ� flag
	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private boolean isChangingPhase = false;

	/**
	 * ������ �����ڷ� ���� ��, list�� �� ��ü, �ʿ��� �����ʸ� �гο� add�Ѵ�.
	 * @param panel �� ������ ����ϴ� �г�
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
	 * �гο� ������ ������ �׸���.
	 * @param g �׸��� ��� Graphics2D ��ü
	 */
	public void paintAll(Graphics2D g){

		g.drawImage(backgroundImage, 0, 0, null);
		Bullets.paintBullets(bulletList, g);
		Enemys.paintEnemys(enemyList, g);
		Items.paintItems(itemList, g);
		player.drawSelf(g);
	}

	/**
	 * ������ �����ϱ� ���� ������ 3���� ���� �����Ѵ�.
	 * ȣ���� ������ ���ο� �����带 ����� �����Ѵ�.
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
	 * ������ �Ͻ����� ��Ű�� �޼ҵ� ��� ��������� ���� ������ ��ٸ���. join ���
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
	 * ������ �ٽ� �����Ų��. startGame()�� ȣ��.
	 */
	public synchronized void gameRestart(){

		isGamePause = false;

		startGame();
	}

	/**
	 * score ��ü�� get �޼ҵ�
	 * @return ���� ���� ������ ������ �ִ� ��ü
	 */
	public Score getScore(){
		return score;
	}

	/**
	 * ���� ���� flag�� true�� �ϰ� ȭ�鿡 ǥ��
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
	 * ����� �ٲٴ� �޼ҵ�
	 */
	private void changePhase(){
		// ���߿� �׸� �׸��� ��� �߰� ����

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
	 * �� ��ü�� �����ϴ� ������
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
	 * �� ������ ���� ��κ��� ������ ������Ʈ�ϴ� ������
	 * �� ��ü���� ��ġ�� �̵���Ű��, �̵� �� �浹 ���ο� ȭ�� ������ �������� ���θ� Ȯ���ϰ�
	 * ������ ��ü���� �����ϴ� �޼ҵ� �������� ������ ����Ǿ����� Ȯ���ϰ� ���� ���� �ð� ������ �����Ѵ�.
	 * ��� ���� �������� repaint()�� ȣ���Ѵ�.
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
	 * �÷��̾ ���� �ð����� ������ �ϰ� �ϴ� ������
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
	 * ������ �Ͻ����� ��Ű�ų� �ٽ� ���۽�Ű�� ������
	 * ���� �� flag�� �޾Ƽ� �� �� � ���� �� ������ ���Ѵ�.
	 */
	class ChangeGameState extends Thread{

		private int flag;

		/**
		 *
		 * @param flag �Ͻ����� ��ų ������, �ٽ� ������ ������ ���ϴ� flag
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
	 * ���� �ð����� ����� �ٲٴ� Ÿ�� �׽�ũ
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
							Thread.sleep(2000); // ���߿� �۾��ϱ� ���� �׽�Ʈ�� sleep
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
	 * ESC�� ������ ������ ���߰� �ϱ� ���� Ű����� Ŭ����
	 * ESC�� �Է� �޾��� ��, �Ͻ� ���� ���̰ų� �ٽ� �������̸� �ƹ� �ϵ� ���� �ʴ´�.
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
