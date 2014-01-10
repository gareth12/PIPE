package pipe.actions.type;

import org.junit.Before;
import org.junit.Test;
import pipe.controllers.PetriNetController;
import pipe.controllers.arcCreator.ArcActionCreator;
import pipe.gui.ApplicationSettings;
import pipe.gui.Constants;
import pipe.historyActions.HistoryManager;
import pipe.models.PetriNet;
import pipe.models.component.Connectable;
import pipe.models.component.Place;
import pipe.models.component.Token;
import pipe.models.component.Transition;
import pipe.models.visitor.connectable.arc.ArcCreatorVisitor;
import pipe.models.visitor.connectable.arc.ArcSourceVisitor;
import pipe.views.PipeApplicationView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import static org.mockito.Mockito.*;

public class ArcActionTest {
    private ArcAction action;
    private PetriNetController mockController;
    private PetriNet mockNet;
    private HistoryManager mockHistory;
    private PipeApplicationView mockApplicationView;
    private Token activeToken;
    private ArcSourceVisitor mockSourceVisitor;
    private ArcActionCreator mockCreatorVisitor;

//    @Before
//    public void setUp() {
//        mockSourceVisitor = mock(ArcSourceVisitor.class);
//        mockCreatorVisitor = mock(ArcActionCreator.class);
//        action = new ArcAction("Inhibitor Arc", Constants.INHIBARC, "Add an inhibitor arc", "H", mockSourceVisitor, mockCreatorVisitor);
//        mockController = mock(PetriNetController.class);
//        mockNet = mock(PetriNet.class);
//        when(mockController.getPetriNet()).thenReturn(mockNet);
//
//        mockHistory = mock(HistoryManager.class);
//        when(mockController.getHistoryManager()).thenReturn(mockHistory);
//
//
//        activeToken = mock(Token.class);
//        mockApplicationView = mock(PipeApplicationView.class);
//        when(mockApplicationView.getSelectedTokenName()).thenReturn("Default");
//        when(mockController.getToken("Default")).thenReturn(activeToken);
//        ApplicationSettings.register(mockApplicationView);
//    }
//
//
//
//
//    @Test
//    public void doesNotCreateArcIfCreatingArcReturnsTrue() {
//        Place place = mock(Place.class);
//        when(mockController.isCurrentlyCreatingArc()).thenReturn(true);
//        when(mockSourceVisitor.canStart(place)).thenReturn(false);
//        action.doConnectableAction(place, mockController);
//        verify(mockCreatorVisitor, never()).visit(place);
//    }
//
//    @Test
//    public void doesNotCreateArcIfBothReturnTrue() {
//        Place place = mock(Place.class);
//        when(mockController.isCurrentlyCreatingArc()).thenReturn(true);
//        when(mockSourceVisitor.canStart(place)).thenReturn(true);
//        action.doConnectableAction(place, mockController);
//        verify(mockCreatorVisitor, never()).visit(place);
//    }
//
//
//    @Test
//    public void finishesArcIfSourceVisitorReturnsFalseAndCreatingReturnsTrue() {
//        Place place = mock(Place.class);
//        when(mockController.isCurrentlyCreatingArc()).thenReturn(true);
//        when(mockSourceVisitor.canStart(place)).thenReturn(false);
//        action.doConnectableAction(place, mockController);
//        verify(mockController).finishCreatingArc(place);
//
//    }
//
//    @Test
//    public void addsIntermediateCurvedPointIfClicked() {
//        when(mockController.isCurrentlyCreatingArc()).thenReturn(true);
//
//        MouseEvent mockEvent = mock(MouseEvent.class);
//        when(mockEvent.isShiftDown()).thenReturn(true);
//        Point point = mock(Point.class);
//        when(mockEvent.getPoint()).thenReturn(point);
//
//        action.doAction(mockEvent, mockController);
//        verify(mockController).addPoint(point, true);
//    }
//
//    @Test
//    public void addsIntermediateStraightPointIfClicked() {
//        when(mockController.isCurrentlyCreatingArc()).thenReturn(true);
//
//        MouseEvent mockEvent = mock(MouseEvent.class);
//        when(mockEvent.isShiftDown()).thenReturn(false);
//        Point point = mock(Point.class);
//        when(mockEvent.getPoint()).thenReturn(point);
//
//        action.doAction(mockEvent, mockController);
//        verify(mockController).addPoint(point, false);
//    }
}
