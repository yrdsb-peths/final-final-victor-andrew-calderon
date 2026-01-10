import greenfoot.*;

public class Hero extends Actor
{
    // ===== IMAGES =====
    GreenfootImage[] idle = new GreenfootImage[10];
    GreenfootImage[][] walk = new GreenfootImage[4][6];
    GreenfootImage[][] attack = new GreenfootImage[4][6];

    int imageIndex = 0;

    // ===== TIMERS =====
    SimpleTimer animationTimer = new SimpleTimer();
    SimpleTimer attackTimer = new SimpleTimer();
    SimpleTimer damageTimer = new SimpleTimer();
    SimpleTimer invincibleTimer = new SimpleTimer();

    // ===== STATE =====
    boolean attacking = false;
    boolean invincible = false;

    int attackDuration = 400;
    int shieldDuration = 0;   // ⭐ FIX: shield duration
    String direction = "down", lastDirection = "down";

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
        loadImages("idle", idle, 60, 75);
        String[] dirs = {"Left","Right","Up","Down"};
        for (int d = 0; d < 4; d++)
        {
            loadImages("walk" + dirs[d], walk[d], 60, 75);
            loadImages("attack" + dirs[d], attack[d], 70, 70);
        }
        setImage(idle[0]);
    }

    private void loadImages(String folder, GreenfootImage[] arr, int w, int h)
    {
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = new GreenfootImage("images/" + folder + "/tile" + i + ".png");
            arr[i].scale(w, h);
        }
    }

    // ================= ADDED =================
    public void addedToWorld(World w)
    {
        setLocation(w.getWidth() / 2, w.getHeight() / 2);
        w.addObject(new HeroHPBar(this), getX(), getY() - 45);

        // Spawn immunity
        activateShield(1000);
    }

    // ================= ACT =================
    public void act()
    {
        // Handle invincibility timing
        if (invincible && invincibleTimer.millisElapsed() > shieldDuration)
        {
            invincible = false;
            shieldDuration = 0;
        }

        handleAttackInput();
        movePlayer();
        checkBulletHit();
        animate();

        if (currentHP <= 0)
            checkDeath();
    }

    // ================= INPUT =================
    private void handleAttackInput()
    {
        if (Greenfoot.isKeyDown("space") && !attacking)
        {
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
        int dx = 0, dy = 0;

        if (Greenfoot.isKeyDown("a") || Greenfoot.isKeyDown("left")) dx--;
        if (Greenfoot.isKeyDown("d") || Greenfoot.isKeyDown("right")) dx++;
        if (Greenfoot.isKeyDown("w") || Greenfoot.isKeyDown("up")) dy--;
        if (Greenfoot.isKeyDown("s") || Greenfoot.isKeyDown("down")) dy++;

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
            lastDirection = direction;
        }
    }

    // ================= ANIMATION =================
    private void animate()
    {
        if (animationTimer.millisElapsed() < 100) return;
        animationTimer.mark();

        if (attacking)
        {
            setImage(getDirImage(attack, lastDirection, imageIndex++));
            if (attackTimer.millisElapsed() > attackDuration)
            {
                attacking = false;
                imageIndex = 0;
            }
            return;
        }

        setImage(getDirImage(walk, direction, imageIndex++));
    }

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
    }

    // ================= DAMAGE =================
    private void checkBulletHit()
    {
        if (invincible) return;
        if (damageTimer.millisElapsed() < damageCooldown) return;

        Bullet b = (Bullet)getOneIntersectingObject(Bullet.class);
        if (b != null)
        {
            currentHP -= 8;
            damageTimer.mark();
            getWorld().removeObject(b);

            activateShield(500); // brief hit invincibility

            if (currentHP < 0) currentHP = 0;
        }
    }

    // ================= DEATH =================
    private void checkDeath()
    {
        World w = getWorld();
        if (w instanceof MyWorld)
            ((MyWorld)w).triggerGameOver();

        w.removeObject(this);
    }
    
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
}

}
