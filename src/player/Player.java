package player;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import bullet.Bullet;
import bullet.Bullets;
import common.Images;
import common.Point;

public class Player implements MouseMotionListener{

	private Bullet currentBulletType;
	private List<Bullet> bulletList;
	private Point point;
	private BufferedImage currentImage;
	private static BufferedImage[] images = new BufferedImage[3];
	private static BufferedImage[] damagedImages; 
	private AttackedImage attackedImage;

	private boolean isDamagedImages = false;
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

		damagedImages = Images.readImages("image/DamagedPlayer");
	}

	public Player(Point point){

		Bullets.setPlayerPoint(point);

		this.currentImage = images[0];
		this.point = point;
		this.currentBulletType = Bullets.TYPE_01;
		this.bulletList = new ArrayList<Bullet>();
		this.width = currentImage.getWidth();
		this.height = currentImage.getHeight();
	}
	
	public synchronized void setCurrentImage(BufferedImage img){
		currentImage = img;
	}

	public boolean isDead(){
		return life <= 0;
	}

	public synchronized void recoverLife(){
		if(++life >= 3)
			life--;
	}

	public synchronized void fallLife(){
		if(--life >= 1){
			setCurrentImage(images[3 - life]);
			
			if(!isDamagedImages){
				isDamagedImages = true;
				attackedImage = new AttackedImage();
				attackedImage.start();
			}
			else
				attackedImage.restart(images[3 - life]);
		}
	}

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
		return images[0].getHeight();
	}

	public int getWidth(){
		return images[0].getWidth();
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

	class AttackedImage extends Thread{
		private int count = 0; 
		private final int maxCount = 10;
		private int current = 0;

		private BufferedImage originalImage;

		@Override
		public void run(){
			originalImage = currentImage;

			while(count < maxCount){
				setCurrentImage(damagedImages[current++]);
				current = current % damagedImages.length;
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				count++;
			}
			
			isDamagedImages = false;
			setCurrentImage(originalImage);
		}
		
		public void restart(BufferedImage original){
			originalImage = original;
			count = 0;
		}
	}
}
