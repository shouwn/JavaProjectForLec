package template;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import common.Score;
import common.ScoreComparator;

public class RankPanel extends JPanel{        // 3번째 순위 패널
	// 클래스 멤버 필드 설정
	private StartFrame RankPanel;

	private Image back_image;
	private Image rankingimage; //결과화면 이미지
	private Image number;

	private JButton mainB;

	private JTextArea rankResult;

	public RankPanel(StartFrame RankPanel) throws IOException {         // 3번째 패널 생성자
		this.RankPanel = RankPanel;

		setLayout(null);

		back_image = Toolkit.getDefaultToolkit().getImage("image/menu/start1.gif");
		rankingimage = Toolkit.getDefaultToolkit().getImage("image/menu/Ranking1.png"); // 결과 화면 이미지
		number = Toolkit.getDefaultToolkit().getImage("image/menu/number.png"); // 결과 화면 이미지

		mainB = new JButton(new ImageIcon("image/menu/Main2.png")); // 메인으로 가는 버튼 이미지
		mainB.setBounds(155, 530, 200, 70); // 컴포넌트 위치, 크기 설정
		mainB.setBackground(Color.red);
		mainB.setBorderPainted(false);
		mainB.setFocusPainted(false);
		mainB.setContentAreaFilled(false);
		mainB.setRolloverIcon(new ImageIcon("image/menu/Main3.png"));
		mainB.setPressedIcon(new ImageIcon("image/menu/Main3.png"));

		// 텍스트 에어리어 , 결과 출력부분
		rankResult = new JTextArea();
		rankResult.setEditable(false);
		rankResult.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		rankResult.setForeground(Color.WHITE);
		rankResult.setBackground(new Color(0, 0 ,0 ,0));
		rankResult.setBounds(180,198,220,350);

		add(rankResult); 

		add(mainB);

		mainB.addActionListener(new ToMainPanel()); //메인으로 버튼을 누르면 메인화면으로 이동하는 액션추가

	}
	public List<Score> readList()  {
		ArrayList<Score> scoreList = new ArrayList<>();
		Scanner scan;
		try {
			String[] line;
			scan = new Scanner(new File("image/Rank.txt"));

			while (scan.hasNextLine()){				
				line = scan.nextLine().split(" ");
				scoreList.add(new Score(line[0], line[1], line[2]));	
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return scoreList;
	}

	public void addTextArea(){  
		List<Score> list;
		rankResult.setText("");
		list = readList();
		list.sort(new ScoreComparator(ScoreComparator.BY_SCORE));
		
		int size = list.size();
		
		if(size > 10)
			size = 10;
		
		for(int i = 0 ; i < size; i++){
			rankResult.append(list.get(i).toString() + "\n");
		}
	}

	@Override  // 배경이미지를 paintComponent 메소드 오버라이드 하여 그려줌
	public void paintComponent(Graphics g) { //   paintComponent는 이미지 위로 버튼이 올라옴
		g.drawImage(back_image,-20,0,500,700,this); //Graphics타입의 g를 이용하여 패널에 그려준다. (이미지객체,시작x,시작y,가로,세로,this);
		g.drawImage(rankingimage,50, 20, 400, 629,this);
		g.drawImage(number,130, 195, 55, 345,this);
	}
	class ToMainPanel implements ActionListener {    // 메인으로 버튼 눌리면 메인화면으로 이동
		@Override
		public void actionPerformed(ActionEvent e) {
			RankPanel.changeMain();
		}
	}

}