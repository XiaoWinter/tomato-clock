package cn.xiaoadong.sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
/**
 * 发声的装置-->CD-->整首歌-->一个个音符
 * 这个类不是线程安全的
 * @author Administrator
 *
 */
public class TomatoSound {
	private Sequencer squ;
	public TomatoSound() {
		try {
			squ = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	//一个个音符
	public static MidiEvent makeEvent(int com, int cha, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(com, cha, one, two);
			//tick时机，播放的时机
			event = new MidiEvent(a, tick);
		}catch(Exception e) {}
		return event;
	}

	//默认值
	private void sound(){
		sound(60,50 );
	}
	
	@SuppressWarnings("static-access")
	public void sound(int tone, int hz) {
		//1.获得音序器并打开(发声的装置)
		try {
			//不是线程安全的，每次调用squ都变了，我们只能关掉最后的squ，所以要在方法外初始化
			//squ = MidiSystem.getSequencer();
			squ.open();//打开音响
			//构造具有指定的定时 division 类型和定时精度的新 MIDI Sequence（CD）
			Sequence sq = new Sequence(Sequence.PPQ, 4);
			//整首信息
			Track tarck = sq.createTrack();
			
			for(int i = 1; i < 4; i++) {
				//频道，音调，音量
				tarck.add(makeEvent(144, 1, tone, 100, i));
				//短促
//				tarck.add(makeEvent(176, 1, 127, 0, i));
				tarck.add(makeEvent(128, 1, tone, 100, i + 2));
			}
			squ.setSequence(sq);//CD放到DVD上
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
			//循环
			squ.setLoopCount(squ.LOOP_CONTINUOUSLY);
			//设置速度，以每分钟的拍数为单位。实际的回放速度是指定值和速度因子的乘积。
			squ.setTempoInBPM(hz);
			squ.start();
	}
	public void launch(Integer tone, Integer hz) {
		if (tone == null || hz == null) {
			sound();
		}
		sound(tone, hz);
	}
	public void close() {
		if (squ != null) {
			squ.close();
		}
	}
	public static void main(String[] args) {
		new TomatoSound().launch(null,null);
	}
}
