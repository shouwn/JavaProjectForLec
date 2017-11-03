package bullet;

import java.awt.Graphics2D;
import java.util.List;

import common.Point;
import enemy.Enemy;

public class Bullets {
	
	public static final Type01 TYPE_01 = new Type01();
	public static final Type02 TYPE_02 = new Type02();
	
	public static void setPlayerPoint(Point point) {
		TYPE_01.setPoint(point);
		TYPE_02.setPoint(point);
	}
	
	public static synchronized void makeBullet(List<Bullet> list, Bullet type){
		list.add(type.makeSelf());
	}
	
	public static synchronized void paintBullets(List<Bullet> list, Graphics2D g){
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).checkUseful())
				list.get(i).drawSelf(g);
	}
	
	public static synchronized void deletBullets(List<Bullet> list) {
		
		for(int i = 0; i < list.size(); i++) {
			if(!list.get(i).checkUseful())
				list.remove(i--);
		}
	}
	
	public static synchronized void moveBullets(List<Bullet> list){
		for(int i = 0; i < list.size(); i++){
			list.get(i).moveSelf();
		}
	}
	
	public static synchronized void checkOutOfScreen(List<Bullet> list, int end){
		Bullet b;
		
		for(int i = 0; i < list.size(); i++){
			b = list.get(i);
			if(b.getPoint().getY() < end)
				b.setUseless();
		}
	}
	
	public static synchronized int checkBulletAttackEnemy(List<Bullet> list, Enemy e){
		Bullet b;
		
		for(int i = 0; i < list.size(); i++){
			b = list.get(i);
			if(b.isCrashed(e)){
				b.setUseless();
				e.fallLife();
				
				if(e.checkDead())
					return e.getScore();
			}
		}
		
		return 0;
	}
}