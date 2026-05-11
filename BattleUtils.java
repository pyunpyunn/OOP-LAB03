import java.util.Random;

/**
 * Utility class for the Dice Battle Arena.
 * Contains overloaded methods for rolling attacks and formatting combat messages.
 * All dice rolls are standard 6-sided dice (1 to 6).
 */
public class BattleUtils {

    // ANSI codes - minimal
    static final String RESET  = "\u001B[0m";
    static final String BOLD   = "\u001B[1m";
    static final String RED    = "\u001B[31m";
    static final String GREEN  = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";

    // Single dark border color used for ALL borders (dark blue)
    static final String BORDER = "\u001B[34m";

    // Box: inner width = 53 chars, total line = 55 chars  (| + 53 + |)
    static final int    INNER  = 53;
    static final String LINE   = "+" + "-".repeat(INNER) + "+";

    // Random object for dice rolls
    static Random random = new Random();

    // --------------------------------------------------
    // Overloaded: rollAttack
    // --------------------------------------------------

    /**
     * Basic attack roll using a single 6-sided die (1d6).
     *
     * @return a random number between 1 and 6
     */
    static int rollAttack() {
        return random.nextInt(6) + 1;
    }

    /**
     * Attack roll with a flat attack-power bonus added to the 1d6 result.
     *
     * @param bonus the attacker's attack power added on top of the dice roll
     * @return the dice roll (1-6) plus the bonus
     */
    static int rollAttack(int bonus) {
        int roll = random.nextInt(6) + 1;
        return roll + bonus;
    }

    /**
     * Weapon-specific attack roll with an attack-power bonus.
     * Axe: rolls two dice and takes the higher (advantage). Bow: 1d6 minus 1 (min 1). Sword: plain 1d6.
     *
     * @param weapon the weapon type: "sword", "axe", or "bow"
     * @param bonus  the attacker's attack power added on top
     * @return the weapon dice result plus the bonus
     */
    static int rollAttack(String weapon, int bonus) {
        int roll;
        if (weapon.equals("axe")) {
            int r1 = random.nextInt(6) + 1;
            int r2 = random.nextInt(6) + 1;
            roll = Math.max(r1, r2);
        } else if (weapon.equals("bow")) {
            roll = Math.max(1, random.nextInt(6));
        } else {
            roll = random.nextInt(6) + 1;
        }
        return roll + bonus;
    }

    // --------------------------------------------------
    // Overloaded: formatLog
    // --------------------------------------------------

    /**
     * Formats a simple event message.
     *
     * @param event a description of what happened
     * @return a formatted log string
     */
    static String formatLog(String event) {
        return "  [*] " + event;
    }

    /**
     * Formats a message showing total damage dealt by an attacker.
     *
     * @param attacker the name of the attacking fighter
     * @param damage   the total damage dealt
     * @return a formatted log string
     */
    static String formatLog(String attacker, int damage) {
        return "  " + attacker + " deals " + damage + " total damage!";
    }

    /**
     * Formats a full hit message showing attacker, target, and damage.
     *
     * @param attacker the name of the attacking fighter
     * @param target   the name of the fighter being hit
     * @param damage   the amount of damage dealt
     * @return a formatted log string
     */
    static String formatLog(String attacker, String target, int damage) {
        return "  " + attacker + " hits " + target + " for " + damage + " damage!";
    }

    // --------------------------------------------------
    // Display helpers
    // --------------------------------------------------

    // Print the box border line
    static void printLine() {
        System.out.println(BORDER + LINE + RESET);
    }

    // Print a blank line
    static void printBlank() {
        System.out.println();
    }

    // Print text centered inside the box (INNER = 53 wide)
    // Pads with spaces so the closing | lands at column 54
    static void printCentered(String text, String color) {
        int total = INNER - text.length();
        int left  = total / 2;
        int right = total - left;

        String ls = "";
        String rs = "";
        for (int i = 0; i < left;  i++) ls = ls + " ";
        for (int i = 0; i < right; i++) rs = rs + " ";

        System.out.println(
            BORDER + "|" + RESET
            + color + ls + text + rs + RESET
            + BORDER + "|" + RESET
        );
    }

    // Print a plain (no color) inner line - used for ASCII art rows
    // text must be exactly INNER chars wide
    static void printRow(String text) {
        System.out.println(BORDER + "|" + RESET + text + BORDER + "|" + RESET);
    }

    // Cute round comments
    static String getRoundComment(int round) {
        if (round == 1) return "The battle begins!";
        if (round == 2) return "The crowd cheers!";
        if (round == 3) return "The tension rises!";
        if (round == 4) return "Nobody holds back!";
        if (round == 5) return "This is intense!";
        if (round == 6) return "Fighters are tiring...";
        if (round == 7) return "Every hit counts!";
        if (round == 8) return "The arena shakes!";
        if (round == 9) return "Who will survive?";
        return "A legendary duel!";
    }
}
