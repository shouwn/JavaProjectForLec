package common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

//this class just made for test

public class Score extends JButton implements ActionListener{
	private int score;
	private JLabel label;
	
	public int getScore(){
		return score;
	}
	
	public synchronized void addScore(int value){
		score += value;
		this.doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		label.setText(String.valueOf(score));
	}
	
	public void setLabel(JLabel label){
		this.label = label;
	}
	
}
