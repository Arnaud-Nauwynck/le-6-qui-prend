package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * info to pass to player when requesting his which card row to pick, because his card is less than all rows
 */
public final class ChooseLessRowRequest {

    public final int myChosenCard;

    /** assert length=4 */
    public final ImmutableList<CardRowSummary> cardRows;

    /** for N players, remainPlayedCards.length=N-1 */
    public final ImmutableList<RemainPlayedCard> remainPlayedCards;

    public static record RemainPlayedCard(int playerId, int playedCard, int currentPlayerGamePenalties,
                                          int totalPlayerPenalties) {
    }

    public ChooseLessRowRequest(int myChosenCard, ImmutableList<CardRowSummary> cardRows, ImmutableList<RemainPlayedCard> remainPlayedCards) {
        this.myChosenCard = myChosenCard;
        this.cardRows = cardRows;
        this.remainPlayedCards = remainPlayedCards;
    }
}
