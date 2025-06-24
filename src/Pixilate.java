public class Pixilate {
    public static void main(String[] args) throws Exception 
    {
        InputPhoto inputPhoto = new InputPhoto();

        inputPhoto.setInputFile("photos/test_image.png"); //sets the file to the test image
    
        int boxSize = 5; //how pixilated you want your photo to become. IT IS RECOMMENDED THAT IT IS A PERFECT SQUARE

        inputPhoto.pixilatePhoto(boxSize);
        
    }
}
