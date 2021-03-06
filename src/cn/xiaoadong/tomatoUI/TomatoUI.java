package cn.xiaoadong.tomatoUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.xiaoadong.sound.TomatoSound;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
/**
 * 使用while在run 方法里空转实现
 * 暂停功能，消耗资源，但是稳定，
 * 绝对不会出问题
 * @author Administrator
 *
 */
public class TomatoUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Boolean WR= true;
	private Timer timer;
	private String time;
	private String countDown;
	private JPanel timepanel;
	private JLabel timeLabel;
	private JLabel timu;
	private JButton button;
	private String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	private int circle;//一个大循环，4番茄+3休息+1大休息
	private volatile static Boolean WORK = false;
	private TomatoSound sound;
	//2ge线程任务
	timeCount tomato;
	timeCount rest;
	timeCount bigRest;
	/**
	 * Create the frame.
	 */
	public TomatoUI() {
		//
		timer = new Timer();
		//
		sound = new TomatoSound();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		timepanel = new JPanel();
		timepanel.setBounds(10, 47, 414, 106);
		timepanel.setVisible(true);
		contentPane.add(timepanel);
		timepanel.setLayout(null);
		
		timeLabel = new JLabel("");
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeLabel.setBounds(10, 48, 394, 35);
		timeLabel.setFont(new Font("yahei", 2, 25));
		timeLabel.setForeground(Color.red);
		timeLabel.setText("点击开始");
		timepanel.add(timeLabel);
		
		timu = new JLabel("");
		timu.setForeground(Color.BLUE);
		timu.setHorizontalAlignment(SwingConstants.CENTER);
		timu.setBounds(10, 10, 394, 28);
		timepanel.add(timu);
		
		JButton button_1 = new JButton("暂停");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}

			
		});
		button_1.setBounds(177, 181, 60, 30);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("重置");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		button_2.setBounds(247, 181, 60, 30);
		contentPane.add(button_2);
		
		button = new JButton("开始");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!WORK) {
					WORK = true;
					sound.close();
					if (WR) {//启动番茄的倒计时
						tomato = new timeCount(25, "小番茄结束");
						button.setText("休息");
						WR = false;
						//番茄
						timu.setText("番茄时间");
						timer.schedule(tomato, new Date(), 1000);
						circle++;
					}
					else {//启动休息的倒计时
						button.setText("番茄");
						WR = true;
						timu.setText("休息时间");
						if (circle == 7) {
							//大休息
							bigRest = new timeCount(15, "大休息结束");
							timer.schedule(bigRest, new Date(), 1000);
							circle = 0;
						}else {
							//小休息
							rest = new timeCount(5, "小休息结束");
							timer.schedule(rest, new Date(), 1000);
							circle++;
						}
					}
				}
	}
});
		button.setBounds(107, 181, 60, 30);
		contentPane.add(button);
		
		//显示当前时间
		JLabel label = new JLabel();
		label.setBounds(312, 10, 112, 30);
		contentPane.add(label);
		
		JLabel lblV = new JLabel("V1.0");
		lblV.setBounds(10, 10, 54, 15);
		contentPane.add(lblV);
		
		timer.schedule(new TimerTask() {
			
			SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
			@Override
			 public void run() {
			  time = dateFormatter.format(Calendar.getInstance().getTime());
			  label.setText(time);
			 }
				
			},
		 new Date(), 1000);
}
	public void reset() {
		if (tomato != null) {
			tomato.resetStop();
			tomato.cancel();
		}
		if (rest != null) {
			rest.resetStop();
			rest.cancel();
		}
		if (bigRest != null) {
			bigRest.resetStop();
			bigRest.cancel();
		}
		sound.close();
		WORK = false;
		WR = true;
		circle = 0;
		timeLabel.setText("点击开始");
		timu.setText("");
		button.setText("开始");
		
		
	}
	public void pause() {
		sound.close();
		if (circle%2 == 1) {
			tomato.stop();
		}else if (circle == 0 && bigRest != null) {
			bigRest.stop();
		}else if (rest != null) {
			rest.stop();
		}
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TomatoUI frame = new TomatoUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
//设计一个任务内部类
	class timeCount extends TimerTask{
		private String prompt;
		private int fanqie;
		private volatile boolean stop;
		public timeCount(int n, String prompt) {
			this.prompt = prompt;
			fanqie = n * 60;
			stop = false;
		}

		@Override
		public void run() {
			int minute = fanqie / 60;
			int second = fanqie % 60;
			countDown = minute + " : " + second;
			timeLabel.setText(countDown);
			if (fanqie <= 0) {
				timeLabel.setText(prompt);
				WORK = false;
				sound.launch(null,null);
				this.cancel();
			}
			fanqie--;
			while(stop) {
			}
		}
		
		public void stop() {
			stop = !stop;
		}
		
		public void resetStop() {
			stop = false;
		}
	}
}
