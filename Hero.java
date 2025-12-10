import greenfoot.*;

public class Hero extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[8];
    int imageIndex = 0;
    public void act()
    {
        if(Greenfoot.isKeyDown("left")) {
            move(-5);
        }
        else if (Greenfoot.isKeyDown("right")) {
            move(5);
        }
        
        animateHeroFrontIdle();
    }
    
    public Hero() {
        for (int i = 0; i < idle.length; i++) {
            idle[i] = new GreenfootImage("images/heroFront_idle/idle" + i + ".png");
        }
        setImage(idle[0]);
    }
    
    public void animateHeroFrontIdle() {
        setImage(idle[imageIndex]);
        imageIndex = (imageIndex + 1) % idle.length;
    }
}
