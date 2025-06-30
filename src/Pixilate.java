import javax.swing.Box;
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
import java.util.Hashtable;
import java.awt.*;


public class Pixilate {
    //panel and frame
    private static JFrame frame;
    private static JPanel panel;

    private static InputPhoto inputPhoto;
    private static boolean newPhoto = false;
    private static boolean pixilatePressed;
    private static JLabel newPhotoImage = null;
    private static JLabel pixilatedPhoto = null;

    public static void main(String[] args) throws Exception 
    {
        //InputPhoto inputPhoto = new InputPhoto();

        //inputPhoto.setInputFile("photos/test_image.png"); //sets the file to the test image
    
        //int boxSize = 1; //how pixilated you want your photo to become. IT IS RECOMMENDED THAT IT IS A PERFECT SQUARE

        //inputPhoto.pixilatePhoto(boxSize);
        
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
        JButton pixilateButton = new JButton("PIXILATE");
        pixilateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //slider
        int[] sliderValues = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        JSlider slider = new JSlider(0, sliderValues.length - 1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

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
                        inputPhoto = new InputPhoto();
                        inputPhoto.setInputFile(selectedFile); //sets the file to the selected file
                        newPhoto = true;
                        System.out.println(selectedFile);
                        refresh(pixilateButton, slider);
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
                if(inputPhoto.getInputFile() != null)
                {
                    pixilatePressed = true;
                    int totalArea = inputPhoto.getWidth() * inputPhoto.getHeight();
                    double percentage = slider.getValue() / 100.0;
                    int pixelsToGroup = (int)(totalArea * percentage);
                    int blockSize = (int)Math.sqrt(pixelsToGroup);
                    blockSize = Math.max(1, blockSize); // prevent 0 or negative sizes

                    inputPhoto.pixilatePhoto(blockSize);
                    refresh(pixilateButton, slider);

                }
            }
        });
    }

    private static Boolean isImage(File file)
    {
        //checks to see if the file is an image that can be used by Buffered Image
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

    static void refresh(JButton pixilateButton, JSlider slider)
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
            ImageIcon icon = new ImageIcon("photos/pixilated_expanded.png"); //file location 
            Image scaledImage = icon.getImage().getScaledInstance((int)(panel.getHeight()*.5), (int)(panel.getWidth()*.5), Image.SCALE_SMOOTH);
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
        if(addedNewPhoto && addedNewPhoto != null) //if you added the new photo it goes here
        {
            newPhotoImage.setAlignmentX(Component.CENTER_ALIGNMENT);
            newPhotoImage.setAlignmentY(Component.CENTER_ALIGNMENT);
            panel.add(newPhotoImage); 
            addedNewPhoto = false;
        }
        else if(newPhotoImage != null && newPhotoImage.getParent() != null)
        {
            panel.remove(newPhotoImage);
            newPhotoImage = null;
        }

        if(addedPixilatedPhoto && pixilatedPhoto != null) //adds the now pixilated image
        {
            pixilatedPhoto.setAlignmentX(Component.CENTER_ALIGNMENT);
            pixilatedPhoto.setAlignmentY(Component.CENTER_ALIGNMENT);
            panel.add(pixilatedPhoto); 
            addedPixilatedPhoto = false;
        }
        else if(pixilatedPhoto != null && pixilatedPhoto.getParent() != null)
        {
            panel.remove(pixilatedPhoto);
            pixilatedPhoto = null;
        }


        //refreshes
        panel.revalidate();
        panel.repaint();

    }

}