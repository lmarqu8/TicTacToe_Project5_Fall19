import java.io.Serializable;


public class GameInfo implements Serializable {

    enum Play {
        A1, A2, A3, B1, B2, B3, C1, C2, C3;
    }

    enum Result {
        WIN, LOSE, DRAW;
    }

    enum State {
        PLAYING, RESULT, FINISHED, PLAYAGAIN, QUIT;
    }

    private int p1Points;
    private int p2Points;
    private Boolean haveTwoPlayers;
    private Boolean playAgain;
    private Boolean bothPlayed;
    private Play p1play;
    private Play p2play;
    private Result p1Result;
    private Result p2Result;
    private Result totalResult;
    private String message;
    private State gameState;

    private static final long serialVersionUID = 123;

    public GameInfo() {

    }

    public GameInfo(String message) {
        this.message = message;
    }

    public GameInfo(Play p1play) {
        this.p1play = p1play;
    }

    public State getGameState() {return gameState;}

    public void setGameState(State gameState) {this.gameState = gameState;}

    public Result getP1Result() {
        return p1Result;
    }

    public void setP1Result(Result p1Result) {
        this.p1Result = p1Result;
    }

    public Result getP2Result() {
        return p2Result;
    }

    public void setP2Result(Result p2Result) {
        this.p2Result = p2Result;
    }

    public boolean getBothPlayed() {
        return bothPlayed;
    }

    public void setBothPlayed(boolean bothPlayed) {
        this.bothPlayed = bothPlayed;
    }

    public int getP1Points() {
        return p1Points;
    }

    public void setP1Points(int p1Points) {
        this.p1Points = p1Points;
    }

    public int getP2Points() {
        return p2Points;
    }

    public void setP2Points(int p2Points) {
        this.p2Points = p2Points;
    }

    public Boolean getHaveTwoPlayers() {
        return haveTwoPlayers;
    }

    public void setHaveTwoPlayers(Boolean haveTwoPlayers) {
        this.haveTwoPlayers = haveTwoPlayers;
    }

    public Boolean getPlayAgain() {
        return playAgain;
    }

    public void setPlayAgain(Boolean playAgain) {
        this.playAgain = playAgain;
    }

    public Play getP1play() {
        return p1play;
    }

    public void setP1play(Play p1play) {
        this.p1play = p1play;
    }

    public Play getP2play() {
        return p2play;
    }

    public void setP2play(Play p2play) {
        this.p2play = p2play;
    }

    public Result getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(Result totalResult) {
        this.totalResult = totalResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "p1Points=" + p1Points +
                ", p2Points=" + p2Points +
                ", haveTwoPlayers=" + haveTwoPlayers +
                ", playAgain=" + playAgain +
                ", bothPlayed=" + bothPlayed +
                ", p1play=" + p1play +
                ", p2play=" + p2play +
                ", p1Result=" + p1Result +
                ", p2Result=" + p2Result +
                ", totalResult=" + totalResult +
                ", message='" + message + '\'' +
                '}';
    }
}
