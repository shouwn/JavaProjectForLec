package template;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Score;

public class MyFrame extends JFrame{

	private MyPanel panel = new MyPanel();
	private ScorePanel scorePanel;

	public MyFrame(){

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();

		this.setLayout(null);

		setSize(500, 700);

		scorePanel = new ScorePanel(panel.getScore());

		add(scorePanel);
		add(panel);
		panel.setBounds(0, 30, 500, 670);
		scorePanel.setBounds(0,0, 500, 30);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation((screenSize.width - this.getWidth())/2 , (screenSize.height - this.getHeight())/ 2);
		setTitle("MyFrame");


		setResizable(false);
		setVisible(true);
	}

	public static void main(String[] args){
		MyFrame frame = new MyFrame();
	}
}

class ScorePanel extends JPanel{
	private JLabel scoreLabel;
	private Score score;

	public ScorePanel(Score score){
		this.score = score;
		scoreLabel = new JLabel("0");
		add(scoreLabel);
		new ScoreThread().start();
	}

	class ScoreThread extends Thread{
		@Override
		public void run(){
			while(true){
				scoreLabel.setText(String.valueOf(score.getScore()));
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
