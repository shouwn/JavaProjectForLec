package common;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JPanel;

import bullet.Bullet;
import bullet.Bullets;
import enemy.Enemy;
import enemy.Enemys;
import item.Item;
import item.Items;
import player.Player;

public class Operater {

	private JPanel panel;

	private List<Bullet> bulletList;
	private List<Enemy> enemyList;
	private List<Item> itemList;
	private Player player;
	private int phase = 1;
	private int phaseMax = 2;
	private Timer timer = new Timer();
	private final Lock suspendLock = new ReentrantLock();
	private final Condition notPause = suspendLock.newCondition();
	private final Condition notRestart = suspendLock.newCondition();

	private BufferedImage currentBackgroundImage;
	private BufferedImage[] images;

	private Thread makeEnemy;
	private Thread updateAll;
	private Thread playerAttack;
	private Thread updateBG;

	private int makeEnemyInterval = 1000;
	private int updateInterval = 30;
	private int playerAttackInterval = 300;

	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private boolean isThreadStop = false;

	private int width, height;
	private Score score;

	public Operater(JPanel panel){
		this.panel = panel;
		this.width = panel.getWidth();
		this.height = panel.getHeight();

		images = Images.readImages("image/background");
		currentBackgroundImage = images[0];

		bulletList = new ArrayList<Bullet>();
		enemyList = new ArrayList<Enemy>();
		itemList = new ArrayList<Item>();
		player = new Player(new Point(0, height - 100));

		score = new Score();

		timer.schedule(new ChangePhase(), 4000, 2000);

		panel.addMouseMotionListener(player);
		panel.addKeyListener(new GamePause());
	}

	public void paintAll(Graphics2D g){

		g.drawImage(currentBackgroundImage, 0, 0, 500, 700, null);
		Bullets.paintBullets(bulletList, g);
		Enemys.paintEnemys(enemyList, g);
		Items.paintItems(itemList, g);
		player.drawSelf(g);
	}

	public void startGame(){
		if(!isGameOver){
			makeEnemy = new MakeEnemy();
			updateAll = new UpdateAll();
			playerAttack = new PlayerAttack();
			updateBG = new UpdateBG();

			makeEnemy.start();
			updateAll.start();
			playerAttack.start();
			updateBG.start();

			isThreadStop = false;
		}
	}

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

		isThreadStop = true;

	}

	public void gameRestart(){

		isGamePause = false;

		startGame();
	}

	public Score getScore(){
		return score;
	}

	private void checkGameOver(){
		if(player.isDead()){
			gamePause();
			isGameOver = true;
		}
	}

	private void changePhase(){
		score.changePhase();
		phase++;
	}

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
							Thread.sleep(1000); // 나중에 작업하기 위한 테스트용 sleep
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

	class GamePause extends KeyAdapter{
		private boolean isTaskOver = true;

		@Override
		public void keyPressed(KeyEvent e){
			System.out.println(isTaskOver);
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && isTaskOver){
				if(!isGamePause){
					isTaskOver = false;
					System.out.println("testPause");
					gamePause();
					isTaskOver = true;
				}
				else if(isThreadStop){
					isTaskOver = false;
					System.out.println("testRestart");
					gameRestart();
					isTaskOver = true;
				}
			}
		}
	}

	class UpdateBG extends Thread{
		private int current = 0;

		@Override
		public void run(){
			while(!isGamePause){
				current = (current + 1) % images.length;
				currentBackgroundImage = images[current];
				try {
					Thread.sleep(90);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
