import greenfoot.*;

public class Hero extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[10];
    GreenfootImage[] walkLeft = new GreenfootImage[6];
    GreenfootImage[] walkRight = new GreenfootImage[6];
    GreenfootImage[] walkUp = new GreenfootImage[6];
    GreenfootImage[] walkDown = new GreenfootImage[6];

    int imageIndex = 0;
    SimpleTimer animationTimer = new SimpleTimer();
    
    SimpleTimer damageTimer = new SimpleTimer();
    int damageCooldown = 500; // milliseconds

    String direction = "idle";
    String lastDirection = "idle"; // Track previous direction

    // HP fields
    int maxHP = 100;
    int currentHP = 100;

    public Hero() {
        // Load idle images
        for (int i = 0; i < idle.length; i++) {
            idle[i] = new GreenfootImage("images/idle/tile" + i + ".png");
            idle[i].scale(60, 75);
        }

        // Load walkLeft images
        for (int i = 0; i < walkLeft.length; i++) {
            walkLeft[i] = new GreenfootImage("images/walkLeft/tile" + i + ".png");
            walkLeft[i].scale(60, 75);
        }

        // Load walkRight images
        for (int i = 0; i < walkRight.length; i++) {
            walkRight[i] = new GreenfootImage("images/walkRight/tile" + i + ".png");
            walkRight[i].scale(60, 75);
        }

        // Load walkUp images
        for (int i = 0; i < walkUp.length; i++) {
            walkUp[i] = new GreenfootImage("images/walkUp/tile" + i + ".png");
            walkUp[i].scale(60, 75);
        }

        // Load walkDown images
        for (int i = 0; i < walkDown.length; i++) {
            walkDown[i] = new GreenfootImage("images/walkDown/tile" + i + ".png");
            walkDown[i].scale(60, 75);
        }

        setImage(idle[0]);
    }

    public void addedToWorld(World world) {
        int x = world.getWidth() / 2;
        int y = world.getHeight() / 2;
        setLocation(x, y);
    }

    public void act() {
        movePlayer();
        checkBulletHit();
        animate();
        drawHPBar();
    }

    private void movePlayer() {
        int x = getX();
        int y = getY();

        boolean moved = false;

        if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
            x -= 5;
            direction = "left";
            moved = true;
        }
        if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
            x += 5;
            direction = "right";
            moved = true;
        }
        if (Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("w")) {
            y -= 5;
            direction = "up";
            moved = true;
        }
        if (Greenfoot.isKeyDown("down") || Greenfoot.isKeyDown("s")) {
            y += 5;
            direction = "down";
            moved = true;
        }

        if (!moved) {
            direction = "idle";
        }

        setLocation(x, y);
    }

    private void animate() {
        if (animationTimer.millisElapsed() < 200) return;

        if (!direction.equals(lastDirection)) {
            imageIndex = 0;
            lastDirection = direction;
        }

        animationTimer.mark();

        switch (direction) {
            case "idle":
                setImage(idle[imageIndex]);
                imageIndex = (imageIndex + 1) % idle.length;
                break;
            case "left":
                setImage(walkLeft[imageIndex]);
                imageIndex = (imageIndex + 1) % walkLeft.length;
                break;
            case "right":
                setImage(walkRight[imageIndex]);
                imageIndex = (imageIndex + 1) % walkRight.length;
                break;
            case "up":
                setImage(walkUp[imageIndex]);
                imageIndex = (imageIndex + 1) % walkUp.length;
                break;
            case "down":
                setImage(walkDown[imageIndex]);
                imageIndex = (imageIndex + 1) % walkDown.length;
                break;
        }
    }
    
    private void checkBulletHit() {
        Bullet bullet = (Bullet) getOneIntersectingObject(Bullet.class);

        if (bullet != null && damageTimer.millisElapsed() > damageCooldown) {
            takeDamage(10);
            damageTimer.mark();
            getWorld().removeObject(bullet);
        }
    }
    
    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP < 0) currentHP = 0;

        if (currentHP == 0) {
            Greenfoot.stop(); // or trigger game over
        }
    }

    
    // Draw a health bar above the hero
    private void drawHPBar() {
        GreenfootImage image = getImage();
        int barWidth = image.getWidth();
        int barHeight = 5;

        // Create a copy of current image to draw on
        GreenfootImage newImage = new GreenfootImage(image);

        // Draw background (red)
        newImage.setColor(Color.RED);
        newImage.fillRect(0, 0, barWidth, barHeight);

        // Draw current HP (green)
        int hpWidth = (int) ((currentHP / (double) maxHP) * barWidth);
        newImage.setColor(Color.GREEN);
        newImage.fillRect(0, 0, hpWidth, barHeight);

        setImage(newImage);
    }
}
