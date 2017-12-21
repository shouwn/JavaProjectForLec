package template;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.Operater;
import common.Score;

public class LevelPanel extends JPanel{        // 2번째 패널
	private JTextField textField;
	private JButton backB;
	private JButton retryB;
	private JButton rankB;

	private StartFrame LevelPanel;

	private ImageIcon playTime; // 여행 시간 이미지
	private ImageIcon pScore; // 점수 이미지
	private ImageIcon pName; // 점수 이미지
	private ImageIcon rankOK; // 점수 이미지
	private Image back_image;  // 배경 이미지
	private Image resultImage; //결과화면 이미지

	private JTextField name;
	private JTextArea pTime;
	private JTextArea sCore;

	private Score score;
	private Score time;
	private Operater op;

	 private Font f1;

	JLabel ptime;
	JLabel sc;	//
	JLabel nameimage;
	JLabel rankregistOK;

	public Score getScore(){
		return score;
	}
	public Score getTime(){
		return time;
	}

	public LevelPanel(StartFrame LevelPanel) {        // 2번째 패널 생성자
		this.LevelPanel = LevelPanel;

		 f1 = new Font(Font.SANS_SERIF, Font.BOLD, 30);


		setLayout(null);
		back_image = Toolkit.getDefaultToolkit().getImage("image/menu/start1.gif");  // 배경이미지
		resultImage = Toolkit.getDefaultToolkit().getImage("image/menu/Result4.png"); // 결과 화면 이미지

		backB = new JButton(new ImageIcon("image/menu/Back3.png")); // 메인으로 가는 버튼 이미지
		backB.setBounds(80, 530, 200,70); // 컴포넌트 위치, 크기 설정
		backB.setRolloverIcon(new ImageIcon("image/menu/Back4.png"));
		backB.setPressedIcon(new ImageIcon("image/menu/Back4.png"));

		retryB = new JButton(new ImageIcon("image/menu/Retry3.png"));
		retryB.setBounds(230, 530, 200, 70); // 컴포넌트 위치, 크기 설정
		retryB.setRolloverIcon(new ImageIcon("image/menu/Retry4.png"));
		retryB.setPressedIcon(new ImageIcon("image/menu/Retry4.png"));

		rankB = new JButton(new ImageIcon("image/menu/Rank3.png")); // 메인으로 가는 버튼 이미지
		rankB.setBounds(150, 455, 200, 70); // 컴포넌트 위치, 크기 설정
		rankB.setRolloverIcon(new ImageIcon("image/menu/Rank4.png"));
		rankB.setPressedIcon(new ImageIcon("image/menu/Rank4.png"));

		backB.setBackground(Color.red);
		backB.setBorderPainted(false);
		backB.setFocusPainted(false);
		backB.setContentAreaFilled(false);
		retryB.setBackground(Color.red);
		retryB.setBorderPainted(false);
		retryB.setFocusPainted(false);
		retryB.setContentAreaFilled(false);
		rankB.setBackground(Color.red);
		rankB.setBorderPainted(false);
		rankB.setFocusPainted(false);
		rankB.setContentAreaFilled(false);

		add(backB);
		add(retryB);
		add(rankB);

		playTime = new ImageIcon("image/menu/PlayTime2.png"); // 여행 시간 이미지
		pScore = new ImageIcon("image/menu/Score2.png"); // 점수 이미지
		pName = new ImageIcon("image/menu/PName.png"); // 이름  이미지
		rankOK = new ImageIcon("image/menu/Rank5.png");

		ptime = new JLabel("",playTime,JLabel.CENTER); // 여행시간 만들기
		ptime.setBounds(80,250,200,70);  // 위치, 크기 설정
		sc =  new JLabel("",pScore,JLabel.CENTER); // 점 수  만들기
		sc.setBounds(80,340,200,70);  // 위치, 크기 설정
		nameimage=  new JLabel("",pName,JLabel.CENTER); // 이름입력 만들기
		nameimage.setBounds(75,400,200,70);  // 위치, 크기 설정
		rankregistOK=  new JLabel("",rankOK,JLabel.CENTER); // 순위등록되었음/ 만들기
		rankregistOK.setBounds(105,470,285,45);  // 위치, 크기 설정

		// 결과 시간 출력부분
		pTime = new JTextArea();
		pTime.setEditable(false);
		pTime.setBorder(null);
		pTime.setBackground(new Color(0, 0 ,0 ,0));
		pTime.setFont(f1);
		pTime.setForeground(new Color(220, 5, 68));
		pTime.setBounds(280,265,200,50);


		// 결과 스코어 출력부분
		sCore = new JTextArea();
		sCore.setEditable(false);
		//sCore.setOpaque(false);
		sCore.setBorder(null);
		sCore.setBackground(new Color(0, 0 ,0 ,0));
		sCore.setFont(f1);
		sCore.setForeground(new Color(220, 5, 68));
		sCore.setBounds(280,355,210,50);

		name = new JTextField();              // 이름 입력 부분
		name.setBounds(245,423,120,25);    //   위치와 크기
		name.setBorder(null);
		name.setBackground(new Color(250, 200, 13));
		name.setFont(f1);


		add(pTime);
		add(sCore);
		add(name);
		add(ptime);
		add(sc);
		add(nameimage);
		add(rankregistOK);


		score = new Score();


		//String getScore = Score.getScore();
		//	String time = score.getTime();

		// 값 가져오기
		rankregistOK.setVisible(false);

		backB.addActionListener(new ToMainPanel()); //메인으로 버튼을 누르면 메인화면으로 이동하는 액션추가
		retryB.addActionListener(new RetryGame()); //다시하기 버튼을 누르면 게임화면으로 이동하는 액션추가
		rankB.addActionListener(new RankResit()); //순위등록 액션추가


	}@Override  // 배경이미지를 paintComponent 메소드 오버라이드 하여 그려줌
	public void paintComponent(Graphics g) { //  ★ paintComponent는 이미지 위로 버튼이 올라옴
		g.drawImage(back_image,-20,0,500,700,this); //Graphics타입의 g를 이용하여 패널에 그려준다. (이미지객체,시작x,시작y,가로,세로,this);
		g.drawImage(resultImage,50, 20, 400, 629,this);
	}
	class ToMainPanel implements ActionListener {    // 메인으로 버튼 눌리면 메인화면으로 이동
		@Override
		public void actionPerformed(ActionEvent e) {
			LevelPanel.changeMain();
			rankregistOK.setVisible(false);
			name.setEditable(true);
			name.setBackground(new Color(250, 200, 13));
			name.setText("");
			rankB.setVisible(true);

		}
	}
	class RetryGame implements ActionListener {    // 다시하기 버튼 눌리면 게임화면으로 이동
		@Override
		public void actionPerformed(ActionEvent e) {
			LevelPanel.changeRetry();
			rankregistOK.setVisible(false);
			name.setEditable(true);
			name.setBackground(new Color(250, 200, 13));
			name.setText("");
			rankB.setVisible(true);

		}
	}
	class RankResit implements ActionListener {    
		@Override
		public void actionPerformed(ActionEvent e) {
			rankB.setVisible(false);
			rankregistOK.setVisible(true);
			name.setEditable(false);
			name.setBackground(new Color(235, 162, 14));
			score.setName(name.getText());
			
			try{
				File file = new File("image/Rank.txt") ;

				FileWriter fw = new FileWriter(file, true) ;

				fw.write(score.toString() + "\n");
				fw.flush();

				fw.close();

			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public void setScore(Score score){
		this.score = score;
		pTime.setText(score.getTime());  // -----------시간 , 점수 출력
		sCore.setText(score.getScore());
	}
}