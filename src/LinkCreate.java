import java.awt.event.MouseEvent;
import java.io.File;
import java.text.CollationElementIterator;
import java.util.List;
import java.awt.*;

import javax.naming.ldap.ManageReferralControl;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class LinkCreate {
    VideoPlayer mainVideoPlayer;

    boolean isDrawing = false;
    int regionIndex = -1;

    Point leftTop;
    Point rightBottom;
    Color color;

    JPanel linkPanel = new JPanel(new BorderLayout());
    JButton createBtn = new JButton("Create Link");
    JButton saveBtn = new JButton(" Save Link ");
    JLabel linkInfo = new JLabel("No link selected", JLabel.CENTER);
    JLabel operationInfo = new JLabel("You can import video and create hyperlink for them");

    LinkCreate that = this;

    LinkCreate(VideoPlayer mainVideoPlayer) {
        this.mainVideoPlayer = mainVideoPlayer;
        mainVideoPlayer.linkCreate = this;
        MouseInputAdapter input = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                if (mainVideoPlayer.filelength == 0 || !Authoring.isCreating)
                    return;
                mainVideoPlayer.audio.stop();
                mainVideoPlayer.isPaused = true;
                isDrawing = true;
                leftTop = e.getPoint();
                if (regionIndex == -1)
                    linkInfo.setText(String.format("from %d to ?", Authoring.mainVideoPlayer.currentFrame));
                else
                    linkInfo.setText(
                            linkInfo.getText().replaceAll("\\?",
                                    Integer.toString(Authoring.mainVideoPlayer.currentFrame)));
                color = regionIndex == -1 ? Color.BLUE : Color.PINK;
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
                if (regionIndex == -1) {
                    Region region = new Region(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                            Math.max(leftTop.x, rightBottom.x), Math.max(leftTop.y, rightBottom.y),
                            mainVideoPlayer.currentFrame, mainVideoPlayer.filelength - 1);
                    mainVideoPlayer.links.putRegion(mainVideoPlayer.videoPath.getAbsolutePath(), region);
                    regionIndex = mainVideoPlayer.links.linkedMap.get(mainVideoPlayer.videoPath.getAbsolutePath())
                            .size() - 1;
                    operationInfo.setText("Next is to set the bound of the end frame");
                    operationInfo.setForeground(Color.PINK);
                } else {
                    Region region = mainVideoPlayer.links.linkedMap.get(mainVideoPlayer.videoPath.getAbsolutePath())
                            .get(regionIndex);
                    region.setEnd(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                            Math.max(leftTop.x, rightBottom.x), Math.max(leftTop.y, rightBottom.y),
                            mainVideoPlayer.currentFrame);
                    isDrawing = false;
                    operationInfo.setText("Rename and save it!");
                    operationInfo.setForeground(Color.BLACK);
                    mainVideoPlayer.repaint();
                }
            }

        };
        mainVideoPlayer.addMouseMotionListener(input);
        mainVideoPlayer.addMouseListener(input);
        operationInfo.setForeground(Color.GRAY);
    }

    void draw(Graphics2D g2) {
        g2.setPaint(color);
        g2.drawRect(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                Math.abs(rightBottom.x - leftTop.x),
                Math.abs(rightBottom.y - leftTop.y));
    }

    JPanel linkArea() {
        JTextField newLinkName = new JTextField(10);
        newLinkName.setHorizontalAlignment(JTextField.CENTER);

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
        scrollPane.setViewportView(linksList);
        linksList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() == false) {
                if (linksList.getSelectedIndex() == -1) {
                    // No selection, disable fire button.
                } else {
                    // System.out.println("Select " + linksList.getSelectedValue());
                    Region region = mainVideoPlayer.links
                            .getItems(mainVideoPlayer.videoPath.getAbsolutePath()).get(
                                    Integer.valueOf(((String) linksList.getSelectedValue())
                                            .split("\\.")[0]));
                    mainVideoPlayer.currentFrame = region.startBound.frame;
                    mainVideoPlayer.cacheIndex = mainVideoPlayer.currentFrame;
                    mainVideoPlayer.currentTotalTime = (long) (mainVideoPlayer.currentFrame * ((double) 1000 / 30));
                    mainVideoPlayer.slider.setValue(mainVideoPlayer.currentFrame + 1);
                    mainVideoPlayer.isPaused = true;
                    mainVideoPlayer.audio.stop();
                    mainVideoPlayer.repaint();

                    int idx = Authoring.ImportVideo.listModel.indexOf(region.getLinkedFile());
                    Authoring.ImportVideo.list.setSelectedIndex(idx);
                    Authoring.secondVideoPlayer.currentFrame = region.linkedFrame;
                }
            }
        });

        JPanel center = new JPanel(
                new BorderLayout());
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
                mainVideoPlayer.audio.stop();
                createBtn.setText("   Cancel  ");
                newLinkName.setText("New link");
                linkInfo.setText(String.format("from %d to ?", Authoring.mainVideoPlayer.currentFrame));
                operationInfo.setText("Please set the first bound of the start frame");
                operationInfo.setForeground(Color.BLUE);
            } else {
                isDrawing = false;
                Authoring.isCreating = false;
                Authoring.mainVideoPlayer.links.removeLast();
                regionIndex = -1;
                createBtn.setText("Create Link");
                newLinkName.setText("");
                linkInfo.setText("No link selected");
                operationInfo.setText("You can create hyperlink for them");
                operationInfo.setForeground(Color.GRAY);
            }
        });
        createBtn.setEnabled(false);

        saveBtn.addActionListener(e -> {
            if (Authoring.isCreating && !isDrawing) {
                if (Authoring.secondVideoPlayer.filelength == 0) {
                    JOptionPane.showMessageDialog(Authoring.authoring, "Please import the second video!",
                            "2st video not specified", 1);
                    return;
                }
                Authoring.isCreating = false;
                dataModel.addElement(regionIndex + "." + newLinkName.getText());
                createBtn.setText("Create Link");
                newLinkName.setText("");
                linkInfo.setText("No link selected");
                operationInfo.setText("You can create hyperlink for them");
                operationInfo.setForeground(Color.GRAY);
                mainVideoPlayer.links.linkedMap.get(mainVideoPlayer.videoPath.getAbsolutePath()).get(regionIndex)
                        .setLinkedInfo(Authoring.secondVideoPlayer.videoPath.getAbsolutePath(),
                                Authoring.secondVideoPlayer.currentFrame);
                regionIndex = -1;
                mainVideoPlayer.links.toLocalFile(mainVideoPlayer.videoPath);
            }
        });
        saveBtn.setEnabled(false);
        return linkPanel;
    }

    public static void main(String[] args) {

    }
}
