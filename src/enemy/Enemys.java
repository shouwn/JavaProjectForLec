package enemy;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import bullet.Bullet;
import bullet.Bullets;
import common.Point;
import player.Player;

public class Enemys {
	
	private static int enemyVariety = 1;
	private static HashMap<Integer, Enemy> enemyType = new HashMap<Integer, Enemy>();
	
	static {
		enemyType.put(0, new TypeE01());
		enemyType.put(1, new TypeE02());
	}
	
	public static synchronized void deletEnemys(List<Enemy> list) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).checkDead())
				list.remove(i--);
		}
	}
	
	public static synchronized void makeEnemy(List<Enemy> list, int area) {
		list.addAll(makeEnemyLine(area));
	}
	
	public static synchronized void paintEnemys(List<Enemy> list, Graphics2D g){
		for(int i = 0; i < list.size(); i++)
			list.get(i).drawSelf(g);
	}
	
	public static synchronized void moveEnemys(List<Enemy> list){
		for(int i = 0; i < list.size(); i++){
			list.get(i).moveSelf();
		}
	}
	
	public static synchronized void checkOutOfScreen(List<Enemy> list, int end){
		Enemy e;
		
		for(int i = 0; i < list.size(); i++){
			e = list.get(i);
			if(e.getPoint().getY() > end){
				e.setDead();
			}
				
		}
	}
	
	public static synchronized void checkEnemysDamaged(List<Enemy> enemyList, List<Bullet> bulletList){
		for(int i = 0; i < enemyList.size(); i++){
			Bullets.checkBulletAttackEnemy(bulletList, enemyList.get(i));
		}
	}
	
	public static synchronized void checkEnemyAttackedPlayer(List<Enemy> list, Player player){
		Enemy e;
		
		for(int i = 0; i < list.size(); i++){
			e = list.get(i);
			if(e.isCrashed(player)){
				e.setDead();
				player.fallLife();
			}
		}
	}
	
	private static List<Enemy> makeEnemyLine(int area){

		List<Enemy> line = new ArrayList<Enemy>();
		Random random = new Random();
		Enemy e = enemyType.get(random.nextInt(enemyVariety));
		
		int range = area - e.getWidth() * 2;
		int startLine = 0; 

		while(true) {
			
			if(range <= 0)
				break;
			
			startLine += random.nextInt(range);
			
			if(startLine + e.getWidth() > area)
				break;
			else
				line.add(e.makeSelf(new Point(startLine, 0)));
			
			startLine += e.getWidth();
			range = area - startLine;

			e = enemyType.get(random.nextInt(enemyVariety));
		}
		
		return line;
	}

}
