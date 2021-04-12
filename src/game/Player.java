package game;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Player implements Runnable {
    protected String name;
    protected final Game game;
    protected List<Token> tokens;
    protected static int globalId = 0;
    protected int id;

    public Player(String name, Game game) {
        this.name = name;
        this.game = game;
        game.addPlayer(this);
        tokens = new LinkedList<>();
        id = globalId++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                Token token = null;

                while(!game.getTurn(id)) {
                    try {
                        synchronized (game) {
                            game.wait();
                        }
                    } catch (InterruptedException e) {

                    }
                }

                while(true) {
                    try {
                        int firstNode = Integer.parseInt(scanner.next());
                        int secondNode = Integer.parseInt(scanner.next());

                        token = game.getToken(firstNode, secondNode);
                        tokens.add(token);
                        break;
                    } catch (TokenDoesNotExists e) {
                        System.out.println(e.getMessage());
                    }
                }

            } catch (NoMoreTokens e) {
                System.out.println(e.getMessage());
                game.endTurn(id);
                synchronized (game) {
                    game.notifyAll();
                }
                break;
            }

            game.endTurn(id);
            synchronized (game) {
                game.notifyAll();
            }
        }
    }

    public int calculateScore() {
        int score = tokens.size() * 10;

        for(Token token : tokens) {
            score += token.value;
        }

        return score;
    }
}
