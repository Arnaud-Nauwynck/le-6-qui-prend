package fr.an.game.le6quiprend.common.run;

import com.google.common.collect.ImmutableList;
import fr.an.game.le6quiprend.common.Card;
import fr.an.game.le6quiprend.common.CardSet;
import fr.an.game.le6quiprend.common.Cards;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Referee to drive a game, between different players
 * <p>
 * synonym: Game driver, referee, simulator, orchestrator ...
 * Design-pattern: mediator, template method, delegate to GamePlayerCallback
 */
public class GameDriver {

    protected final int playerCount; // = players.length;

    protected final CurrentPlayerState[] playerStates;

    protected final int totalTurnCount;

    protected static class CurrentPlayerState {
        public final int playerId;
        public final String playerName;
        public final String playerDescription;
        public final GamePlayerCallback playerCallback;
        public final boolean playerNeedNotify;
        protected final CardSet remainCards;
        
        protected int currentPlayerGamePenalties;
        protected int totalPlayerPenalties;

        public CurrentPlayerState(int playerId, String playerName, String playerDescription, GamePlayerCallback playerCallback, boolean playerNeedNotify, CardSet remainCards) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.playerDescription = playerDescription;
            this.playerCallback = playerCallback;
            this.playerNeedNotify = playerNeedNotify;
            this.remainCards = remainCards;
        }
    }

    protected final boolean anyPlayerNeedNotify;

    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;

    protected final List<GameTurnResult> gameTurnResults = new ArrayList<>();

    protected List<CardGameEventListener> listeners = new ArrayList<>();

    protected static final class CardForPlayer implements Comparable<CardForPlayer> {
        public final int playerId;
        protected Card card; // mutable, and reusing buffered object.. but could be Immutable and using new

        public CardForPlayer(int playerId) {
            this.playerId = playerId;
        }

        @Override
        public int compareTo(CardForPlayer other) {
            return Integer.compare(card.value, other.card.value);
        }
    }

    protected final CardForPlayer[] bufferCardForPlayers;
    protected final CardForPlayer[] sortedCardForPlayers;

    //---------------------------------------------------------------------------------------------

    public record GamePlayerParams(String playerName, String playerDescription, GamePlayerCallback playerCallback) {
    }

    public GameDriver(List<GamePlayerParams> playerParams, List<Collection<Card>> playersCards) {
        Objects.requireNonNull(playerParams);
        this.playerCount = playerParams.size();
        this.playerStates = new CurrentPlayerState[playerCount];
        this.totalTurnCount = playersCards.get(0).size();
        this.bufferCardForPlayers = new CardForPlayer[playerCount];
        this.sortedCardForPlayers = new CardForPlayer[playerCount];
        boolean tmpAnyPlayerNeedNotify = false;
        for (int i = 0; i < playerCount; i++) {
            GamePlayerParams p = playerParams.get(i);
            Collection<Card> playerCards = playersCards.get(i);
            if (totalTurnCount != playerCards.size()) {
                throw new IllegalArgumentException("bad number of cards for player[" + i + "]");
            }
            boolean playerNeedNotify = p.playerCallback.needNotifyGameTurnResult();
            tmpAnyPlayerNeedNotify |= playerNeedNotify;

            this.playerStates[i] = new CurrentPlayerState(i, p.playerName, p.playerDescription,
                    p.playerCallback, playerNeedNotify, new CardSet(playerCards));
            this.bufferCardForPlayers[i] = new CardForPlayer(i);
            this.sortedCardForPlayers[i] = this.bufferCardForPlayers[i];
        }
        this.anyPlayerNeedNotify = tmpAnyPlayerNeedNotify;
    }

    //---------------------------------------------------------------------------------------------

    public void addListener(CardGameEventListener p) {
        listeners.add(p);
    }

    public void removeListener(CardGameEventListener p) {
        listeners.remove(p);
    }

    protected void fireEventListeners(CardGameEvent event) {
        for (CardGameEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception ex) {
                // ignore!
            }
        }
    }


    public void runGame() {
        onStartGame();
        for (int gameRoundIndex = 0; gameRoundIndex < totalTurnCount; gameRoundIndex++) {
            runGameRound();
        }
        onEndGame();
    }

    protected void runGameRound() {
        // ask all players to choose 1 card
        // to be fair... should be parallelized in N threads
        int[] cardPlayedBy = new int[playerCount];
        for (var player : playerStates) {
            // recap for each player their card, score, ..
            ImmutableList<Integer> playerAvailableCards = null; // TODO
            ImmutableList<CardRowSummary> cardRows = null; // TODO
            ChooseCardRequest chooseCardReq = new ChooseCardRequest(playerAvailableCards, cardRows);
            long chooseCardStartTime = System.currentTimeMillis();

            int chosenCardValue = player.playerCallback.chooseCard(chooseCardReq);

            long chooseCardMillis = System.currentTimeMillis() - chooseCardStartTime;
            cardPlayedBy[player.playerId] = chosenCardValue;
            // check card is valid to take, and remove
            Card card = Cards.cardOf(chosenCardValue);
            player.remainCards.take(card);
            bufferCardForPlayers[player.playerId].card = card; // cf sort sortedCardForPlayers next (different array, same pointers)
        }

        // TODO fireEvent(..);

        // sort (card,player) by ascending card values
        Arrays.sort(sortedCardForPlayers);

        // if smaller played card if smaller than min(all) card rows, ask this player to choose a row
        int minPlayedCard = sortedCardForPlayers[0].card.value;
        int minRowValue = 0; // TODO
        if (minPlayedCard < minRowValue) {
            CurrentPlayerState player = null; // TODO
            int chosenCard = 0; // TODO
            ImmutableList<CardRowSummary> cardRows = null; // TODO
            ImmutableList<ChooseLessRowRequest.RemainPlayedCard> remainPlayedCards = null; // TODO
            ChooseLessRowRequest chooseLessRowRequest = new ChooseLessRowRequest(chosenCard, cardRows, remainPlayedCards);

            long chooseRowStartTime = System.currentTimeMillis();

            int chosenRow = player.playerCallback.chooseLessRow(chooseLessRowRequest);

            long chooseRowMillis = System.currentTimeMillis() - chooseRowStartTime;

            // TODO fireEvent(..);
        }

        // assign cards to rows in order
        // TODO

        onTurnEnd();
    }


    protected void onStartGame() {
        this.startDateTime = LocalDateTime.now();
        if (!listeners.isEmpty()) {
            ImmutableList<CardGameEvent.StartGamePlayerInfo> playerInfos = null; // TODO
            CardGameEvent.StartGameEvent event = new CardGameEvent.StartGameEvent(
                    startDateTime, playerInfos);
            fireEventListeners(event);
        }

        if (!anyPlayerNeedNotify) {
            notifyNeededPlayersInit();
        }
    }

    protected void notifyNeededPlayersInit() {
        if (!anyPlayerNeedNotify) {
            return;
        }
        ImmutableList<PlayerGameInitInfo.PlayerInfo> playerInfos = null; // TODO
        for(var player: playerStates) {
            if (player.playerNeedNotify) {
                ImmutableList<Integer> cards = null; // TODO
                var req = new PlayerGameInitInfo(player.playerId, cards, playerInfos);
                player.playerCallback.notifyGameInit(req);
            }
        }
    }

    protected void onTurnEnd() {
        // if player need to be notified of turn results, send him summary
        if (!anyPlayerNeedNotify) {
            notifyNeededPlayersTurnResult();
        }

        // TODO fireEventListeners
    }

    protected void notifyNeededPlayersTurnResult() {
        if (!anyPlayerNeedNotify) {
            return;
        }
        ImmutableList<Integer> cardsPlayedByPlayer = null; // TODO
        int rowChosenIfLess = 0; // TODO
        GameTurnResult req= new GameTurnResult(cardsPlayedByPlayer, rowChosenIfLess);
        for(var player: playerStates) {
            if (player.playerNeedNotify) {
                player.playerCallback.notifyGameTurnResult(req);
            }
        }
    }

    protected void onEndGame() {
        this.endDateTime = LocalDateTime.now();
        if (!listeners.isEmpty()) {
            int gameDurationMillis = (int) ChronoUnit.MILLIS.between(startDateTime, endDateTime);
            ImmutableList<CardGameEvent.EndGamePlayerInfo> endGamePlayerInfos = null; // TODO
            ImmutableList<Integer> playerIdByRank = null; // TODO
            CardGameEvent.EndGameEvent event = new CardGameEvent.EndGameEvent(
                    endDateTime, gameDurationMillis,
                    endGamePlayerInfos, playerIdByRank);
            fireEventListeners(event);
        }
    }

}
