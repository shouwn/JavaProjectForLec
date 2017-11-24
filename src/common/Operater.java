package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

/**
 * ������� ������� Ÿ�̸� �����췯���� �����Ͽ� �� 5��. �� �� 3���� ���� ������ ���� �������̰�
 * �� �ܿ� �� ���� ���� ������ ������ ���� Ÿ�̸� �׽�ũ, �Ͻ������� ���� �����尡 �ִ�.
 * ������ �Ű������� JPanel�� �޾� �� �гο� �׸��� �׸���.
 *
 */

public class Operater {

	private String information;

	private Boss boss;

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
	private int changePhaseInterval = 10000;

	private int phase = 1; // ���� ������/ 1���� ����
	private int phaseMax = 4; // �ִ� ���� ������

	// ���� ������ ���� ����� ������
	private Thread makeEnemy;
	private Thread updateAll;
	private Thread playerAttack;
	private Checker checker;

	// ���� ���� �� ����� flag ��
	private boolean isInChanging = false; // ����� �ٲ�� �ִ��� Ȯ���ϴ� flag
	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private boolean isNeedPaintInformation = false;
	private boolean isBossPhase = false;

	private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 60);

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

		checker = new Checker();
		checker.start();

		panel.addMouseMotionListener(player);
		panel.addKeyListener(new GamePause());
		panel.addMouseListener(new GameMousePause());
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

		if(isNeedPaintInformation) {
			g.drawImage(pauseBG, 0, 0, null);
			TextLayout text = new TextLayout(information, font, g.getFontRenderContext());
			Rectangle2D rect = text.getBounds();

			g.setColor(Color.WHITE);
			text.draw(g, (width - (float) rect.getWidth())/2, (height - (float) rect.getHeight())/2);
			isNeedPaintInformation = false;
		}
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

			updateAll.start();
			playerAttack.start();
			makeEnemy.start();
		}
	}

	/**
	 * ������ �Ͻ����� ��Ű�� �޼ҵ� ��� ��������� ���� ������ ��ٸ���. join ���
	 */
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
	 * ����� �ٲٴ� �޼ҵ�
	 */
	private void changePhase(){
		// ���߿� �׸� �׸��� ��� �߰� ����

		score.changePhase();
		phase++;

		information = score.getPhase();

		if(phase == phaseMax){
			information = "BOSS";
			boss = Enemys.makeBoss(enemyList, width);
			makeEnemyInterval /= 2;
			isBossPhase = true;
		}

		isNeedPaintInformation = true;
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

	class Checker extends Thread{

		public static final int PAUSE = -1;
		public static final int RESTART = 1;
		public static final int NOTHING = 0;

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
				}

				switch(order) {
				case PAUSE:
					gamePause();

					information = "PAUSE";
					isNeedPaintInformation = true;

					break;
				case RESTART:
					gameRestart();

					break;
				default:
					if(!isGamePause) {
						score.addTime(updateInterval);
						remainingTime -= updateInterval;
					}

					if(isBossPhase && boss.checkDead()){
						gamePause();

						information = "CLEAR";
						isNeedPaintInformation = true;
						isGameOver = true;
					}


					if(remainingTime <= 0 && phase < phaseMax) {

						remainingTime = changePhaseInterval;

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

		public void setOrder(int order) {
			this.order = order;
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
					checker.setOrder(Checker.PAUSE);
				}
				else {
					isInChanging = true;
					checker.setOrder(Checker.RESTART);
				}
			}
		}
	}

	class GameMousePause extends MouseAdapter{
		@Override
		public void mouseExited(MouseEvent e){
			if(!isInChanging && !isGamePause){
				isInChanging = true;
				checker.setOrder(Checker.PAUSE);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e){
			if(!isInChanging && isGamePause){
				isInChanging = true;
				checker.setOrder(Checker.RESTART);
			}
		}


	}

}
