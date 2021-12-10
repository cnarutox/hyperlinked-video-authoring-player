
import java.awt.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.*;
import javax.swing.text.AttributeSet.ColorAttribute;

import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Authoring extends JFrame {

    public static Authoring authoring;
    static boolean isCreating = false;
    static VideoPlayer mainVideoPlayer, secondVideoPlayer;
    static ImportVideo importPanel = new ImportVideo();

    public Authoring(String s) {
        super(s);
    }

    public static class ImportVideo extends JPanel {
        JLabel label = new JLabel("Main Video");
        JTextField mainVideoName = new JTextField(10);
        JButton mainBtn = new JButton("Import Main Video");
        JButton secondBtn = new JButton("Import Linked Video");
        JButton saveFile = new JButton("Save HyperLinked Video");
        static DefaultListModel listModel = new DefaultListModel();
        static JList list = new JList(listModel);
        JScrollPane scrollPane = new JScrollPane();

        static HashMap<String, File> videofiles = new HashMap<String, File>();

        public ImportVideo() {
            super(new FlowLayout(FlowLayout.CENTER, 10, 5));
            add(label);
            add(mainVideoName);
            add(mainBtn);
            add(secondBtn);
            add(scrollPane);
            add(saveFile);

            list.setVisibleRowCount(2);
            list.setLayoutOrientation(JList.VERTICAL_WRAP);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane.setViewportView(list);
            scrollPane.setPreferredSize(new Dimension(150, 60));

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
                            System.out.println("Select File" + list.getSelectedValue());
                            secondVideoPlayer.importVideo(videofiles.get(list.getSelectedValue()), 0);
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

            saveFile.addActionListener(e -> {
                if (mainVideoPlayer.videoPath != null)
                    mainVideoPlayer.links.toLocalFile(mainVideoPlayer.videoPath);
            });
        }

    }

    public JPanel createVideoArea() {
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
        authoring = new Authoring("HyperLinked Video Authoring Tool");
        authoring.setLayout(new BorderLayout());

        authoring.getContentPane().add(importPanel, BorderLayout.NORTH);

        JPanel mainArea = authoring.createVideoArea();
        mainVideoPlayer = (VideoPlayer) mainArea.getComponent(0);
        authoring.add(mainArea, BorderLayout.WEST);

        JPanel linkPanel = new LinkCreate(mainVideoPlayer).linkArea();
        authoring.add(linkPanel, BorderLayout.CENTER);

        JPanel secondArea = authoring.createVideoArea();
        secondVideoPlayer = (VideoPlayer) secondArea.getComponent(0);
        authoring.add(secondArea, BorderLayout.EAST);

        authoring.pack();
        authoring.setVisible(true);
        authoring.setLocationRelativeTo(null);
        authoring.setDefaultCloseOperation(3);
        authoring.setPreferredSize(new Dimension(720, 480));
    }

}