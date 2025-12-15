import greenfoot.*;
import java.util.Random;

public class Xijinping extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[4];
    int imageIndex = 0;
    SimpleTimer animationTimer = new SimpleTimer();
    Random rand = new Random();

    public Xijinping() 
    {
        for (int i = 0; i < idle.length; i++) 
        {
            idle[i] = new GreenfootImage("images/xijinping_idle/idle" + i + ".png");
            idle[i].scale(156, 262);
        }
        setImage(idle[0]);
    }

    public void act()
    {
        animateXijinping();

        // Press space to randomize position and shoot bullet
        if (Greenfoot.isKeyDown("space")) 
        {
            randomizePosition();
            shootBullet();
        }
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

    public void randomizePosition() 
    {
        World world = getWorld();
        if (world == null) return;

        int x = rand.nextInt(world.getWidth() - getImage().getWidth()) + getImage().getWidth() / 2;
        int y = rand.nextInt(world.getHeight() - getImage().getHeight()) + getImage().getHeight() / 2;

        setLocation(x, y);
    }

    public void shootBullet() 
    {
        World world = getWorld();
        if (world == null) return;

        Bullet bullet = new Bullet();
        bullet.setLocation(getX(), getY() - getImage().getHeight()/2); // shoot from top of Xijinping
        world.addObject(bullet, bullet.getX(), bullet.getY());
    }
}
