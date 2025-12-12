import greenfoot.*;

public class Hero extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[8];
    GreenfootImage[] walkLeft = new GreenfootImage[6];
    GreenfootImage[] walkRight = new GreenfootImage[6];
    GreenfootImage[] walkUp = new GreenfootImage[6];
    GreenfootImage[] walkDown = new GreenfootImage[6];
    
    int imageIndex = 0;
    SimpleTimer animationTimer = new SimpleTimer();
    
    String direction = "idle";
    
    public void act()
    {
        int x = getX();
        int y = getY();

        if (Greenfoot.isKeyDown("left")) {
            x -= 5;
            direction = "left";
        }
        else if (Greenfoot.isKeyDown("right")) {
            x += 5;
            direction = "right";
        }

        else if (Greenfoot.isKeyDown("up")) {
            y -= 5;
            direction = "up";
        }
        else if (Greenfoot.isKeyDown("down")) {
            y += 5;
            direction = "down";
        }
        else {
            direction = "idle";
        }

        setLocation(x, y);

        animate();
    }

    
    public void addedToWorld(World world) {
        int x = world.getWidth()/2;
        int y = world.getHeight()/2;
        setLocation(x, y);
    }
    
    public Hero() {
        for (int i = 0; i < idle.length; i++) {
            idle[i] = new GreenfootImage("images/heroFront_idle/idle" + i + ".png");
        }
        
        for (int i = 0; i < walkLeft.length; i++) {
            walkLeft[i] = new GreenfootImage("images/walkLeft/tile" + i + ".png");
        }
        
        for (int i = 0; i < walkRight.length; i++) {
            walkRight[i] = new GreenfootImage("images/walkRight/tile" + i + ".png");
        }
        
        for (int i = 0; i < walkUp.length; i++) {
            walkUp[i] = new GreenfootImage("images/walkUp/tile" + i + ".png");
        }
        
        for (int i = 0; i < walkDown.length; i++) {
            walkDown[i] = new GreenfootImage("images/walkDown/tile" + i + ".png");
        }
        
        setImage(idle[0]);
    }
    
    public void animate() {
        if(animationTimer.millisElapsed() < 200) {
            return;
        }
        animationTimer.mark();
        
        if(direction.equals("idle")) {
            setImage(idle[imageIndex]);
            imageIndex = (imageIndex + 1) % idle.length;
        }
        else if(direction.equals("left")) {
            setImage(walkLeft[imageIndex]);
            imageIndex = (imageIndex + 1) % walkLeft.length;
        }
        else if(direction.equals("right")) {
            setImage(walkRight[imageIndex]);
            imageIndex = (imageIndex + 1) % walkRight.length;
        }
        else if(direction.equals("up")) {
            setImage(walkUp[imageIndex]);
            imageIndex = (imageIndex + 1) % walkUp.length;
        }
        else if(direction.equals("down")) {
            setImage(walkDown[imageIndex]);
            imageIndex = (imageIndex + 1) % walkDown.length;
        }
    }
}
