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
	
}
