## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies
- `photos`: the folder to hold photos that you upload and the photos that contain the output

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Other

My name is Chasyn Pollard and Pixelate is a little project that I decided to make for fun and for practice. 
My github is: https://github.com/ChasynPollard

## How This Works

Right now there isn't a UI (Later there will be a UI GUI to upload and download photos)

1. Add your photo to the photos folder and change the file path in Pixilate.
2. Change blockSize in Pixilate (this determines how pixilated the image will become it is best to use a number that is a perfect square)
3. Once you run Pixilate it will make a temporary image that is small but is written pixel by pixel.
4. Then another function called expandPixelBlocks will keep that pixilated look but make it the orginal size of the image. This makes sure that computers doesn't blur the image.