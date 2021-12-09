import java.awt.event.MouseEvent;
import java.awt.*;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class LinkCreate {
    VideoPlayer mainVideoPlayer;

    boolean isDrawing = false;

    Point leftTop;
    Point rightBottom;

    JPanel linkPanel = new JPanel(new BorderLayout());
    JButton createBtn = new JButton("Create Link");
    JLabel linkInfo = new JLabel("No link selected", JLabel.CENTER);
    JLabel operationInfo = new JLabel("You can import video and create hyperlink for them");

    LinkCreate(VideoPlayer mainVideoPlayer) {
        this.mainVideoPlayer = mainVideoPlayer;
        mainVideoPlayer.linkCreate = this;
        MouseInputAdapter input = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                if (mainVideoPlayer.filelength == 0 || !Authoring.isCreating)
                    return;
                mainVideoPlayer.isPaused = true;
                mainVideoPlayer.audio.stop();
                isDrawing = true;
                leftTop = e.getPoint();
                linkInfo.setText(String.format("from %d to ?", Authoring.mainVideoPlayer.currentFrame));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                if (!isDrawing)
                    return;
                rightBottom = e.getPoint();
                mainVideoPlayer.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                if (!isDrawing)
                    return;
                isDrawing = false;
                Region region = new Region(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                        Math.max(leftTop.x, rightBottom.x), Math.max(leftTop.y, rightBottom.y),
                        mainVideoPlayer.currentFrame, mainVideoPlayer.filelength - 1);
                mainVideoPlayer.links.putRegion(mainVideoPlayer.videoPath.getAbsolutePath(), region);
                operationInfo.setText("Next is to set the bound of the end frame");
            }

        };
        mainVideoPlayer.addMouseMotionListener(input);
        mainVideoPlayer.addMouseListener(input);
    }

    void draw(Graphics2D g2) {
        g2.drawRect(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                Math.abs(rightBottom.x - leftTop.x),
                Math.abs(rightBottom.y - leftTop.y));
    }

    JPanel linkArea() {
        JTextField newLinkName = new JTextField(10);
        newLinkName.setHorizontalAlignment(JTextField.CENTER);
        JButton saveBtn = new JButton(" Save Link ");

        JPanel north = new JPanel();
        north.add(createBtn);
        north.add(newLinkName);
        north.add(saveBtn);
        // north.setMaximumSize(new Dimension(200, 100));
        linkPanel.add(north, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        DefaultListModel dataModel = new DefaultListModel<String>();
        JList linksList = new JList<String>(dataModel);
        linksList.setVisibleRowCount(5);
        linksList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        linksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataModel.addElement("element");
        scrollPane.setViewportView(linksList);

        JPanel center = new JPanel(new BorderLayout());
        center.add(linkInfo, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        linkPanel.add(center, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.add(operationInfo);
        south.setMinimumSize(new Dimension(20, 40));
        linkPanel.add(south, BorderLayout.SOUTH);

        createBtn.addActionListener(e -> {
            if (createBtn.getText() == "Create Link") {
                Authoring.isCreating = true;
                mainVideoPlayer.isPaused = true;
                createBtn.setText("   Cancel  ");
                newLinkName.setText("New link");
                linkInfo.setText(String.format("from %d to ?", Authoring.mainVideoPlayer.currentFrame));
                operationInfo.setText("Please set the first bound of the start frame");
            } else {
                Authoring.isCreating = false;
                Authoring.mainVideoPlayer.links.removeLast();
                createBtn.setText("Create Link");
                newLinkName.setText("");
                linkInfo.setText("No link selected");
                operationInfo.setText("You can create hyperlink for them");
            }
        });

        return linkPanel;
    }

    public static void main(String[] args) {

    }
}
