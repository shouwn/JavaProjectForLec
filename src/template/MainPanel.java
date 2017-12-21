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

public class MainPanel extends JPanel {      // 1��° �г� - ���θ޴�
	private JButton jButton1;
	private JButton jButton2; //JButton1,2 ���ӽ���,����Ȯ�� ��ư 
	private JButton jBVolume; //���� on off ��� ��ư
	private JButton jBMute; // ���� on off ��� ��ư


	private JButton jBHelpO; // ���� â ���� ��ư
	private JButton jBHelpX; // ���� â ���ֱ� ��ư

	private StartFrame MainPanel;

	private Image back_image; //����̹���
	private Image backGround;	//��� ���� �̹���

	private ImageIcon image1;	//�����̸��̹���

	private ImageIcon helpImage;  //���� �̹���

	JLabel lb1;	//�����̸� �̹��� ���̺�
	JLabel lb2;	

	JLabel help; //���� �̹��� ���̺�
	
	private Sound bgm = new Sound(Sound.MAINTHEME);

	public MainPanel(StartFrame MainPanel) {        // 1��° �г� ������
		this.MainPanel = MainPanel;
		setLayout(null);
		
		bgm.playMusic(true);  // ���� BGM
		back_image = Toolkit.getDefaultToolkit().getImage("image/menu/start1.gif");  //����̹��� ����
		backGround = Toolkit.getDefaultToolkit().getImage("image/menu/BG.png");   //��� �� �����̹��� ����

		image1 = new ImageIcon("image/menu/GameName.png");  //���� �̸� �̹��� ����
		helpImage = new ImageIcon("image/menu/guide.png.");   //���� �̹��� ����

		jButton1 = new JButton(new ImageIcon("image/menu/GameStart1.png"));   //���ӽ��۹�ư �̹���
		jButton1.setBounds(90, 350, 300, 110); // ������Ʈ ��ġ, ũ�� ����
		jButton1.setRolloverIcon(new ImageIcon("image/menu/GameStart2.png"));
		jButton1.setPressedIcon(new ImageIcon("image/menu/GameStart3.png"));

		jButton2 = new JButton(new ImageIcon("image/menu/GameRank1.png")); //����Ȯ�ι�ư �̹���
		jButton2.setBounds(90, 470, 300, 110); // ������Ʈ ��ġ, ũ�� ����
		jButton2.setRolloverIcon(new ImageIcon("image/menu/GameRank2.png"));
		jButton2.setPressedIcon(new ImageIcon("image/menu/GameRank3.png"));


		jBVolume = new JButton(new ImageIcon("image/menu/Volume1.png")); //��Ʈ ��ư
		jBVolume.setBounds(415, 10, 50, 50); //
		jBVolume.setRolloverIcon(new ImageIcon("image/menu/Volume2.png"));

		jBMute = new JButton(new ImageIcon("image/menu/Mute1.png")); //��ƮOFF��ư
		jBMute.setBounds(415, 10, 50, 50);
		jBMute.setRolloverIcon(new ImageIcon("image/menu/Mute2.png"));

		jBHelpO = new JButton(new ImageIcon("image/menu/Help1.png")); //���򸻹�ư
		jBHelpO.setBounds(360, 10, 50, 50); 
		jBHelpO.setRolloverIcon(new ImageIcon("image/menu/Help2.png"));

		jBHelpX = new JButton(new ImageIcon("image/menu/HelpX1.png")); //���� �ݱ��ư
		jBHelpX.setBounds(360, 10, 50, 50); 
		jBHelpX.setRolloverIcon(new ImageIcon("image/menu/HelpX2.png"));

		lb1 = new JLabel("",image1,JLabel.CENTER); //�����̸� ���̺� �����
		lb1.setBounds(90,80,300,250);   //�����̸� ��ġ, ũ�� ����

		help = new JLabel("",helpImage,JLabel.CENTER);
		help.setBounds(100, 100, 300, 500);

		jButton1.setBackground(Color.red);  //��ư �̹����� �����ϰ� ����.
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

		add(jButton1); //���ӽ��۹�ư �߰�
		add(jButton2); //����Ȯ�� ��ư �߰�

		add(jBVolume);  //���� ��/������ư �߰�
		add(jBHelpO);  // ���� ��ư �߰�
		add(jBHelpX);   // ���� ��ư ������ Xǥ�� ��ư �߰�
		add(help);      //���� ��ư ���� �� ���� ȭ�鿡 ǥ��!
		add(jBMute);
		add(lb1);  //�����̸� ���̺� �߰�

		jButton1.addActionListener(new ToGamePanel()); // ��ư �� �׼� �߰�
		jButton2.addActionListener(new ToRankPanel());
		jBVolume.addActionListener(new BgmMute());
		jBMute.addActionListener(new BgmPlay());
		jBHelpO.addActionListener(new ToHelpImage());
		jBHelpX.addActionListener(new ToMainPanel());

		new Sound(Sound.YUPPDUK).playMusic(false);
	}

	@Override  // ����̹����� paintComponent �޼ҵ� �������̵� �Ͽ� �׷���
	public void paintComponent(Graphics g) { //  �� paintComponent�� �̹��� ���� ��ư�� �ö��
		g.drawImage(back_image,-20,0,500,700,this); //GraphicsŸ���� g�� �̿��Ͽ� �гο� �׷��ش�. (�̹�����ü,����x,����y,����,����,this);
		g.drawImage(backGround,-20,0,500,700,this); 
	}

	class ToGamePanel implements ActionListener {    // ��ư Ű ������ �г� 4��(����ȭ��) ȣ��
		@Override
		public void actionPerformed(ActionEvent e) {
			new Sound(Sound.GAMESTART).playMusic(false);
			
			bgm.stopMusic();
			MainPanel.changeStart();
		}
	}
	class ToRankPanel implements ActionListener {     // ��ư Ű ������ �г� 3��(����Ȯ��) ȣ��
		@Override
		public void actionPerformed(ActionEvent e) {
			MainPanel.changeRank();
		}
	}
	class BgmMute implements ActionListener {    // ��Ʈ ��ư ������ BGM ��Ʈ
		@Override
		public void actionPerformed(ActionEvent e) {
			jBVolume.setVisible(false);
			jBMute.setVisible(true);
			Sound.mute();

		}
	}
	class BgmPlay implements ActionListener {   //  ���� ��ư ������ BGM ȣ��
		@Override
		public void actionPerformed(ActionEvent e) {
			jBVolume.setVisible(true);
			jBMute.setVisible(false);
			Sound.muteOff();
			bgm.playMusic(true);
		}
	}
	class ToHelpImage implements ActionListener {    // ���� ��ư ������ ���� �̹��� ȣ��
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
	class ToMainPanel implements ActionListener {    // ���� X ��ư ������ �ٽ� ���� ȭ������ ��ȯ
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