
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.Timer;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

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
	int cacheIndex;
	int soundindex;

	Links links;
	LinkDisplay linkDisplay;

	// static PlaySound playSound;

	BufferedImage frameImg;
	Thread t;

	VideoPlayer that = this;

	public VideoPlayer() {
		// links = new Links();

		// Region region1 = new Region(33, 33, 66, 77, 600);
		// region1.setEnd(10, 900, 200, 200, 1000);
		// // region1.setLinkedInfo("AIFilmOne", 50);
		// // Region region2 = new Region(10, 10, 200, 200, 20);
		// // region2.setEnd(110, 110, 210, 210, 2000);
		// // region2.setLinkedInfo("AIFilmOne", 1000);

		// links.putRegion("AIFilmOne", region1);
		// // links.putRegion("AIFilmOne", region2);
		// // // System.out.println("links: " + links.linkedMap.get("C:/Users/16129/OneDrive -
		// // // University of Southern
		// // // California/CS576/DS/AIFilmOne").get(0).getVetex(true)[0]);
		// // linkDisplay = new LinkDisplay();
		// // links.toLocalFile("links.txt");
		// links.readLocalFile("C:/Users/16129/OneDrive - University of Southern California/CS576/CSCI576 - 20213 - Multimedia Systems Design - 1242021 - 354 PM/links.txt");
		
		// for (String str: links.getKeySet()){
		// 	ArrayList<Region> regions = (ArrayList<Region>)links.getItems(str);
		// 	for (Region r: regions){	
		// 		System.out.println(r);
		// 	}
		// }
		
	}

	public VideoPlayer(Authoring.ImportVideo importPanel) {
		importPanel.linkedPlayer = this;
	}

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
		g2.drawImage(frameImg, 0, 0, this);

		ArrayList<Region> regions = (ArrayList<Region>) links.inRegion(
			"AlFilmOne", currentFrame);
		System.out.println(regions.size());
		if (regions.size() > 0) {

			for (Iterator<Region> it = regions.iterator(); it.hasNext();) {
				Region curRegion = (Region) it.next();

				linkDisplay.draw(curRegion, g2);
			}
		}
		// g2.drawRect(13, 56, 200, 100);
		// g2.drawImage(bi, null, 0, 0);
	}

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

	private static BufferedImage readImageRGB(int width, int height, String imgPath, BufferedImage img) {
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
		return img;
	}

	boolean beforeDragStatus = false;
	JButton btnPause = new JButton("Pause");
	JButton btnPlay = new JButton("Play");
	JTextField yourInputField = new JTextField(16);
	JButton bSelect = new JButton("Select");
	JLabel frameLabel;

	public void link(Authoring.ImportVideo importVideo, JSlider jSlider, JButton play, JButton pause, JLabel label) {
		importVideo.linkedPlayer = this;
		slider = jSlider;
		btnPlay = play;
		btnPause = pause;
		frameLabel = label;
	}

	public void importVideo(Authoring.ImportVideo importPanel) {

		File videoPath = importPanel.videoFile;
		Sound audio = new Sound(
				String.format("%s/%s.wav", videoPath.getAbsolutePath(), videoPath.getName()));
		cacheIndex = 0;

		int index = 0;
		files = new String[videoPath.listFiles().length];
		for (final File fileEntry : videoPath.listFiles()) {
			if (fileEntry.getName().endsWith(".rgb")) {
				files[index] = fileEntry.getName();
				index += 1;
			}
		}
		filelength = index;
		slider.setMaximum(filelength);
		System.out.println("Loaded " + videoPath);

		BI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					// System.out.println(currentFrame + " " + cache.size());
					if (cacheIndex >= filelength) {
						continue;
					}
					synchronized (cache) {
						if (cache.size() > 0 && currentFrame - 150 > cache.keySet().iterator().next()) {
							cache.remove(cache.keySet().iterator().next());
						}
					}
					if (currentFrame + 150 > cacheIndex) {
						synchronized (cache) {
							if (!cache.containsKey(cacheIndex)) {

								String imgPath = videoPath.getAbsolutePath() + "/" + files[cacheIndex];
								BI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
								readImageRGB(width, height, imgPath, BI);
								cache.put(cacheIndex, BI);
							}
						}
						cacheIndex += 1;
					}
				}
			}
		});
		t.start();

		// frame.setLayout(new BoxLayout(frame, BoxLayout.PAGE_AXIS));

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				synchronized (cache) {
					if (currentFrame < filelength && !isPaused) {
						long currentTime = System.currentTimeMillis();
						if ((currentTotalTime + currentTime - lastStartTime) % 1000 < 10) {
							currentFrame = (int) ((currentTotalTime + currentTime - lastStartTime)
									/ 1000) * 30;
						}
						if (cache.containsKey(currentFrame)) {
							that.frameImg = cache.get(currentFrame);
						} else {
							that.frameImg = readImageRGB(width, height,
									videoPath.getAbsolutePath() + "/" + files[currentFrame], BI);
						}
						that.repaint();
						currentFrame += 1;
						slider.setValue(currentFrame + 1);
					}
				}
			}
		}, 0, 1000 / 30);

		btnPause.addActionListener(e -> {
			if (!isPaused) {
				currentTotalTime += System.currentTimeMillis() - lastStartTime;
			}
			isPaused = true;
			audio.stop();
		});
		slider.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				beforeDragStatus = isPaused;
				System.out.println("pressed " + isPaused);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				cacheIndex = currentFrame;
				if (!beforeDragStatus) {
					currentTotalTime = (long) (currentFrame * ((double) 1000 / 30));
					audio.play(currentTotalTime);
					lastStartTime = System.currentTimeMillis();
				}
				isPaused = beforeDragStatus;
				System.out.println("release " + isPaused);
			}
		});
		slider.addChangeListener(e -> {
			// TODO Auto-generated method stub
			JSlider source = (JSlider) e.getSource();
			frameLabel.setText(String.valueOf(currentFrame));
			if (!source.getValueIsAdjusting()) {
				return;
			}
			isPaused = true;
			audio.stop();
			currentFrame = source.getValue() - 1;

			if (cache.containsKey(currentFrame)) {
				that.frameImg = cache.get(currentFrame);
			} else {
				String imgPath = videoPath.getAbsolutePath() + "/" + files[currentFrame];
				that.frameImg = readImageRGB(width, height, imgPath, BI);
			}
			that.repaint();
		});
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isPaused) {
					lastStartTime = System.currentTimeMillis();
					audio.play(currentTotalTime);
				}
				isPaused = false;
			}
		});
	}

	public static void main(String[] args) {

		VideoPlayer VP = new VideoPlayer();
	}
}
