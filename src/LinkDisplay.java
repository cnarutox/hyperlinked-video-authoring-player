import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.util.*;

public class LinkDisplay {

    

    public void draw(Region curRegion, Graphics g) {
        double[] vetexes = curRegion.getVetex(true);
        g.drawRect((int) vetexes[0], (int) vetexes[1], (int) (vetexes[2] - vetexes[0]),
                (int) (vetexes[3] - vetexes[1]));
    }
    
    
}
