package enemy;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import bullet.Bullet;
import bullet.Bullets;
import common.Point;
import common.Score;
import item.Item;
import item.Items;
import player.Player;

/**
 * �� ��ü�� �޼ҵ带 ȣ���ؼ� �� ��ü�� ��ü������ �����ϴ� Ŭ����
 *
 */
public class Enemys {
	
	// �� ��ü�� ������ �� �� ������ ���� ������ �����ϴ� ����
	private static int enemyVariety = 1;
	// �� �ִ� ���� ������ �����ϴ� Map
	private static HashMap<Integer, Enemy> enemyType = new HashMap<Integer, Enemy>();
	// ���� ������ �� ����� ���� ��ü
	private static Random random = new Random();
	
	// Map�� ���� �߰�
	static {
		enemyType.put(0, new TypeE01());
		enemyType.put(1, new TypeE02());
	}
	
	/**
	 * ���� ȭ�� ������ �����ų� ���� ������ 0�� �� ���� �����ϴ� �޼ҵ�
	 * @param list �� ��ü�� �ִ� ����Ʈ 
	 * @param itemList ���� �׾��� �� �������� ���� �߰��� ����Ʈ
	 * @param score ���� �׾��� �� ������ �ø��� ���� �Ѱܹ��� Score ��ü
	 */
	public static synchronized void deletEnemys(List<Enemy> list, List<Item> itemList, Score score) {
		Enemy e;
		
		for(int i = 0; i < list.size(); i++) {
			e = list.get(i);
			
			if(e.checkDead()){
				if(random.nextInt(100) + 1 < e.getItemProbability())
					Items.makeItem(itemList, e.getEachItemProbability(), e.getPoint().add(e.getWidth()/2, 0));
				
				score.addScore(e.getScore());
				list.remove(i--);
			}
			
			if(e.checkOutOfScreen())
				list.remove(i--);
		}
	}
	
	/**
	 * ���� ����� �޼ҵ�
	 * @param list ���� ���� �߰��� ����Ʈ
	 * @param area ���� ������� ���� - ���� ����
	 */
	public static synchronized void makeEnemy(List<Enemy> list, int area) {
		list.addAll(makeEnemyLine(area));
	}
	
	/**
	 * �� ��ü���� �׸��� �޼ҵ�
	 * @param list �׸� �� ��ü�� �ִ� ����Ʈ
	 * @param g �׸��� ���� {@link Graphics2D} ��ü
	 */
	public static synchronized void paintEnemys(List<Enemy> list, Graphics2D g){
		for(int i = 0; i < list.size(); i++) {
			if(!(list.get(i).checkDead() || list.get(i).checkOutOfScreen()))
					list.get(i).drawSelf(g);
		}
	}
	
	/**
	 * �� ��ü���� �ڽ��� ���Ͽ� ���� �̵��ϰ� �ϴ� �޼ҵ�
	 * @param list �̵� ��ų �� ��ü�� �ִ� ����Ʈ
	 */
	public static synchronized void moveEnemys(List<Enemy> list){
		for(int i = 0; i < list.size(); i++){
			list.get(i).moveSelf();
		}
	}
	
	/**
	 * �� ��ü���� ȭ�� ������ �������� Ȯ���ϴ� �޼ҵ�
	 * @param list �� ��ü�� ����ִ� ����Ʈ
	 * @param end ȭ���� ��� - ȭ�� ����
	 */
	public static synchronized void checkOutOfScreen(List<Enemy> list, int end){
		Enemy e;
		
		for(int i = 0; i < list.size(); i++){
			e = list.get(i);
			if(e.getPoint().getY() > end){
				e.setOutOfScreen();
			}
				
		}
	}
	
	/**
	 * ���� �Ѿ˰� �浹�Ͽ����� Ȯ���ϴ� �޼ҵ�
	 * @param enemyList �� ��ü�� ����ִ� ����Ʈ
	 * @param bulletList �Ѿ� ��ü�� ����ִ� ����Ʈ
	 */
	public static synchronized void checkEnemysDamaged(List<Enemy> enemyList, List<Bullet> bulletList){
		
		
		for(int i = 0; i < enemyList.size(); i++){
			Bullets.checkBulletAttackEnemy(bulletList, enemyList.get(i));
		}
	}
	
	/**
	 * ���� �÷��̾�� ������� ����� Ȯ���ϴ� �޼ҵ�
	 * @param list �� ��ü�� ����ִ� ����Ʈ
	 * @param player �÷��̾� ��ü
	 */
	public static synchronized void checkEnemyAttackedPlayer(List<Enemy> list, Player player){
		Enemy e;
		
		for(int i = 0; i < list.size(); i++){
			e = list.get(i);
			
			if(e.checkDead())
				continue;
			
			if(e.isCrashed(player)){
				e.setDead();
				player.fallLife();
			}
		}
	}
	
	/**
	 * ���� �� �� ������ ����� �޼ҵ�
	 * @param area ���� �� �ִ� ���� - ȭ�� ����
	 * @return ������� ���� ����Ʈ�� ��ȯ��
	 */
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

	/**
	 * ���� ��������� ������ ������Ű�� �޼ҵ�
	 */
	public static void changeEnemyVariety() {
		enemyVariety++;
	}
}
