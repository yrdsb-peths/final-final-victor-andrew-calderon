import greenfoot.*;

public class Bullet extends Actor
{
    // ================= CONSTANTS =================
    private static final int SPEED = 4;
    private static final int IMAGE_SIZE = 40;

    public Bullet() 
    {
        setupImage();
    }

    private void setupImage()
    {
        GreenfootImage img = new GreenfootImage("images/bullet.png");
        img.scale(IMAGE_SIZE, IMAGE_SIZE);
        setImage(img);
    }

    public void act()
    {
        move(SPEED); // Move straight

        // Remove bullet if it reaches the edge
        World world = getWorld();
        if (world != null && isAtEdge()) {
            world.removeObject(this);
        }
    }
}
