
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Authoring extends JFrame {

    public Authoring(String s) {
        super(s);
    }

    public static class ImportVideo extends JPanel {
        JLabel label = new JLabel("Imported Video Path");
        JTextField videoNameField = new JTextField(25);
        JButton viewBtn = new JButton("View");
        File videoFile;
        VideoPlayer linkedPlayer;

        public ImportVideo() {
            super();
            this.add(label);
            this.add(videoNameField);
            this.add(viewBtn);
            ImportVideo that = this;
            viewBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser(
                            "C:/Users/16129/OneDrive - University of Southern California/CS576/CSCI576 - 20213 - Multimedia Systems Design - 1242021 - 354 PM/DS/AIFilmOne");
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int val = fc.showOpenDialog(null); 
                    if (val == JFileChooser.APPROVE_OPTION) {
                        videoFile = fc.getSelectedFile();
                        System.out.println(videoFile);
                        videoNameField.setText(videoFile.getAbsolutePath());
                        linkedPlayer.importVideo(that);
                    } else {
                        // videoName.setText("file not selected");
                    }
                }
            });
        }

    }

    public static void main(String[] args) {
        Authoring authoring = new Authoring("HyperLinked Video Authoring Tool");
        authoring.setLayout(new BorderLayout());

        ImportVideo importPanel = new ImportVideo();
        authoring.getContentPane().add(importPanel, BorderLayout.NORTH);

        VideoPlayer videoPlayer = new VideoPlayer();
        authoring.getContentPane().add(videoPlayer, BorderLayout.CENTER);

        JLabel frameLabel = new JLabel("0", JLabel.CENTER);
        JSlider slider = new JSlider(1, 9000);
        slider.setMajorTickSpacing(900);
        // slider.setMinorTickSpacing(30);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setValue(0);

        JPanel btnPanel = new JPanel();
        JButton pauseBtn = new JButton("Pause");
        JButton playBtn = new JButton("Play");
        btnPanel.add(pauseBtn);
        btnPanel.add(playBtn);
        videoPlayer.link(importPanel, slider, playBtn, pauseBtn, frameLabel);

        JPanel frameSlider = new JPanel(new BorderLayout(10, 5));
        frameSlider.add(btnPanel, BorderLayout.NORTH);
        frameSlider.add(frameLabel, BorderLayout.CENTER);
        frameSlider.add(slider, BorderLayout.SOUTH);

        authoring.getContentPane().add(frameSlider, BorderLayout.SOUTH);
        authoring.pack();
        authoring.setVisible(true);
        authoring.setPreferredSize(new Dimension(720, 480));
        authoring.setLocationRelativeTo(null);
        authoring.setDefaultCloseOperation(3);
    }
}