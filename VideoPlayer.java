
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;

import java.io.File; // Import the File class
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.Timer;
import java.util.GregorianCalendar;

public class VideoPlayer extends JPanel{

	static int width = 352;
	static int height = 288;
	static BufferedImage frameN;
	static int currentFrame = 0;
	static BufferedImage BI;
	static String[] files;
	static Boolean isPaused = true;

	BufferedImage bi;

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(2*width, 2*height);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("paintComponent");

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(bi, 0, 0, this);
		// g2.drawImage(bi, null, 0, 0);
	}

	class TimePrinter implements ActionListener {
		private VideoPlayer frame;

		TimePrinter(VideoPlayer frame) {
			this.frame = frame;
		}

		public void actionPerformed(ActionEvent event) {
			if (currentFrame < files.length && !isPaused) {
				String imgPath = "DS/AIFilmOne/" + files[currentFrame];
				System.out.println(imgPath);

				readImageRGB(width, height, imgPath, BI);
				this.frame.bi = BI;
				this.frame.repaint();
				currentFrame += 1;
			}
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

	public static void main(String[] args) {

		VideoPlayer VP = new VideoPlayer();
		BI = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		File folder = new File("DS/AIFilmOne");

		files = new String[folder.listFiles().length];
		int index = 0;
		for (final File fileEntry : folder.listFiles()) {
			// System.out.println(fileEntry.getName());
			if (fileEntry.getName().endsWith(".rgb")) {
				files[index] = fileEntry.getName();
				index += 1;
			}
		}
		System.out.println(files.length);

		// readImageRGB(width, height, "DS/AlFilmOneSub/AIFilmOne0011.rgb", BI);

		JFrame frame = new JFrame();
		frame.setSize(width, height);
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

		Runnable rn = new Runnable() {
			public void run() {
				ActionListener listener = VP.new TimePrinter(pp);
				Timer t = new Timer((int) 1000 / 30, listener);
				t.start();
			}
		};
		EventQueue.invokeLater(rn);
		bPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isPaused = true;
			}
		});
		bSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(yourInputField.getText()) < files.length) {
					currentFrame = Integer.parseInt(yourInputField.getText());
					String imgPath = "DS/AIFilmOne/" + files[currentFrame];
					System.out.println(imgPath);

					readImageRGB(width, height, imgPath, BI);
					pp.bi = BI;
					pp.repaint();
				}

			}
		});
		bResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isPaused = false;
			}
		});
		frame.getContentPane().add(bPause);
		frame.getContentPane().add(bResume);
		frame.getContentPane().add(bSelect);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
