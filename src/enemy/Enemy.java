package enemy;

import java.awt.Graphics2D;
import java.util.Map;

import common.Point;
import item.Item;
import player.Player;
/**
 * ������ ���� ��ü�� �����ؾ��� �������̽�
 *
 */
public interface Enemy {
	
	/**
	 * �ڱ� �ڽ��� ���� ��ȯ
	 * @param point �ڽ��� ������ ��ġ ������ ���� �Ķ����
	 * @return ������� ��ü�� ��ȯ
	 */
	Enemy makeSelf(Point point);
	/**
	 * Graphics2D ��ü�� �޾� �ű⿡ �ڽ��� �׸�
	 * @param g ��ü�� �׸� �Ķ����
	 */
	void drawSelf(Graphics2D g);
	/**
	 * �ڽ��� ��ġ�� ������ ���� ���Ͽ� ���� �����̴� �޼ҵ�
	 */
	void moveSelf();
	
	/**
	 * ��ü�� ������ 1 ����
	 */
	void fallLife();
	
	/**
	 * ��ü�� ���� ��������(���� 0) Ȯ��
	 * @return ���� �������� �� ���� ��ȯ
	 */
	boolean checkDead();
	/**
	 * ��ü�� ȭ�� ������ �������� Ȯ��
	 * @return ȭ�� ������ �������� �� ����
	 */
	boolean checkOutOfScreen();
	/**
	 * �Ķ���ͷ� �÷��̾ �޾� �� ��ġ�� ����ؼ� �浹�ߴ��� Ȯ��
	 * @param player ���� ����ڰ� ����ϴ� �÷��̾�
	 * @return �浹�ߴ��� ����
	 */
	boolean isCrashed(Player player);
	
	/**
	 * ��ü�� �������� ������ Ȯ���� ��ȯ�ϴ� �޼ҵ�
	 * @return  ������ ���� Ȯ��
	 */
	int getItemProbability();
	/**
	 * ��ü�� ���̸� �̹����� ���ʷ� �Ͽ� ��ȯ
	 * @return ��ü�� ����
	 */
	int getWidth();
	/**
	 * ��ü�� ���̸� �̹����� ���ʷ� �Ͽ� ��ȯ
	 * @return ��ü�� ����
	 */
	int getHeight();
	/**
	 * ������ getter
	 * @return Point ��ü
	 */
	Point getPoint();
	/**
	 * ��ü�� �� ��¥������ ��ȯ
	 * @return ����
	 */
	int getScore();
	/**
	 * ��ü�� ���� �� �ִ� �������� ���� �� �ۼ�Ʈ�� Ȯ���� ����������� Map���� ��ȯ
	 * @return �� �������� ����Ȯ�� Map
	 */
	Map<Item, Integer> getEachItemProbability();
	
	/**
	 * ��ü�� ��ũ���� ���������ٰ� �����ϴ� �޼ҵ�
	 */
	void setOutOfScreen();
	/**
	 * ��ü�� ���� ���·� ����
	 */
	void setDead();
}
