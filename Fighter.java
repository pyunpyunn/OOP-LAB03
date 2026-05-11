/**
 * Represents a fighter in the Dice Battle Arena.
 * Each fighter has a name, HP, and attack power.
 */
public class Fighter {

    private String name;
    private int hp;
    private int attackPower;
    private boolean eliminated;

    // Constructor
    public Fighter(String name, int hp, int attackPower) {
        this.name        = name;
        this.hp          = hp;
        this.attackPower = attackPower;
        this.eliminated  = false;
    }

    // Check if the fighter is still alive
    public boolean isAlive() {
        return this.hp > 0 && !this.eliminated;
    }

    // Take damage and reduce HP
    public void takeDamage(int damage) {
        this.hp = this.hp - damage;
        if (this.hp <= 0) {
            this.hp        = 0;
            this.eliminated = true;
        }
    }

    // Getters
    public String  getName()        { return name; }
    public int     getHp()          { return hp; }
    public int     getAttackPower() { return attackPower; }
    public boolean isEliminated()   { return eliminated; }

    // Setters
    public void setName(String name)           { this.name = name; }
    public void setHp(int hp)                  { this.hp = hp; }
    public void setAttackPower(int ap)         { this.attackPower = ap; }
    public void setEliminated(boolean e)       { this.eliminated = e; }
}
