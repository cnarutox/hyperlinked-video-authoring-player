import java.util.*;

public class Region {
    class Bound{
        double xLeftTop, yLeftTop, xRightBottom, yRightBottom;
        int frame;
        
        Bound(){
            this.xLeftTop = -1;
            this.yLeftTop = -1;
            this.xRightBottom = -1;
            this.yRightBottom = -1;
            this.frame = -1;
        }

        Bound(double xLeftTop, double yLeftTop, double xRightBottom, double yRightBottom, int frame){
            this.xLeftTop = xLeftTop;
            this.yLeftTop = yLeftTop;
            this.xRightBottom = xRightBottom;
            this.yRightBottom = yRightBottom;
            this.frame = frame;
        }
        
        public int getFrame(){
            return this.frame;
        }

        public void resetBound(double xLeftTop, double yLeftTop, double xRightBottom, double yRightBottom){
            this.xLeftTop = xLeftTop;
            this.yLeftTop = yLeftTop;
            this.xRightBottom = xRightBottom;
            this.yRightBottom = yRightBottom;
        }

        public void resetFrame(int frame){
            this.frame = frame;
        }
    }
    
    Bound startBound;
    Bound endBound;

    String linkedFile;
    int linkedFrame;

    Region() {

    }

    Region(double xStartLeftTop, double yStartLeftTop, double xStartRightBottom, double yStartRightBottom, int startFrame) {
        this.startBound = new Bound(xStartLeftTop, yStartLeftTop, xStartRightBottom, yStartRightBottom, startFrame);
        this.endBound = new Bound(xStartLeftTop, yStartLeftTop, xStartRightBottom, yStartRightBottom, startFrame);
    }

    

    public void setEnd(double xEndLeftTop, double yEndLeftTop, double xEndRightBottom, double yEndRightBottom, int endFrame) {
        this.endBound.resetBound(xEndLeftTop, yEndLeftTop, xEndRightBottom, yEndRightBottom);
        this.endBound.resetFrame(endFrame);
    }

    public void setLinkedFile(String linkedFile, int linkedFrame) {
        this.linkedFile = linkedFile;
        this.linkedFrame = linkedFrame;
    }
    
    public Region getBound(int frame){
        if (frame < this.startBound.getFrame() || frame > this.endBound.getFrame()){
            return null;
        }
        double curXLeftTop = this.startBound.xLeftTop + (this.endBound.xLeftTop - this.startBound.xLeftTop) / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        double curYLeftTop = this.startBound.yLeftTop + (this.endBound.yLeftTop - this.startBound.yLeftTop) / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        double curXRightBottom = this.startBound.xRightBottom + (this.endBound.xRightBottom - this.startBound.xRightBottom) / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        double curYRightBottom = this.startBound.yRightBottom + (this.endBound.yRightBottom - this.startBound.yRightBottom) / (this.endBound.frame - this.startBound.frame + 1) * (frame - this.startBound.frame);
        return new Region(curXLeftTop, curYLeftTop, curXRightBottom, curYRightBottom, frame);
    }

}
