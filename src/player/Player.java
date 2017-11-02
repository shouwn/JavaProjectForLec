package player;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import bullet.Bullet;
import bullet.Bullets;
import common.Point;

public class Player implements MouseMotionListener{
	
	private Bullet currentBulletType;
	private List<Bullet> bulletList;
	private Point point;
	private BufferedImage currentImage;
	private static BufferedImage[] images = new BufferedImage[3];
	
	private int width, height;
	private int life = 3;
	
	static{
		try {
			images[0] = ImageIO.read(new File("PlayerGreen.gif"));
			images[1] = ImageIO.read(new File("PlayerOrange.gif"));
			images[2] = ImageIO.read(new File("PlayerRed.gif"));
		} catch (IOException e) {
			System.err.println("Fail Load Player Image");
			System.exit(0);
		}
	}
	
	public Player(Point point){

		Bullets.setPlayerPoint(point);
		
		this.currentImage = images[0];
		this.point = point;
		this.currentBulletType = Bullets.TYPE_01;
		this.bulletList = new Vector<Bullet>();
		this.width = currentImage.getWidth();
		this.height = currentImage.getHeight();
	}
	
	public boolean isDead(){
		return life <= 0;
	}
	
	public synchronized void recoverLife(){
		if(++life >= 3)
			life--;
	}
	
	public synchronized void fallLife(){
		if(--life >= 1)
			currentImage = images[3 - life];
	}
	
	// 상관 없다 생각하지만 동기화 주의
	public void changeBulletTypeTo(Bullet bulletType){
		currentBulletType = bulletType;
	}
	
	public void drawSelf(Graphics2D g){
		g.drawImage(currentImage, AffineTransform.getTranslateInstance(point.getX(), point.getY()), null);
	}
	
	public void attack(){
		Bullets.makeBullet(bulletList, currentBulletType);
	}
	
	public void setBulletList(List<Bullet> list){
		this.bulletList = list;
	}
	
	public Bullet getCurrentBulletType() {
		return currentBulletType;
	}
	
	public int getHeight(){
		return currentImage.getHeight();
	}
	
	public int getWidth(){
		return currentImage.getWidth();
	}
	
	public Point getPoint(){
		return point;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		point.setX(e.getX() - width/2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		point.setX(e.getX() - width/2);
	}
}
