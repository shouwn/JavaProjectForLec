package template;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Operater;

public class MyPanel extends JPanel{

	private Operater op;

	private JLabel score;
	private JLabel time;

	public MyPanel(){

		this.setSize(new Dimension(500, 700));
		setDoubleBuffered(true);
		setLayout(null);

		op = new Operater(this);

		Font font = new Font(Font.DIALOG, Font.BOLD, 30);

		score = new JLabel("0");
		time = new JLabel("00:00");

		score.setForeground(Color.WHITE);
		time.setForeground(Color.WHITE);

		score.setFont(font);
		time.setFont(font);
		score.setPreferredSize(new Dimension(150, 75));

		score.setBounds(420, -10, 150, 75);
		time.setBounds(220, -10, 150, 75);

		add(score);
		add(time);

		new UpdateLabel().start();
		this.setFocusable(true);
		this.requestFocusInWindow();
		op.startGame();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		op.paintAll((Graphics2D) g);
	}

	class UpdateLabel extends Thread{
		@Override
		public void run() {
			while(true) {
				score.setText(op.getScore().getScore());
				time.setText(op.getScore().getTime());

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
