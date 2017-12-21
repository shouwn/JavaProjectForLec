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

	// 현재 플레이어 총알 타입과 좌표
	private Bullet currentBulletType;
	private Point point;

	// 플레이어 이미지
	private BufferedImage currentImage;
	private static BufferedImage[] images = new BufferedImage[3];
	private static BufferedImage[] damagedImages;

	// 피격 이미지를 위한 스레드와 일정 시간 총알 타입이 바뀌는 스레드 
	private AttackedImage attackedImage;
	private ChangeItemBullet changeItemBullet;

	// flag와 이미지 가로 세로, 목숨
	private boolean isChangedBullet = false;
	private boolean isDamagedImages = false;
	private int width, height;
	private int life = 3;
	
	// 이미지 로드
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

	// 생성자
	public Player(Point point){

		Bullets.setPlayerPoint(point);

		this.currentImage = images[0];
		this.point = point;
		this.currentBulletType = Bullets.TYPE_01; // 기본 총알 타입은 1
		this.width = currentImage.getWidth();
		this.height = currentImage.getHeight();
	}

	// 현재 이미지를 바꾸기 위한 메소드
	public synchronized void setCurrentImage(BufferedImage img){
		currentImage = img;
	}

	// 죽었는지 체크
	public boolean isDead(){
		return life <= 0;
	}

	// 플레이어 생명력 회복
	public synchronized void recoverLife(int value){
		life += value;
		if(life > 3)
			life = 3;
		else
			new Sound(Sound.HEALED).playMusic(false);		
		
		// 회복 시 이미지 변경
		setCurrentImage(images[3 - life]);
		
		// 만약 피격 이미지 중이면
		if(attackedImage != null)
			this.attackedImage.setOriginal(images[3 - life]); // 피격시 원래 이미지로 돌아오게 세팅
	}

	// 라이프 다운
	public synchronized void fallLife(){
		if(--life >= 1){
			setCurrentImage(images[3 - life]); // 이미지 바꿈

			// 만약 피격 이미지 상태가 아니면 피격이미지로 변경
			if(!isDamagedImages){
				new Sound(Sound.HOT).playMusic(false);
				isDamagedImages = true;
				attackedImage = new AttackedImage();
				attackedImage.start();
			}
			else // 아니면 피격 끝나고 나와야하는 이미지를 변경 후 피격 이미지 재시작
				attackedImage.restart(images[3 - life]);
				

			
		}
		else  // 만약 라이프가 1보다 적으면 죽은 것임
			new Sound(Sound.GAMEOVER).playMusic(false);

	}

	// 총알 타입을 매개변수로 받은 것으로 바꿈
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

	// 플레이어 움직임을 위한 리스너
	@Override
	public void mouseDragged(MouseEvent e) {
		point.setX(e.getX() - width/2);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		point.setX(e.getX() - width/2);
	}

	// 피격시 이미지를 위한 스레드
	class AttackedImage extends Thread{
		private int count = 0;
		private final int maxCount = 20;
		private int current = 0;

		// 피격 이미지 끝나고 원래로 돌아갈 이미지
		private BufferedImage originalImage;

		// 0.1초 동안 20번 즉 2초동안 피격 이미지로 나타냄
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

		// 카운트 초기화 해서 다시 2초간 피격 이미지를 출력
		public void restart(BufferedImage original){
			originalImage = original;
			count = 0;
		}
		
		// 피격시 돌아갈 이미지를 set
		public void setOriginal(BufferedImage original) {
			this.originalImage = original;
		}
	}

	// 일정 시간 동안 총알 타입이 바뀌게 하는 스레드
	class ChangeItemBullet extends Thread{
		private int count = 0;
		private final int maxCount = 20;
		private Bullet originalBulletType; // 본래 타입
		private Bullet changedBulletType; // 바뀌는 타입

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
