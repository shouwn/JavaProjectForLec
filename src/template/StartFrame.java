package template;

import java.awt.Point;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import common.Score;


public class StartFrame extends JFrame{
	
	public MainPanel mainpanel = null;
	public LevelPanel levelpanel = null;
	public RankPanel rankpanel = null;
	public GamePanel gamepanel = null;

	public void changeStart() {  // ���ӽ��۹�ư ���� �� ����ȭ������ �̵�
			getContentPane().removeAll();
			gamepanel.gameStart();
			getContentPane().add(gamepanel);
			revalidate();
			repaint();
		
	}
	public void changeRank() {  //����Ȯ�ι�ư ���� �� ����Ȯ�� ȭ������ �̵�
			getContentPane().removeAll();
			getContentPane().add(rankpanel);
			rankpanel.addTextArea();
			revalidate();
			repaint();
	}
	public void changeMain() {
			getContentPane().removeAll();
			getContentPane().add(mainpanel);
			revalidate();
			repaint();
	}
	public void changeResult(Score score) {
			getContentPane().removeAll();
			getContentPane().add(levelpanel);
			levelpanel.setScore(score);
			revalidate();
			repaint();
	}
	public void changeRetry() {
			getContentPane().removeAll();
			gamepanel.gameStart();
			getContentPane().add(gamepanel);
			revalidate();
			repaint();
	}
	
	public static void main(String[] args) throws IOException  {
		StartFrame StartMenu = new StartFrame();
		StartMenu.setTitle("������ ���� ");
		
		StartMenu.mainpanel = new MainPanel(StartMenu);
		StartMenu.levelpanel = new LevelPanel(StartMenu);
		StartMenu.rankpanel = new RankPanel(StartMenu);
		StartMenu.gamepanel = new GamePanel(StartMenu);

		JTabbedPane jtab = new JTabbedPane();   //  JTabbedPane 
		jtab.addTab("����ȭ��1", StartMenu.mainpanel );
		jtab.addTab("���ȭ��2", StartMenu.levelpanel );
		jtab.addTab("����Ȯ��3", StartMenu.rankpanel );
		jtab.addTab("����ȭ��4", StartMenu.gamepanel );


		StartMenu.add (jtab);

		jtab.setUI(new BasicTabbedPaneUI() {     //Tab�� ����� �޼ҵ�
			@Override
			protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
				if (jtab.getTabCount() > 4)  //tab�� 4�� ���ϸ� �����.
					return super.calculateTabAreaHeight(tab_placement, run_count, max_tab_height);
				else
					return 0;
			}
		});
		
		StartMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X��ư ������ �� ���� ����.
		StartMenu.setLocation(new Point(600,200));   
		StartMenu.setSize(500,700);
		StartMenu.setVisible(true);   

	}

}

