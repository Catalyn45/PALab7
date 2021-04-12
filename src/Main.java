import game.Bot;
import game.Game;
import game.Player;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(3);
        Player p1 = new Player("P1", game);
        Player p2 = new Bot("P2", game);
        game.start();
    }
}
