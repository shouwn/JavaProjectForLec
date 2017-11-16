package common;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

	// �׸��� �׸� �г�
	private JPanel panel;
	private int width, height; //�г��� ���̿� ����

	// �� ��ü��� ��ü�� ������ list
	private List<Bullet> bulletList;
	private List<Enemy> enemyList;
	private List<Item> itemList;
	private Player player;
	private Score score; // ������ �÷��� �ð� ���� �������� ������ ������ �ִ� ��ü

	private int phase = 1; // ���� ������/ 1���� ����
	private int phaseMax = 2; // �ִ� ���� ������
	private Timer timer = new Timer(); // ������ ��ȯ�� ���� ����� Ÿ�̸� ��ü

	// ���� ������ ���� ����� ������
	private Thread makeEnemy;
	private Thread updateAll;
	private Thread playerAttack;

	// �� �������� ����
	private int makeEnemyInterval = 1000;
	private int updateInterval = 30;
	private int playerAttackInterval = 300;

	// ���� ���� �� ����� flag ��
	private boolean isInChanging = false; // ����� �ٲ�� �ִ��� Ȯ���ϴ� flag
	private boolean isGameOver = false;
	private boolean isGamePause = false;

	/**
	 * ������ �����ڷ� ���� ��, list�� �� ��ü, �ʿ��� �����ʸ� �гο� add�Ѵ�.
	 * @param panel �� ������ ����ϴ� �г�
	 */
	public Operater(JPanel panel){
		this.panel = panel;
		this.width = panel.getWidth();
		this.height = panel.getHeight();

		backgroundImage = new ImageIcon("background.gif").getImage();

		bulletList = new ArrayList<Bullet>();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();

		player = new Player(new Point(0, height - 100));
		score = new Score();

		timer.schedule(new ChangePhase(), 4000, 2000);

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
		}
	}

	/**
	 * ������ �Ͻ����� ��Ű�� �޼ҵ� ��� ��������� ���� ������ ��ٸ���. join ���
	 */
	public void gamePause(){

		isGamePause = true;

		try {
			makeEnemy.join();
			updateAll.join();
			playerAttack.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ������ �ٽ� �����Ų��. startGame()�� ȣ��.
	 */
	public void gameRestart(){

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
	 * �÷��̾ �׾����� Ȯ���Ͽ� �׾����� ���� ���� flag�� true�� �Ѵ�.
	 */
	private void checkGameOver(){
		if(player.isDead()){
			gamePause();
			isGameOver = true;
		}
	}

	/**
	 * ����� �ٲٴ� �޼ҵ�
	 */
	private void changePhase(){
		// ���߿� �׸� �׸��� ��� �߰� ����
		score.changePhase();
		phase++;
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
				checkGameOver();

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

			if(flag == PAUSE)
				gamePause();
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
						gamePause();
						Enemys.changeEnemyVariety();
						changePhase();
						try {
							Thread.sleep(1000); // ���߿� �۾��ϱ� ���� �׽�Ʈ�� sleep
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						gameRestart();
					}
				}.start();
			}
			else
				timer.cancel();
		}

	}

	/**
	 * ESC�� ������ ������ ���߰� �ϱ� ���� Ű����� Ŭ����
	 * ESC�� �Է� �޾��� ��, �Ͻ� ���� ���̰ų� �ٽ� �������̸� �ƹ� �ϵ� ���� �ʴ´�.
	 */
	class GamePause extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && !isInChanging){
				if(!isGamePause){
					isInChanging = true;
					System.out.println("testPause");
					new ChangeGameState(PAUSE).start();
				}
				else {
					isInChanging = true;
					new ChangeGameState(RESTART).start();
					System.out.println("testRestart");
				}
			}
		}
	}

}
