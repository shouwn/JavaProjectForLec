package template;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Operater;

public class MyPanel extends JPanel{

	private Operater op;

	private JLabel score;
	private JLabel time;
	private JLabel phase;

	public MyPanel(){
		score = new JLabel("0");
		time = new JLabel("00:00");
		phase = new JLabel("PHASE 1");

		score.setForeground(Color.WHITE);
		time.setForeground(Color.WHITE);
		phase.setForeground(Color.WHITE);

		this.setSize(new Dimension(500, 700));
		setDoubleBuffered(true);

		op = new Operater(this);
		add(score);
		add(time);
		add(phase);
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
				phase.setText(op.getScore().getPhase());

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
