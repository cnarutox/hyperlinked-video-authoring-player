import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class Authoring extends JFrame {

    public Authoring(String s) {
        super(s);
    }

    public static void main(String[] args) {
        Authoring authoring = new Authoring("HyperLinked Video Authoring Tool");
        authoring.setLayout(new BorderLayout());

        JPanel player = new JPanel();
        player.add(new JLabel(
                "authoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoringauthoring"));
        authoring.getContentPane().add(player, BorderLayout.NORTH);

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