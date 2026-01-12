import greenfoot.*;

public class Hero extends Actor
{
    // ===== IMAGES =====
    GreenfootImage[] idle = new GreenfootImage[10];
    GreenfootImage[] walkLeft = new GreenfootImage[6];
    GreenfootImage[] walkRight = new GreenfootImage[6];
    GreenfootImage[] walkUp = new GreenfootImage[6];
    GreenfootImage[] walkDown = new GreenfootImage[6];

    GreenfootImage[] attackLeft = new GreenfootImage[6];
    GreenfootImage[] attackRight = new GreenfootImage[6];
    GreenfootImage[] attackUp = new GreenfootImage[6];
    GreenfootImage[] attackDown = new GreenfootImage[6];

    int imageIndex = 0;

    // ===== TIMERS =====
    SimpleTimer animationTimer = new SimpleTimer();
    SimpleTimer attackTimer = new SimpleTimer();
    SimpleTimer damageTimer = new SimpleTimer();

    // ===== STATE =====
    boolean attacking = false;
    int attackDuration = 400;

    int shieldDuration = 0;   // ⭐ FIX: shield duration
    String direction = "down", lastDirection = "down";


    String direction = "down";
    String lastDirection = "down";

    // ===== HP =====
    public int maxHP = 100;
    public int currentHP = 100;
    int damageCooldown = 500;

    // ===== MOVEMENT =====
    int moveSpeed = 5;
    int attackMoveSpeed = 2;

    // ===== SOUND =====
    GreenfootSound attackSound = new GreenfootSound("sword-slice-393847.mp3");

    // ================= CONSTRUCTOR =================
    public Hero()
    {
        for (int i = 0; i < idle.length; i++) {
            idle[i] = new GreenfootImage("images/idle/tile" + i + ".png");
            idle[i].scale(60, 75);
        }

        for (int i = 0; i < walkLeft.length; i++) {
            walkLeft[i] = new GreenfootImage("images/walkLeft/tile" + i + ".png");
            walkLeft[i].scale(60, 75);

            walkRight[i] = new GreenfootImage("images/walkRight/tile" + i + ".png");
            walkRight[i].scale(60, 75);

            walkUp[i] = new GreenfootImage("images/walkUp/tile" + i + ".png");
            walkUp[i].scale(60, 75);

            walkDown[i] = new GreenfootImage("images/walkDown/tile" + i + ".png");
            walkDown[i].scale(60, 75);
        }

        for (int i = 0; i < attackLeft.length; i++) {
            attackLeft[i] = new GreenfootImage("images/attackLeft/tile" + i + ".png");
            attackLeft[i].scale(70, 70);

            attackRight[i] = new GreenfootImage("images/attackRight/tile" + i + ".png");
            attackRight[i].scale(70, 70);

            attackUp[i] = new GreenfootImage("images/attackUp/tile" + i + ".png");
            attackUp[i].scale(70, 70);

            attackDown[i] = new GreenfootImage("images/attackDown/tile" + i + ".png");
            attackDown[i].scale(70, 70);
        }

        setImage(idle[0]);
    }

    // ================= WORLD =================
    public void addedToWorld(World w)
    {
        setLocation(w.getWidth() / 2, w.getHeight() / 2);
        w.addObject(new HeroHPBar(this), getX(), getY() - 45);
<<<<<<< HEAD

        // Spawn immunity
        activateShield(1000);
=======
>>>>>>> parent of e30cce3 (code optimization)
    }

    // ================= ACT =================
    public void act()
    {
<<<<<<< HEAD
        // Handle invincibility timing
        if (invincible && invincibleTimer.millisElapsed() > shieldDuration)
        {
            invincible = false;
            shieldDuration = 0;
        }

=======
>>>>>>> parent of e30cce3 (code optimization)
        handleAttackInput();
        movePlayer();
        checkBulletHit();
        animate();
        checkDeath();
    }

    // ================= INPUT =================
    private void handleAttackInput()
    {
        if (Greenfoot.isKeyDown("space") && !attacking) {
            attacking = true;
            attackTimer.mark();
            imageIndex = 0;

            attackSound.stop();
            attackSound.play();
        }
    }

    // ================= MOVEMENT =================
    private void movePlayer()
    {
        int speed = attacking ? attackMoveSpeed : moveSpeed;

        int dx = 0;
        int dy = 0;

        if (Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left"))  dx--;
        if (Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right")) dx++;
        if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up"))    dy--;
        if (Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down"))  dy++;

<<<<<<< HEAD
        if (dx != 0 || dy != 0)
        {
            double len = Math.sqrt(dx * dx + dy * dy);
            dx = (int)(dx / len * speed);
            dy = (int)(dy / len * speed);

            World w = getWorld();
            if (w != null)
            {
                int hw = getImage().getWidth() / 2;
                int hh = getImage().getHeight() / 2;

                setLocation(
                    Math.max(hw, Math.min(w.getWidth() - hw, getX() + dx)),
                    Math.max(hh, Math.min(w.getHeight() - hh, getY() + dy))
                );
            }

            direction = Math.abs(dx) > Math.abs(dy)
                    ? (dx > 0 ? "right" : "left")
                    : (dy > 0 ? "down" : "up");
=======
        if (dx != 0 || dy != 0) {
            double len = Math.sqrt(dx*dx + dy*dy);
            dx = (int)(dx / len * speed);
            dy = (int)(dy / len * speed);

            int newX = getX() + dx;
            int newY = getY() + dy;

            // Prevent going off the world
            World w = getWorld();
            if (w != null) {
                int halfWidth = getImage().getWidth() / 2;
                int halfHeight = getImage().getHeight() / 2;

                newX = Math.max(halfWidth, Math.min(w.getWidth() - halfWidth, newX));
                newY = Math.max(halfHeight, Math.min(w.getHeight() - halfHeight, newY));
            }

            setLocation(newX, newY);

            // Update direction
            if (Math.abs(dx) > Math.abs(dy))
                direction = (dx > 0) ? "right" : "left";
            else
                direction = (dy > 0) ? "down" : "up";

>>>>>>> parent of e30cce3 (code optimization)
            lastDirection = direction;
        }
    }


    // ================= ANIMATION =================
    private void animate()
    {
        if (animationTimer.millisElapsed() < 100) return;
        animationTimer.mark();

        if (attacking) {
            switch (lastDirection) {
                case "left":  setImage(attackLeft[imageIndex % attackLeft.length]); break;
                case "right": setImage(attackRight[imageIndex % attackRight.length]); break;
                case "up":    setImage(attackUp[imageIndex % attackUp.length]); break;
                case "down":  setImage(attackDown[imageIndex % attackDown.length]); break;
            }

            imageIndex++;

            if (attackTimer.millisElapsed() > attackDuration) {
                attacking = false;
                imageIndex = 0;
            }
            return;
        }

        switch (direction) {
            case "left":  setImage(walkLeft[imageIndex % walkLeft.length]); break;
            case "right": setImage(walkRight[imageIndex % walkRight.length]); break;
            case "up":    setImage(walkUp[imageIndex % walkUp.length]); break;
            case "down":  setImage(walkDown[imageIndex % walkDown.length]); break;
            default:      setImage(idle[imageIndex % idle.length]); break;
        }

<<<<<<< HEAD
    private GreenfootImage getDirImage(GreenfootImage[][] arr, String dir, int idx)
    {
        int d = switch (dir)
        {
            case "left" -> 0;
            case "right" -> 1;
            case "up" -> 2;
            default -> 3;
        };
        return arr[d][idx % arr[d].length];
=======
        imageIndex++;
>>>>>>> parent of e30cce3 (code optimization)
    }

    // ================= DAMAGE =================
    private void checkBulletHit()
    {
<<<<<<< HEAD
        if (invincible) return;
        if (damageTimer.millisElapsed() < damageCooldown) return;

        Bullet b = (Bullet)getOneIntersectingObject(Bullet.class);
        if (b != null)
        {
=======
        Bullet bullet = (Bullet)getOneIntersectingObject(Bullet.class);

        if (bullet != null && damageTimer.millisElapsed() > damageCooldown) {
>>>>>>> parent of e30cce3 (code optimization)
            currentHP -= 8;
            damageTimer.mark();
            getWorld().removeObject(bullet);

<<<<<<< HEAD
            activateShield(500); // brief hit invincibility
=======
            if (currentHP <= 0) {
                currentHP = 0;
>>>>>>> parent of e30cce3 (code optimization)

                World w = getWorld();
                if (w != null) {
                    // Remove Hero
                    w.removeObject(this);

                    // Remove Xi Jinping
                    for (Xijinping xi : w.getObjects(Xijinping.class)) {
                        w.removeObject(xi);
                    }

                    // Remove all bullets
                    for (Bullet b : w.getObjects(Bullet.class)) {
                        w.removeObject(b);
                    }
                }

                // Stop the game if you want
                Greenfoot.stop();
            }
        }
    }
    
    private void checkDeath()
{
    if (currentHP <= 0)
    {
        World w = getWorld();
        if (w instanceof MyWorld)
        {
            ((MyWorld)w).triggerGameOver();
        }

        // DO NOT call getWorld() again
        if (getWorld() != null)
        {
            w.removeObject(this);
        }
    }
<<<<<<< HEAD
    
    public void heal(int amount)
{
    currentHP = Math.min(maxHP, currentHP + amount);
}

    // ================= POWER UPS =================
    public void activateShield(int duration)
    {
        invincible = true;
        shieldDuration = duration;
        invincibleTimer.mark();
    }

    public void refillHP()
    {
        currentHP = maxHP;
    }
    
    boolean shielded = false;
SimpleTimer shieldTimer = new SimpleTimer();


public boolean isShielded()
{
    if (shielded && shieldTimer.millisElapsed() > 5000)
        shielded = false;

    return shielded;
=======
>>>>>>> parent of e30cce3 (code optimization)
}

}
