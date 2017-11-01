package enemy;

import java.awt.Graphics2D;

import common.Point;
import player.Player;

public interface Enemy {
	
	
	Enemy makeSelf(Point point);
	void drawSelf(Graphics2D g);
	void moveSelf();
	
	void fallLife();
	
	int getScore();
	boolean checkDead();
	int getWidth();
	int getHeight();
	void setDead();
	Point getPoint();
	
	boolean isCrashed(Player player);
}
