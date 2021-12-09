import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class Links {

    Map<String, List<Region>> linkedMap = new HashMap<String, List<Region>>();

    public void putRegion(String fromFile, Region newRegion) {
        if (linkedMap.containsKey(fromFile)) {
            ArrayList<Region> regionList = (ArrayList<Region>) linkedMap.get(fromFile);
            regionList.add(newRegion);
            linkedMap.put(fromFile, regionList);
        } else {
            ArrayList<Region> regionList = new ArrayList<Region>() {
                {
                    add(newRegion);
                }
            };
            linkedMap.put(fromFile, regionList);
        }
    }

    // get all region(with bound and linkedframe) within frame
    public List<Region> inRegion(String fromFile, int frame) {
        ArrayList<Region> regions = new ArrayList<Region>();
        if (!linkedMap.containsKey(fromFile)) {
            return null;
        }
        for (Iterator<Region> it = linkedMap.get(fromFile).iterator(); it.hasNext();) {
            Region curRegion = (Region) it.next();
            if (curRegion.getBound(frame) != null) {
                regions.add(curRegion.getBound(frame));
            }
        }
        // System.out.println("inregion " + frame + " " + regions.size());
        return regions;
    }

    public void toLocalFile(String localFilePath) {

        File file = new File(localFilePath);
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(localFilePath));
            for (Map.Entry<String, List<Region>> entry : this.linkedMap.entrySet()) {

                // put key and value separated by a colon
                String curLine = "";
                ArrayList<Region> curRegionList = (ArrayList<Region>) entry.getValue();
                for (Iterator<Region> it = curRegionList.iterator(); it.hasNext();) {
                    Region curRegion = (Region) it.next();
                    double[] vetexes = curRegion.getVetex(true);
                    for (int i = 0; i < 4; i++) {
                        curLine += vetexes[i] + ",";
                    }
                    curLine += curRegion.startBound.frame + "-";
                    vetexes = curRegion.getVetex(false);
                    for (int i = 0; i < 4; i++) {
                        curLine += vetexes[i] + ",";
                    }
                    curLine += curRegion.endBound.frame + "\n";
                }
                bf.write(entry.getKey() + "\n"
                        + curLine);
                // new line
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
