package item;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import common.Point;
import common.Points;
import player.Player;

public class TypeI01 implements Item{

	private float speed;
	private float gravity;
	private int score;
	private Point point;
	private boolean isUseful = true;
	
	private static BufferedImage image;
	
	static{
		try {
			image = ImageIO.read(new File("CoolPeaceLarge.png"));
		} catch (IOException e) {
			System.err.println("Fail Load Bullet Image");
			System.exit(0);
		}
	}
	
	public TypeI01(){
	}
	
	public TypeI01(Point point){
		speed = 20;
		gravity = 2;
		score = 20;
		this.point = point;
	}

	@Override
	public void drawSelf(Graphics2D g) {		
		g.drawImage(image, AffineTransform.getTranslateInstance(point.getX(), point.getY()), null);
	}

	@Override
	public void moveSelf() {
		speed -= gravity;
		point.move(0, speed);
	}

	@Override
	public void setUseless() {
		isUseful = false;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public Point getPoint() {
		return point;
	}

	@Override
	public void affectTo(Player player) {
		// this item just raise the score
	}

	@Override
	public boolean isUseful() {
		return isUseful;
	}

	@Override
	public boolean isCrashed(Player player) {
		
		if(!isUseful())
			return false;
		
		if(Points.checkAreaInArea(
				player.getPoint(), 
				new Point(player.getPoint()).add(player.getWidth(), player.getHeight()),
				this.point, 
				new Point(this.point).add(image.getWidth(), image.getHeight())))
			
			return true;
		
		return false;
	}

	@Override
	public Item makeSelf(Point point) {
		return new TypeI01(point.add(-image.getWidth()/2, 0));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(gravity);
		result = prime * result + (isUseful ? 1231 : 1237);
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		result = prime * result + score;
		result = prime * result + Float.floatToIntBits(speed);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeI01 other = (TypeI01) obj;
		if (Float.floatToIntBits(gravity) != Float.floatToIntBits(other.gravity))
			return false;
		if (isUseful != other.isUseful)
			return false;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		if (score != other.score)
			return false;
		if (Float.floatToIntBits(speed) != Float.floatToIntBits(other.speed))
			return false;
		return true;
	}
	
}
