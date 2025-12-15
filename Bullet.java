import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Bullet extends Actor
{
    private int speed = 10; // pixels per act cycle

    public Bullet() {
        // Set the bullet image (replace with your image file if needed)
        GreenfootImage img = new GreenfootImage("images/bullet.png");
        img.scale(40, 40); // resize the bullet
        setImage(img);
    }

    public void act()
    {
        moveBullet();
        checkEdge();
    }

    // Move the bullet upwards
    private void moveBullet() {
        setLocation(getX(), getY() - speed);
    }

    // Remove the bullet if it goes off the screen
    private void checkEdge() {
        if (getY() < 0) {
            getWorld().removeObject(this);
        }
    }
}
