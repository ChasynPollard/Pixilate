import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
//https://www.geeksforgeeks.org/java/image-processing-in-java-get-and-set-pixels/

public class InputPhoto 
{
    private BufferedImage img;
    private File inputFile; // the orignal img
    private int height; // the height of the image 
    private int width; //the width

    public InputPhoto()
    {
        this.img = null;
        this.inputFile = null;
        this.height = 0;
        this.width = 0;
    }
   
    //setters (some of these realistically arn't needed but they are nice to have just in case and for testing)
    public BufferedImage setImg(BufferedImage img)
    {
        this.img = img; 
        return this.img; 
    }

    public File setInputFile(String file)
    {
        this.inputFile = new File(file); //sets the file
        return this.inputFile; //returns the now set file;
    }

    public int setHeight(int height)
    {
        this.height = height;
        return this.height;
    }
    
    public int setWidth(int width)
    {
        this.width = width;
        return this.width;
    }

    //getters

    public int getHeight()
    {
        if(!img.equals(null))
        {
            return 0;
        }
        else
        {
           return img.getHeight(); 
        }
    }

    public int getWidth()
    {
        if(!img.equals(null))
        {
            return 0;
        }
        else
        {
           return img.getWidth(); 
        }
    }

    public File getInputFile()
    {
        return this.inputFile; 
    }

    //functions 

    public void pixilatePhoto(int blockSize) 
    {
        //resets img in case this was ran twice
        try{
            setImg(ImageIO.read(this.inputFile));
        } catch(Exception e){
            System.out.println("Image did not reset properly " + e);
            return;
        }
        this.width = img.getWidth();
        this.height = img.getHeight();

        
        if (img == null) 
        {
            System.out.println("Image not loaded correctly");
            return;
        }

        //this ensures that there isn't an out of bounds error 
        int blocksWide = (int) Math.ceil(width / (double) blockSize);
        int blocksHigh = (int) Math.ceil(height / (double) blockSize);
        BufferedImage pixilated = new BufferedImage(blocksWide, blocksHigh, BufferedImage.TYPE_INT_ARGB);

        //giant loop that goes through the pixels
        for (int y = 0; y < height; y += blockSize) 
        {
            for (int x = 0; x < width; x += blockSize) 
            {
                long sumR = 0, sumG = 0, sumB = 0;

                for (int dy = 0; dy < blockSize; dy++) {
                    for (int dx = 0; dx < blockSize; dx++) {
                        int pxX = x + dx;
                        int pxY = y + dy;

                        if (pxX < width && pxY < height) {
                            int px = img.getRGB(pxX, pxY);
                            Color c = new Color(px);
                            sumR += c.getRed();
                            sumG += c.getGreen();
                            sumB += c.getBlue();
                        }
                    }
                }

                //the size of the new image
                int numPixels = blockSize * blockSize;
                //gets the avarages 
                int avgR = (int)(sumR / numPixels);
                int avgG = (int)(sumG / numPixels);
                int avgB = (int)(sumB / numPixels);

                Color avgColor = new Color(avgR, avgG, avgB); //avarages out the color
                pixilated.setRGB(x / blockSize, y / blockSize, avgColor.getRGB()); //sets the color
            }
        }

        //saves the image 
        try {
            File f = new File("lib/pixilated.png");
            ImageIO.write(pixilated, "png", f);

        } catch (IOException e) {
            System.out.println("Error saving image: " + e);
        }
        expandPixelBlocks(blockSize);
    }

    public void expandPixelBlocks(int blockSize) {
    try {
        // Load the pixelated image (assumes it was previously saved)
        BufferedImage smallImg = ImageIO.read(new File("lib/pixilated.png"));
        int newWidth = smallImg.getWidth() * blockSize;
        int newHeight = smallImg.getHeight() * blockSize;

        BufferedImage expanded = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < smallImg.getHeight(); y++) 
        {
            for (int x = 0; x < smallImg.getWidth(); x++) 
            {
                int rgb = smallImg.getRGB(x, y);

                // fill a block of size blockSize × blockSize
                for (int dy = 0; dy < blockSize; dy++) 
                {
                    for (int dx = 0; dx < blockSize; dx++) 
                    {
                        int px = x * blockSize + dx;
                        int py = y * blockSize + dy;
                        expanded.setRGB(px, py, rgb);
                    }
                }
            }
        }

        // Save the re-expanded image
        File f = new File("lib/pixilated_expanded.png");
        ImageIO.write(expanded, "png", f);
        System.out.println("Restored full-size pixilated image saved!");
    } catch (IOException e) {
        System.out.println("Error processing pixelated image: " + e);
    }
}
}
