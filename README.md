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

Run Pixilate.java and use file to import an image and use the slider and pixilate button to pixilate your image.

When you pixilate it runs the pixilate function in PixelatePhoto.java

When you press the download button it expands the image to its old size then saves it on your computer 

## TODO

1. make this run more efficiently (threading/look into cuda java libraies)
3. fix scaleing of images so it doesn't stretch
2. make GUI prettier 