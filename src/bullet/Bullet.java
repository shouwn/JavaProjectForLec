package bullet;

import java.awt.Graphics2D;
import java.util.List;

import common.Point;
import enemy.Enemy;

public interface Bullet{
	
	List<Bullet> makeSelf();
	void drawSelf(Graphics2D g);
	void moveSelf();
	void setPoint(Point point);
	boolean checkUseful();
	void setUseless();
	Point getPoint();
	boolean isCrashed(Enemy e);

}
