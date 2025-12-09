import greenfoot.*;

public class Hero extends Actor
{
    GreenfootImage idle = new GreenfootImage("images/heroFront_idle/idle0.png");
    GreenfootImage[] idle = new GreenfootImage[8];
    int imageIndex = 0;
    public void act()
    {
        // Add your action code here.
    }
    
    public Hero() {
        for (int i = 0; i < idle.length; i++) {
            idle[i] = new GreenfootImage("images/heroFront_idle/idle" + i + ".png");
        }
        setImage(idle[0]);
    }
    
    public void animateHeroFrontIdle() {
        setImage(idle[imageIndex]);
        imageIndex = imageIndex + 1 % idle.length;
    }
}
