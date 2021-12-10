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

[![](https://mermaid.ink/img/eyJjb2RlIjoic3RhdGVEaWFncmFtLXYyXG5bKl0gLS0-IExvYWRWaWRlbzFcblxuTG9hZFZpZGVvMSAtLT4gTG9hZFZpZGVvMlxuTG9hZFZpZGVvMiAtLT4gU2VsZWN0TGlua2VkRnJhbWVcblxuTG9hZFZpZGVvMSAtLT4gU2xpZGVPdmVyVmlkZW8xXG5TbGlkZU92ZXJWaWRlbzEgLS0-IFNlbGVjdFN0YXJ0RnJhbWVcblNlbGVjdFN0YXJ0RnJhbWUgLS0-IENyZWF0ZVN0YXJ0Qm94XG5DcmVhdGVTdGFydEJveCAtLT4gU2VsZWN0RW5kRnJhbWVcbkNyZWF0ZVN0YXJ0Qm94IC0tPiBDYW5jZWxTdGFydEJveFxuQ2FuY2VsU3RhcnRCb3ggLS0-IFNlbGVjdFN0YXJ0RnJhbWVcblxuU2VsZWN0RW5kRnJhbWUgLS0-IENyZWF0ZUVuZEJveFxuQ3JlYXRlRW5kQm94IC0tPiBDYW5jZWxFbmRCb3hcbkNhbmNlbEVuZEJveCAtLT4gU2VsZWN0RW5kRnJhbWVcblxuXG5DcmVhdGVFbmRCb3ggLS0-IE5hbWVIeXBlckxpbmtcblNlbGVjdExpbmtlZEZyYW1lIC0tPiBOYW1lSHlwZXJMaW5rXG5OYW1lSHlwZXJMaW5rIC0tPiBMb2FkVmlkZW8xXG5cblxuTmFtZUh5cGVyTGluayAtLT4gU2F2ZUh5cGVyTGlua3NcblxuXG5TYXZlSHlwZXJMaW5rcyAtLT4gWypdXG4iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid.live/edit/#eyJjb2RlIjoic3RhdGVEaWFncmFtLXYyXG5bKl0gLS0-IExvYWRWaWRlbzFcblxuTG9hZFZpZGVvMSAtLT4gTG9hZFZpZGVvMlxuTG9hZFZpZGVvMiAtLT4gU2VsZWN0TGlua2VkRnJhbWVcblxuTG9hZFZpZGVvMSAtLT4gU2xpZGVPdmVyVmlkZW8xXG5TbGlkZU92ZXJWaWRlbzEgLS0-IFNlbGVjdFN0YXJ0RnJhbWVcblNlbGVjdFN0YXJ0RnJhbWUgLS0-IENyZWF0ZVN0YXJ0Qm94XG5DcmVhdGVTdGFydEJveCAtLT4gU2VsZWN0RW5kRnJhbWVcbkNyZWF0ZVN0YXJ0Qm94IC0tPiBDYW5jZWxTdGFydEJveFxuQ2FuY2VsU3RhcnRCb3ggLS0-IFNlbGVjdFN0YXJ0RnJhbWVcblxuU2VsZWN0RW5kRnJhbWUgLS0-IENyZWF0ZUVuZEJveFxuQ3JlYXRlRW5kQm94IC0tPiBDYW5jZWxFbmRCb3hcbkNhbmNlbEVuZEJveCAtLT4gU2VsZWN0RW5kRnJhbWVcblxuXG5DcmVhdGVFbmRCb3ggLS0-IE5hbWVIeXBlckxpbmtcblNlbGVjdExpbmtlZEZyYW1lIC0tPiBOYW1lSHlwZXJMaW5rXG5OYW1lSHlwZXJMaW5rIC0tPiBMb2FkVmlkZW8xXG5cblxuTmFtZUh5cGVyTGluayAtLT4gU2F2ZUh5cGVyTGlua3NcblxuXG5TYXZlSHlwZXJMaW5rcyAtLT4gWypdXG4iLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)


* This is the first list item.
* Here's the second list item.

> A blockquote would look great below the second list item.

```java
  private void index(){
    MessageBox.Show("hello world");
}
``` 
<pre>
$ python3 apple.py <em>arg1</em>
</pre>

* And here's the third list item.
- 
- ``Use `code` in your Markdown file.``
- 
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
- `Cache`: use _LinkedHashMap_ (hashing-based data structure maintaining _insertion order_ or _access order_) as a Buffer to pre-load image data for rendering.
- `Periodical Manual Synchronize`: amend precision error based on given frame rate to ensure frames per second.
- `Thread`:  allow cache-updating to excute concurrently throughout video playing
  ```java
  private void index(){
    MessageBox.Show("hello world");
  }
  ``` 
- `Clip`: a special kind of data line whose audio data can be loaded prior to playback, instead of being streamed in real time.


| variable | data structure  |                              usage                               |
|:--------:|:---------------:|:----------------------------------------------------------------:|
| _cache_  | _LinkedHashMap_ | ``pre-load image file to accelerate render image at frame rate`` |
| _links_  |    _HashMap_    |                                                                  |


