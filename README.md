  ## Overview
  > _**Hypervideo**, or **Hyperlinked video**. Hypervideo video is a displayed video stream that contains embedded, user-clickable anchors allowing navigation between video and other hypermedia elements._
  
  ## Feature
  A _**Low latency**_, _**high concurrency**_, _**synchronized**_, _**easy-to-use**_ tool integrating functions of both authoring hyperlinks for videos and playing hyperlinked videos.

  ### https://github.com/cnarutox/hyperlinked-video-authoring-player

  ## Folder Structure

  The workspace contains two folders by default, where:

  - `src`: the folder to maintain sources
  - `lib`: the folder to maintain dependencies

  Meanwhile, the compiled output files will be generated in the `bin` folder by default.

  > If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

  ## Dependency Management

  The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


  ## Project Architecture
  ### Authoring Tool

  > __`Tasks`__: A authoring tool integrating __*Load File*__, __*Slide over Frame*__, __*Create Editable Link*__, __*Editable Bounding Box*__, __*Save HyperLink*__ to create hyperlinks from one file to another.

  #### How it works
  [![](https://mermaid.ink/img/eyJjb2RlIjoic3RhdGVEaWFncmFtLXYyXG5bKl0gLS0-IExvYWRWaWRlbzFcblxuXG5cbkxvYWRWaWRlbzEgLS0-IFNsaWRlT3ZlclZpZGVvMVxuU2xpZGVPdmVyVmlkZW8xIC0tPiBTZWxlY3RGcmFtZVxuU2VsZWN0RnJhbWUgLS0-IENyZWF0ZUJveFxuQ3JlYXRlQm94IC0tPiBDYW5jZWxCb3hcbkNhbmNlbEJveCAtLT4gU2VsZWN0RnJhbWVcbkNyZWF0ZUJveCAtLT4gTmFtZUh5cGVyTGlua1xuXG5Mb2FkVmlkZW8xIC0tPiBMb2FkVmlkZW8yXG5Mb2FkVmlkZW8yIC0tPiBTZWxlY3RMaW5rZWRGcmFtZVxuXG5cblNlbGVjdExpbmtlZEZyYW1lIC0tPiBOYW1lSHlwZXJMaW5rXG5OYW1lSHlwZXJMaW5rIC0tPiBMb2FkVmlkZW8xXG5cbk5hbWVIeXBlckxpbmsgLS0-IFNhdmVIeXBlckxpbmtzXG5cblxuXG5cblxuXG5TYXZlSHlwZXJMaW5rcyAtLT4gWypdXG4iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid.live/edit/#eyJjb2RlIjoic3RhdGVEaWFncmFtLXYyXG5bKl0gLS0-IExvYWRWaWRlbzFcblxuXG5cbkxvYWRWaWRlbzEgLS0-IFNsaWRlT3ZlclZpZGVvMVxuU2xpZGVPdmVyVmlkZW8xIC0tPiBTZWxlY3RGcmFtZVxuU2VsZWN0RnJhbWUgLS0-IENyZWF0ZUJveFxuQ3JlYXRlQm94IC0tPiBDYW5jZWxCb3hcbkNhbmNlbEJveCAtLT4gU2VsZWN0RnJhbWVcbkNyZWF0ZUJveCAtLT4gTmFtZUh5cGVyTGlua1xuXG5Mb2FkVmlkZW8xIC0tPiBMb2FkVmlkZW8yXG5Mb2FkVmlkZW8yIC0tPiBTZWxlY3RMaW5rZWRGcmFtZVxuXG5cblNlbGVjdExpbmtlZEZyYW1lIC0tPiBOYW1lSHlwZXJMaW5rXG5OYW1lSHlwZXJMaW5rIC0tPiBMb2FkVmlkZW8xXG5cbk5hbWVIeXBlckxpbmsgLS0-IFNhdmVIeXBlckxpbmtzXG5cblxuXG5cblxuXG5TYXZlSHlwZXJMaW5rcyAtLT4gWypdXG4iLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)

  #### How to run
  ```console
  Run Authoring.java
  Make sure to import **directory** like `C:\CSCI576\Project\AIFilmTwo`
  Make sure to click Save HyperLinked Video before chaning main video
  ```

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
  #### Meta file
  - `file format`: `.txt` file with `same base name` as the primary video saved within primary video files directory.
  > _e.g._ The meta file for video _/usr/AIFilmOne/_ will be save at _/usr/AIFilmOne/AIFilmOne.txt._
  - `Content format`: The first line will refer to `primary file`, while the following lines will refer to `Regions of Interest` with hyperlink information respectively.
  > _e.g._ Example of _/usr/AIFilmOne/AIFilmOne.txt._ is as below.
  ```console
  /usr/AIFilmOne
  [region1start.shapeInfo, region1start.frame]?[region1end.shapeInfo, region1end.frame]?linkedFile?linkedframe
  [region2start.shapeInfo, region2start.frame]?[region2end.shapeInfo, region2end.frame]?linkedFile?linkedframe
  [region3start.shapeInfo, region3start.frame]?[region3end.shapeInfo, region3end.frame]?linkedFile?linkedframe
  ...
  [regionNstart.shapeInfo, regionNstart.frame]?[regionNend.shapeInfo, regionNend.frame]?linkedFile?linkedframe
  ```
  ### Video Player

  > __`Tasks`__: Synchronize _Video source_ (.rgb files) and _Audio source_ (.wav files) manually with given frame rate.
 

   #### How to run
  ```console
  Run VideoPlayer.java
  Make sure to import **directory** like `C:\CSCI576\Project\AIFilmTwo`
  Make sure to click Save HyperLinked Video before chaning main video
  ```

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
    



