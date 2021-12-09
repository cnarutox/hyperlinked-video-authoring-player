import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.util.*;

public class LinkDisplay {
    Links links = new Links();
   
    public void draw(String fromFile, Graphics g, int frame){
        ArrayList<Region> regions = links.inRegion(fromFile, frame);
    }
}
