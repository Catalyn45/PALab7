package game;

import java.util.Scanner;

public class Bot extends Player{

    public Bot(String name, Game game) {
        super(name, game);
    }

    @Override
    public void run() {
        int n = game.getN();

        while(true) {
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

                while (true) {
                    int i = (int) (Math.random() * n);
                    int j = (int) (Math.random() * n);
                    try {
                        token = game.getToken(i, j);
                        tokens.add(token);
                        System.out.println("Bot got: " + i + " " + j);
                        break;
                    } catch (TokenDoesNotExists e) {
                    }
                }

            } catch (NoMoreTokens e) {
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
}
