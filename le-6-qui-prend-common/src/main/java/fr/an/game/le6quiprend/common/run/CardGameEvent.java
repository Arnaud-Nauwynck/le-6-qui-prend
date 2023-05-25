package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * base class for all game event sub-classes
 *
 * all sub-classes are Immutable
 */
public abstract class CardGameEvent {

    // public final LocalDateTime time;

    public record StartGamePlayerInfo(int playerId, String playerName) {
        // TOADD public final String playerDescription; // bot / human..
    }

    public static class StartGameEvent extends CardGameEvent {
        public final LocalDateTime time;
        public final ImmutableList<StartGamePlayerInfo> playerInfos;

        public StartGameEvent(LocalDateTime time, ImmutableList<StartGamePlayerInfo> playerInfos) {
            this.time = time;
            this.playerInfos = playerInfos;
        }
    }

    /**
     * @param score sum of card penalties taken
     * @param rank  rank: 1 is best of game
     */
    public record EndGamePlayerInfo(
            int playerId,
            int score,
            int rank,
            int countRowTakeAs6,
            int countRowTakeAsLess,
            int playerCallbacksDurationTotalMillis) {
            // public final int playerCallbacksDurationMaxMillis;
    }

    public static class EndGameEvent extends CardGameEvent {
        public final LocalDateTime time;
        public final int gameDurationMillis;
        public final ImmutableList<EndGamePlayerInfo> playerInfos;
        /** index from 0(for rank 1) to N player-1 */
        public final ImmutableList<Integer> playerIdByRank;

        public EndGameEvent(LocalDateTime time, int gameDurationMillis, ImmutableList<EndGamePlayerInfo> playerInfos, ImmutableList<Integer> playerIdByRank) {
            this.time = time;
            this.gameDurationMillis = gameDurationMillis;
            this.playerInfos = playerInfos;
            this.playerIdByRank = playerIdByRank;
        }
    }

}
