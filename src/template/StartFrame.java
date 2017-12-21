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

	public void changeStart() {  // 게임시작버튼 누를 시 게임화면으로 이동
			getContentPane().removeAll();
			gamepanel.gameStart();
			getContentPane().add(gamepanel);
			revalidate();
			repaint();
		
	}
	public void changeRank() {  //순위확인버튼 누를 시 순위확인 화면으로 이동
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
		StartMenu.setTitle("엽떡의 여행 ");
		
		StartMenu.mainpanel = new MainPanel(StartMenu);
		StartMenu.levelpanel = new LevelPanel(StartMenu);
		StartMenu.rankpanel = new RankPanel(StartMenu);
		StartMenu.gamepanel = new GamePanel(StartMenu);

		JTabbedPane jtab = new JTabbedPane();   //  JTabbedPane 
		jtab.addTab("메인화면1", StartMenu.mainpanel );
		jtab.addTab("결과화면2", StartMenu.levelpanel );
		jtab.addTab("순위확인3", StartMenu.rankpanel );
		jtab.addTab("게임화면4", StartMenu.gamepanel );


		StartMenu.add (jtab);

		jtab.setUI(new BasicTabbedPaneUI() {     //Tab을 숨기는 메소드
			@Override
			protected int calculateTabAreaHeight(int tab_placement, int run_count, int max_tab_height) {
				if (jtab.getTabCount() > 4)  //tab이 4개 이하면 숨긴다.
					return super.calculateTabAreaHeight(tab_placement, run_count, max_tab_height);
				else
					return 0;
			}
		});
		
		StartMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //X버튼 눌렀을 시 게임 종료.
		StartMenu.setLocation(new Point(600,200));   
		StartMenu.setSize(500,700);
		StartMenu.setVisible(true);   

	}

}

