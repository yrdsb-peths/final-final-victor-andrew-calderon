import greenfoot.*;

public class VladimirPutin extends Actor
{
    /**
     * Act - do whatever the VladimirPutin wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
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
    SimpleTimer spawnTimer = new SimpleTimer(); // spawn grace period

    // ===== STATE =====
    String direction = "idle";
    String state = "move";

    int speed = 4;

    // ===== HP (MATCH XI) =====
    int maxHP = 250;
    int currentHP = 250;
    int damageCooldown = 400;

    // ===== SOUND =====
    GreenfootSound bossSound = new GreenfootSound("game-start-317318.mp3");

    boolean dead = false;

    // ================= CONSTRUCTOR =================
    public VladimirPutin()
    {
        for (int i = 0; i < 4; i++)
        {
            idle[i] = new GreenfootImage("images/vladimirputin_idle/tile" + i + ".png");
            idle[i].scale(95, 145);

            walkLeft[i] = new GreenfootImage("images/vladimirputin_walkLeft/tile" + i + ".png");
            walkLeft[i].scale(95, 145);

            walkRight[i] = new GreenfootImage("images/vladimirputin_walkRight/tile" + i + ".png");
            walkRight[i].scale(95, 145);

            walkUp[i] = new GreenfootImage("images/vladimirputin_walkUp/tile" + i + ".png");
            walkUp[i].scale(95, 145);

            walkDown[i] = new GreenfootImage("images/vladimirputin_walkDown/tile" + i + ".png");
            walkDown[i].scale(95, 145);
        }

        setImage(idle[0]);

        shootingTimer.mark();
        stateTimer.mark();
        damageTimer.mark();
        spawnTimer.mark();

        bossSound.setVolume(90);
    }

    public void addedToWorld(World w)
    {
        bossSound.play();
        
        if (!w.getObjects(Hero.class).isEmpty())
        {
            Hero hero = w.getObjects(Hero.class).get(0);
            hero.refillHP();
        }
    }

    // ================= ACT =================
    public void act()
    {
        // Add your action code here.
        if (dead) return;

        animate();
        preventOverlap();

        // Grace period after spawn
        if (spawnTimer.millisElapsed() < 800) return;

        checkHeroAttack();
        drawHPBar();

        if (state.equals("move"))
            moveTowardHero();
        else
        {
            autoShoot();
            if (stateTimer.millisElapsed() > 2000)
            {
                state = "move";
                stateTimer.mark();
            }
        }
    }

    // ================= MOVEMENT =================
    private void moveTowardHero()
    {
        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        int dx = hero.getX() - getX();
        int dy = hero.getY() - getY();

        direction = Math.abs(dx) > Math.abs(dy)
                ? (dx > 0 ? "right" : "left")
                : (dy > 0 ? "down" : "up");

        if (Math.hypot(dx, dy) < 120)
        {
            state = "pause";
            stateTimer.mark();
            return;
        }

        setLocation(
            getX() + Math.min(speed, Math.abs(dx)) * Integer.signum(dx),
            getY() + Math.min(speed, Math.abs(dy)) * Integer.signum(dy)
        );
    }

    // ================= SHOOTING =================
    private void autoShoot()
    {
        if (shootingTimer.millisElapsed() < 300) return;
        shootingTimer.mark();

        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        int baseRotation = getBulletRotation(direction);
        int[] spread = {-30, -15, 0, 15, 30};

        for (int angle : spread)
        {
            Bullet b = new Bullet();

            int bx = getX() + (int)(Math.cos(Math.toRadians(baseRotation)) * 40);
            int by = getY() + (int)(Math.sin(Math.toRadians(baseRotation)) * 40);

            getWorld().addObject(b, bx, by);
            b.setRotation(baseRotation + angle);
        }
    }

    // ================= DAMAGE =================
    private void checkHeroAttack()
    {
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

        bossSound.stop();
        World w = getWorld();
        if (w != null) w.removeObject(this);
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

    // ================= PREVENT OVERLAP =================
    private void preventOverlap()
    {
        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        double d = Math.hypot(hero.getX() - getX(), hero.getY() - getY());
        if (d < 85 && d > 0)
        {
            int px = (int)((hero.getX() - getX()) / d * (85 - d));
            int py = (int)((hero.getY() - getY()) / d * (85 - d));
            hero.setLocation(hero.getX() + px, hero.getY() + py);
        }
    }

    private int getBulletRotation(String dir)
    {
        switch (dir)
        {
            case "up": return 270;
            case "down": return 90;
            case "left": return 180;
            case "right": return 0;
            default: return 0;
        }
    }
}