import java.util.*;
import java.io.*;

public class Links {

    Map<String, List<Region>> linkedMap = new HashMap<String, List<Region>>();
    String fromFile;

    public Set<String> getKeySet() {
        return this.linkedMap.keySet();
    }

    @Override
    public String toString() {
        String str = "";
        str += linkedMap.keySet() + "\n";
        for (String key : linkedMap.keySet()) {
            for (Region region : linkedMap.get(key)) {
                // System.out.println(region);
                str += region + "\n";
            }
        }
        return str;
    }

    public List<Region> getItems(String fileName) {
        return this.linkedMap.get(fileName);
    }

    public void removeLast() {
        if (linkedMap.containsKey(fromFile) && linkedMap.get(fromFile).size() > 1) {
            linkedMap.get(fromFile).remove(linkedMap.get(fromFile).size() - 1);
        }
    }

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
        fromFile.replace("\\", "\\/");
        ArrayList<Region> regions = new ArrayList<Region>();
        if (!linkedMap.containsKey(fromFile)) {
            return regions;
        }
        for (Iterator<Region> it = linkedMap.get(fromFile).iterator(); it.hasNext();) {
            Region curRegion = (Region) it.next();
            if (curRegion.getBound(frame) != null) {
                regions.add(curRegion.getBound(frame));
            }

        }
        return regions;
    }

    public void readLocalFile(String localFilePath) {
        BufferedReader br = null;
        try {
            File file = new File(localFilePath);
            br = new BufferedReader(new FileReader(file));
            String line;

            int index = 0;
            Region curRegion;
            String fromFilePath = "";
            while ((line = br.readLine()) != null) {
                if (index == 0) {
                    fromFilePath = line;
                } else {
                    String[] info = line.split("\\?", -2);
                    if (info.length != 4) {
                        System.out.println(String.format(
                                "Error: readLocalFile file format mismatch. Index(%d) SplitBy(?) Length != 4", index));
                        continue;
                    }
                    String[] firstInfo = info[0].split(",", -2);
                    if (firstInfo.length != 5) {
                        System.out.println(String.format(
                                "Error: readLocalFile file format mismatch. Index(%d) SplitBy(,) Length != 5", index));
                        continue;
                    }
                    curRegion = new Region(Double.valueOf(firstInfo[0]), Double.valueOf(firstInfo[1]),
                            Double.valueOf(firstInfo[2]), Double.valueOf(firstInfo[3]), Integer.valueOf(firstInfo[4]));
                    firstInfo = info[1].split(",", -2);
                    if (firstInfo.length != 5) {
                        System.out.println(String.format(
                                "Error: readLocalFile file format mismatch. Index(%d) SplitBy(,) Length != 5", index));
                        continue;
                    }
                    curRegion.setEnd(Double.valueOf(firstInfo[0]), Double.valueOf(firstInfo[1]),
                            Double.valueOf(firstInfo[2]), Double.valueOf(firstInfo[3]), Integer.valueOf(firstInfo[4]));
                    curRegion.setLinkedInfo(info[2], Integer.valueOf(info[3]));
                    putRegion(fromFilePath, curRegion);
                }
                index += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                    curLine += curRegion.startBound.frame + "?";
                    vetexes = curRegion.getVetex(false);
                    for (int i = 0; i < 4; i++) {
                        curLine += vetexes[i] + ",";
                    }
                    curLine += curRegion.endBound.frame + "?";
                    curLine += curRegion.getLinkedFile() + "?" + curRegion.getLinkedFrame() + "\n";
                }
                bf.write(entry.getKey() + "\n"
                        + curLine.substring(0, curLine.length() - 1));
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
