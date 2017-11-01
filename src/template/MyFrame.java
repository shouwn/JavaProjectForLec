package template;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MyFrame extends JFrame{

	private MyPanel panel = new MyPanel();

	public MyFrame(){

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		
		setSize(panel.getSize());
		
		add(panel);
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
