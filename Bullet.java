import greenfoot.*;

public class Bullet extends Actor
{
    private int speed = 4;

    public Bullet() {
        GreenfootImage img = new GreenfootImage("images/bullet.png");
        img.scale(40, 40);
        setImage(img);
    }

    public void act()
    {
        move(speed);   // ✅ straight movement only

        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }
}