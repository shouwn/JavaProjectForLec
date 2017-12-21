package template;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Operater;

public class GamePanel extends JPanel{

	private Operater op;

	private JLabel score;
	private JLabel time;
	private StartFrame startMenu;

	public GamePanel(StartFrame startMenu){

		this.startMenu = startMenu;

		this.setSize(new Dimension(500, 700));
		setDoubleBuffered(true);
		setLayout(null);

		Font font = new Font(Font.DIALOG, Font.BOLD, 30);

		score = new JLabel("0");
		time = new JLabel("00:00");

		score.setForeground(Color.WHITE);
		time.setForeground(Color.WHITE);

		score.setFont(font);
		time.setFont(font);
		score.setPreferredSize(new Dimension(150, 75));

		score.setBounds(400, -10, 150, 75);
		time.setBounds(220, -10, 150, 75);

		add(score);
		add(time);

		this.setFocusable(true);
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		op.paintAll((Graphics2D) g);
	}

	public void gameStart() {
		this.requestFocusInWindow();
		op = new Operater(this);
		new UpdateLabel().start();
		op.startGame();
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

	public void changeResultPanel() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.startMenu.changeResult(op.getScore());
	}
}
