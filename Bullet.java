import greenfoot.*;

public class Bullet extends Actor
{
    private int speed = 10;

    public Bullet() {
        GreenfootImage img = new GreenfootImage("images/bullet.png");
        img.scale(40, 40);
        img.rotate(180);
        setImage(img);
    }

    public void act()
    {
        move(speed);     // ✅ move in facing direction
        checkEdge();
    }

    private void checkEdge() {
        if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }
}
