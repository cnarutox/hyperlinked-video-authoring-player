import java.awt.event.MouseEvent;
import java.io.File;
import java.text.CollationElementIterator;
import java.util.Arrays;
import java.util.List;
import java.awt.*;

import javax.naming.ldap.ManageReferralControl;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class LinkCreate {
    VideoPlayer mainPlayer;

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
    DefaultListModel dataModel = new DefaultListModel<String>();

    LinkCreate that = this;

    LinkCreate(VideoPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
        mainPlayer.linkCreate = this;
        MouseInputAdapter input = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                if (mainPlayer.filelength == 0 || !Authoring.isCreating)
                    return;
                mainPlayer.audio.stop();
                mainPlayer.isPaused = true;
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
                mainPlayer.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                if (!isDrawing)
                    return;
                if (regionIndex == -1) {
                    Region region = new Region(leftTop, rightBottom, mainPlayer.currentFrame,
                            mainPlayer.filelength - 1);
                    mainPlayer.links.putRegion(mainPlayer.videoPath.getAbsolutePath(), region);
                    regionIndex = mainPlayer.links.linkedMap.get(mainPlayer.videoPath.getAbsolutePath())
                            .size() - 1;
                    operationInfo.setText("Next is to set the bound of the end frame");
                    operationInfo.setForeground(Color.PINK);
                } else {
                    Region region = mainPlayer.links.linkedMap.get(mainPlayer.videoPath.getAbsolutePath())
                            .get(regionIndex);
                    region.setEnd(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                            Math.max(leftTop.x, rightBottom.x), Math.max(leftTop.y, rightBottom.y),
                            mainPlayer.currentFrame);
                    isDrawing = false;
                    saveBtn.setEnabled(true);
                    operationInfo.setText("Rename and save it!");
                    operationInfo.setForeground(Color.BLACK);
                    mainPlayer.repaint();
                }
            }

        };
        mainPlayer.addMouseMotionListener(input);
        mainPlayer.addMouseListener(input);
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
        JList linksList = new JList<String>(dataModel);
        linksList.setVisibleRowCount(5);
        linksList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        linksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(linksList);
        linksList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() == false) {
                if (linksList.getSelectedIndex() == -1) {
                    // No selection, disable fire button.
                    System.out.println("No selection");
                } else {
                    Region region = mainPlayer.links
                            .getItems(mainPlayer.videoPath.getAbsolutePath()).get(
                                    Integer.valueOf(((String) linksList.getSelectedValue())
                                            .split("\\.")[0]));
                    mainPlayer.refresh(region.startBound.frame);
                    Authoring.secondPlayer.refresh(region.startBound.frame);

                    int idx = Authoring.ImportVideo.listModel.indexOf(new File(region.getLinkedFile()).getName());
                    Authoring.ImportVideo.list.setSelectedIndex(idx);
                    Authoring.secondPlayer.currentFrame = region.linkedFrame;
                    Authoring.secondPlayer.slider.setValue(region.linkedFrame);
                    Authoring.secondPlayer.repaint();
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
                mainPlayer.isPaused = true;
                mainPlayer.audio.stop();
                createBtn.setText("   Cancel  ");
                newLinkName.setText("New link");
                linkInfo.setText(String.format("from %d to ?", Authoring.mainVideoPlayer.currentFrame));
                linksList.clearSelection();
                operationInfo.setText("Please set the first bound of the start frame");
                operationInfo.setForeground(Color.BLUE);
            } else {
                if (regionIndex != -1)
                    Authoring.mainVideoPlayer.links.removeLast();
                isDrawing = false;
                regionIndex = -1;
                Authoring.isCreating = false;
                createBtn.setText("Create Link");
                saveBtn.setEnabled(false);
                newLinkName.setText("");
                linkInfo.setText("No link selected");
                operationInfo.setText("You can create hyperlink for them");
                operationInfo.setForeground(Color.GRAY);
            }
        });
        createBtn.setEnabled(false);

        saveBtn.addActionListener(e -> {
            if (Authoring.isCreating && !isDrawing) {
                if (Authoring.secondPlayer.filelength == 0) {
                    JOptionPane.showMessageDialog(Authoring.authoring, "Please import the second video!",
                            "2st video not specified", 1);
                    return;
                }
                Authoring.isCreating = false;
                dataModel.addElement(regionIndex + "." + newLinkName.getText());

                createBtn.setText("Create Link");
                saveBtn.setEnabled(false);
                Authoring.ImportVideo.saveFile.setEnabled(true);

                newLinkName.setText("");
                linkInfo.setText("No link selected");
                operationInfo.setText("You can create hyperlink for them");
                operationInfo.setForeground(Color.GRAY);
                mainPlayer.links.linkedMap.get(mainPlayer.videoPath.getAbsolutePath()).get(regionIndex)
                        .setLinkedInfo(Authoring.secondPlayer.videoPath.getAbsolutePath(),
                                Authoring.secondPlayer.currentFrame);
                regionIndex = -1;
            }
        });
        saveBtn.setEnabled(false);
        return linkPanel;
    }

    public static void main(String[] args) {

    }
}
