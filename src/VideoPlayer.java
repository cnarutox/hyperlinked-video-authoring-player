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

	int width = 352;
	int height = 288;
	BufferedImage frameN;
	int currentFrame = 0;
	String[] files;
	Boolean isPaused = true;

	boolean firstStart = true;
	long startTime;
	long currentTotalTime = 0;
	long lastStartTime;
	int filelength = 0;
	JSlider slider;
	int cacheIndex = 0;
	int soundindex;
	Timer timer = new Timer();
	File videoPath;
	Links links;
	LinkDisplay linkDisplay;
	Sound audio;

	// static PlaySound playSound;

	BufferedImage frameImg;
	Thread t;

	VideoPlayer that = this;

	public VideoPlayer() {
		links = new Links();

		Region region1 = new Region(10, 10, 20, 20, 10);
		region1.setEnd(100, 100, 200, 200, 1000);
		Region region2 = new Region(10, 10, 200, 200, 20);
		region2.setEnd(110, 110, 210, 210, 2000);

		// links.putRegion("C:/Users/16129/OneDrive - University of Southern
		// California/CS576/DS/AIFilmOne", region1);
		// links.putRegion("C:/Users/16129/OneDrive - University of Southern
		// California/CS576/DS/AIFilmOne", region2);
		linkDisplay = new LinkDisplay();
		// links.toLocalFile("C:/Users/16129/OneDrive - University of Southern
		// California/CS576/DS/links.txt");

		t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					// System.out.println(currentFrame + " " + cache.size());
					if (cacheIndex >= filelength) {
						continue;
					}
					synchronized (cache) {
						int first = cache.keySet().iterator().next();
						if (cache.size() > 0 && currentFrame - 150 > first) {
							cache.remove(first);
						}
					}
					if (currentFrame + 150 > cacheIndex) {
						synchronized (cache) {
							if (!cache.containsKey(cacheIndex)) {
								String imgPath = videoPath.getAbsolutePath() + "/" + files[cacheIndex];
								readImageRGB(width, height, imgPath, that.frameImg);
								cache.put(cacheIndex, that.frameImg);
							}
						}
						cacheIndex += 1;
					}
				}
			}
		});
		t.start();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (currentFrame >= filelength || isPaused)
					return;
				long currentTime = System.currentTimeMillis();
				if ((currentTotalTime + currentTime - lastStartTime) % 1000 < 10) {
					currentFrame = (int) ((currentTotalTime + currentTime - lastStartTime) / 1000) * 30;
				}
				synchronized (cache) {
					if (cache.containsKey(currentFrame)) {
						that.frameImg = cache.get(currentFrame);
						that.repaint();
						currentFrame += 1;
						slider.setValue(currentFrame + 1);
						return;
					}
				}
				that.frameImg = readImageRGB(width, height, videoPath.getAbsolutePath() + "/" + files[currentFrame],
						that.frameImg);
				that.repaint();
				currentFrame += 1;
				slider.setValue(currentFrame + 1);
			}
		}, 0, 1000 / 30);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width + 10, height + 10);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(frameImg, 5, 5, this);

		if (videoPath == null)
			return;
		ArrayList<Region> regions = (ArrayList<Region>) links.inRegion(
				videoPath.getAbsolutePath(), currentFrame);
		for (Iterator<Region> it = regions.iterator(); it.hasNext();) {
			Region curRegion = (Region) it.next();
			linkDisplay.draw(curRegion, g2);
		}
	}

	boolean beforeDragStatus = false;
	JButton btnPause = new JButton("Pause");
	JButton btnPlay = new JButton("Play");
	JTextField yourInputField = new JTextField(16);
	JButton bSelect = new JButton("Select");
	JLabel frameLabel;

	public void binding(JSlider jSlider, JButton play, JButton pause, JLabel label) {
		slider = jSlider;
		btnPlay = play;
		btnPause = pause;
		frameLabel = label;
		btnPause.addActionListener(e -> {
			if (filelength == 0)
				return;
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
			}
		});
		slider.addChangeListener(e -> {
			// TODO Auto-generated method stub
			JSlider source = (JSlider) e.getSource();
			frameLabel.setText(String.valueOf(currentFrame));
			if (!source.getValueIsAdjusting() || audio == null || videoPath == null) {
				return;
			}
			isPaused = true;
			audio.stop();
			currentFrame = source.getValue() - 1;
			if (cache.containsKey(currentFrame)) {
				that.frameImg = cache.get(currentFrame);
			} else {
				String imgPath = videoPath.getAbsolutePath() + "/" + files[currentFrame];
				that.frameImg = readImageRGB(width, height, imgPath, that.frameImg);
			}
			that.repaint();
		});
		btnPlay.addActionListener(e -> {
			if (videoPath == null)
				return;
			if (isPaused) {
				lastStartTime = System.currentTimeMillis();
				audio.play(currentTotalTime);
			}
			isPaused = false;
		});
	}

	public void importVideo(File videoPath) {
		this.videoPath = videoPath;
		isPaused = true;
		beforeDragStatus = true;
		currentFrame = 0;
		cacheIndex = 0;
		lastStartTime = 0;
		cache.clear();
		slider.setValue(1);
		if (audio != null)
			audio.stop();

		audio = new Sound(
				String.format("%s/%s.wav", videoPath.getAbsolutePath(), videoPath.getName()));

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
		if (frameImg == null)
			frameImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		frameImg = readImageRGB(width, height, videoPath.getAbsolutePath() + "/" + files[0], frameImg);
		repaint();
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

	public static void main(String[] args) {

		VideoPlayer VP = new VideoPlayer();
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

}