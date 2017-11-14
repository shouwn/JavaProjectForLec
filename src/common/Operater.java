package common;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

	private int makeEnemyInterval = 1000;
	private int updateInterval = 30;
	private int playerAttackInterval = 300;

	private boolean isGameOver = false;
	private boolean isGamePause = false;
	private int width, height;
	private Score score;

	public Operater(JPanel panel){
		this.panel = panel;
		this.width = panel.getWidth();
		this.height = panel.getHeight();

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

		Bullets.paintBullets(bulletList, g);
		Enemys.paintEnemys(enemyList, g);
		Items.paintItems(itemList, g);
		player.drawSelf(g);
	}

	public void startGame(){

		new MakeEnemy().start();
		new UpdateAll().start();
		new PlayerAttack().start();
	}

	public void gamePause(){
		isGamePause = true;
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

		@Override
		public void keyPressed(KeyEvent e){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				if(isGamePause)
					gameRestart();
				else
					gamePause();
			}

		}

	}
}
