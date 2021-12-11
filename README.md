  # HyperLinked Video Authoring Tool & Player
  > **Hypervideo**, or **Hyperlinked video**. Hypervideo video is a displayed video stream that contains embedded, user-clickable anchors allowing navigation between video and other hypermedia elements. https://github.com/cnarutox/hyperlinked-video-authoring-player
  

  ## Feature
  A **Low latency**, **synchronized**, **easy-to-use** tool integrating functions of both authoring hyperlinks for videos and playing hyperlinked videos.

[Authoring Tool Demo Video](https://youtu.be/yfJ6UC1cnnc) 

  ## Project Architecture
  ### 1. Authoring Tool

  A authoring tool integrating `Import Video`, `Slide over Frame`, `Create Editable Link`, `Edit Bounding Box`, `Save HyperLink` to create hyperlinks from one file to another.

<p align="center"><img src="https://github.com/cnarutox/cnarutox/blob/main/img/Authoring.gif?raw=true"></p>

  #### How it works
  
<p align="center"><img src="https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggVEQ7XG5cbkltcG9ydFZpZGVvW0ltcG9ydCBNYWluIFZpZGVvXSAtLT4gU2xpZGVPdmVyVmlkZW8xKFNpbGRlIEZyYW1lcyk7XG5TbGlkZU92ZXJWaWRlbzEgLS0-IFNlbGVjdEZyYW1lW0NyZWF0ZSBMaW5rXVxuU2VsZWN0RnJhbWUgLS0-IENyZWF0ZUJveFxuQ3JlYXRlQm94W0RyYXcgQm94IEJvdWRpbmddIC0tPiB8Q2FuY2VsfCBTZWxlY3RGcmFtZVxuQ3JlYXRlQm94IC0tPiBOYW1lSHlwZXJMaW5re1JlbmFtZSBMaW5rfVxuXG5JbXBvcnRWaWRlbyAtLT4gTG9hZFZpZGVvMlxuTG9hZFZpZGVvMltJbXBvcnQgT3RoZXIgVmlkZW9zXSAtLT4gU2VsZWN0TGlua2VkRnJhbWUoU2VsZWN0IFRhcmdldCBGcmFtZSlcblxuXG5TZWxlY3RMaW5rZWRGcmFtZSAtLT4gTmFtZUh5cGVyTGlua1xuTmFtZUh5cGVyTGluayAtLT4gSW1wb3J0VmlkZW9cblxuTmFtZUh5cGVyTGluayAtLT4gU2F2ZUh5cGVyTGlua3NbU2F2ZSBMaW5rXVxuIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ" alt="https://mermaid.live/edit/#eyJjb2RlIjoiZ3JhcGggVEQ7XG5cbkltcG9ydFZpZGVvW0ltcG9ydCBNYWluIFZpZGVvXSAtLT4gU2xpZGVPdmVyVmlkZW8xKFNpbGRlIEZyYW1lcyk7XG5TbGlkZU92ZXJWaWRlbzEgLS0-IFNlbGVjdEZyYW1lW0NyZWF0ZSBMaW5rXVxuU2VsZWN0RnJhbWUgLS0-IENyZWF0ZUJveFxuQ3JlYXRlQm94W0RyYXcgQm94IEJvdWRpbmddIC0tPiB8Q2FuY2VsfCBTZWxlY3RGcmFtZVxuQ3JlYXRlQm94IC0tPiBOYW1lSHlwZXJMaW5re1JlbmFtZSBMaW5rfVxuXG5JbXBvcnRWaWRlbyAtLT4gTG9hZFZpZGVvMlxuTG9hZFZpZGVvMltJbXBvcnQgT3RoZXIgVmlkZW9zXSAtLT4gU2VsZWN0TGlua2VkRnJhbWUoU2VsZWN0IFRhcmdldCBGcmFtZSlcblxuXG5TZWxlY3RMaW5rZWRGcmFtZSAtLT4gTmFtZUh5cGVyTGlua1xuTmFtZUh5cGVyTGluayAtLT4gSW1wb3J0VmlkZW9cblxuTmFtZUh5cGVyTGluayAtLT4gU2F2ZUh5cGVyTGlua3NbU2F2ZSBMaW5rXVxuIiwibWVybWFpZCI6IntcbiAgXCJ0aGVtZVwiOiBcImRlZmF1bHRcIlxufSIsInVwZGF0ZUVkaXRvciI6ZmFsc2UsImF1dG9TeW5jIjp0cnVlLCJ1cGRhdGVEaWFncmFtIjpmYWxzZX0"></p>


  #### Run `Authoring Tool`
  ```console
  javac src/*.java -d bin
  java -cp bin Authoring
  ```
  - Make sure to import **directory** like `C:\CSCI576\Project\AIFilmTwo`
  - Make sure to click Save HyperLinked Video before chaning main video
  
  #### Data Structure
  - `HashMap`: restore links of maping pair of _primary file_  to _list of Region of Interest (includes region shape, size and hyperlink)_.
  ```java
  public class Links {

      Map<String, List<Region>> linkedMap = new HashMap<String, List<Region>>();

      public List<Region> get(String fileName) {
        // get item from links
      }

      public void put(String fromFile, Region newRegion) {
          // put item into links
      }

      public List<Region> inRegion(String fromFile, int frame) {
          // interpolate all regions given certain frame
      }

      public void readLocalFile(String localFilePath) {
          // initial links by reading file
      }

      public void toLocalFile(File curFile) {
          // write links info into file
      }

  }
  
  ```
  

  
  <div align="center"><img src="http://liuyutsing.com/wp-content/uploads/2021/12/hashmap.png" width="1000", height="250"></div>
  
  <div align="center"><img src="http://liuyutsing.com/wp-content/uploads/2021/12/ROI.png" width="500", height="250"></div>
  
  
  #### Meta file
  - `File path`: `.txt` file with `same base name` as the primary video saved within primary video files directory.
  > _e.g._ The meta file for video _/usr/AIFilmOne/_ will be save at _/usr/AIFilmOne/AIFilmOne.txt._
  - `Content format`: The first line will refer to `primary file`, while the following lines will refer to `Regions of Interest` with hyperlink information respectively.
  > _e.g._ Example of _/usr/AIFilmOne/AIFilmOne.txt._ is as below.
  ```console
  /usr/AIFilmOne
  [region1start.shapeInfo, region1start.frame]?[region1end.shapeInfo, region1end.frame]?targetFile1?targetframe1
  [region2start.shapeInfo, region2start.frame]?[region2end.shapeInfo, region2end.frame]?targetFile2?targetframe2
  [region3start.shapeInfo, region3start.frame]?[region3end.shapeInfo, region3end.frame]?targetFile3?targetframe3
  ...
  [regionNstart.shapeInfo, regionNstart.frame]?[regionNend.shapeInfo, regionNend.frame]?targetFileN?targetframeN
  ```
  ### 2. Video Player

  > __`Description`__: Play video and synchronize _Video source_ (.rgb files) and _Audio source_ (.wav files) automatically for the given frame rate.
 
 <p align="center"><img src="https://github.com/cnarutox/cnarutox/blob/main/img/VideoPlayer.gif?raw=true"></p>

  #### How to run `Video Player`
  ```console
  javac src/*.java -d bin
  java -cp bin VideoPlayer
  ```
  - Make sure to import **directory** like `C:\CSCI576\Project\AIFilmTwo`
  - Make sure to click Save HyperLinked Video before chaning main video

  #### Input Format
  - __``Video Format``__
    - `Framerate`: _30 fps_.
    - `File Format:` _rgb file_.
    - `Total Frames`: _9000_
    - `Resolution`: _352×288_
  - __``Audio Format``__
    - `Sampling Rate`: _44100_
    - `Bits Per Sample`: _16 bits_

  #### Synchronize Mechanism

  - `Timer.scheduleAtFixedRate`: schedule _rendering image_ task for _repeated fixed-rate (frame rate)_ execution, beginning after the specified delay.
  
      ```java
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        public void run() {
          // Render Image Operation
        }
      }, 0, 1000 / 30);
    ```
  - `Cache`: use _LinkedHashMap_ (hashing-based data structure maintaining _insertion order_ or _access order_) as a Buffer to pre-load image data for rendering.
    ```java
    class Cache extends LinkedHashMap<Integer, BufferedImage> {
      private int capacity;

      public Cache(int capacity) {
        super(capacity, 0.75F, false); // Maintain access order
      }

      @Override
      protected boolean removeEldestEntry(Map.Entry<Integer, BufferedImage> eldest) {
        return size() >= capacity; // Maintain maximum size of cache.
      }
    }

    ```
  - `Thread`:  allow cache-updating to excute concurrently throughout video playing.
    ``````JAVA
    new Thread(new Runnable() {
        public void run() {
          while (true) {
            // pre-loading cache
          }
        }
      }).start();
  - `Periodical Manual Synchronize`: amend precision error based on given frame rate to ensure frames per second.
    ```java
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
        public void run() {
          // Check every integral second to synchronize
          if ((currentTotalTime + currentTime - lastStartTime) % 1000 < 10) {
            currentFrame = (int) ((currentTotalTime + currentTime - lastStartTime) / 1000) * 30;
          }
        }
      }, 0, 1000 / 30);
    ```
  
  - `Clip`: a special kind of data line whose audio data can be loaded prior to playback, and supports _`play()`_, _`stop()`_ and _`setMicrosecondPosition()`_ which is essential when redirect through hyperlink.
    ```java
    public class Sound {
      private Clip clip;

      public Sound(String fileName) {
        // Initiate sound
      }

      public void play(long microseconds) {
        clip.setMicrosecondPosition(microseconds * 1000);
        clip.start();
      }

      public void stop() {
        clip.stop();
      }
    }
    ```
  ## Folder Structure

  The workspace contains two folders by default, where:

  - `src`: the folder to maintain sources
  - `lib`: the folder to maintain dependencies

  Meanwhile, the compiled output files will be generated in the `bin` folder by default.

  > If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there. https://github.com/cnarutox/hyperlinked-video-authoring-player

  ## Dependency Management

  The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

