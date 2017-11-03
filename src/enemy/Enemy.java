package enemy;

import java.awt.Graphics2D;

import common.Point;
import player.Player;

public interface Enemy {
	
	
	Enemy makeSelf(Point point);
	void drawSelf(Graphics2D g);
	void moveSelf();
	
	void fallLife();
	
	boolean checkDead();
	boolean isCrashed(Player player);
	
	int getItemProbability();
	int getWidth();
	int getHeight();
	void setDead();
	Point getPoint();
	int getScore();
	
}
