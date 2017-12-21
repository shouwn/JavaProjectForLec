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
	private int changePhaseInterval = 30000;

	private int phase = 1; // ���� ������/ 1���� ����
	private int phaseMax = 4; // �ִ� ���� ������

	// ���� ������ ���� ����� ������ 4��
	private Thread makeEnemy; // ���� ����� ������
	private Thread updateAll; // ��� ��������� ��ġ�� �ǰ� ������ �ϴ� ������
	private Thread playerAttack; // �÷��̾��� ������ ���� ������
	private Checker checker; // ���� ����� �ٲ������, �Ͻ��������� ������ �������� Ȯ���ϴ� ������

	// ���� ���� �� ����� flag ��
	private boolean isInChanging = false; // ����� �ٲ�� �ִ��� Ȯ���ϴ� flag
	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private boolean isNeedPaintInformation = false; // �Ͻ������� ���� ������ ǥ���� �ʿ䰡 �ִ��� �ʿ��Ѱ�
	private boolean isBossPhase = false; 

	//���������� ����� ��Ʈ
	private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 60);

	// ���� ���� ����� bgm��
	private Sound[] bgms = new Sound[5];
	
	// ������, �Ķ���ͷ� �� ������ �׸� �г��� �Ѱܹ���
	public Operater(JPanel panel){
		this.panel = panel;
		this.width = panel.getWidth();
		this.height = panel.getHeight();
		
		// ��� �̹��� �ε�
		backgroundImage = new ImageIcon("image/background.gif").getImage();

		// ���� ������ �� �̹��� �ε�
		try {
			pauseBG = ImageIO.read(new File("image/pauseBG.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ����� bgms���� ����
		bgms[0] = new Sound(Sound.STAGECLEAR);
		bgms[1] = new Sound(Sound.MAINTHEME);
		bgms[2] = new Sound(Sound.STAGE2);
		bgms[3] = new Sound(Sound.STAGE3);
		bgms[4] = new Sound(Sound.BOSS);
		
		// �� ���� ������ �ʱ�ȭ
		Enemys.resetVariety();
		
		bulletList = new ArrayList<Bullet>();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();

		player = new Player(new Point(0, height - 50));
		score = new Score();
		
		checker = new Checker();
		checker.start();
		
		bgms[phase].playMusic(false);
		
		panel.addMouseMotionListener(player); // �÷��̾� �������� ����  ������
		panel.addKeyListener(new GamePause()); // ���� �Ͻ������� ���� ������ - ���� ��Ŀ�� ������ �۵����� ����
	}

	// �гο� �� ���� ������Ҹ� �׸�
	public void paintAll(Graphics2D g){

		g.drawImage(backgroundImage, 0, 0, null);
		Bullets.paintBullets(bulletList, g);
		Enemys.paintEnemys(enemyList, g);
		Items.paintItems(itemList, g);
		player.drawSelf(g);

		// ���� PAUSE �� ���� ���� ������ ǥ���� �ʿ䰡 ������ �׸�
		if(isNeedPaintInformation) {
			g.drawImage(pauseBG, 0, 0, null);
			TextLayout text = new TextLayout(information, font, g.getFontRenderContext());
			Rectangle2D rect = text.getBounds();

			g.setColor(Color.WHITE);
			text.draw(g, (width - (float) rect.getWidth())/2, (height - (float) rect.getHeight())/2);
			isNeedPaintInformation = false;
		}
	}

	// ������ ������ �� 3���� �����带 ���۽�Ŵ
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

	// ������ �Ͻ����� ��Ű�� �޼ҵ� ��� ��������� ���� ������ ��ٸ���. join ���
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

		// ������ ������ �ٲ�
		bgms[phase].stopMusic();
		score.changePhase();
		phase++;
		bgms[phase].playMusic(false);
		information = score.getPhase();

		// ���� ������ �������� ������ ���� �� ���� �ӵ��� 2�� ������ ��
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
	 * �� ������ ���� ��κ��� ������ ������Ʈ�ϴ� ������
	 * �� ��ü���� ��ġ�� �̵���Ű��, �̵� �� �浹 ���ο� ȭ�� ������ �������� ���θ� Ȯ���ϰ�
	 * ��� ���� �������� repaint()�� ȣ���Ѵ�.
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

	/*
	 * ���� ��ü���� ��Ȳ�� Ȯ���ϰ� �����ϴ� ������
	 */
	class Checker extends Thread{

		public static final int PAUSE = -1;
		public static final int RESTART = 1;
		public static final int NOTHING = 0;

		// �ܺο��� �� ������ ��ü�� �� ������ ����� ������ �۵��ϱ� ���� ����
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

					// ���� ���� �� �г� �ٲٴ� �޼ҵ� ����
					((GamePanel) panel).changeResultPanel();
				}

				// ��ɿ� ���� �۵�
				switch(order) {
				case PAUSE: // �Ͻ������� �Ͻ�����
					gamePause();

					information = "PAUSE";
					isNeedPaintInformation = true;

					break;
				case RESTART: // ������̸� �����
					gameRestart();

					break;
				default: // Ȯ�� �۾�
					if(!isGamePause) { // ������ ������ �ʾ����� �ð� ���� �� ������ ��ȯ�� ���� ���� �ð��� ����
						score.addTime(updateInterval);
						remainingTime -= updateInterval;
					}

					// ���� ������� ������ �׾����� Ȯ���ϰ� �׾����� �ȳ����� �Բ� ������ ����
					if(isBossPhase && boss.checkDead()){
						gamePause();

						information = "CLEAR";
						isNeedPaintInformation = true;
						isGameOver = true;
						bgms[phase].stopMusic();
						
						((GamePanel) panel).changeResultPanel();
					}

					// ���� ������ ������ ���� �ð��� ���� ��쿡 ������ ����� ���� ������
					if(remainingTime <= 0 && phase < phaseMax) {

						// ������ ���� ���� �ð� ������Ʈ
						remainingTime = changePhaseInterval;

						// �� ������ ����
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

				// ���� ���Ḧ �˸��� ����� �ƹ��͵� �������� �ٲ�
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

		// �ܺο��� �� ������ ��ü�� ����� ������ ���� �޼ҵ�
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
			// �Է� Ű�� ESC�̰� ���� �ٲ�� ���� �ƴϸ� �Ͻ������� ������� ����
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
