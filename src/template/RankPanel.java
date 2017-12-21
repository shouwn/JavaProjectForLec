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

public class RankPanel extends JPanel{        // 3��° ���� �г�
	// Ŭ���� ��� �ʵ� ����
	private StartFrame RankPanel;

	private Image back_image;
	private Image rankingimage; //���ȭ�� �̹���
	private Image number;

	private JButton mainB;

	private JTextArea rankResult;

	public RankPanel(StartFrame RankPanel) throws IOException {         // 3��° �г� ������
		this.RankPanel = RankPanel;

		setLayout(null);

		back_image = Toolkit.getDefaultToolkit().getImage("image/menu/start1.gif");
		rankingimage = Toolkit.getDefaultToolkit().getImage("image/menu/Ranking1.png"); // ��� ȭ�� �̹���
		number = Toolkit.getDefaultToolkit().getImage("image/menu/number.png"); // ��� ȭ�� �̹���

		mainB = new JButton(new ImageIcon("image/menu/Main2.png")); // �������� ���� ��ư �̹���
		mainB.setBounds(155, 530, 200, 70); // ������Ʈ ��ġ, ũ�� ����
		mainB.setBackground(Color.red);
		mainB.setBorderPainted(false);
		mainB.setFocusPainted(false);
		mainB.setContentAreaFilled(false);
		mainB.setRolloverIcon(new ImageIcon("image/menu/Main3.png"));
		mainB.setPressedIcon(new ImageIcon("image/menu/Main3.png"));

		// �ؽ�Ʈ ����� , ��� ��ºκ�
		rankResult = new JTextArea();
		rankResult.setEditable(false);
		rankResult.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		rankResult.setForeground(Color.WHITE);
		rankResult.setBackground(new Color(0, 0 ,0 ,0));
		rankResult.setBounds(180,198,220,350);

		add(rankResult); 

		add(mainB);

		mainB.addActionListener(new ToMainPanel()); //�������� ��ư�� ������ ����ȭ������ �̵��ϴ� �׼��߰�

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

	@Override  // ����̹����� paintComponent �޼ҵ� �������̵� �Ͽ� �׷���
	public void paintComponent(Graphics g) { //   paintComponent�� �̹��� ���� ��ư�� �ö��
		g.drawImage(back_image,-20,0,500,700,this); //GraphicsŸ���� g�� �̿��Ͽ� �гο� �׷��ش�. (�̹�����ü,����x,����y,����,����,this);
		g.drawImage(rankingimage,50, 20, 400, 629,this);
		g.drawImage(number,130, 195, 55, 345,this);
	}
	class ToMainPanel implements ActionListener {    // �������� ��ư ������ ����ȭ������ �̵�
		@Override
		public void actionPerformed(ActionEvent e) {
			RankPanel.changeMain();
		}
	}

}