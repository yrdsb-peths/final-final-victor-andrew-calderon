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
    int damageCooldown = 400; // ms between hits

    // ================= CONSTRUCTOR =================
    public Xijinping() 
    {
        for (int i = 0; i < idle.length; i++) {
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

    // ================= ACT =================
    public void act()
    {
        animate();
        preventOverlap();
        checkHeroAttack();
        drawHPBar();

        if (state.equals("move")) {
            moveTowardHero();
        } 
        else if (state.equals("pause")) {
            autoShoot();
            if (stateTimer.millisElapsed() > 2000) {
                stateTimer.mark();
                state = "move";
            }
        }
    }

    // ================= MOVEMENT =================
    private void moveTowardHero()
    {
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);
        if (hero == null) return;

        int dx = hero.getX() - getX();
        int dy = hero.getY() - getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }

        int stopDistance = 120;
        if (Math.hypot(dx, dy) < stopDistance) {
            state = "pause";
            stateTimer.mark();
            return;
        }

        int stepX = Math.min(speed, Math.abs(dx)) * Integer.signum(dx);
        int stepY = Math.min(speed, Math.abs(dy)) * Integer.signum(dy);

        setLocation(getX() + stepX, getY() + stepY);
    }

    // ================= SHOOTING =================
    private void autoShoot() 
    {
        if (shootingTimer.millisElapsed() < 300) return;
        shootingTimer.mark();

        int baseRotation = getBulletRotation(direction);
        int[] spreadAngles = {-30, -15, 0, 15, 30};

        for (int angle : spreadAngles) {
            Bullet bullet = new Bullet();

            int bx = getX();
            int by = getY();

            switch (direction) {
                case "left":  bx -= 40; break;
                case "right": bx += 40; break;
                case "up":    by -= 40; break;
                case "down":  by += 40; break;
            }

            getWorld().addObject(bullet, bx, by);
            bullet.setRotation(baseRotation + angle);
        }
    }

    // ================= DAMAGE FROM HERO =================
    private void checkHeroAttack()
    {
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);
        if (hero == null) return;

        int attackRange = 100;

        double distance = Math.hypot(
            hero.getX() - getX(),
            hero.getY() - getY()
        );

        if (hero.attacking && distance <= attackRange &&
            damageTimer.millisElapsed() > damageCooldown)
        {
            currentHP -= 20;
            damageTimer.mark();

            if (currentHP <= 0) {
                currentHP = 0;
                getWorld().removeObject(this); // boss defeated
            }
        }
    }

    // ================= ANIMATION =================
    private void animate() 
    {
        if (animationTimer.millisElapsed() < 200) return;
        animationTimer.mark();

        switch (direction) {
            case "idle": setImage(idle[imageIndex]); break;
            case "left": setImage(walkLeft[imageIndex]); break;
            case "right": setImage(walkRight[imageIndex]); break;
            case "up": setImage(walkUp[imageIndex]); break;
            case "down": setImage(walkDown[imageIndex]); break;
        }

        imageIndex = (imageIndex + 1) % 4;
    }

    // ================= HP BAR =================
    private void drawHPBar()
    {
        GreenfootImage base = getImage();
        GreenfootImage img = new GreenfootImage(base);

        int barWidth = base.getWidth();
        int barHeight = 6;

        img.setColor(Color.RED);
        img.fillRect(0, 0, barWidth, barHeight);

        img.setColor(Color.GREEN);
        int hpWidth = (int)((currentHP / (double)maxHP) * barWidth);
        img.fillRect(0, 0, hpWidth, barHeight);

        setImage(img);
    }

    // ================= UTILS =================
    private int getBulletRotation(String dir) {
        switch(dir) {
            case "up": return 270;
            case "down": return 90;
            case "left": return 180;
            case "right": return 0;
            default: return 0;
        }
    }
    
    private void preventOverlap()
    {
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);
        if (hero == null) return;

        int minDistance = 85; // collision radius

        int dx = hero.getX() - getX();
        int dy = hero.getY() - getY();
        double distance = Math.hypot(dx, dy);

        if (distance < minDistance && distance > 0) {
            double push = minDistance - distance;

            int pushX = (int)(push * dx / distance);
            int pushY = (int)(push * dy / distance);

            hero.setLocation(
            hero.getX() + pushX,
            hero.getY() + pushY
            );
        }
    }

}
