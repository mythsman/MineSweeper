import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Clock extends JLabel implements Runnable {
	private static final long serialVersionUID = 1L;
	public int time;
	private boolean interrupt;
	private Thread thread;

	private String getTime() {
		int minutes = time / 6000;
		int seconds = (time - minutes * 6000) / 100;
		int millis = time - minutes * 6000 - seconds * 100;
		return String.format("%02d:%02d:%02d\n", minutes, seconds, millis);
	}

	public void start() {
		interrupt = false;
		this.setText(getTime());
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFont(new Font("ÖÐ»ªÐÐ¿¬", 1, 40));
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (!interrupt) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (time == 600000) {
				stop();
			}
			time += 1;
			this.setText(getTime());

		}
	}

	public void stop() {
		interrupt = true;
	}
}
