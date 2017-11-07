package enemy;

import java.awt.Graphics2D;
import java.util.Map;

import common.Point;
import item.Item;
import player.Player;
/**
 * 적으로 만들 객체가 구현해야할 인터페이스
 *
 */
public interface Enemy {
	
	/**
	 * 자기 자신을 만들어서 반환
	 * @param point 자신을 생성할 위치 정보를 가진 파라미터
	 * @return 만들어진 객체를 반환
	 */
	Enemy makeSelf(Point point);
	/**
	 * Graphics2D 객체를 받아 거기에 자신을 그림
	 * @param g 객체를 그릴 파라미터
	 */
	void drawSelf(Graphics2D g);
	/**
	 * 자신의 위치를 스스로 정한 패턴에 따라 움직이는 메소드
	 */
	void moveSelf();
	
	/**
	 * 객체의 생명을 1 줄임
	 */
	void fallLife();
	
	/**
	 * 객체가 죽은 상태인지(생명 0) 확인
	 * @return 죽은 상태인지 참 거짓 반환
	 */
	boolean checkDead();
	/**
	 * 객체가 화면 밖으로 나갔는지 확인
	 * @return 화면 밖으로 나갔는지 참 거짓
	 */
	boolean checkOutOfScreen();
	/**
	 * 파라메터로 플레이어를 받아 그 위치를 계산해서 충돌했는지 확인
	 * @param player 현재 사용자가 사용하는 플레이어
	 * @return 충돌했는지 여부
	 */
	boolean isCrashed(Player player);
	
	/**
	 * 객체가 아이템을 생성할 확률을 반환하는 메소드
	 * @return  아이템 생성 확률
	 */
	int getItemProbability();
	/**
	 * 객체의 넓이를 이미지를 기초로 하여 반환
	 * @return 객체의 넓이
	 */
	int getWidth();
	/**
	 * 객체의 높이를 이미지를 기초로 하여 반환
	 * @return 객체의 높이
	 */
	int getHeight();
	/**
	 * 포인터 getter
	 * @return Point 객체
	 */
	Point getPoint();
	/**
	 * 객체가 몇 점짜리인지 반환
	 * @return 점수
	 */
	int getScore();
	/**
	 * 객체가 만들 수 있는 아이템이 각각 몇 퍼센트의 확률로 만들어지는지 Map으로 반환
	 * @return 각 아이템의 생성확률 Map
	 */
	Map<Item, Integer> getEachItemProbability();
	
	/**
	 * 객체가 스크린을 빠져나갔다고 설정하는 메소드
	 */
	void setOutOfScreen();
	/**
	 * 객체를 죽은 상태로 만듬
	 */
	void setDead();
}
