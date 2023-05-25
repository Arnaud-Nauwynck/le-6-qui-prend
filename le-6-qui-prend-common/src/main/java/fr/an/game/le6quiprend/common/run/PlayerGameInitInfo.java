package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;

public class PlayerGameInitInfo {

    public final int playerId;

    public final ImmutableList<Integer> cards;

    public final ImmutableList<PlayerInfo> playerInfos;

    public static class PlayerInfo {
        public final int playerId;
        public final String playerName;
        public final String playerDescription;
        // public final int playerRank;
        // public final int playerAverageScore;

        public PlayerInfo(int playerId, String playerName, String playerDescription) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.playerDescription = playerDescription;
        }
    }

    public PlayerGameInitInfo(int playerId, ImmutableList<Integer> cards, ImmutableList<PlayerInfo> playerInfos) {
        this.playerId = playerId;
        this.cards = cards;
        this.playerInfos = playerInfos;
    }
}
