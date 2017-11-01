package enemy;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Point;
import common.Points;
import player.Player;

public class TypeE01 implements Enemy{
	
	private int score;
	private int life;
	private Point point;
	private float speed;
	private static BufferedImage image;
	
	static {
		try {
			image = ImageIO.read(new File("TteokBokki.gif"));
		} catch (IOException e) {
			System.err.println("Fail Load Bullet Image");
			System.exit(0);
		}
	}
	
	public TypeE01() {
		
	}
	
	public TypeE01(Point point) {
		score = 10;
		speed = 10;
		life = 1;
		
		this.point = point;
	}

	@Override
	public Enemy makeSelf(Point point) {
		return new TypeE01(point);
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public void drawSelf(Graphics2D g) {
		g.drawImage(image, AffineTransform.getTranslateInstance(point.getX(), point.getY()), null);
	}

	@Override
	public void moveSelf() {
		point.move(0, -speed);
	}

	@Override
	public void fallLife() {
		life--;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public boolean checkDead() {
		return life <= 0;
	}

	@Override
	public void setDead() {
		life = 0;
	}
	
	@Override
	public Point getPoint(){
		return point;
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public boolean isCrashed(Player player) {
		
		if(checkDead())
			return false;
		
		if(Points.checkAreaInArea(
				player.getPoint(), 
				new Point(player.getPoint()).add(player.getWidth(), player.getHeight()),
				this.point, 
				new Point(this.point).add(getWidth(), getHeight())))
			
			return true;
		
		return false;
	}


}