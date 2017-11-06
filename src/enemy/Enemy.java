package enemy;

import java.awt.Graphics2D;
import java.util.Map;

import common.Point;
import item.Item;
import player.Player;

public interface Enemy {
	
	
	Enemy makeSelf(Point point);
	void drawSelf(Graphics2D g);
	void moveSelf();
	
	void fallLife();
	
	boolean checkDead();
	boolean isCrashed(Player player);
	boolean checkOutOfScreen();
	
	void setOutOfScreen();
	int getItemProbability();
	int getWidth();
	int getHeight();
	void setDead();
	Point getPoint();
	int getScore();
	Map<Item, Integer> getEachItemProbability();
	
}
