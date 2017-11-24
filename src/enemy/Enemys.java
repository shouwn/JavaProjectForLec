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
 * 적 객체의 메소드를 호출해서 적 객체를 전체적으로 관리하는 클래스
 *
 */
public class Enemys {

	// 적 객체를 생성할 때 몇 종류를 만들 것인지 결정하는 변수
	private static int enemyVariety = 1;
	// 총 있는 적의 종류를 저장하는 Map
	private static HashMap<Integer, Enemy> enemyType = new HashMap<Integer, Enemy>();
	private static HashMap<Integer, Enemy> bossType = new HashMap<>();

	// 적을 생성할 때 사용할 랜덤 객체
	private static Random random = new Random();

	// Map에 적을 추가
	static {
		enemyType.put(0, new TypeE01());
		enemyType.put(1, new TypeE02());
		enemyType.put(2, new TypeE03());
		enemyType.put(3, new TypeE04());
		enemyType.put(4, new TypeE05());

		bossType.put(1, new Boss());
	}

	/**
	 * 적이 화면 밖으로 나가거나 적의 생명이 0일 때 적을 삭제하는 메소드
	 * @param list 적 객체가 있는 리스트
	 * @param itemList 적이 죽었을 때 아이템을 만들어서 추가할 리스트
	 * @param score 적이 죽었을 때 점수를 올리기 위해 넘겨받은 Score 객체
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
	 * 적을 만드는 메소드
	 * @param list 만든 적을 추가할 리스트
	 * @param area 적이 만들어질 범위 - 가로 길이
	 */
	public static synchronized void makeEnemy(List<Enemy> list, int area) {
		list.addAll(makeEnemyLine(area));
	}


	public static synchronized Boss makeBoss(List<Enemy> list, int area) {
		Enemy e = bossType.get(1);

		Boss boss = (Boss) e.makeSelf(new Point((area - e.getWidth())/2, 0));
		boss.setArea(area);

		list.add(boss);
		return boss;
	}


	/**
	 * 적 객체들을 그리는 메소드
	 * @param list 그릴 적 객체가 있는 리스트
	 * @param g 그리기 위한 {@link Graphics2D} 객체
	 */
	public static synchronized void paintEnemys(List<Enemy> list, Graphics2D g){
		for(int i = 0; i < list.size(); i++) {
			if(!(list.get(i).checkDead() || list.get(i).checkOutOfScreen()))
					list.get(i).drawSelf(g);
		}
	}

	/**
	 * 적 객체들이 자신의 패턴에 따라 이동하게 하는 메소드
	 * @param list 이동 시킬 적 객체가 있는 리스트
	 */
	public static synchronized void moveEnemys(List<Enemy> list){
		for(int i = 0; i < list.size(); i++){
			list.get(i).moveSelf();
		}
	}

	/**
	 * 적 객체들이 화면 밖으로 나갔는지 확인하는 메소드
	 * @param list 적 객체가 들어있는 리스트
	 * @param end 화면의 경계 - 화면 높이
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
	 * 적이 총알과 충돌하였는지 확인하는 메소드
	 * @param enemyList 적 객체가 들어있는 리스트
	 * @param bulletList 총알 객체가 들어있는 리스트
	 */
	public static synchronized void checkEnemysDamaged(List<Enemy> enemyList, List<Bullet> bulletList){


		for(int i = 0; i < enemyList.size(); i++){
			Bullets.checkBulletAttackEnemy(bulletList, enemyList.get(i));
		}
	}

	/**
	 * 적이 플레이어에게 대미지를 줬는지 확인하는 메소드
	 * @param list 적 객체가 들어있는 리스트
	 * @param player 플레이어 객체
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
	 * 적을 한 줄 단위로 만드는 메소드
	 * @param area 만들 수 있는 범위 - 화면 가로
	 * @return 만들어진 줄을 리스트로 반환함
	 */
	private static List<Enemy> makeEnemyLine(int area){

		List<Enemy> line = new ArrayList<Enemy>();
		Enemy e = enemyType.get(random.nextInt(enemyVariety));

		int range = area - e.getWidth() * 2;
		int startLine = 0;
		int randomRange;

		while(true) {

			if(range <= 0)
				break;

			randomRange = random.nextInt(range);

			if(randomRange > 120)
				randomRange = 120;

			startLine += randomRange;


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
	 * 적이 만들어지는 종류를 증가시키는 메소드
	 */
	public static void changeEnemyVariety() {

		if(enemyVariety + 2 > enemyType.size())
			enemyVariety = enemyType.size();
		else
			enemyVariety += 2;
	}
}
