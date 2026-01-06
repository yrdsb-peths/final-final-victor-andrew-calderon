import greenfoot.*;

public class StartButton extends Actor
{
    public StartButton()
    {
        GreenfootImage img = new GreenfootImage("images/playButton.png");
        img.scale(300, 100); // adjust to your liking
        setImage(img);
    }

    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            Greenfoot.setWorld(new MyWorld());
        }
    }
}
