package cn.xiaoadong.setTomato;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.xiaoadong.tomatoUI.TomatoUI2;

import javax.swing.JLabel;
import java.awt.TextField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class TimeSet1 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	@SuppressWarnings("unused")
	private TomatoUI2 tomatoUI2;
	
	public TimeSet1(TomatoUI2 tomatoUI2) {
		//传入要修改的番茄钟
		this.tomatoUI2 = tomatoUI2;
		String[] timeset = tomatoUI2.getTimeset();
		URL resource = tomatoUI2.getResource();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("番茄时间：");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(121, 82, 92, 23);
		contentPane.add(label);
		
		TextField textField = new TextField();
		textField.setText(String.valueOf(tomatoUI2.getTomatoTime()));
		textField.setBounds(240, 82, 43, 23);
		contentPane.add(textField);
		
		JLabel lable_1 = new JLabel("小休息时间：");
		lable_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lable_1.setBounds(121, 123, 92, 23);
		contentPane.add(lable_1);
		
		JLabel label_2 = new JLabel("大休息时间：");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(131, 162, 82, 23);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("设置");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(10, 10, 414, 29);
		label_3.setFont(new Font("yahei", 2, 25));
		label_3.setForeground(Color.red);
		contentPane.add(label_3);
		
		TextField textField_1 = new TextField();
		textField_1.setText(String.valueOf(tomatoUI2.getRestTime()));
		textField_1.setBounds(240, 123, 43, 23);
		contentPane.add(textField_1);
		
		TextField textField_2 = new TextField();
		textField_2.setText(String.valueOf(tomatoUI2.getBigRestTime()));
		textField_2.setBounds(240, 162, 43, 23);
		contentPane.add(textField_2);
		
		JButton button = new JButton("提交设置");
		button.setFont(new Font("楷体", Font.BOLD, 12));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isUnsignedInteger(textField.getText())) {
					timeset[0] = textField.getText();
					tomatoUI2.setTomatoTime(Integer.parseInt(textField.getText()));
				}
				if (isUnsignedInteger(textField_1.getText())) {
					timeset[1] = textField_1.getText();
					tomatoUI2.setRestTime(Integer.parseInt(textField_1.getText()));				
				}
				if (isUnsignedInteger(textField_2.getText())) {
					timeset[2] = textField_2.getText();
					tomatoUI2.setBigRestTime(Integer.parseInt(textField_2.getText()));
				}
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(new File(resource.getFile())));
					String timesetstr = timeset[0]+"-"
							+timeset[1]+"-"
							+timeset[2];
					writer.write(timesetstr);
					writer.close();
					label_3.setText("设置成功");
				} catch (Exception e2) {
				}
			}
		});
		button.setBounds(170, 209, 100, 29);
		contentPane.add(button);
		
	}
	 public boolean isUnsignedInteger(String str) {  
	        Pattern pattern = Pattern.compile("[\\d]*$");  
	        return pattern.matcher(str).matches();  
	  }
}
