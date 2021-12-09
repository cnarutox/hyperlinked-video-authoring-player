import java.awt.event.MouseEvent;
import java.awt.*;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class LinkCreate {
    VideoPlayer videoPanel;
    boolean isDrawing = false;
    Point leftTop;
    Point rightBottom;

    LinkCreate(VideoPlayer videoPlayer) {
        videoPanel = videoPlayer;
        videoPanel.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                videoPanel.isPaused = true;
                leftTop = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                rightBottom = e.getPoint();
                Graphics g = videoPanel.getGraphics();
                g.drawRect(leftTop.x, leftTop.y, rightBottom.x - leftTop.x, rightBottom.y - leftTop.y);
                videoPanel.paintComponent(g);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub

            }

        });
    }

    public static void main(String[] args) {

    }
}
