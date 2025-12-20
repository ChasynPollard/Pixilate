import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
//https://www.geeksforgeeks.org/java/image-processing-in-java-get-and-set-pixels/

public class PixelatePhoto 
{
    private BufferedImage img;
    private File inputFile; // the orignal imgage
    private int height; // the height of the image 
    private int width; //the width
    private int blockSize; //blockSize

    public PixelatePhoto()
    {
        this.img = null;
        this.inputFile = null;
        this.blockSize = 1;
        this.height = 0;
        this.width = 0;
    }
   
    //setters (some of these realistically arn't needed but they are nice to have just in case and for testing)
    public BufferedImage setImg(BufferedImage img)
    {
        this.img = img; 
        return this.img; 
    }

    public File setInputFile(File file)
    {
        this.inputFile = file; //sets the file
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

    public int setBlockSize(int blockSize)
    {
        this.blockSize = blockSize;
        return blockSize;
    }

    //getters

    public int getHeight()
    {
        if(img ==null)
        {
            if(inputFile != null)
            {
                try
                {
                    setImg(ImageIO.read(this.inputFile));
                    return img.getHeight();
                } catch(Exception e){
                    System.out.println("Image did not reset properly " + e);
                }
                return 0;
            }
            else
            {
                return 0;
            }
        }
        else
        {
           return img.getHeight(); 
        }
    }

    public int getWidth()
    {
        if(img ==null)
        {
            if(inputFile != null)
            {
                try
                {
                    setImg(ImageIO.read(this.inputFile));
                    return img.getWidth();
                } catch(Exception e){
                    System.out.println("Image did not reset properly " + e);
                }
                return 0;
            }
            else
            {
                return 0;
            }
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

    public int getBlockSize()
    {
        return this.blockSize;
    }

    //functions 

    public void pixilate(int blockSize) 
    {
        blockSize = setBlockSize(blockSize);
        //resets img in case this was ran twice
        try{
            setImg(ImageIO.read(this.inputFile));
        } catch(Exception e){
            System.out.println("Image did not reset properly " + e);
            return;
        }
        this.width = img.getWidth();
        this.height = img.getHeight();

        //this ensures that there isn't an out of bounds error
        //HAS TO BE OUTSIDE OF IF & ELSE STATEMENTS 

        BufferedImage pixilated = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); //default Also makes it not throw errors
        if(blockSize != -1)
        {   
            int blocksWide = (int) Math.ceil(width / (double) blockSize);
            int blocksHigh = (int) Math.ceil(height / (double) blockSize);
            pixilated = new BufferedImage(blocksWide, blocksHigh, BufferedImage.TYPE_INT_ARGB);
        }


        //base cases
        if (img == null) //null check
        {
            System.out.println("Image not loaded correctly");
            return;
        }

        if(blockSize == 0) //no changes return default image
        {
            //techinally this shouldn't change anything but due to how the image is resized it changes
            return;
        }
        else if(blockSize == -1) // return only one pixel does 100% of the image
        {
            long sumR = 0, sumG = 0, sumB = 0; // sums of all RBG values
            for(int y=0;  y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    //get the RGB values
                    int p = img.getRGB(x, y);
                    Color c = new Color(p);

                    //add to the sum 
                    sumR += c.getRed();
                    sumG += c.getGreen();
                    sumB += c.getBlue();
                }
            }
            //gets the avarages 
            int avgR = (int)(sumR);
            int avgG = (int)(sumG);
            int avgB = (int)(sumB);

            Color avgColor = new Color(avgR, avgG, avgB); //avarages out the color
            pixilated.setRGB(1, 1, avgColor.getRGB()); //sets the color
        }
        else // when there is an input that isnt -1 or 0
        {
            //giant loop that goes through the pixels
            for (int y = 0; y < height; y += blockSize) //TODO: right now this is O(n^3) make it run faster
            {
                for (int x = 0; x < width; x += blockSize) 
                {
                    long sumR = 0, sumG = 0, sumB = 0;
                    
                    int pixelSize = 0;
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
                                pixelSize++; // this makes it so it doesn't add pixels that are out of bounds
                            }
                        }
                    }

                    
                    //gets the avarages 
                    int avgR = (int)(sumR / pixelSize);
                    int avgG = (int)(sumG / pixelSize);
                    int avgB = (int)(sumB / pixelSize);

                    Color avgColor = new Color(avgR, avgG, avgB); //avarages out the color
                    pixilated.setRGB(x / blockSize, y / blockSize, avgColor.getRGB()); //sets the color
                }
            }
        }
        //saves the image 
        try {
            File f = new File("photos/pixilated.png");
            ImageIO.write(pixilated, "png", f);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e);
        }
        System.out.println("Pixilate!");
    }

    public void expandPixelBlocks(int blockSize) 
    {
        try {
            // Load the pixelated image (assumes it was previously saved)
            BufferedImage smallImg = ImageIO.read(new File("photos/pixilated.png"));
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
            File f = new File("photos/pixilated_expanded.png");
            ImageIO.write(expanded, "png", f);
            System.out.println("Expanded Image!");
        } catch (IOException e) {
            System.out.println("Error processing pixelated image: " + e);
        }
    }

}

