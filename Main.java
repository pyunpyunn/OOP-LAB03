// API classes used in this program:
// 1. ArrayList   - stores the list of Fighter objects
// 2. Collections - used to check living fighters
// 3. Math        - used with Math.max() to enforce minimum damage of 1
// 4. Random      - used inside BattleUtils for dice rolls (1d6)
// 5. Scanner     - reads all player input from the console

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main driver for the Dice Battle Arena console game.
 * Handles the game loop, player input, and all display output.
 */
public class Main {

    static Scanner scanner = new Scanner(System.in);

    /**
     * Entry point of the program.
     * Sets up fighters, runs the battle loop, and declares a winner.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        boolean playAgain = true;

        while (playAgain) {

            // HP 20-28, attack power 6-8
            // Each hit = dice (1-6) + atk (6-8) = 7-14 damage
            // HP 20-28 means each fighter falls in 2-4 hits
            // With 5 fighters each needing 2-4 hits, total ~5-8 rounds
            ArrayList<Fighter> fighters = new ArrayList<>();
            fighters.add(new Fighter("Riko",    15, 6));
            fighters.add(new Fighter("Reg",     18, 8));
            fighters.add(new Fighter("Nanachi", 15, 7));
            fighters.add(new Fighter("Prushka", 12, 6));
            fighters.add(new Fighter("Marulk",  10, 6));

            showTitle();
            showFighters(fighters);

            int round = 1;

            while (countAlive(fighters) > 1) {

                BattleUtils.printLine();
                BattleUtils.printBlank();

                // Choose attacker
                Fighter attacker = chooseFighter(fighters, "Choose Fighter");
                if (attacker == null) continue;

                // Choose opponent
                Fighter target = chooseFighter(fighters, "Choose Opponent");
                if (target == null) continue;

                // Cannot fight yourself
                if (attacker.getName().equals(target.getName())) {
                    System.out.println(BattleUtils.YELLOW
                            + "  You cannot fight yourself! Try again."
                            + BattleUtils.RESET);
                    BattleUtils.printBlank();
                    continue;
                }

                BattleUtils.printBlank();

                // Roll dice prompt
                boolean rolled = askRollDice();
                if (!rolled) {
                    BattleUtils.printBlank();
                    continue;
                }

                // Round header
                BattleUtils.printBlank();
                BattleUtils.printLine();
                String roundLabel = "ROUND " + round + ": " + BattleUtils.getRoundComment(round);
                BattleUtils.printCentered(roundLabel, BattleUtils.BOLD);
                BattleUtils.printBlank();

                // Damage: roll 1d6 (overload 1), then add attack power
                int diceRoll = BattleUtils.rollAttack();
                int damage   = diceRoll + attacker.getAttackPower();
                damage = Math.max(1, damage);

                System.out.println("  " + attacker.getName() + " rolls: " + diceRoll
                        + "  (+" + attacker.getAttackPower() + " ATK = " + damage + " dmg)");
                System.out.println(BattleUtils.RED
                        + BattleUtils.formatLog(attacker.getName(), target.getName(), damage)
                        + BattleUtils.RESET);

                target.takeDamage(damage);

                if (!target.isAlive()) {
                    BattleUtils.printBlank();
                    System.out.println(BattleUtils.RED
                            + "  " + target.getName() + " is eliminated x_x..."
                            + BattleUtils.RESET);
                }

                BattleUtils.printBlank();
                pressToContinue();
                showStatus(fighters);
                round++;

                if (round > 50) {
                    System.out.println("  Battle exceeded 50 rounds. Stopping.");
                    break;
                }


System.out.print("  Ready for the next round? Press Enter...");

if (!safeNextLine()) {
    System.out.println("\nProgram terminated safely.");
    return;
}

                BattleUtils.printBlank();
            }

            // Winner screen
            Fighter winner = getWinner(fighters);

            BattleUtils.printLine();
            BattleUtils.printBlank();
            BattleUtils.printCentered("WINNER", BattleUtils.BOLD + BattleUtils.YELLOW);
            BattleUtils.printBlank();
            System.out.println(BattleUtils.GREEN + BattleUtils.BOLD
                    + "  " + winner.getName() + " wins!" + BattleUtils.RESET);
            BattleUtils.printBlank();

            // Battle log - demonstrates all 3 overloaded formatLog versions visibly
            System.out.println(BattleUtils.BOLD + "  BATTLE LOG" + BattleUtils.RESET);
            System.out.println(BattleUtils.formatLog(
                    "Battle ended after " + (round - 1) + " round(s)."));
            System.out.println(BattleUtils.formatLog(
                    winner.getName(), winner.getAttackPower()));
            System.out.println(BattleUtils.formatLog(
                    winner.getName(), "the Arena",
                    BattleUtils.rollAttack("axe", winner.getAttackPower())));
            BattleUtils.printBlank();

            // Winner final status
            BattleUtils.printLine();
            BattleUtils.printCentered("FIGHTERS STATUS", BattleUtils.BOLD);
            BattleUtils.printLine();
            BattleUtils.printBlank();
            System.out.printf("  %-10s | HP: %-5d | Attack Power: %d%n",
                    winner.getName(), winner.getHp(), winner.getAttackPower());
            BattleUtils.printBlank();

            pressToContinue();
            BattleUtils.printLine();
            BattleUtils.printBlank();

            // Play again?
            System.out.print("  Play again? (y/n): ");
            String again = getInput().toLowerCase();

            if (again.equals("y")) {
                playAgain = true;
            } else {
                playAgain = false;
                BattleUtils.printBlank();
                System.out.println("  Thanks for playing! Exiting...");
                BattleUtils.printBlank();
                BattleUtils.printLine();
            }
        }

        scanner.close();
    }

// --------------------------------------------------
// Title screen - perfectly aligned to border width
// Inner width matches printLine exactly: 53 chars
// --------------------------------------------------
static void showTitle() {
    BattleUtils.printBlank();
    BattleUtils.printLine();

    BattleUtils.printRow("                                                     ");
    BattleUtils.printRow("   ____  _               ____        _   _   _       ");
    BattleUtils.printRow("  |  _ \\(_) ___ ___     | __ )  __ _| |_| |_| |___   ");
    BattleUtils.printRow("  | | | | |/ __/ _ \\    |  _ \\ / _` | __| __| / _ \\  ");
    BattleUtils.printRow("  | |_| | | (_|  __/    | |_) | (_| | |_| |_| |  __/ ");
    BattleUtils.printRow("  |____/|_|\\___\\___|    |____/ \\__,_|\\__|\\__|_|\\___| ");
    BattleUtils.printRow("                                                     ");

    BattleUtils.printCentered(
        "* D I C E   B A T T L E   A R E N A *",
        BattleUtils.BOLD + BattleUtils.YELLOW
    );

    BattleUtils.printRow("                                                     ");
    BattleUtils.printLine();
}

    // --------------------------------------------------
    // Show all fighters and their starting stats
    // --------------------------------------------------
    static void showFighters(ArrayList<Fighter> fighters) {
        BattleUtils.printBlank();
        BattleUtils.printLine();
        BattleUtils.printCentered("FIGHTERS", BattleUtils.BOLD);
        BattleUtils.printLine();
        BattleUtils.printBlank();

        for (int i = 0; i < fighters.size(); i++) {
            Fighter f = fighters.get(i);
            System.out.println(    BattleUtils.YELLOW    + "  | (" + (i + 1) + ") " + f.getName()    + BattleUtils.RESET);
            System.out.println("  | HP: " + f.getHp());
            System.out.println("  | Attack Power Stat: " + f.getAttackPower());
            BattleUtils.printBlank();
        }

        BattleUtils.printLine();
    }

    // --------------------------------------------------
    // Choose a fighter by number or name
    // --------------------------------------------------

static Fighter chooseFighter(ArrayList<Fighter> fighters, String prompt) {
    while (true) {
        System.out.print("  " + prompt + ": ");

        String input = getInput();

        // Try number
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < fighters.size()) {
                Fighter f = fighters.get(index);
                if (!f.isAlive()) {
                    System.out.println(BattleUtils.RED
                            + "  " + f.getName() + " is already eliminated! Pick another."
                            + BattleUtils.RESET);
                    continue;
                }
                return f;
            }
        } catch (NumberFormatException e) {
            // not a number
        }

        // Try name
        for (Fighter f : fighters) {
            if (f.getName().equalsIgnoreCase(input)) {
                if (!f.isAlive()) {
                    System.out.println(BattleUtils.RED
                            + "  " + f.getName() + " is already eliminated! Pick another."
                            + BattleUtils.RESET);
                    break;
                }
                return f;
            }
        }

        System.out.println(BattleUtils.RED
                + "  Invalid choice. Enter a number (1-" + fighters.size() + ") or a name."
                + BattleUtils.RESET);
    }
}

    // --------------------------------------------------
    // Ask if the player wants to roll dice, with Go Back
    // --------------------------------------------------
static boolean askRollDice() {
    while (true) {
        System.out.print("  Roll Dice (y/n): ");
        String input = getInput().toLowerCase();

        if (input.equals("y")) {
            return true;
        } else if (input.equals("n")) {
            System.out.print("  Go Back (y/n): ");
            String back = getInput().toLowerCase();

            if (back.equals("y")) {
                return false;
            } else if (back.equals("n")) {
                continue;
            } else {
                System.out.println(BattleUtils.RED + "  Please enter y or n." + BattleUtils.RESET);
            }
        } else {
            System.out.println(BattleUtils.RED + "  Please enter y or n." + BattleUtils.RESET);
        }
    }
}

// --------------------------------------------------
// Show fighter status after each round
// Names only in yellow, spacing unchanged
// --------------------------------------------------
static void showStatus(ArrayList<Fighter> fighters) {
    BattleUtils.printLine();
    BattleUtils.printCentered("FIGHTERS STATUS", BattleUtils.BOLD);
    BattleUtils.printLine();
    BattleUtils.printBlank();

    for (Fighter f : fighters) {

        if (f.isAlive()) {
            System.out.printf("  %s%-10s%s | HP: %-5d | Attack Power: %d%n",
                    BattleUtils.YELLOW,
                    f.getName(),
                    BattleUtils.RESET,
                    f.getHp(),
                    f.getAttackPower());

        } else {
            System.out.printf("  %s%-10s%s | ELIMINATED!%n",
                    BattleUtils.YELLOW,
                    f.getName(),
                    BattleUtils.RESET);
        }
    }

    BattleUtils.printBlank();
    BattleUtils.printLine();
    BattleUtils.printBlank();
}

    // --------------------------------------------------
    // Pause until the player presses Enter
    // --------------------------------------------------
    static void pressToContinue() {
        System.out.print("  Press Enter to continue...");
        getInput();
        BattleUtils.printBlank();
    }

    // --------------------------------------------------
    // Count living fighters
    // --------------------------------------------------
    static int countAlive(ArrayList<Fighter> fighters) {
        int count = 0;
        for (Fighter f : fighters) {
            if (f.isAlive()) count = count + 1;
        }
        return count;
    }

    // --------------------------------------------------
    // Return the last surviving fighter
    // --------------------------------------------------
    static Fighter getWinner(ArrayList<Fighter> fighters) {
        for (Fighter f : fighters) {
            if (f.isAlive()) return f;
        }
        return fighters.get(0);
    }

    static boolean safeNextLine() {
    try {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
            return true;
        }
    } catch (Exception e) {
        return false;
    }
    return false;
}

static String getInput() {
    if (!scanner.hasNextLine()) {
        System.out.println("\nProgram terminated.");
        System.exit(0);
    }
    return scanner.nextLine().trim();
}

}
