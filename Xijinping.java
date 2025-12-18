import greenfoot.*;

public class Xijinping extends Actor
{
    GreenfootImage[] idle = new GreenfootImage[4];
    GreenfootImage[] walkLeft = new GreenfootImage[4];
    GreenfootImage[] walkRight = new GreenfootImage[4];
    GreenfootImage[] walkUp = new GreenfootImage[4];
    GreenfootImage[] walkDown = new GreenfootImage[4];

    int imageIndex = 0;
    SimpleTimer animationTimer = new SimpleTimer();
    SimpleTimer shootingTimer = new SimpleTimer();
    SimpleTimer stateTimer = new SimpleTimer();

    String direction = "idle";
    String state = "move"; // states: move, pause

    int moveDistance = 0; // distance moved in current move state
    int moveLimit = 200;   // move 200 pixels before pausing
    
    int[][] waypoints = {
    {200, 150},
    {500, 150},
    {500, 400},
    {200, 400}
    };

    int currentWaypoint = 0;
    int speed = 4;


    public Xijinping() 
    {
        // Load idle images
        for (int i = 0; i < idle.length; i++) {
            idle[i] = new GreenfootImage("images/xijinping_idle/tile" + i + ".png");
            idle[i].scale(78, 131);
        }
        for (int i = 0; i < walkLeft.length; i++) {
            walkLeft[i] = new GreenfootImage("images/xijinping_walkLeft/tile" + i + ".png");
            walkLeft[i].scale(78, 131);
        }
        for (int i = 0; i < walkRight.length; i++) {
            walkRight[i] = new GreenfootImage("images/xijinping_walkRight/tile" + i + ".png");
            walkRight[i].scale(78, 131);
        }
        for (int i = 0; i < walkUp.length; i++) {
            walkUp[i] = new GreenfootImage("images/xijinping_walkUp/tile" + i + ".png");
            walkUp[i].scale(78, 131);
        }
        for (int i = 0; i < walkDown.length; i++) {
            walkDown[i] = new GreenfootImage("images/xijinping_walkDown/tile" + i + ".png");
            walkDown[i].scale(78, 131);
        }

        setImage(idle[0]);
        shootingTimer.mark();
        stateTimer.mark();

        chooseNewDirection(); // start with a random direction
    }

    public void act()
    {
        animate();
        if (state.equals("move")) {
            moveTowardHero();
        } else if (state.equals("pause")) {
            autoShoot();
            // stay paused for 2 second
            if (stateTimer.millisElapsed() > 2000) {
                stateTimer.mark();
                state = "move";
                currentWaypoint = (currentWaypoint + 1) % waypoints.length;
            }
        }
    }

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

    private void moveStep() {
        int x = getX();
        int y = getY();

        int step = 5; // 5 pixels per act

        switch (direction) {
            case "up": y -= step; moveDistance += step; break;
            case "down": y += step; moveDistance += step; break;
            case "left": x -= step; moveDistance += step; break;
            case "right": x += step; moveDistance += step; break;
        }

        // Keep inside world bounds
        World world = getWorld();
        if (x < 0) x = 0;
        if (x > world.getWidth() - 1) x = world.getWidth() - 1;
        if (y < 0) y = 0;
        if (y > world.getHeight() - 1) y = world.getHeight() - 1;

        setLocation(x, y);

        // Check if moved enough
        if (moveDistance >= moveLimit) {
            state = "pause";
            stateTimer.mark();
        }
    }

    private void autoShoot() 
    {
        if (shootingTimer.millisElapsed() < 300) return;

        shootingTimer.mark();

        int baseRotation = getBulletRotation(direction);

        // Spread angles (degrees)
        int[] spreadAngles = {-45, -30, -15, 0, 15, 30, 45}; // bullet spread

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
    
    private void moveTowardHero()
    {
        Hero hero = (Hero)getWorld().getObjects(Hero.class).get(0);
        if (hero == null) return;

        int dx = hero.getX() - getX();
        int dy = hero.getY() - getY();

        // Face hero
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }

        // Stop when close enough
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



    private int getBulletRotation(String dir) {
        switch(dir) {
            case "up": return 270;
            case "down": return 90;
            case "left": return 180;
            case "right": return 0;
            default: return 0;
        }
    }

    private void chooseNewDirection() {
        String[] dirs = {"up", "down", "left", "right"};
        direction = dirs[Greenfoot.getRandomNumber(dirs.length)];
    }
    
    private void moveToWaypoint()
    {
        int targetX = waypoints[currentWaypoint][0];
        int targetY = waypoints[currentWaypoint][1];

        int dx = targetX - getX();
        int dy = targetY - getY();

        // Decide direction (for animation + bullets)
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = (dx > 0) ? "right" : "left";
        } else {
            direction = (dy > 0) ? "down" : "up";
        }

        // Move toward target
        int stepX = Math.min(speed, Math.abs(dx)) * Integer.signum(dx);
        int stepY = Math.min(speed, Math.abs(dy)) * Integer.signum(dy);

        setLocation(getX() + stepX, getY() + stepY);

        // Check if arrived
        if (Math.abs(dx) <= speed && Math.abs(dy) <= speed) {
            setLocation(targetX, targetY);
            state = "pause";
            stateTimer.mark();
        }
    }

}
