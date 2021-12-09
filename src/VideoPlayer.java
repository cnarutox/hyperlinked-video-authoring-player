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
	boolean isPaused = true;

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

	LinkCreate linkCreate;
	VideoPlayer that = this;

	public VideoPlayer() {
		links = new Links();

		// String videoSourcePath = videoPath.getAbsolutePath();

		// Region region1 = new Region(10, 10, 20, 20, 10);
		// region1.setEnd(100, 100, 200, 200, 1000);
		// region1.setLinkedInfo(String.format("%sAIFilmTwo", videoSourcePath), 50);
		// Region region2 = new Region(100, 100, 200, 200, 20);
		// region2.setEnd(10, 10, 20, 20, 2000);
		// region2.setLinkedInfo(String.format("%sAIFilmTwo", videoSourcePath), 1000);

		// links.putRegion(String.format("%s", videoSourcePath), region1);
		// links.putRegion(String.format("%s", videoSourcePath), region2);
		// links.toLocalFile(videoSourcePath);

		// System.out.println(String.format("read from %s",
		// String.format("%sAIFilmOne/%s.txt", videoSourcePath, "AIFilmOne")));

		// links.readLocalFile(String.format("%sAIFilmOne/%s.txt", videoSourcePath,
		// "AIFilmOne"));
		// for (String key : links.getKeySet()) {
		// for (Region region : links.getItems(key)) {
		// System.out.println(region);
		// }
		// }

		linkDisplay = new LinkDisplay(this);

		t = new Thread(new Runnable() {
			public void run() {
				while (true) {
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
		if (videoPath == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(frameImg, 5, 5, this);
		if (Authoring.isCreating && linkCreate != null && linkCreate.isDrawing)
			linkCreate.draw(g2);
		linkDisplay.drawRegion(g2);
		// }
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
				// System.out.println("mousePressed");
				beforeDragStatus = isPaused;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("mouseReleased");
				cacheIndex = currentFrame;
				currentTotalTime = (long) (currentFrame * ((double) 1000 / 30));
				if (!beforeDragStatus) {
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

	public void importVideo(File videoPath, int startFrame) {
		if (audio != null)
			audio.stop();

		this.videoPath = videoPath;
		System.out.println("importVideo " + videoPath);
		links.fromFile = videoPath.getAbsolutePath();
		isPaused = true;
		beforeDragStatus = true;
		cache.clear();
		lastStartTime = 0;
		if (linkCreate != null) {
			linkCreate.createBtn.setEnabled(true);
			linkCreate.saveBtn.setEnabled(true);
		}

		currentFrame = startFrame;
		cacheIndex = currentFrame;
		currentTotalTime = (long) (currentFrame * ((double) 1000 / 30));
		slider.setValue(startFrame + 1);

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
		frameImg = readImageRGB(width, height, videoPath.getAbsolutePath() + "/" + files[currentFrame], frameImg);
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

		// VideoPlayer VP = new VideoPlayer();
		Authoring authoring = new Authoring("HyperLinked Video Authoring Tool");
		// Authoring.ImportVideo importPanel = new Authoring.ImportVideo();
		// authoring.getContentPane().add(importPanel, BorderLayout.NORTH);
		JPanel mainVideo = authoring.createVideoArea();
		authoring.add(mainVideo);

		VideoPlayer mainVideoPlayer = (VideoPlayer) mainVideo.getComponent(0);

		JPanel importVideoPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
		JTextField mainVideoName = new JTextField(10);
		JButton mainBtn = new JButton("Import Video");

		mainBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int val = fc.showOpenDialog(null);
				if (val == JFileChooser.APPROVE_OPTION) {
					File videoFile = fc.getSelectedFile();
					mainVideoName.setText(videoFile.getName());
					mainVideoPlayer.importVideo(videoFile, 0);
					System.out.println("readLocalFile: "
							+ String.format("%s/%s.txt", videoFile.getAbsolutePath(), videoFile.getName()));
					mainVideoPlayer.links.readLocalFile(
							String.format("%s/%s.txt", videoFile.getAbsolutePath(), videoFile.getName()));
					System.out.println(mainVideoPlayer.links);
					// System.out.println(mainVideoPlayer.links.getKeySet());
					// for (String key : mainVideoPlayer.links.getKeySet()) {
					// for (Region region : mainVideoPlayer.links.getItems(key)) {
					// System.out.println(region);
					// }
					// }
				} else {
					// videoName.setText("file not selected");
				}
			}
		});
		importVideoPanel.add(mainVideoName);
		importVideoPanel.add(mainBtn);
		authoring.getContentPane().add(importVideoPanel, BorderLayout.NORTH);

		authoring.pack();
		authoring.setVisible(true);
		authoring.setPreferredSize(new Dimension(720, 480));
		authoring.setLocationRelativeTo(null);
		authoring.setDefaultCloseOperation(3);
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
