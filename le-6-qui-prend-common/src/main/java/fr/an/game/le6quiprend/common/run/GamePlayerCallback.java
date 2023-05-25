package fr.an.game.le6quiprend.common.run;

public abstract class GamePlayerCallback {

    /**
     * callback of player (Bot or human) to decide which card to play in a round
     * @return card value in [1, 104], among valid remaining cards
     */
    public abstract int chooseCard(ChooseCardRequest req);

    /**
     * callback of player (Bot or human) to decide which card row to pick, because his card is less than all rows
     * @return row index in [0, 3]
     */
    public abstract int chooseLessRow(ChooseLessRowRequest req);


    // optionnal, to receive state info of Game
    //---------------------------------------------------------------------------------------------

    /** to override when requesting calls to notifyGameTurnResult() after each turn */
    public boolean needNotifyGameTurnResult() {
        return false;
    }

    /** to override  if needed */
    public void notifyGameInit(PlayerGameInitInfo req) {
        // default: do nothing
    }

    /** to override  if needed */
    public void notifyGameTurnResult(GameTurnResult req) {
        // default: do nothing
    }

}
