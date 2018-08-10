package cn.xiaoadong.setTomato;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.xiaoadong.sound.TomatoSound;
import cn.xiaoadong.tomatoUI.TomatoUI2;

import javax.swing.JSlider;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;

public class SoundSet1 extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	@SuppressWarnings("unused")
	private TomatoUI2 tomatoUI2;
	private JLabel label;


	/**
	 * Create the frame.
	 */
	public SoundSet1(TomatoUI2 tomatoUI2) {
		//传入
		this.tomatoUI2 = tomatoUI2;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JSlider slider = new JSlider();
		slider.setValue(60);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(20);
		slider.setValue(tomatoUI2.getTone()==null ? 60 : tomatoUI2.getTone());
		slider.setMaximum(120);
		slider.setBounds(104, 70, 304, 46);
		contentPane.add(slider);
		
		JSlider slider_1 = new JSlider();
		slider_1.setSnapToTicks(true);
		slider_1.setPaintTicks(true);
		slider_1.setPaintLabels(true);
		slider_1.setMinorTickSpacing(5);
		slider_1.setMajorTickSpacing(20);
		slider_1.setValue(tomatoUI2.getHz() == null ? 50 : tomatoUI2.getHz());
		slider_1.setBounds(104, 126, 304, 52);
		contentPane.add(slider_1);
		
		label = new JLabel("声音设置");
		label.setForeground(Color.RED);
		label.setFont(new Font("仿宋", Font.BOLD, 25));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(10, 10, 414, 50);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("音调：");
		label_1.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(20, 70, 74, 44);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("频率：");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("微软雅黑 Light", Font.PLAIN, 14));
		label_2.setBounds(20, 124, 74, 44);
		contentPane.add(label_2);
		
		JButton button = new JButton("设置");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tomatoUI2.setTone(slider.getValue());
				tomatoUI2.setHz(slider_1.getValue());
				label.setText("设置成功");
			}
		});
		button.setFont(new Font("仿宋", Font.BOLD, 16));
		button.setBounds(237, 200, 93, 37);
		contentPane.add(button);
		
		JButton button_1 = new JButton("测试");
		button_1.addActionListener(new ActionListener() {
			Timer timer = new Timer();
			public void actionPerformed(ActionEvent e) {
				TomatoSound sound = new TomatoSound();
				sound.launch(slider.getValue(), slider_1.getValue());
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						sound.close();
					}
				}, 3000);
			}
		});
		button_1.setBounds(114, 201, 93, 37);
		contentPane.add(button_1);
	}
}
