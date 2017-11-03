package fkg.other.home.chara;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javafx.scene.media.AudioClip;

public class IdleTimer extends Timer {
	private final PartCharacter partCharacter;
	private TimerTask idleTimerTask = null;

	public IdleTimer(PartCharacter partCharacter) {
		super(true);//后台线程
		this.partCharacter = partCharacter;
	}

	public void reset() {
		if (this.idleTimerTask != null) {
			this.idleTimerTask.cancel();
		}
		this.purge();

		this.idleTimerTask = new TimerTask() {
			@Override
			public void run() {
				AudioClip idleVoice = IdleTimer.this.partCharacter.charaRes.characterData.getIdleVoice();
				idleVoice.play();
			}
		};
		this.schedule(this.idleTimerTask, TimeUnit.MINUTES.toMillis(3), TimeUnit.MINUTES.toMillis(3));
	}
}
