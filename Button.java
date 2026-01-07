import greenfoot.*;

public class Button extends Actor
{
    private String text;
    private int size;
    private GreenfootImage img;
    private boolean clicked = false;

    public Button(String text, int size)
    {
        this.text = text;
        this.size = size;
        updateImage();
    }

    private void updateImage()
    {
        img = new GreenfootImage(200, 60);
        img.setColor(Color.BLACK);
        img.fill();
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", size));
        img.drawString(text, 20, 40);
        setImage(img);
    }

    public void act()
    {
        if (Greenfoot.mouseClicked(this))
            clicked = true;
    }

    public boolean isClicked()
    {
        return clicked;
    }
}
