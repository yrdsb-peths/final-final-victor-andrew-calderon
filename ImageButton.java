import greenfoot.*;

public class ImageButton extends Actor
{
    public ImageButton(String imagePath)
    {
        GreenfootImage img = new GreenfootImage(imagePath);

        // ===== SCALE SIZE HERE =====
        int targetWidth = 300;   // ← adjust this number if needed

        int targetHeight = img.getHeight() * targetWidth / img.getWidth();
        img.scale(targetWidth, targetHeight);

        setImage(img);
    }

    public boolean isClicked()
    {
        return Greenfoot.mouseClicked(this);
    }
}
