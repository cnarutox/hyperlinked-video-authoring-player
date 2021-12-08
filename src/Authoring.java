import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;

class Authoring extends JFrame {

    public Authoring(String s) {
        super(s);
    }

    static class ImportVideo extends JPanel {
        JLabel label = new JLabel("Imported Video Path");
        JTextField videoNameField = new JTextField(25);
        JButton viewBtn = new JButton("View");

        public ImportVideo() {
            super();
            this.add(label);
            this.add(videoNameField);
            this.add(viewBtn);
            viewBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser(
                            "C:/Users/cwx/OneDrive - University of Southern California/CSCI576/Project");
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int val = fc.showOpenDialog(null); // 文件打开对话框
                    if (val == JFileChooser.APPROVE_OPTION) {
                        videoNameField.setText(fc.getSelectedFile().toString());
                    } else {
                        // videoName.setText("未选择文件");
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
        videoPlayer.setBackground(Color.BLUE);
        authoring.getContentPane().add(videoPlayer, BorderLayout.CENTER);

        JPanel frameSlider = new JPanel();
        JLabel frameLabel = new JLabel("0");
        // frameLabel.setMinimumSzie(new Dimension(500, 100));
        JSlider slider = new JSlider(1, 1000);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                JSlider source = (JSlider) e.getSource();
                videoPlayer.currentFrame = source.getValue() - 1;
                frameLabel.setText(String.valueOf(videoPlayer.currentFrame));
            }
        });
        slider.setMajorTickSpacing(150);
        slider.setMinorTickSpacing(30);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setValue(0);
        videoPlayer.slider = slider;
        frameSlider.add(frameLabel, BorderLayout.WEST);
        frameSlider.add(slider, BorderLayout.CENTER);
        authoring.getContentPane().add(frameSlider, BorderLayout.SOUTH);

        authoring.pack();
        authoring.setVisible(true);
        authoring.setPreferredSize(new Dimension(720, 480));
        authoring.setLocationRelativeTo(null);
        authoring.setDefaultCloseOperation(3);
    }
}