
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.AttributeSet.ColorAttribute;

import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Authoring extends JFrame {

    public Authoring(String s) {
        super(s);
    }

    public static class ImportVideo extends JPanel {
        JLabel label = new JLabel("Main Vieo Path");
        JTextField mainVideoName = new JTextField(20);
        JButton mainBtn = new JButton("Import Main Video");
        JButton secondBtn = new JButton("Import Linked Video");
        DefaultListModel listModel = new DefaultListModel();
        JScrollPane scrollPane = new JScrollPane();

        HashMap<String, File> videofiles = new HashMap<String, File>();

        VideoPlayer mainPlayer, secondPlayer;

        public ImportVideo() {
            super(new FlowLayout(FlowLayout.LEADING, 30, 20));
            add(label);
            add(mainVideoName);
            add(mainBtn);
            add(secondBtn);
            add(scrollPane);

            JList list = new JList(listModel);
            list.setVisibleRowCount(2);
            list.setLayoutOrientation(JList.VERTICAL_WRAP);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane.setViewportView(list);
            scrollPane.setPreferredSize(new Dimension(300, 60));

            mainBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int val = fc.showOpenDialog(null);
                    if (val == JFileChooser.APPROVE_OPTION) {
                        File videoFile = fc.getSelectedFile();
                        mainVideoName.setText(videoFile.getAbsolutePath());
                        mainPlayer.importVideo(videoFile);
                        System.out.println("Main Video: " + videoFile);
                    } else {
                        // videoName.setText("file not selected");
                    }
                }
            });
            list.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // TODO Auto-generated method stub
                    if (e.getValueIsAdjusting() == false) {
                        if (list.getSelectedIndex() == -1) {
                            // No selection, disable fire button.
                        } else {
                            System.out.println("Select " + list.getSelectedValue());
                            secondPlayer.importVideo(videofiles.get(list.getSelectedValue()));
                        }
                    }
                }
            });
            secondBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int val = fc.showOpenDialog(null);
                    if (val == JFileChooser.APPROVE_OPTION) {
                        File videoFile = fc.getSelectedFile();
                        if (!videofiles.containsKey(videoFile.getName())) {
                            listModel.addElement(videoFile.getName());
                            videofiles.put(videoFile.getName(), videoFile);
                            list.setSelectedIndex(listModel.lastIndexOf(listModel.lastElement()));
                            System.out.println("Load Video: " + videoFile);
                        }
                    } else {
                        // videoName.setText("file not selected");
                    }
                }
            });
        }

    }

    public JPanel createVideoPlayer() {
        JPanel videoArea = new JPanel(new BorderLayout());

        VideoPlayer videoPlayer = new VideoPlayer();
        videoPlayer.setBackground(Color.YELLOW);

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
        videoPlayer.binding(slider, playBtn, pauseBtn, frameLabel);

        JPanel frameSlider = new JPanel(new BorderLayout(10, 5));
        frameSlider.add(btnPanel, BorderLayout.NORTH);
        frameSlider.add(frameLabel, BorderLayout.CENTER);
        frameSlider.add(slider, BorderLayout.SOUTH);

        videoArea.add(videoPlayer, BorderLayout.CENTER);
        videoArea.add(frameSlider, BorderLayout.SOUTH);
        return videoArea;
    }

    public static void main(String[] args) {
        Authoring authoring = new Authoring("HyperLinked Video Authoring Tool");
        authoring.setLayout(new BorderLayout());

        ImportVideo importPanel = new ImportVideo();
        authoring.getContentPane().add(importPanel, BorderLayout.NORTH);

        JPanel mainVideo = authoring.createVideoPlayer();
        importPanel.mainPlayer = (VideoPlayer) mainVideo.getComponent(0);
        authoring.add(mainVideo, BorderLayout.WEST);

        JPanel secondVideo = authoring.createVideoPlayer();
        importPanel.secondPlayer = (VideoPlayer) secondVideo.getComponent(0);
        authoring.add(secondVideo, BorderLayout.EAST);

        authoring.pack();
        authoring.setVisible(true);
        authoring.setPreferredSize(new Dimension(720, 480));
        authoring.setLocationRelativeTo(null);
        authoring.setDefaultCloseOperation(3);
    }
}