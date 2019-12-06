import javafx.application.Platform;

import java.util.ArrayList;

public class TicTacToeGameLogic {

    private Server server;
    private GameInfo gameInfo;
    private Integer p1Points = 0;
    private Integer p2Points = 0;

    public ArrayList<GameInfo> evalBoard (GameInfo.Play p1play, GameInfo.Play p2play) {
        GameInfo gameInfo1 = new GameInfo();
        GameInfo gameInfo2 = new GameInfo();
       
        ArrayList<GameInfo> gameInfos = new ArrayList<>();
        gameInfos.add(gameInfo1);
        gameInfos.add(gameInfo2);
        return gameInfos;
    }

    public ArrayList<GameInfo> evalWinner (int p1, int p2) {
        ArrayList<GameInfo> gameInfos = new ArrayList<>();
        GameInfo gameInfo1 = new GameInfo();
        GameInfo gameInfo2 = new GameInfo();
        if (p1 == 1) {
            gameInfo1.setTotalResult(GameInfo.Result.WIN);
            gameInfo2.setTotalResult(GameInfo.Result.LOSE);
        }
        if (p2 == 1) {
            gameInfo1.setTotalResult(GameInfo.Result.LOSE);
            gameInfo2.setTotalResult(GameInfo.Result.WIN);
        }
        gameInfos.add(gameInfo1);
        gameInfos.add(gameInfo2);
        return gameInfos;
    }

}
