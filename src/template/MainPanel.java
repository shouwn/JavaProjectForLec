package template;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.Sound;

public class MainPanel extends JPanel {      // 1번째 패널 - 메인메뉴
	private JButton jButton1;
	private JButton jButton2; //JButton1,2 게임시작,순위확인 버튼 
	private JButton jBVolume; //사운드 on off 기능 버튼
	private JButton jBMute; // 사운드 on off 기능 버튼


	private JButton jBHelpO; // 도움말 창 띄우기 버튼
	private JButton jBHelpX; // 도움말 창 없애기 버튼

	private StartFrame MainPanel;

	private Image back_image; //배경이미지
	private Image backGround;	//배경 투명도 이미지

	private ImageIcon image1;	//게임이름이미지

	private ImageIcon helpImage;  //도움말 이미지

	JLabel lb1;	//게임이름 이미지 레이블
	JLabel lb2;	

	JLabel help; //도움말 이미지 레이블
	
	private Sound bgm = new Sound(Sound.MAINTHEME);

	public MainPanel(StartFrame MainPanel) {        // 1번째 패널 생성자
		this.MainPanel = MainPanel;
		setLayout(null);
		
		bgm.playMusic(true);  // 메인 BGM
		back_image = Toolkit.getDefaultToolkit().getImage("image/menu/start1.gif");  //배경이미지 설정
		backGround = Toolkit.getDefaultToolkit().getImage("image/menu/BG.png");   //배경 위 투명이미지 설정

		image1 = new ImageIcon("image/menu/GameName.png");  //게임 이름 이미지 설정
		helpImage = new ImageIcon("image/menu/guide.png.");   //도움말 이미지 설정

		jButton1 = new JButton(new ImageIcon("image/menu/GameStart1.png"));   //게임시작버튼 이미지
		jButton1.setBounds(90, 350, 300, 110); // 컴포넌트 위치, 크기 설정
		jButton1.setRolloverIcon(new ImageIcon("image/menu/GameStart2.png"));
		jButton1.setPressedIcon(new ImageIcon("image/menu/GameStart3.png"));

		jButton2 = new JButton(new ImageIcon("image/menu/GameRank1.png")); //순위확인버튼 이미지
		jButton2.setBounds(90, 470, 300, 110); // 컴포넌트 위치, 크기 설정
		jButton2.setRolloverIcon(new ImageIcon("image/menu/GameRank2.png"));
		jButton2.setPressedIcon(new ImageIcon("image/menu/GameRank3.png"));


		jBVolume = new JButton(new ImageIcon("image/menu/Volume1.png")); //뮤트 버튼
		jBVolume.setBounds(415, 10, 50, 50); //
		jBVolume.setRolloverIcon(new ImageIcon("image/menu/Volume2.png"));

		jBMute = new JButton(new ImageIcon("image/menu/Mute1.png")); //뮤트OFF버튼
		jBMute.setBounds(415, 10, 50, 50);
		jBMute.setRolloverIcon(new ImageIcon("image/menu/Mute2.png"));

		jBHelpO = new JButton(new ImageIcon("image/menu/Help1.png")); //도움말버튼
		jBHelpO.setBounds(360, 10, 50, 50); 
		jBHelpO.setRolloverIcon(new ImageIcon("image/menu/Help2.png"));

		jBHelpX = new JButton(new ImageIcon("image/menu/HelpX1.png")); //도움말 닫기버튼
		jBHelpX.setBounds(360, 10, 50, 50); 
		jBHelpX.setRolloverIcon(new ImageIcon("image/menu/HelpX2.png"));

		lb1 = new JLabel("",image1,JLabel.CENTER); //게임이름 레이블 만들기
		lb1.setBounds(90,80,300,250);   //게임이름 위치, 크기 설정

		help = new JLabel("",helpImage,JLabel.CENTER);
		help.setBounds(100, 100, 300, 500);

		jButton1.setBackground(Color.red);  //버튼 이미지들 투명하게 해줌.
		jButton1.setBorderPainted(false);
		jButton1.setFocusPainted(false);
		jButton1.setContentAreaFilled(false);
		jButton2.setBackground(Color.red);
		jButton2.setBorderPainted(false);
		jButton2.setFocusPainted(false);
		jButton2.setContentAreaFilled(false);

		jBVolume.setBackground(Color.red);
		jBVolume.setBorderPainted(false);
		jBVolume.setFocusPainted(false);
		jBVolume.setContentAreaFilled(false);
		jBMute.setBackground(Color.red);
		jBMute.setBorderPainted(false);
		jBMute.setFocusPainted(false);
		jBMute.setContentAreaFilled(false);

		jBHelpO.setBackground(Color.red);
		jBHelpO.setBorderPainted(false);
		jBHelpO.setFocusPainted(false);
		jBHelpO.setContentAreaFilled(false);
		jBHelpX.setBackground(Color.red);
		jBHelpX.setBorderPainted(false);
		jBHelpX.setFocusPainted(false);
		jBHelpX.setContentAreaFilled(false);

		help.setVisible(false);
		jBHelpX.setVisible(false);
		jBMute.setVisible(false);

		add(jButton1); //게임시작버튼 추가
		add(jButton2); //순위확인 버튼 추가

		add(jBVolume);  //볼륨 온/오프버튼 추가
		add(jBHelpO);  // 도움말 버튼 추가
		add(jBHelpX);   // 도움말 버튼 누를시 X표시 버튼 추가
		add(help);      //도움말 버튼 누를 시 도움말 화면에 표시!
		add(jBMute);
		add(lb1);  //게임이름 레이블 추가

		jButton1.addActionListener(new ToGamePanel()); // 버튼 별 액션 추가
		jButton2.addActionListener(new ToRankPanel());
		jBVolume.addActionListener(new BgmMute());
		jBMute.addActionListener(new BgmPlay());
		jBHelpO.addActionListener(new ToHelpImage());
		jBHelpX.addActionListener(new ToMainPanel());

		new Sound(Sound.YUPPDUK).playMusic(false);
	}

	@Override  // 배경이미지를 paintComponent 메소드 오버라이드 하여 그려줌
	public void paintComponent(Graphics g) { //  ★ paintComponent는 이미지 위로 버튼이 올라옴
		g.drawImage(back_image,-20,0,500,700,this); //Graphics타입의 g를 이용하여 패널에 그려준다. (이미지객체,시작x,시작y,가로,세로,this);
		g.drawImage(backGround,-20,0,500,700,this); 
	}

	class ToGamePanel implements ActionListener {    // 버튼 키 눌리면 패널 4번(게임화면) 호출
		@Override
		public void actionPerformed(ActionEvent e) {
			new Sound(Sound.GAMESTART).playMusic(false);
			
			bgm.stopMusic();
			MainPanel.changeStart();
		}
	}
	class ToRankPanel implements ActionListener {     // 버튼 키 눌리면 패널 3번(순위확인) 호출
		@Override
		public void actionPerformed(ActionEvent e) {
			MainPanel.changeRank();
		}
	}
	class BgmMute implements ActionListener {    // 뮤트 버튼 눌리면 BGM 뮤트
		@Override
		public void actionPerformed(ActionEvent e) {
			jBVolume.setVisible(false);
			jBMute.setVisible(true);
			Sound.mute();

		}
	}
	class BgmPlay implements ActionListener {   //  볼륨 버튼 눌리면 BGM 호출
		@Override
		public void actionPerformed(ActionEvent e) {
			jBVolume.setVisible(true);
			jBMute.setVisible(false);
			Sound.muteOff();
			bgm.playMusic(true);
		}
	}
	class ToHelpImage implements ActionListener {    // 도움말 버튼 눌리면 도움말 이미지 호출
		@Override
		public void actionPerformed(ActionEvent e) {
			help.setVisible(true);
			jBHelpX.setVisible(true);
			jButton1.setVisible(false);
			jButton2.setVisible(false);
			jBHelpO.setVisible(false);
			jBHelpX.setVisible(true);
			lb1.setVisible(false);
		}

	}
	class ToMainPanel implements ActionListener {    // 도움말 X 버튼 눌리면 다시 메인 화면으로 전환
		@Override
		public void actionPerformed(ActionEvent e) {
			help.setVisible(false);
			jBHelpX.setVisible(false);
			jButton1.setVisible(true);
			jButton2.setVisible(true);
			jBHelpO.setVisible(true);
			jBHelpX.setVisible(false);
			lb1.setVisible(true);
		}

	}

}