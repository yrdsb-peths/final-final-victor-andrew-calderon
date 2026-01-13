import greenfoot.*;

public class Xijinping extends Actor
{
    // ===== IMAGES =====
    GreenfootImage[] idle = new GreenfootImage[4];
    GreenfootImage[] walkLeft = new GreenfootImage[4];
    GreenfootImage[] walkRight = new GreenfootImage[4];
    GreenfootImage[] walkUp = new GreenfootImage[4];
    GreenfootImage[] walkDown = new GreenfootImage[4];

    int imageIndex = 0;

    // ===== TIMERS =====
    SimpleTimer animationTimer = new SimpleTimer();
    SimpleTimer shootingTimer = new SimpleTimer();
    SimpleTimer stateTimer = new SimpleTimer();
    SimpleTimer damageTimer = new SimpleTimer();

    // ===== STATE =====
    String direction = "down";
    String state = "move"; // move = vulnerable, shoot = invincible

    int speed = 4;

    // ===== SMOOTH POSITION =====
    double preciseX;
    double preciseY;

    // ===== RANDOM MOVE TARGET =====
    int targetX;
    int targetY;
    boolean hasTarget = false;

    // ===== HP =====
    int maxHP = 250;
    int currentHP = 250;
    int damageCooldown = 400;

    // ===== SOUND =====
    GreenfootSound startSound = new GreenfootSound("game-start-317318.mp3");

    boolean dead = false;

    // ================= CONSTRUCTOR =================
    public Xijinping()
    {
        for (int i = 0; i < 4; i++)
        {
            idle[i] = new GreenfootImage("images/xijinping_idle/tile" + i + ".png");
            idle[i].scale(78, 131);

            walkLeft[i] = new GreenfootImage("images/xijinping_walkLeft/tile" + i + ".png");
            walkLeft[i].scale(78, 131);

            walkRight[i] = new GreenfootImage("images/xijinping_walkRight/tile" + i + ".png");
            walkRight[i].scale(78, 131);

            walkUp[i] = new GreenfootImage("images/xijinping_walkUp/tile" + i + ".png");
            walkUp[i].scale(78, 131);

            walkDown[i] = new GreenfootImage("images/xijinping_walkDown/tile" + i + ".png");
            walkDown[i].scale(78, 131);
        }

        setImage(idle[0]);
        shootingTimer.mark();
        stateTimer.mark();
        damageTimer.mark();
    }

    public void addedToWorld(World w)
    {
        preciseX = getX();
        preciseY = getY();

        startSound.setVolume(90);
        startSound.play();
    }

    // ================= ACT =================
    public void act()
    {
        if (dead) return;

        animate();

        if (state.equals("shoot"))
        {
            // Face hero during shooting
            faceHeroDirection();
            autoShoot();

            if (stateTimer.millisElapsed() > 3000)
            {
                state = "move";
                hasTarget = false;
                stateTimer.mark();
            }
        }
        else // MOVE STATE
        {
            // Move to random position (axis-aligned)
            moveAxisAligned();
            checkHeroAttack();

            // Set direction based on movement (not hero)
            setDirectionFacingMovement();

            if (stateTimer.millisElapsed() > 2500)
            {
                state = "shoot";
                stateTimer.mark();
            }
        }

        drawHPBar();
    }

    // ================= FACE HERO =================
    private void faceHeroDirection()
    {
        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        double dx = hero.getX() - getX();
        double dy = hero.getY() - getY();

        if (Math.abs(dx) > Math.abs(dy))
            direction = (dx > 0) ? "right" : "left";
        else
            direction = (dy > 0) ? "down" : "up";
    }

    // ================= AXIS-ALIGNED RANDOM MOVEMENT =================
    private void moveAxisAligned()
    {
        if (!hasTarget)
        {
            targetX = Greenfoot.getRandomNumber(getWorld().getWidth() - 100) + 50;
            targetY = Greenfoot.getRandomNumber(getWorld().getHeight() - 100) + 50;
            hasTarget = true;
        }

        double dx = targetX - preciseX;
        double dy = targetY - preciseY;

        // Move X first
        if (Math.abs(dx) > 1)
        {
            preciseX += Math.signum(dx) * speed;
        }
        // Then move Y
        else if (Math.abs(dy) > 1)
        {
            preciseY += Math.signum(dy) * speed;
        }

        setLocation((int)preciseX, (int)preciseY);
    }

    // ================= DIRECTION DURING MOVE =================
    private void setDirectionFacingMovement()
    {
        double dx = targetX - preciseX;
        double dy = targetY - preciseY;

        if (Math.abs(dx) > Math.abs(dy))
        {
            direction = (dx > 0) ? "right" : "left";
        }
        else if (Math.abs(dy) > 1)
        {
            direction = (dy > 0) ? "down" : "up";
        }
        // If very close to target, keep current direction
    }

    // ================= SHOOTING =================
    private void autoShoot()
    {
        if (shootingTimer.millisElapsed() < 400) return;
        shootingTimer.mark();

        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        int dx = hero.getX() - getX();
        int dy = hero.getY() - getY();

        int rotation = (int)Math.toDegrees(Math.atan2(dy, dx));

        Bullet b = new Bullet();
        getWorld().addObject(b, getX(), getY());
        b.setRotation(rotation);
    }

    // ================= DAMAGE =================
    private void checkHeroAttack()
    {
        if (state.equals("shoot")) return; // INVINCIBLE

        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        double d = Math.hypot(hero.getX() - getX(), hero.getY() - getY());

        if (hero.attacking && d <= 100 &&
            damageTimer.millisElapsed() > damageCooldown)
        {
            currentHP -= 20;
            damageTimer.mark();

            if (currentHP <= 0)
                onDeath();
        }
    }

    // ================= DEATH =================
    private void onDeath()
    {
        if (dead) return;
        dead = true;

        World w = getWorld();
        if (w == null) return;

        if (!w.getObjects(Hero.class).isEmpty())
        {
            Hero hero = w.getObjects(Hero.class).get(0);
            hero.currentHP = hero.maxHP;
        }

        if (w instanceof MyWorld)
            ((MyWorld) w).spawnKim(getX(), getY());

        startSound.stop();
        w.removeObject(this);
    }

    // ================= ANIMATION =================
    private void animate()
    {
        if (animationTimer.millisElapsed() < 200) return;
        animationTimer.mark();

        switch (direction)
        {
            case "left": setImage(walkLeft[imageIndex]); break;
            case "right": setImage(walkRight[imageIndex]); break;
            case "up": setImage(walkUp[imageIndex]); break;
            case "down": setImage(walkDown[imageIndex]); break;
            default: setImage(idle[imageIndex]);
        }

        imageIndex = (imageIndex + 1) % 4;
    }

    // ================= HP BAR =================
    private void drawHPBar()
    {
        GreenfootImage base = getImage();
        GreenfootImage img = new GreenfootImage(base);

        img.setColor(Color.RED);
        img.fillRect(0, 0, base.getWidth(), 6);

        img.setColor(Color.GREEN);
        int w = (int)((currentHP / (double)maxHP) * base.getWidth());
        img.fillRect(0, 0, w, 6);

        setImage(img);
    }
}
