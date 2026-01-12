import greenfoot.*;

public class KimJongUn extends Enemy
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
    String direction = "idle";
    String state = "move";

    int speed = 5;

    // ===== HP =====
    int maxHP = 350;
    int currentHP = 350;
    int damageCooldown = 400;

    // ===== SOUND =====
    GreenfootSound kimVoice = new GreenfootSound("game-start-317318.mp3");

    // ===== DEAD FLAG =====
    boolean dead = false;

    // ================= CONSTRUCTOR =================
    public KimJongUn()
    {
        for (int i = 0; i < 4; i++)
        {
            idle[i] = new GreenfootImage("images/kimjongun_idle/tile" + i + ".png");
            idle[i].scale(90, 140);

            walkLeft[i] = new GreenfootImage("images/kimjongun_walkLeft/tile" + i + ".png");
            walkLeft[i].scale(90, 140);

            walkRight[i] = new GreenfootImage("images/kimjongun_walkRight/tile" + i + ".png");
            walkRight[i].scale(90, 140);

            walkUp[i] = new GreenfootImage("images/kimjongun_walkUp/tile" + i + ".png");
            walkUp[i].scale(90, 140);

            walkDown[i] = new GreenfootImage("images/kimjongun_walkDown/tile" + i + ".png");
            walkDown[i].scale(90, 140);
        }

        setImage(idle[0]);
        shootingTimer.mark();
        stateTimer.mark();
        damageTimer.mark();
        kimVoice.setVolume(90);
    }

    // ================= START SOUND =================
    public void addedToWorld(World w)
    {
        kimVoice.play();
    }

    // ================= ACT =================
    public void act()
    {
        if (dead) return; // stop if dead

        animate();
        preventOverlap();
        checkHeroAttack();
        if (dead) return; // stop immediately if died

        drawHPBar();

        if (state.equals("move"))
            moveTowardHero();
        else
        {
            autoShoot();
            if (stateTimer.millisElapsed() > 2000)
            {
                stateTimer.mark();
                state = "move";
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

        if (Math.abs(dx) > Math.abs(dy))
            direction = (dx > 0) ? "right" : "left";
        else
            direction = (dy > 0) ? "down" : "up";

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
        if (shootingTimer.millisElapsed() < 250) return;
        shootingTimer.mark();

        int baseRotation = getBulletRotation(direction);
        int[] spread = {-40, -20, 0, 20, 40};

        for (int angle : spread)
        {
            Bullet b = new Bullet();
            getWorld().addObject(b, getX(), getY());
            if (!getWorld().getObjects(Hero.class).isEmpty())
            {
                Hero hero = getWorld().getObjects(Hero.class).get(0);
                b.turnTowards(hero.getX(), hero.getY());
            }
        }
    }

    // ================= DAMAGE =================
    private void checkHeroAttack()
    {
        if (dead) return; // stop if dead
        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = getWorld().getObjects(Hero.class).get(0);

        double d = Math.hypot(hero.getX() - getX(), hero.getY() - getY());

        if (hero.attacking && d <= 100 &&
            damageTimer.millisElapsed() > damageCooldown)
        {
            currentHP -= 25;
            damageTimer.mark();

            if (currentHP <= 0)
            {
                onDeath();
                return;
            }
        }
    }

    // ================= DEATH EVENT =================
    private void onDeath()
    {
        if (dead) return;
        dead = true;

        World w = getWorld();
        if (w == null) return;

        // Refill Hero HP
        if (!w.getObjects(Hero.class).isEmpty())
        {
            Hero hero = w.getObjects(Hero.class).get(0);
            hero.currentHP = hero.maxHP;
        }

        // Spawn Vladimir Putin
        VladimirPutin putin = new VladimirPutin();
        w.addObject(putin, getX(), getY());

        kimVoice.stop();
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
