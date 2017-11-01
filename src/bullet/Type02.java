package bullet;

import java.awt.Graphics2D;

import common.Point;
import enemy.Enemy;

public class Type02 implements Bullet{

	public Type02() {
		
	}
	
	public Type02(Point point) {
		
	}
	
	@Override
	public Bullet makeSelf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawSelf(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveSelf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPoint(Point point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkUseful() {
		return false;
	}

	@Override
	public void setUseless() {
	}

	@Override
	public Point getPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCrashed(Enemy e) {
		// TODO Auto-generated method stub
		return false;
	}
}
