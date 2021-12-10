## Getting Started
> Run Authoring.java
> Run VideoPlayer.java
> Make sure to import **directory** like `C:\CSCI576\Project\AIFilmTwo`
> Make sure to click Save HyperLinked Video before chaning main video
### https://github.com/cnarutox/hyperlinked-video-authoring-player

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Overview
> _**Hypervideo**, or **Hyperlinked video**. Hypervideo video is a displayed video stream that contains embedded, user-clickable anchors allowing navigation between video and other
hypermedia elements._

### Authoring Tool

> __`Tasks`__: A authoring tool integrating __*Load File*__, __*Slide over Frame*__, __*Create Editable Link*__, __*Editable Bounding Box*__, __*Save HyperLink*__ to create hyperlinks from one file to another.

[![](https://mermaid.ink/img/eyJjb2RlIjoic3RhdGVEaWFncmFtLXYyXG5bKl0gLS0-IExvYWRWaWRlbzFcblxuXG5cbkxvYWRWaWRlbzEgLS0-IFNsaWRlT3ZlclZpZGVvMVxuU2xpZGVPdmVyVmlkZW8xIC0tPiBTZWxlY3RGcmFtZVxuU2VsZWN0RnJhbWUgLS0-IENyZWF0ZUJveFxuQ3JlYXRlQm94IC0tPiBDYW5jZWxCb3hcbkNhbmNlbEJveCAtLT4gU2VsZWN0RnJhbWVcbkNyZWF0ZUJveCAtLT4gTmFtZUh5cGVyTGlua1xuXG5Mb2FkVmlkZW8xIC0tPiBMb2FkVmlkZW8yXG5Mb2FkVmlkZW8yIC0tPiBTZWxlY3RMaW5rZWRGcmFtZVxuXG5cblNlbGVjdExpbmtlZEZyYW1lIC0tPiBOYW1lSHlwZXJMaW5rXG5OYW1lSHlwZXJMaW5rIC0tPiBMb2FkVmlkZW8xXG5cbk5hbWVIeXBlckxpbmsgLS0-IFNhdmVIeXBlckxpbmtzXG5cblxuXG5cblxuXG5TYXZlSHlwZXJMaW5rcyAtLT4gWypdXG4iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid.live/edit/#eyJjb2RlIjoic3RhdGVEaWFncmFtLXYyXG5bKl0gLS0-IExvYWRWaWRlbzFcblxuXG5cbkxvYWRWaWRlbzEgLS0-IFNsaWRlT3ZlclZpZGVvMVxuU2xpZGVPdmVyVmlkZW8xIC0tPiBTZWxlY3RGcmFtZVxuU2VsZWN0RnJhbWUgLS0-IENyZWF0ZUJveFxuQ3JlYXRlQm94IC0tPiBDYW5jZWxCb3hcbkNhbmNlbEJveCAtLT4gU2VsZWN0RnJhbWVcbkNyZWF0ZUJveCAtLT4gTmFtZUh5cGVyTGlua1xuXG5Mb2FkVmlkZW8xIC0tPiBMb2FkVmlkZW8yXG5Mb2FkVmlkZW8yIC0tPiBTZWxlY3RMaW5rZWRGcmFtZVxuXG5cblNlbGVjdExpbmtlZEZyYW1lIC0tPiBOYW1lSHlwZXJMaW5rXG5OYW1lSHlwZXJMaW5rIC0tPiBMb2FkVmlkZW8xXG5cbk5hbWVIeXBlckxpbmsgLS0-IFNhdmVIeXBlckxpbmtzXG5cblxuXG5cblxuXG5TYXZlSHlwZXJMaW5rcyAtLT4gWypdXG4iLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)


### Video Player

> __`Tasks`__: Synchronize _Video source_ (.rgb files) and _Audio source_ (.wav files) manually with given frame rate.

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
  

| variable | data structure  |                              usage                               |
|:--------:|:---------------:|:----------------------------------------------------------------:|
| _cache_  | _LinkedHashMap_ | ``pre-load image file to accelerate render image at frame rate`` |
| _links_  |    _HashMap_    |                                                                  |


