package item;

import java.awt.Graphics2D;

import common.Point;
import player.Player;

// The score of item that change player's bullet type is 0 

public interface Item {
	
	void drawSelf(Graphics2D g);
	void moveSelf();
	Item makeSelf(Point point);
	
	void setOutOfScreen();
	void setUseless();
	int getScore();
	Point getPoint();
	
	void affectTo(Player player);
	
	boolean isOutOfScreen();
	boolean isUseful();
	boolean isCrashed(Player player);
}
