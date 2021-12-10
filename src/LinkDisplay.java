import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class LinkDisplay {

    VideoPlayer videoPlayer;

    Point mouseClicked = new Point(-1, -1);

    LinkDisplay(VideoPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
        MouseInputAdapter input = new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClicked = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // System.out.println("mouseReleased ");
                if (videoPlayer.filelength > 0 && videoPlayer.isPaused) {
                    // import if clicked in ROI
                    ArrayList<Region> regions = getInRegions(videoPlayer.videoPath.getAbsolutePath(),
                            videoPlayer.currentFrame);
                    for (Region curRegion : regions) {
                        double[] vetexes = curRegion.getVetex(true);
                        if (isClickedInROI(vetexes)) {
                            videoPlayer.isPaused = true;
                            videoPlayer.audio.stop();
                            videoPlayer.importVideo(new File(curRegion.getLinkedFile()), curRegion.getLinkedFrame());
                            break;
                        }
                    }
                }
                mouseClicked = new Point(-1, -1);
            }
        };
        if (Authoring.authoring == null)
            this.videoPlayer.addMouseListener(input);
    }

    public boolean isClickedInROI(double[] vetexes) {
        if (mouseClicked.getX() >= vetexes[0] && mouseClicked.getX() <= vetexes[2]
                && mouseClicked.getY() >= vetexes[1]
                && mouseClicked.getY() <= vetexes[3]) {
            return true;
        }
        return false;
    }

    public ArrayList<Region> getInRegions(String fromFile, int frame) {
        return (ArrayList<Region>) videoPlayer.links.inRegion(
                fromFile, frame);
    }

    public void drawRegion(Graphics g2) {
        ArrayList<Region> regions = getInRegions(videoPlayer.videoPath.getAbsolutePath(), videoPlayer.currentFrame);
        for (Region region : regions) {
            if (draw(region, g2)) {
                videoPlayer.isPaused = true;
                videoPlayer.audio.stop();
                videoPlayer.importVideo(new File(region.getLinkedFile()), region.getLinkedFrame());
            }
        }
    }

    public boolean draw(Region curRegion, Graphics g) {
        double[] vetexes = curRegion.getVetex(true);
        g.setColor(Color.BLACK);
        g.drawRect((int) vetexes[0], (int) vetexes[1], (int) (vetexes[2] - vetexes[0]),
                (int) (vetexes[3] - vetexes[1]));
        if (isClickedInROI(vetexes)) {
            return true;
        }
        return false;
    }

}
