package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;

public class PlayerGameInitInfo {

    public final int playerId;

    public final ImmutableList<Integer> cards;

    public final ImmutableList<PlayerInfo> playerInfos;

    public static record PlayerInfo(int playerId, String playerName, String playerDescription
                             // int playerRank,
                             // int playerAverageScore
    ) {
    }

    public PlayerGameInitInfo(int playerId, ImmutableList<Integer> cards, ImmutableList<PlayerInfo> playerInfos) {
        this.playerId = playerId;
        this.cards = cards;
        this.playerInfos = playerInfos;
    }
}
