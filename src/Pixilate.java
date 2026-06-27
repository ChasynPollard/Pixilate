import javax.imageio.ImageIO;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Pixilate {
    //panel and frame
    private static JFrame frame;
    private static JPanel panel;

    //widgets on the GUI
    private static JButton pixilateButton;
    private static JButton downloadButton;
    private static JSlider slider;
    

    private static PixelatePhoto inputPhoto;
    private static boolean newPhoto = false;
    private static boolean pixilatePressed;
    private static JLabel newPhotoImage = null;
    private static JLabel pixilatedPhoto = null;

    //base case checks
    private static boolean sliderZero = false;
    private static boolean slizerHund = true;


    public static void main(String[] args) throws Exception 
    {
        //PixelatePhoto inputPhoto = new PixelatePhoto();

        //inputPhoto.setInputFile("photos/test_image.png"); //sets the file to the test image
    
        //int boxSize = 1; //how pixilated you want your photo to become. IT IS RECOMMENDED THAT IT IS A PERFECT SQUARE

        //inputPhoto.pixilate(boxSize);
        
        gui();
    }

    private static void gui()
    {
        //start the gui window
        frame = new JFrame("Pixilate");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000); //size of the window

        //set up a pannel
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        

        //menu 
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Attach menu bar to the frame
        frame.setJMenuBar(menuBar);          

        //menu items 
        JMenuItem importImage= new JMenuItem("Import Image");
        fileMenu.add(importImage);

        //buttons 
        //pixilateButton
        pixilateButton = new JButton("PIXILATE");
        pixilateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //download button 
        downloadButton = new JButton("DOWNLOAD");
        downloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // slider stuff

        //this will start out really small to get an exponential size increase  
        double[] sliderValues = { 
            0, .0001, .001, .01, .1, .2, .3, .4, .5, 1, 2, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50,
            55, 60, 65, 70, 75, 80, 85, 90, 95, 100
        };


        slider = new JSlider(0, sliderValues.length - 1);

        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        //slider.setPaintLabels(true); 

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i < sliderValues.length; i++) {
            labelTable.put(i, new JLabel(String.valueOf(sliderValues[i])));
        }
        slider.setLabelTable(labelTable);
       
        //adds panel and frame
        frame.add(panel);
        frame.setVisible(true);

        importImage.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                System.out.println("Import Image clicked!");
                JFileChooser fileChooser = new JFileChooser(); //makes a new file chooser

                int result = fileChooser.showOpenDialog(null);

                
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    if(isImage(selectedFile))
                    {
                        inputPhoto = new PixelatePhoto();
                        inputPhoto.setInputFile(selectedFile); //sets the file to the selected file
                        newPhoto = true;
                        System.out.println(selectedFile);
                        refresh();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(frame, "File Type Not Supported", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } 
                else 
                {
                    System.out.println("File selection cancelled.");
                }
            }
        });



        pixilateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                int totalArea;
                double percentage;
                int pixelsToGroup;
                int blockSize;

                if(inputPhoto.getInputFile() != null)
                {
                    System.out.println("SliderValue: " + slider.getValue());
                    pixilatePressed = true;

                    totalArea = inputPhoto.getWidth() * inputPhoto.getHeight();
                    percentage = sliderValues[slider.getValue()] / 100.0;
                    pixelsToGroup = (int)(totalArea * percentage);
                    blockSize = (int)Math.sqrt(pixelsToGroup);
                    blockSize = Math.max(1, blockSize); // prevent 0 or negative sizes


                    if(slider.getValue() != 0 ||slider.getValue() != 100 )
                    {
                        totalArea = inputPhoto.getWidth() * inputPhoto.getHeight();
                        percentage = slider.getValue() / 100.0;
                        pixelsToGroup = (int)(totalArea * percentage);
                        blockSize = (int)Math.sqrt(pixelsToGroup);
                        blockSize = Math.max(1, blockSize); 

                        inputPhoto.pixilate(blockSize);
                    }
                    else if(slider.getValue() == 0 ) //no changes 
                    {
                        inputPhoto.pixilate(0); // there will be an if loop that catches this in PixelatedPhoto.java
                        sliderZero = false;
                    }
                    else if(slider.getValue() == 100) //only return one pixel 
                    {
                        inputPhoto.pixilate(-1); // there will be an if loop that catches this in PixelatedPhoto.java
                    }
                    refresh();
                }
            }
        });

        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                download(); 
            }
        });
    }

        private static Boolean isImage(File file)
    {
        //checks to see if the file is an image that can be used by Buffered Image
        //using else if and not || so I can read it in editor  
        if(file.getName().toLowerCase().endsWith("png"))
        {
            return true;
        }
        else if(file.getName().toLowerCase().endsWith("jpg"))
        {
            return true;
        }
        else if(file.getName().toLowerCase().endsWith("jpeg"))
        {
            return true;
        }
        else if(file.getName().toLowerCase().endsWith("bmp"))
        {
            return true;
        }
        else if(file.getName().toLowerCase().endsWith("wbmp"))
        {
            return true;
        }
        return false;
    }

    private static void refresh()
    {
        panel.removeAll(); //this helps with the dupication 

        // if you add a new photo then before you pixilate it you get to see it
        Boolean addedNewPhoto = false;
        if (newPhoto == true && inputPhoto.getInputFile() != null) //if file button is clicked and getInputFile isnt null
        {
            addedNewPhoto = true; //set a boolean saying you added the file
            newPhoto = false; //resets this boolean 
            //makes the new photo image may get a little streched but doesn't affect the final outcome  
            ImageIcon icon = new ImageIcon(inputPhoto.getInputFile().getAbsolutePath());
            Image scaledImage = icon.getImage().getScaledInstance((int)(panel.getHeight()*.5), (int)(panel.getWidth()*.5), Image.SCALE_SMOOTH);
            newPhotoImage = new JLabel(new ImageIcon(scaledImage));
        }
        else // if you didn't click the new file it removes it if it is there 
        {
            if(newPhotoImage != null) //makes sure you don't try to remove a null variable 
            {
                panel.remove(newPhotoImage); //removes it from the panel
                newPhotoImage = null;
            }
        }

        Boolean addedPixilatedPhoto = false; 
        if (pixilatePressed == true) //if you pressed the pixilate button 
        {
            addedPixilatedPhoto = true; 
            pixilatePressed = false; //resets it 
            //adds the photo to pixilated photo make get a little stretched but if you download it it'll be fine 
            ImageIcon icon = new ImageIcon("photos/pixilated.png"); //file location 
            Image scaledImage = icon.getImage().getScaledInstance((int)(panel.getWidth()*.5), (int)(panel.getHeight()*.5), Image.SCALE_SMOOTH);
            pixilatedPhoto = new JLabel(new ImageIcon(scaledImage));
        }
        else
        {
            if (pixilatedPhoto != null) //if you didn't press pixilate it removes it 
            {
                panel.remove(pixilatedPhoto);
                pixilatedPhoto = null;
            }

        }
        
        //adds everything
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        //panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(slider);
        pixilateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       // panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(pixilateButton);
        //panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        //import button pressed 
        if(addedNewPhoto && addedNewPhoto != null) //if you added the new photo it goes here
        {
            newPhotoImage.setAlignmentX(Component.CENTER_ALIGNMENT);
            newPhotoImage.setAlignmentY(Component.CENTER_ALIGNMENT);
            panel.add(newPhotoImage); 
            addedNewPhoto = false;
        }
        else if(newPhotoImage != null && newPhotoImage.getParent() != null) //import button not pressed
        {
            panel.remove(newPhotoImage);
            newPhotoImage = null;
        }

        //pixilate pressed 
        if(addedPixilatedPhoto && pixilatedPhoto != null) //adds the now pixilated image
        {
            pixilatedPhoto.setAlignmentX(Component.CENTER_ALIGNMENT);
            pixilatedPhoto.setAlignmentY(Component.CENTER_ALIGNMENT);
            panel.add(pixilatedPhoto);
            panel.add(downloadButton);
            addedPixilatedPhoto = false;
        }
        else if(pixilatedPhoto != null && pixilatedPhoto.getParent() != null) //pixilate not pressed
        {
            panel.remove(pixilatedPhoto);
            panel.remove(downloadButton);
            pixilatedPhoto = null;
        }


        //refreshes
        panel.revalidate();
        panel.repaint();

    }

    private static void download()
    {
        //expand the image for download 
        inputPhoto.expandPixelBlocks(inputPhoto.getBlockSize());
        
        //pick the file
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setDialogTitle("Select Folder to Save Image");
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int userSelection = folderChooser.showSaveDialog(frame);

        //now save it
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            BufferedImage processedImage = null;
            try{
                processedImage = ImageIO.read(new File("photos/pixilated_expanded.png"));
            } catch (IOException ey) {
                JOptionPane.showMessageDialog(frame, "Error: " + ey.getMessage());
            }
            
            File selectedFolder = folderChooser.getSelectedFile();

            String baseName = "user_expanded";
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            File fileToSave = new File(selectedFolder, baseName + "_" + timeStamp + ".png");

            //try block to save the image 
            try (FileWriter writer = new FileWriter(fileToSave)) {
                ImageIO.write(processedImage, "png", fileToSave);
                JOptionPane.showMessageDialog(frame, "Downloaded Successful");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        }

    }

}