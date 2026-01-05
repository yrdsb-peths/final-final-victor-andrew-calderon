import greenfoot.*;

public class KimJongUn extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[4];
    GreenfootImage[] walk = new GreenfootImage[4];

    int imageIndex = 0;

    SimpleTimer animationTimer = new SimpleTimer();
    SimpleTimer shootTimer = new SimpleTimer();
    SimpleTimer damageTimer = new SimpleTimer();

    int maxHP = 350;
    int currentHP = 350;
    int speed = 5;

    GreenfootSound kimVoice =
        new GreenfootSound("kim_jong_un_speech.wav");

    public KimJongUn()
    {
        for (int i = 0; i < 4; i++) {
            idle[i] = new GreenfootImage("images/kim_idle/tile" + i + ".png");
            idle[i].scale(90, 140);

            walk[i] = new GreenfootImage("images/kim_walk/tile" + i + ".png");
            walk[i].scale(90, 140);
        }

        setImage(idle[0]);
        kimVoice.setVolume(90);
    }

    public void addedToWorld(World w)
    {
        kimVoice.play();
    }

    public void act()
    {
        animate();
        moveTowardHero();
        shoot();
        checkHeroAttack();
        drawHPBar();
    }

    // ================= MOVEMENT =================
    private void moveTowardHero()
    {
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);
        if (hero == null) return;

        int dx = hero.getX() - getX();
        int dy = hero.getY() - getY();

        setLocation(
            getX() + Integer.signum(dx) * speed,
            getY() + Integer.signum(dy) * speed
        );
    }

    // ================= SHOOTING =================
    private void shoot()
    {
        if (shootTimer.millisElapsed() < 200) return;
        shootTimer.mark();

        Bullet b = new Bullet();
        getWorld().addObject(b, getX(), getY());
        b.turnTowards(
            getWorld().getObjects(Hero.class).get(0).getX(),
            getWorld().getObjects(Hero.class).get(0).getY()
        );
    }

    // ================= DAMAGE =================
    private void checkHeroAttack()
    {
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);
        if (hero == null) return;

        if (hero.attacking &&
            damageTimer.millisElapsed() > 400 &&
            Math.hypot(hero.getX()-getX(), hero.getY()-getY()) < 100)
        {
            currentHP -= 25;
            damageTimer.mark();

            if (currentHP <= 0) {
                kimVoice.stop();
                getWorld().removeObject(this);
            }
        }
    }

    // ================= ANIMATION =================
    private void animate()
    {
        if (animationTimer.millisElapsed() < 200) return;
        animationTimer.mark();

        setImage(walk[imageIndex]);
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
