import greenfoot.*;

public class MyWorld extends World
{
    // ================= CONSTANTS =================
    private static final int WORLD_WIDTH = 900;
    private static final int WORLD_HEIGHT = 600;

    // ================= GAME STATE =================
    public enum GameState
    {
        PLAYING,
        WIN,
        LOSE
    }

    private GameState gameState = GameState.PLAYING;

    // ================= SOUNDS =================
    private GreenfootSound winSound;
    private GreenfootSound gameOverSound;

    // ================= FLAGS =================
    private boolean kimSpawned = false;
    private boolean vladimirSpawned = false;

    public MyWorld()
    {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);
        setupWorld();
        setupSounds();
    }

    // ================= SETUP =================

    private void setupWorld()
    {
        setBackground(new GreenfootImage("images/dungeon.jpg"));

        addObject(new Hero(), WORLD_WIDTH / 2, 200);
        addObject(new Xijinping(), WORLD_WIDTH / 2, 150);
    }

    private void setupSounds()
    {
        winSound = new GreenfootSound("winner-game-sound-404167.mp3");
        gameOverSound = new GreenfootSound("game-over-417465.mp3");

        winSound.setVolume(90);
        gameOverSound.setVolume(90);
    }

    // ================= STATE TRIGGERS =================

    public void triggerGameOver()
    {
        changeState(GameState.LOSE);
        Greenfoot.setWorld(new GameOverWorld());
        gameOverSound.play();
    }

    public void triggerWin()
    {
        changeState(GameState.WIN);
        Greenfoot.setWorld(new WinWorld());
        winSound.play();
    }

    private void changeState(GameState newState)
    {
        if (gameState != GameState.PLAYING) return;

        gameState = newState;
        clearBullets();
    }

    // ================= BOSS SPAWN =================

    public void spawnKim(int x, int y)
    {
        if (kimSpawned) return;

        kimSpawned = true;
        addObject(new KimJongUn(), x, y);
    }
    
    public void spawnVladimir(int x, int y)
    {
        if (vladimirSpawned) return;
        vladimirSpawned = true;
        addObject(new VladimirPutin(), x, y);
    }

    // ================= HELPERS =================

    private void clearBullets()
    {
        removeObjects(getObjects(Bullet.class));
    }
}
