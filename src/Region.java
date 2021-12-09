import java.util.*;

public class Region {
    class Bound {
        double xLeftTop, yLeftTop, xRightBottom, yRightBottom;
        int frame;

        Bound() {
            this.xLeftTop = -1;
            this.yLeftTop = -1;
            this.xRightBottom = -1;
            this.yRightBottom = -1;
            this.frame = -1;
        }

        Bound(double xLeftTop, double yLeftTop, double xRightBottom, double yRightBottom, int frame) {
            this.xLeftTop = xLeftTop;
            this.yLeftTop = yLeftTop;
            this.xRightBottom = xRightBottom;
            this.yRightBottom = yRightBottom;
            this.frame = frame;
        }

        @Override
        public String toString(){
            return String.format("(%.2f, %.2f)(%.2f, %.2f)(%d)", this.xLeftTop, this.yLeftTop, this.xRightBottom, this.yRightBottom, this.frame);
        }
        
        public int getFrame() {
            return this.frame;
        }

        

        public void resetBound(double xLeftTop, double yLeftTop, double xRightBottom, double yRightBottom) {
            this.xLeftTop = xLeftTop;
            this.yLeftTop = yLeftTop;
            this.xRightBottom = xRightBottom;
            this.yRightBottom = yRightBottom;
        }

        public void resetFrame(int frame) {
            this.frame = frame;
        }

        public double[] getVetex() {
            double[] vetexes = new double[4];
            vetexes[0] = this.xLeftTop;
            vetexes[1] = this.yLeftTop;
            vetexes[2] = this.xRightBottom;
            vetexes[3] = this.yRightBottom;
            return vetexes;
        }
    }

    Bound startBound;
    Bound endBound;

    String linkedFile;
    int linkedFrame;

    Region() {

    }

    Region(double xStartLeftTop, double yStartLeftTop, double xStartRightBottom, double yStartRightBottom,
            int startFrame) {
        this.startBound = new Bound(xStartLeftTop, yStartLeftTop, xStartRightBottom, yStartRightBottom, startFrame);
        this.endBound = new Bound(xStartLeftTop, yStartLeftTop, xStartRightBottom, yStartRightBottom, startFrame);
    }

    @Override
    public String toString(){
        return String.format("%s->%s linked to: [%s](%d)", this.startBound.toString(), this.endBound.toString(), this.linkedFile, this.linkedFrame);
    }

    
    public void setEnd(double xEndLeftTop, double yEndLeftTop, double xEndRightBottom, double yEndRightBottom,
            int endFrame) {
        this.endBound.resetBound(xEndLeftTop, yEndLeftTop, xEndRightBottom, yEndRightBottom);
        this.endBound.resetFrame(endFrame);
    }

    public void setLinkedInfo(String linkedFile, int linkedFrame) {
        this.linkedFile = linkedFile;
        this.linkedFrame = linkedFrame;
    }

    public Region getBound(int frame) {
        if (frame < this.startBound.getFrame() || frame > this.endBound.getFrame()) {
            return null;
        }
        double curXLeftTop = this.startBound.xLeftTop + (this.endBound.xLeftTop - this.startBound.xLeftTop)
                / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        double curYLeftTop = this.startBound.yLeftTop + (this.endBound.yLeftTop - this.startBound.yLeftTop)
                / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        double curXRightBottom = this.startBound.xRightBottom
                + (this.endBound.xRightBottom - this.startBound.xRightBottom)
                        / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        double curYRightBottom = this.startBound.yRightBottom
                + (this.endBound.yRightBottom - this.startBound.yRightBottom)
                        / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        Region newRegion = new Region(curXLeftTop, curYLeftTop, curXRightBottom, curYRightBottom, frame);
        newRegion.setLinkedInfo(this.linkedFile, this.linkedFrame);
        return newRegion;
    }
    public int getLinkedFrame(){
        return this.linkedFrame;
    }

    public String getLinkedFile(){
        return this.linkedFile;
    }

    public double[] getVetex(Boolean isStart) {

        if (isStart) {
            return this.startBound.getVetex();
        } else {
            return this.endBound.getVetex();
        }

    }

}
