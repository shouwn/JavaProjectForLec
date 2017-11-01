package enemy;

import java.awt.Graphics2D;

import common.Point;
import player.Player;

public class TypeE02 implements Enemy{

	@Override
	public Enemy makeSelf(Point point) {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
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
	public void fallLife() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean checkDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCrashed(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
