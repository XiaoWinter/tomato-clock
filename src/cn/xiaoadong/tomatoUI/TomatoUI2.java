package cn.xiaoadong.tomatoUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.xiaoadong.setTomato.SetTomato1;
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
import javax.swing.WindowConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
/**
 * 使用锁实现暂停功能，问题是
 * 可能会出问题说不清除，有良好的代码可读性
 * 符合面向对象的设计方法
 * @author Administrator
 *
 */
public class TomatoUI2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Boolean WR= true;
	private Timer timer;
	//当前时间
	private String time;
	//计时时间
	private String countDown;
	//显示时间
	private JLabel timeLabel;
	//显示状态
	private JLabel timu;
	//开始按钮
	private JButton button;
	//日期格式
	private String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	private int circle;//一个大循环，4番茄+3休息+1大休息
	//工作状态
	private volatile static Boolean WORK = false;
	private volatile static Boolean STOP = false;
	private TomatoSound sound;
	//2ge线程任务
	private timeCount tomato;
	private timeCount rest;
	private timeCount bigRest;
	//番茄时间，休息时间
	private int tomatoTime;
	private int restTime;
	private int bigRestTime;
	//自身的引用
	private TomatoUI2 tUi2;
	//显示日期
	private JLabel label;
	/**
	 * Create the frame.
	 */
	public TomatoUI2() {
		//
		timer = new Timer();
		//
		sound = new TomatoSound();
		//
		tomatoTime = 25;
		restTime = 7;
		bigRestTime = 15;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu_1 = new JMenu("报表");
		menuBar.add(menu_1);
		
		JMenuItem menuItem_3 = new JMenuItem("今日历程");
		menu_1.add(menuItem_3);
		
		JMenuItem menuItem_4 = new JMenuItem("本周总结");
		menu_1.add(menuItem_4);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("本月总结");
		menu_1.add(mntmNewMenuItem);
		
		JMenu menu = new JMenu("设置");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("时间设置");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//重置番茄钟
				reset();
				//弹出窗体
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							SetTomato1 frame = new SetTomato1(tUi2);
							frame.setVisible(true);
							frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		menu.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("提醒音设置");
		menu.add(menuItem_1);
		
		JMenuItem menuItem_2 = new JMenuItem("自定义设置");
		menu.add(menuItem_2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel timepanel = new JPanel();
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
					STOP = false;//防止线程休眠
					sound.close();
					if (WR) {//启动番茄的倒计时
						tomato = new timeCount(tomatoTime, "小番茄结束");
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
							bigRest = new timeCount(bigRestTime, "大休息结束");
							timer.schedule(bigRest, new Date(), 1000);
							circle = 0;
						}else {
							//小休息
							rest = new timeCount(restTime, "小休息结束");
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
		label = new JLabel();
		label.setBounds(312, 10, 112, 30);
		contentPane.add(label);
		
		JLabel lblV = new JLabel("V1.1");
		lblV.setBounds(370, 225, 54, 15);
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
		tUi2 = this;
}
	public void reset() {
		huifu();
		//处理线程,要先唤醒，否则会出bug，
		if (tomato != null) {
			tomato.cancel();
		}
		if (rest != null) {
			rest.cancel();
		}
		if (bigRest != null) {
			bigRest.cancel();
		}
		//字段重置
		sound.close();
		WORK = false;
		WR = true;
		STOP = false;
		circle = 0;
		timeLabel.setText("点击开始");
		timu.setText("");
		button.setText("开始");
	}
	public void pause() {
		sound.close();
		if (STOP) {
			STOP = false;//让线程休眠
			huifu();
		}else {
			STOP = true;
		}
	}
	/**
	 * 唤醒休眠的timeTask
	 */
	private void huifu() {
		if (tomato != null) {
			synchronized (tomato) {
				tomato.notify();
			}
		}
		if (rest != null) {
			synchronized (rest) {
				rest.notify();
			}
		}
		if (bigRest != null) {
			synchronized (bigRest) {
				bigRest.notify();
			}
		}
	}
	//番茄钟时间的设置
	public int getTomatoTime() {
		return tomatoTime;
	}
	public void setTomatoTime(int tomatoTime) {
		this.tomatoTime = tomatoTime;
	}
	public int getRestTime() {
		return restTime;
	}
	public void setRestTime(int restTime) {
		this.restTime = restTime;
	}
	public int getBigRestTime() {
		return bigRestTime;
	}
	public void setBigRestTime(int bigRestTime) {
		this.bigRestTime = bigRestTime;
	}
//设计一个任务内部类
	class timeCount extends TimerTask{
		private String prompt;
		private int fanqie;
		public timeCount(int n, String prompt) {
			this.prompt = prompt;
			fanqie = n * 60;
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
				sound.launch();
				this.cancel();
			}
			
			fanqie--;
			
			if (STOP) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TomatoUI2 frame = new TomatoUI2();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}