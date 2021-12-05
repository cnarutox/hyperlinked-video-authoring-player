import java.awt.*;
import java.awt.BorderLayout;
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

        JPanel frameSlider = new JPanel();
        JLabel frameLabel = new JLabel("0");
        // frameLabel.setMinimumSize(new Dimension(500, 100));
        JSlider slider = new JSlider(1, 1000);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                JSlider source = (JSlider) e.getSource();
                frameLabel.setText(String.valueOf(source.getValue()));
            }
        });
        slider.setMajorTickSpacing(150);
        slider.setMinorTickSpacing(30);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setValue(0);
        frameSlider.add(frameLabel);
        frameSlider.add(slider);
        authoring.getContentPane().add(frameSlider, BorderLayout.SOUTH);

        authoring.pack();
        authoring.setVisible(true);
        authoring.setPreferredSize(new Dimension(720, 480));
        authoring.setLocationRelativeTo(null);
        authoring.setDefaultCloseOperation(3);
    }
}