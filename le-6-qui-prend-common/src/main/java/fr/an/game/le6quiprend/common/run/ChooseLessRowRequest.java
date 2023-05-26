package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;

/**
 * info to pass to player when requesting his which card row to pick, because his card is less than all rows
 */
public record ChooseLessRowRequest(int myChosenCard,
                                   ImmutableList<CardRowSummary> cardRows,
                                   ImmutableList<RemainPlayedCard> remainPlayedCards) {

    public static record RemainPlayedCard(int playerId,
                                          int playedCard,
                                          int currentPlayerGamePenalties,
                                          int totalPlayerPenalties) {
    }

}
