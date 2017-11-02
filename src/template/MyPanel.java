package template;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import common.Operater;
import common.Score;

public class MyPanel extends JPanel{
	
	private Operater op;
	
	private BufferedImage backgroundImage;
	
	public MyPanel(){
		this.setSize(new Dimension(500, 600));
		setDoubleBuffered(true);
		
		try {
			backgroundImage = ImageIO.read(new File("start222.png"));
		} catch (IOException e) {
			System.err.println("Fail Load Background Image");
		}
		
		op = new Operater(this);
		op.startGame();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(backgroundImage, 0, 0, 500, 600, null);
		op.paintAll(g2);
	}
	
	public Score getScore(){
		return op.getScore();
	}
}
