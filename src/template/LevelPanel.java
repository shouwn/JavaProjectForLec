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

public class LevelPanel extends JPanel{        // 2��° �г�
	private JTextField textField;
	private JButton backB;
	private JButton retryB;
	private JButton rankB;

	private StartFrame LevelPanel;

	private ImageIcon playTime; // ���� �ð� �̹���
	private ImageIcon pScore; // ���� �̹���
	private ImageIcon pName; // ���� �̹���
	private ImageIcon rankOK; // ���� �̹���
	private Image back_image;  // ��� �̹���
	private Image resultImage; //���ȭ�� �̹���

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

	public LevelPanel(StartFrame LevelPanel) {        // 2��° �г� ������
		this.LevelPanel = LevelPanel;

		 f1 = new Font(Font.SANS_SERIF, Font.BOLD, 30);


		setLayout(null);
		back_image = Toolkit.getDefaultToolkit().getImage("image/menu/start1.gif");  // ����̹���
		resultImage = Toolkit.getDefaultToolkit().getImage("image/menu/Result4.png"); // ��� ȭ�� �̹���

		backB = new JButton(new ImageIcon("image/menu/Back3.png")); // �������� ���� ��ư �̹���
		backB.setBounds(80, 530, 200,70); // ������Ʈ ��ġ, ũ�� ����
		backB.setRolloverIcon(new ImageIcon("image/menu/Back4.png"));
		backB.setPressedIcon(new ImageIcon("image/menu/Back4.png"));

		retryB = new JButton(new ImageIcon("image/menu/Retry3.png"));
		retryB.setBounds(230, 530, 200, 70); // ������Ʈ ��ġ, ũ�� ����
		retryB.setRolloverIcon(new ImageIcon("image/menu/Retry4.png"));
		retryB.setPressedIcon(new ImageIcon("image/menu/Retry4.png"));

		rankB = new JButton(new ImageIcon("image/menu/Rank3.png")); // �������� ���� ��ư �̹���
		rankB.setBounds(150, 455, 200, 70); // ������Ʈ ��ġ, ũ�� ����
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

		playTime = new ImageIcon("image/menu/PlayTime2.png"); // ���� �ð� �̹���
		pScore = new ImageIcon("image/menu/Score2.png"); // ���� �̹���
		pName = new ImageIcon("image/menu/PName.png"); // �̸�  �̹���
		rankOK = new ImageIcon("image/menu/Rank5.png");

		ptime = new JLabel("",playTime,JLabel.CENTER); // ����ð� �����
		ptime.setBounds(80,250,200,70);  // ��ġ, ũ�� ����
		sc =  new JLabel("",pScore,JLabel.CENTER); // �� ��  �����
		sc.setBounds(80,340,200,70);  // ��ġ, ũ�� ����
		nameimage=  new JLabel("",pName,JLabel.CENTER); // �̸��Է� �����
		nameimage.setBounds(75,400,200,70);  // ��ġ, ũ�� ����
		rankregistOK=  new JLabel("",rankOK,JLabel.CENTER); // ������ϵǾ���/ �����
		rankregistOK.setBounds(105,470,285,45);  // ��ġ, ũ�� ����

		// ��� �ð� ��ºκ�
		pTime = new JTextArea();
		pTime.setEditable(false);
		pTime.setBorder(null);
		pTime.setBackground(new Color(0, 0 ,0 ,0));
		pTime.setFont(f1);
		pTime.setForeground(new Color(220, 5, 68));
		pTime.setBounds(280,265,200,50);


		// ��� ���ھ� ��ºκ�
		sCore = new JTextArea();
		sCore.setEditable(false);
		//sCore.setOpaque(false);
		sCore.setBorder(null);
		sCore.setBackground(new Color(0, 0 ,0 ,0));
		sCore.setFont(f1);
		sCore.setForeground(new Color(220, 5, 68));
		sCore.setBounds(280,355,210,50);

		name = new JTextField();              // �̸� �Է� �κ�
		name.setBounds(245,423,120,25);    //   ��ġ�� ũ��
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

		// �� ��������
		rankregistOK.setVisible(false);

		backB.addActionListener(new ToMainPanel()); //�������� ��ư�� ������ ����ȭ������ �̵��ϴ� �׼��߰�
		retryB.addActionListener(new RetryGame()); //�ٽ��ϱ� ��ư�� ������ ����ȭ������ �̵��ϴ� �׼��߰�
		rankB.addActionListener(new RankResit()); //������� �׼��߰�


	}@Override  // ����̹����� paintComponent �޼ҵ� �������̵� �Ͽ� �׷���
	public void paintComponent(Graphics g) { //  �� paintComponent�� �̹��� ���� ��ư�� �ö��
		g.drawImage(back_image,-20,0,500,700,this); //GraphicsŸ���� g�� �̿��Ͽ� �гο� �׷��ش�. (�̹�����ü,����x,����y,����,����,this);
		g.drawImage(resultImage,50, 20, 400, 629,this);
	}
	class ToMainPanel implements ActionListener {    // �������� ��ư ������ ����ȭ������ �̵�
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
	class RetryGame implements ActionListener {    // �ٽ��ϱ� ��ư ������ ����ȭ������ �̵�
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
		pTime.setText(score.getTime());  // -----------�ð� , ���� ���
		sCore.setText(score.getScore());
	}
}