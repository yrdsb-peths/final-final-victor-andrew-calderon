import greenfoot.*;

public class Xijinping extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[4];
    int imageIndex = 0;
    SimpleTimer animationTimer = new SimpleTimer();
    
    public void act()
    {
        animateXijinping();
    }
    
    public Xijinping() 
    {
        for (int i = 0; i < idle.length; i++) 
        {
            idle[i] = new GreenfootImage("images/xijinping_idle/idle" + i + ".png");
            idle[i].scale(156, 262);
        }
        setImage(idle[0]);
    }
    
    public void animateXijinping() 
    {
        if (animationTimer.millisElapsed() < 200) 
        {
            return;
        }
        
        animationTimer.mark();
        setImage(idle[imageIndex]);
        imageIndex = (imageIndex + 1) % idle.length;
    }
}