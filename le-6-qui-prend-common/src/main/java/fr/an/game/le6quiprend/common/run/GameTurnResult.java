package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;

/**
 * Immutable info for a past game turn
 */
public record GameTurnResult(ImmutableList<Integer> cardsPlayedByPlayer, int rowChosenIfLess) {

}
