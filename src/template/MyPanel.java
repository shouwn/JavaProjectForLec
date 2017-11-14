package template;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Operater;

public class MyPanel extends JPanel{

	private Operater op;

	private BufferedImage backgroundImage;
	private JLabel score;

	public MyPanel(){
		score = new JLabel("0");
		this.setSize(new Dimension(500, 700));
		setDoubleBuffered(true);

		try {
			backgroundImage = ImageIO.read(new File("start222.png"));
		} catch (IOException e) {
			System.err.println("Fail Load Background Image");
		}

		op = new Operater(this);
		add(score);
		new UpdateLabel().start();
		this.setFocusable(true);
		this.requestFocusInWindow();
		op.startGame();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(backgroundImage, 0, 0, 500, 700, null);
		op.paintAll(g2);
	}

	class UpdateLabel extends Thread{
		@Override
		public void run() {
			while(true) {
				score.setText(String.valueOf(op.getScore().getScore()));
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
