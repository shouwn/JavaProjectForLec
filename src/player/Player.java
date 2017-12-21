package player;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import bullet.Bullet;
import bullet.Bullets;
import common.Images;
import common.Point;
import common.Sound;

public class Player implements MouseMotionListener{

	// ���� �÷��̾� �Ѿ� Ÿ�԰� ��ǥ
	private Bullet currentBulletType;
	private Point point;

	// �÷��̾� �̹���
	private BufferedImage currentImage;
	private static BufferedImage[] images = new BufferedImage[3];
	private static BufferedImage[] damagedImages;

	// �ǰ� �̹����� ���� ������� ���� �ð� �Ѿ� Ÿ���� �ٲ�� ������ 
	private AttackedImage attackedImage;
	private ChangeItemBullet changeItemBullet;

	// flag�� �̹��� ���� ����, ���
	private boolean isChangedBullet = false;
	private boolean isDamagedImages = false;
	private int width, height;
	private int life = 3;
	
	// �̹��� �ε�
	static{
		try {
			images[0] = ImageIO.read(new File("image/Player/PlayerGreen.gif"));
			images[1] = ImageIO.read(new File("image/Player/PlayerOrange.gif"));
			images[2] = ImageIO.read(new File("image/Player/PlayerRed.gif"));
		} catch (IOException e) {
			System.err.println("Fail Load Player Image");
			System.exit(0);
		}

		damagedImages = Images.readImages("image/Player/DamagedPlayer");
	}

	// ������
	public Player(Point point){

		Bullets.setPlayerPoint(point);

		this.currentImage = images[0];
		this.point = point;
		this.currentBulletType = Bullets.TYPE_01; // �⺻ �Ѿ� Ÿ���� 1
		this.width = currentImage.getWidth();
		this.height = currentImage.getHeight();
	}

	// ���� �̹����� �ٲٱ� ���� �޼ҵ�
	public synchronized void setCurrentImage(BufferedImage img){
		currentImage = img;
	}

	// �׾����� üũ
	public boolean isDead(){
		return life <= 0;
	}

	// �÷��̾� ����� ȸ��
	public synchronized void recoverLife(int value){
		life += value;
		if(life > 3)
			life = 3;
		else
			new Sound(Sound.HEALED).playMusic(false);		
		
		// ȸ�� �� �̹��� ����
		setCurrentImage(images[3 - life]);
		
		// ���� �ǰ� �̹��� ���̸�
		if(attackedImage != null)
			this.attackedImage.setOriginal(images[3 - life]); // �ǰݽ� ���� �̹����� ���ƿ��� ����
	}

	// ������ �ٿ�
	public synchronized void fallLife(){
		if(--life >= 1){
			setCurrentImage(images[3 - life]); // �̹��� �ٲ�

			// ���� �ǰ� �̹��� ���°� �ƴϸ� �ǰ��̹����� ����
			if(!isDamagedImages){
				new Sound(Sound.HOT).playMusic(false);
				isDamagedImages = true;
				attackedImage = new AttackedImage();
				attackedImage.start();
			}
			else // �ƴϸ� �ǰ� ������ ���;��ϴ� �̹����� ���� �� �ǰ� �̹��� �����
				attackedImage.restart(images[3 - life]);
				

			
		}
		else  // ���� �������� 1���� ������ ���� ����
			new Sound(Sound.GAMEOVER).playMusic(false);

	}

	// �Ѿ� Ÿ���� �Ű������� ���� ������ �ٲ�
	public void changeBulletTypeTo(Bullet bulletType){
		if(!isChangedBullet){
			isChangedBullet = true;
			changeItemBullet = new ChangeItemBullet(bulletType);
			changeItemBullet.start();
		}
		else
			changeItemBullet.restart(bulletType);
	}

	public void drawSelf(Graphics2D g){
		g.drawImage(currentImage, AffineTransform.getTranslateInstance(point.getX(), point.getY()), null);
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

	// �÷��̾� �������� ���� ������
	@Override
	public void mouseDragged(MouseEvent e) {
		point.setX(e.getX() - width/2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		point.setX(e.getX() - width/2);
	}

	// �ǰݽ� �̹����� ���� ������
	class AttackedImage extends Thread{
		private int count = 0;
		private final int maxCount = 20;
		private int current = 0;

		// �ǰ� �̹��� ������ ������ ���ư� �̹���
		private BufferedImage originalImage;

		// 0.1�� ���� 20�� �� 2�ʵ��� �ǰ� �̹����� ��Ÿ��
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

		// ī��Ʈ �ʱ�ȭ �ؼ� �ٽ� 2�ʰ� �ǰ� �̹����� ���
		public void restart(BufferedImage original){
			originalImage = original;
			count = 0;
		}
		
		// �ǰݽ� ���ư� �̹����� set
		public void setOriginal(BufferedImage original) {
			this.originalImage = original;
		}
	}

	// ���� �ð� ���� �Ѿ� Ÿ���� �ٲ�� �ϴ� ������
	class ChangeItemBullet extends Thread{
		private int count = 0;
		private final int maxCount = 20;
		private Bullet originalBulletType; // ���� Ÿ��
		private Bullet changedBulletType; // �ٲ�� Ÿ��

		public ChangeItemBullet(Bullet changedBulletType){
			this.originalBulletType = currentBulletType;
			this.changedBulletType = changedBulletType;

		}

		@Override
		public void run() {
			while(count < maxCount){
				currentBulletType = changedBulletType;

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				count++;
			}

			isChangedBullet = false;
			currentBulletType = originalBulletType;
		}

		public void restart(Bullet changedBulletType){
			this.changedBulletType = changedBulletType;
			count = 0;
		}

	}

}
