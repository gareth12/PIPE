package pipe.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pipe.historyActions.HistoryItem;
import pipe.historyActions.PlaceCapacity;
import pipe.models.component.place.Place;
import pipe.models.component.token.Token;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlaceControllerTest {
    @Mock
    Place place;

    PlaceController placeController;

    @Before
    public void setUp() {
        placeController = new PlaceController(place);
    }

    @Test
    public void setCapacityCreatesNewHistoryItem() {
        int oldCapacity = 5;
        int newCapacity = 10;
        when(place.getCapacity()).thenReturn(oldCapacity);

        placeController.setCapacity(newCapacity);

        HistoryItem capacityItem = new PlaceCapacity(place, oldCapacity, newCapacity);
    }

    @Test
    public void setCapacityModifiesPlaceCapacity() {
        int oldCapacity = 5;
        int newCapacity = 10;
        when(place.getCapacity()).thenReturn(oldCapacity);

        placeController.setCapacity(newCapacity);
        verify(place).setCapacity(newCapacity);
    }

    //    @Test
    //    public void setTokenCountsCreatesHistoryItem() {
    //        Map<Token, Integer> tokenCounts = new HashMap<Token, Integer>();
    //        Token defaultToken = new Token("Default", true, 0, new Color(0, 0, 0));
    //        int oldCount = 7;
    //        int newCount = 5;
    //
    //        when(place.getTokenCount(defaultToken)).thenReturn(oldCount);
    //        tokenCounts.put(defaultToken, newCount);
    //        placeController.setTokenCounts(tokenCounts);
    //
    //        verify(historyManager).newEdit();
    //
    //        HistoryItem placeMarking =
    //                new ChangePlaceTokens(place, defaultToken, oldCount, newCount);
    //        verify(historyManager).addEdit(placeMarking);
    //    }

    @Test
    public void setTokenCountModifiesPlace() {
        Map<Token, Integer> tokenCounts = new HashMap<Token, Integer>();
        Token defaultToken = new Token("Default", true, 0, new Color(0, 0, 0));
        int oldCount = 7;
        int newCount = 5;

        when(place.getTokenCount(defaultToken)).thenReturn(oldCount);
        tokenCounts.put(defaultToken, newCount);
        placeController.setTokenCounts(tokenCounts);

        verify(place).setTokenCount(defaultToken, newCount);
    }

    @Test
    public void incrementsPlaceCounter() {
        int count = 1;
        Token token = new Token("Default", true, 0, new Color(0, 0, 0));
        when(place.getTokenCount(token)).thenReturn(count);
        placeController.addTokenToPlace(token);
        verify(place).setTokenCount(token, count + 1);
    }
    //
    //    @Test
    //    public void incrementPlaceCounterCreatesHistoryItem() {
    //        Token defaultToken = new Token("Default", true, 0, new Color(0, 0, 0));
    //        int oldCount = 7;
    //
    //        when(place.getTokenCount(defaultToken)).thenReturn(oldCount);
    //        placeController.addTokenToPlace(defaultToken);
    //
    //        verify(historyManager).newEdit();
    //        HistoryItem placeMarking =
    //                new ChangePlaceTokens(place, defaultToken, oldCount, oldCount + 1);
    //        verify(historyManager).addEdit(placeMarking);
    //    }

    @Test
    public void decrementsPlaceCounter() {
        int count = 1;
        Token token = new Token("Default", true, 0, new Color(0, 0, 0));
        when(place.getTokenCount(token)).thenReturn(count);
        placeController.deleteTokenInPlace(token);
        verify(place).setTokenCount(token, count - 1);
    }

    //    @Test
    //    public void decrementPlaceCounterCreatesHistoryItem() {
    //        Token defaultToken = new Token("Default", true, 0, new Color(0, 0, 0));
    //        int oldCount = 7;
    //
    //        when(place.getTokenCount(defaultToken)).thenReturn(oldCount);
    //        placeController.deleteTokenInPlace(defaultToken);
    //
    //        verify(historyManager).newEdit();
    //        HistoryItem placeMarking =
    //                new ChangePlaceTokens(place, defaultToken, oldCount, oldCount - 1);
    //        verify(historyManager).addEdit(placeMarking);
    //    }

}
