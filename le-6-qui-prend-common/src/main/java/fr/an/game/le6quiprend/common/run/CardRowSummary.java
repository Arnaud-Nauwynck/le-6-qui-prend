package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;


/**
 * @param cardCount    in [1, 5]
 * @param value        in [1, 104]
 * @param sumPenalties in [1, 7+4*5]
 * @param cards        details should be useless to decide...
 */
public record CardRowSummary(int cardCount, int value, int sumPenalties, ImmutableList<Integer> cards) {
}
