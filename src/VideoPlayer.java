import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.Timer;

import javax.sound.sampled.*;
import javax.swing.*;

public class VideoPlayer extends JPanel {

	static int width = 352;
	static int height = 288;
	static BufferedImage frameN;
	static int currentFrame = 0;
	static BufferedImage BI;
	static String[] files;
	static Boolean isPaused = true;

	static boolean firstStart = true;
	static long startTime;
	static long currentTotalTime = 0;
	static long lastStartTime;
	static int filelength = 9000;
	JSlider slider;

	Thread t;

	class LRUCache extends LinkedHashMap<Integer, BufferedImage> {
		private int capacity;

		public LRUCache(int capacity) {
			super(capacity, 0.75F, false);
			this.capacity = capacity;
		}

		public BufferedImage get(int key) {
			return super.getOrDefault(key, null);
		}

		public void put(int key, BufferedImage value) {
			super.put(key, value);
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<Integer, BufferedImage> eldest) {
			return size() >= capacity;
		}
	}

	LRUCache cache = new LRUCache(300);
	int cacheIndex;

	int soundindex;

	// static PlaySound playSound;

	BufferedImage bi;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(2 * width, 2 * height);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// System.out.println("paintComponent");

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bi, 0, 0, this);
		// g2.drawImage(bi, null, 0, 0);
	}

	// class TimePrinter implements ActionListener {
	// private VideoPlayer frame;

	// TimePrinter(VideoPlayer frame) {
	// this.frame = frame;
	// }

	// public void actionPerformed(ActionEvent event) {
	// synchronized (cache) {

	// if (currentFrame < filelength && !isPaused) {
	// // Starts the music :P

	// // String imgPath = "DS/AIFilmOne/" + files[currentFrame];
	// // // System.out.println(imgPath);

	// // readImageRGB(width, height, imgPath, BI);

	// // System.out.println(currentFrame + " " + cache.keySet());
	// // System.out.println(t.isAlive());
	// System.out.println(currentFrame + " " + cache.size());

	// System.out.println(System.currentTimeMillis());
	// this.frame.bi = cache.get(currentFrame);
	// this.frame.repaint();

	// currentFrame += 1;
	// slider.setValue(currentFrame + 1);
	// }
	// }
	// }
	// }

	public class PlayWaveException extends Exception {

		public PlayWaveException(String message) {
			super(message);
		}

		public PlayWaveException(Throwable cause) {
			super(cause);
		}

		public PlayWaveException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	public class Sound {
		private Clip clip;

		public Sound(String fileName) {
			try {
				File file = new File(fileName);
				if (file.exists()) {
					AudioInputStream sound = AudioSystem.getAudioInputStream(file);
					// load the sound into memory (a Clip)
					clip = AudioSystem.getClip();
					clip.open(sound);
				} else {
					throw new RuntimeException("Sound: file not found: " + fileName);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Malformed URL: " + e);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Unsupported Audio File: " + e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Input/Output Error: " + e);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
			}

			// play, stop, loop the sound clip
		}

		public void play(long microseconds) {
			// clip.setFramePosition(0); // Must always rewind!
			clip.setMicrosecondPosition(microseconds * 1000);
			clip.start();
		}

		public void loop() {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}

		public void stop() {
			clip.stop();
		}
	}

	private static void readImageRGB(int width, int height, String imgPath, BufferedImage img) {
		try {
			int frameLength = width * height * 3;

			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind + height * width];
					byte b = bytes[ind + height * width * 2];

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					// int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x, y, pix);
					ind++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playVideo() {

		BI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		String filename = "C:/Users/16129/OneDrive - University of Southern California/CS576/CSCI576 - 20213 - Multimedia Systems Design - 1242021 - 354 PM/DS/AIFilmOne/AIFilmOne.wav";

		Sound sound = new Sound(filename);
		File folder = new File(
				"C:/Users/16129/OneDrive - University of Southern California/CS576/CSCI576 - 20213 - Multimedia Systems Design - 1242021 - 354 PM/DS/AIFilmOne");

		files = new String[filelength];
		int index = 0;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getName().endsWith(".rgb")) {
				files[index] = fileEntry.getName();
				index += 1;
			}
		}

		cacheIndex = 0;

		t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					synchronized (cache) {

						// System.out.println(currentFrame + " " + cache.size());
						if (cacheIndex <  filelength){
							if (cache.size() > 0 && currentFrame - 150 > cache.keySet().iterator().next()) {
								// System.out.println("Remove " + cache.keySet().iterator().next());
	
								cache.remove(cache.keySet().iterator().next());
							}
							if (currentFrame + 150 > cacheIndex) {
								// System.out.println(currentFrame + " " + cacheIndex);
								if (!cache.containsKey(cacheIndex)) {
	
									String imgPath = "DS/AIFilmOne/" + files[cacheIndex];
									// BufferedImage cBI = new BufferedImage(width, height,
									// BufferedImage.TYPE_INT_RGB);
									BI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
									readImageRGB(width, height, imgPath, BI);
	
									cache.put(cacheIndex, BI);
								}
	
								cacheIndex += 1;
							}
						}
						
					}
				}
			}
		});
		t.start();

		JFrame frame = new JFrame();
		frame.setSize(2 * width, 2 * height);
		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

		// frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));

		VideoPlayer pp = new VideoPlayer();
		JButton bPause = new JButton("Pause");
		JButton bResume = new JButton("Resume");
		JTextField yourInputField = new JTextField(16);
		JButton bSelect = new JButton("Select");

		frame.getContentPane().add(yourInputField);
		frame.getContentPane().add(pp);

		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// ActionListener listener = new TimePrinter(pp);
		// Timer t = new Timer(1000 / 30, listener);
		// t.start();
		// }
		// });
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				synchronized (cache) {
					if (currentFrame < filelength && !isPaused) {
						long currentTime = System.currentTimeMillis();
						if ((currentTotalTime + currentTime - lastStartTime) % 1000 < 10) {
							// System.out.println(currentTotalTime + currentTime -
							// lastStartTime);
							currentFrame = (int) ((currentTotalTime + currentTime - lastStartTime)
									/ 1000) * 30;
						}

						// System.out.println(currentFrame + " " + cache.keySet());
						if (cache.containsKey(currentFrame)) {
							pp.bi = cache.get(currentFrame);
						} else {
							String imgPath = "DS/AIFilmOne/" + files[currentFrame];
							readImageRGB(width, height, imgPath, BI);
							pp.bi = BI;
						}
						pp.repaint();

						currentFrame += 1;
						// slider.setValue(currentFrame + 1);
					}
				}
			}
		}, 0, 1000 / 30);

		bPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isPaused) {
					// currentTotalTime += (int)((System.currentTimeMillis() - lastStartTime) / (1000/30)) * (1000/30);
					currentTotalTime += System.currentTimeMillis() - lastStartTime;
					// System.out.println("pause " + currentTotalTime);
				}
				isPaused = true;
				sound.stop();
			}
		});
		bSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (cache) {

					if (yourInputField.getText().length() > 0 && Integer.valueOf(yourInputField.getText()) < filelength
							&& Integer.valueOf(yourInputField.getText()) >= 0) {
						currentFrame = Integer.valueOf(yourInputField.getText());
						cacheIndex = currentFrame;
						currentTotalTime = (long)(currentFrame * ((double)1000 / 30));
						System.out.println("select " + currentTotalTime);
						if (!isPaused) {
							lastStartTime = System.currentTimeMillis();
							sound.play(currentTotalTime);
						} else if (isPaused) {
							// BufferedImage BI = new BufferedImage(width, height,
							// BufferedImage.TYPE_INT_RGB);
							// String imgPath = "DS/AIFilmOne/" + files[currentFrame];
							// readImageRGB(width, height, imgPath, BI);
							if (cache.containsKey(currentFrame)) {
								pp.bi = cache.get(currentFrame);
							} else {
								String imgPath = "DS/AIFilmOne/" + files[currentFrame];
								readImageRGB(width, height, imgPath, BI);
								pp.bi = BI;
							}
							pp.repaint();
						}
					}
				}

			}
		});
		bResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstStart) {
					startTime = System.currentTimeMillis();
					firstStart = false;
				}
				if (isPaused) {
					lastStartTime = System.currentTimeMillis();
					sound.play(currentTotalTime);
				}
				isPaused = false;
				// new Thread(new Runnable() {
				// public void run() {
				// String filename = "C:/Users/16129/OneDrive - University of Southern
				// California/CS576/CSCI576 - 20213 - Multimedia Systems Design - 1242021 - 354
				// PM/DS/AIFilmOne/AIFilmOne.wav";
				// FileInputStream inputStream;
				// try {
				// inputStream = new FileInputStream(filename);
				// } catch (FileNotFoundException e) {
				// e.printStackTrace();
				// return;
				// }
				// playSound = new PlaySound(inputStream);
				// AudioInputStream audioInputStream = null;
				// try {
				// // InputStream bufferedIn = new BufferedInputStream(this.waveStream); // new
				// audioInputStream = AudioSystem
				// .getAudioInputStream(new BufferedInputStream(inputStream));
				// } catch (UnsupportedAudioFileException e1) {
				// e1.printStackTrace();
				// } catch (IOException e1) {
				// e1.printStackTrace();
				// }

				// // Obtain the information about the AudioInputStream
				// AudioFormat audioFormat = audioInputStream.getFormat();
				// Info info = new Info(SourceDataLine.class, audioFormat);

				// // opens the audio channel
				// SourceDataLine dataLine = null;
				// try {
				// dataLine = (SourceDataLine) AudioSystem.getLine(info);
				// dataLine.open(audioFormat, 26460000);
				// } catch (LineUnavailableException e1) {
				// e1.printStackTrace();
				// }

				// // Starts the music :P

				// int readBytes = 0;
				// byte[] audioBuffer = new byte[2940];
				// // byte[] audioBuffer = new byte[88200];
				// soundindex = 0;

				// try {
				// // while (readBytes != -1 && soundindex < currentFrame) {
				// // readBytes = audioInputStream.read(audioBuffer, 0, audioBuffer.length);
				// // soundindex += 1;
				// // }
				// // System.out.println("start " + currentFrame + " " + soundindex);

				// while (readBytes != -1) {

				// readBytes = audioInputStream.read(audioBuffer, 0, audioBuffer.length);
				// dataLine.start();
				// if (readBytes >= 0) {
				// dataLine.write(audioBuffer, 0, readBytes);
				// soundindex += 1;
				// }

				// }
				// } catch (IOException e1) {
				// // throw new PlayWaveException(e1);
				// e1.printStackTrace();
				// } finally {
				// // plays what's left and and closes the audioChannel
				// dataLine.drain();
				// dataLine.close();
				// }
				// }
				// }).start();
			}
		});
		frame.getContentPane().add(bPause);
		frame.getContentPane().add(bResume);
		frame.getContentPane().add(bSelect);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		VideoPlayer VP = new VideoPlayer();
		VP.playVideo();
	}
}
