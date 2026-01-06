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
    String direction = "idle";
    String state = "move";

    int speed = 4;

    // ===== HP =====
    int maxHP = 250;
    int currentHP = 250;
    int damageCooldown = 400;

    // ===== SOUND =====
    GreenfootSound startSound = new GreenfootSound("game-start-317318.mp3");

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

    // ================= START SOUND =================
    public void addedToWorld(World w)
    {
        startSound.setVolume(90);
        startSound.play();
    }

    // ================= ACT =================
    public void act()
    {
        animate();
        preventOverlap();
        checkHeroAttack();
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
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);

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
        if (shootingTimer.millisElapsed() < 300) return;
        shootingTimer.mark();

        int baseRotation = getBulletRotation(direction);
        int[] spread = {-30, -15, 0, 15, 30};

        for (int angle : spread)
        {
            Bullet b = new Bullet();
            getWorld().addObject(b, getX(), getY());
            b.setRotation(baseRotation + angle);
        }
    }

    // ================= DAMAGE =================
    private void checkHeroAttack()
    {
        if (getWorld().getObjects(Hero.class).isEmpty()) return;
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);

        double d = Math.hypot(hero.getX() - getX(), hero.getY() - getY());

        if (hero.attacking && d <= 100 &&
            damageTimer.millisElapsed() > damageCooldown)
        {
            currentHP -= 20;
            damageTimer.mark();

            if (currentHP <= 0)
                getWorld().removeObject(this);
        }
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
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);

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
