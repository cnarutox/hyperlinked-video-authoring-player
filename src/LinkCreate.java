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
        MouseInputAdapter input = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
                if (videoPanel.filelength == 0 || !Authoring.isCreating)
                    return;
                videoPanel.isPaused = true;
                videoPanel.audio.stop();
                isDrawing = true;
                leftTop = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                if (!isDrawing)
                    return;
                rightBottom = e.getPoint();
                videoPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                if (!isDrawing)
                    return;
                isDrawing = false;
                Region region = new Region(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                        Math.max(leftTop.x, rightBottom.x), Math.max(leftTop.y, rightBottom.y),
                        videoPanel.currentFrame, videoPanel.filelength - 1);
                videoPanel.links.putRegion(videoPanel.videoPath.getAbsolutePath(), region);
            }

        };
        videoPanel.addMouseMotionListener(input);
        videoPanel.addMouseListener(input);
    }

    void draw(Graphics2D g2) {
        g2.drawRect(Math.min(leftTop.x, rightBottom.x), Math.min(leftTop.y, rightBottom.y),
                Math.abs(rightBottom.x - leftTop.x),
                Math.abs(rightBottom.y - leftTop.y));
    }

    public static void main(String[] args) {

    }
}
