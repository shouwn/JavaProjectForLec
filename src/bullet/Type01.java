package bullet;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import common.Point;
import common.Points;
import enemy.Enemy;

public class Type01 implements Bullet{

	private static BufferedImage image;
	private Point point;
	private float speed = 20;
	private boolean isUseful = true;

	static {
		try {
			image = ImageIO.read(new File("Fork.gif"));
		} catch (IOException e) {
			System.err.println("Fail Load Bullet Image");
			System.exit(0);
		}
	}

	public Type01() {

	}

	public Type01(Point point) {
		this.point = point;
	}

	@Override
	public List<Bullet> makeSelf() {
		List<Bullet> list = new ArrayList<Bullet>();
		list.add(new Type01(new Point(this.point).move(25, 0)));
		return list;
	}

	@Override
	public void drawSelf(Graphics2D g) {
		g.drawImage(image, AffineTransform.getTranslateInstance(point.getX(), point.getY()), null);
	}

	@Override
	public void moveSelf() {
		point.move(0, speed);
	}

	@Override
	public void setPoint(Point point) {
		this.point = point;
	}

	@Override
	public boolean checkUseful() {
		return isUseful;
	}

	@Override
	public void setUseless() {
		isUseful = false;
	}

	@Override
	public Point getPoint() {
		return point;
	}

	@Override
	public boolean isCrashed(Enemy e) {

		if(!isUseful)
			return false;

		float x1 = e.getPoint().getX();
		float x2 = x1 + e.getWidth();
		float y1 = e.getPoint().getY();
		float y2 = y1 + e.getHeight();

		if(Points.checkAreaInArea(new Point(point), new Point(point).add(image.getTileWidth(), image.getHeight()), new Point(x1, y1), new Point(x2, y2)))
			return true;

		return false;
	}
}
