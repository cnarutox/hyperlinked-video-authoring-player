import java.util.*;

public class Links extends HashMap<String, List<Region>> {

    // Map<String, List<Region>> linkedMap = new HashMap<String, List<Region>>();

    public void put(String fromFile, Region newRegion) {
        if (containsKey(fromFile)) {
            ArrayList<Region> regionList = (ArrayList<Region>) get(fromFile);
            regionList.add(newRegion);
            put(fromFile, regionList);
        } else {
            ArrayList<Region> regionList = new ArrayList<Region>() {
                {
                    add(newRegion);
                }
            };
            put(fromFile, regionList);
        }
    }

    // get all region(with bound and linkedframe) within frame
    public List<Region> inRegion(String fromFile, int frame) {
        ArrayList<Region> regions = new ArrayList<Region>();
        for (Iterator<Region> it = get(fromFile).iterator(); it.hasNext();) {
            Region curRegion = (Region) it.next();
            if (curRegion.getBound(frame) != null) {
                regions.add(curRegion.getBound(frame));
            }
        }
        return regions;
    }

}
