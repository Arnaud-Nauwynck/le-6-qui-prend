package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;

/**
 * request pass from GameDriver to player when requesting to choose a card.
 * This gives info updates on the current turn.
 *
 * @param availableCards choose among available cards ... length in [1, 10]
 * @param rows           current 4 card rows summary
 */
public record ChooseCardRequest(
        ImmutableList<Integer> availableCards,
        ImmutableList<CardRowSummary> rows) {

    // implicit: public final int myPlayerId;

    // info on previous run?
    // whichPlayer played which cards, ... which player choosed which rows

}
