import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class LinkDisplay {

    VideoPlayer videoPlayer;

    Point clickedPoint = new Point(-1, -1);

    LinkDisplay(VideoPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
        MouseInputAdapter input = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("mouseReleased ");
                clickedPoint = e.getPoint();
                if (videoPlayer.filelength > 0) {
                    // import if clicked in ROI
                    ArrayList<Region> regions = getInRegions(videoPlayer.videoPath.getAbsolutePath(),
                            videoPlayer.currentFrame);
                    for (Region curRegion : regions) {
                        double[] vetexes = curRegion.getVetex(true);
                        if (isClickedInROI(vetexes)) {
                            videoPlayer.isPaused = true;
                            videoPlayer.audio.stop();
                            videoPlayer.importVideo(new File(curRegion.getLinkedFile()), curRegion.getLinkedFrame());
                            clickedPoint = new Point(-1, -1);
                            break;
                        }
                    }
                }
            }
        };
        if (Authoring.authoring == null) {
            this.videoPlayer.addMouseListener(input);
        }
    }

    public boolean isClickedInROI(double[] vetexes) {
        if (clickedPoint.getX() >= vetexes[0] && clickedPoint.getX() <= vetexes[2]
                && clickedPoint.getY() >= vetexes[1]
                && clickedPoint.getY() <= vetexes[3]) {
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
            // if (draw(region, g2)) {
            // videoPlayer.isPaused = true;
            // videoPlayer.audio.stop();
            // videoPlayer.importVideo(new File(region.getLinkedFile()),
            // region.getLinkedFrame());
            // break;
            // }
            draw(region, g2);
        }
    }

    public boolean draw(Region curRegion, Graphics g) {
        double[] vetexes = curRegion.getVetex(true);
        g.setColor(Color.BLACK);
        g.drawRect((int) vetexes[0], (int) vetexes[1], (int) (vetexes[2] - vetexes[0]),
                (int) (vetexes[3] - vetexes[1]));
        // if (isClickedInROI(vetexes)) {
        // return true;
        // }
        return false;
    }

}
