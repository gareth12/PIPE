package pipe.reachability.algorithm;

import pipe.animation.AnimationLogic;
import pipe.animation.PetriNetAnimationLogic;
import pipe.models.component.place.Place;
import pipe.models.component.token.Token;
import pipe.models.component.transition.Transition;
import pipe.models.petrinet.PetriNet;
import pipe.parsers.FunctionalResults;
import pipe.parsers.PetriNetWeightParser;
import pipe.visitor.ClonePetriNet;
import uk.ac.imperial.state.ClassifiedState;
import uk.ac.imperial.state.HashedClassifiedState;
import uk.ac.imperial.state.HashedStateBuilder;
import uk.ac.imperial.state.State;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Useful methods to help explore the state space.
 * <p/>
 * Performs caching of frequent computations in a thread safe manner
 */
public class CachingExplorerUtilities implements ExplorerUtilities {
    /**
     *
     */
    private final PetriNet petriNet;

    /**
     * Animator for the Petri net
     */
    private final AnimationLogic animationLogic;

    /**
     * Cached successors is used when exploring states to quickly determine
     * a states successors it has already seen before.
     * <p/>
     * It will be most useful when exploring cyclic transitions
     * It is thread safe due to the nature of this class being accessed from many threads
     */
    private Map<ClassifiedState, Map<ClassifiedState, Collection<Transition>>> cachedSuccessors = new ConcurrentHashMap<>();

    /**
     * Takes a copy of the Petri net to use for state space exploration so
     * not to affect the reference
     *
     * @param petriNet petri net to use for state space exploration
     */
    public CachingExplorerUtilities(PetriNet petriNet) {
        this.petriNet = ClonePetriNet.clone(petriNet);
        animationLogic = new PetriNetAnimationLogic(this.petriNet);
    }

    /**
     * Finds successors of the given state. A successor is a state that occurs
     * when one of the enabled transitions in the current state is fired.
     * <p/>
     * Performs caching of the successors to speed up computation time
     * when a state is queried more than once. This is particularly useful
     * if on the fly vanishing state exploration is used
     *
     * @param state
     * @return map of successor states to the transitions that caused them
     */
    @Override
    public Map<ClassifiedState, Collection<Transition>> getSuccessorsWithTransitions(ClassifiedState state) {

        if (cachedSuccessors.containsKey(state)) {
            return cachedSuccessors.get(state);
        }

        Map<ClassifiedState, Collection<Transition>> successors = new HashMap<>();
        for (Map.Entry<State, Collection<Transition>> entry : animationLogic.getSuccessors(
                state).entrySet()) {
            successors.put(createState(entry.getKey()), entry.getValue());
        }
        cachedSuccessors.put(state, successors);

        return successors;
    }

    @Override
    public Collection<ClassifiedState> getSuccessors(ClassifiedState state) {
        return getSuccessorsWithTransitions(state).keySet();
    }

    @Override
    public double rate(ClassifiedState state, ClassifiedState successor) {
        Collection<Transition> transitionsToSuccessor = getTransitions(state, successor);
        return getWeightOfTransitions(transitionsToSuccessor);
    }


    /**
     * Creates a new state containing the token counts for the
     * current Petri net.
     *
     * @return current state of the Petri net
     */
    @Override
    public ClassifiedState getCurrentState() {
        HashedStateBuilder builder = new HashedStateBuilder();
        for (Place place : petriNet.getPlaces()) {
            for (Token token : petriNet.getTokens()) {
                builder.placeWithToken(place.getId(), token.getId(), place.getTokenCount(token.getId()));
            }
        }

        return createState(builder.build());
    }

    private ClassifiedState createState(State tokenCounts) {
        boolean tanigble = isTangible(tokenCounts);
        return (tanigble ? HashedClassifiedState.tangibleState(tokenCounts) :
                HashedClassifiedState.vanishingState(tokenCounts));
    }

    /**
     * A tangible state is one in which:
     * a) Has no enabled transitions
     * b) Has entirely timed transitions leaving it
     *
     * @param tokenCounts to test for tangibility
     * @return true if the current token count setting is tangible
     */
    private boolean isTangible(State tokenCounts) {
        Set<Transition> enabledTransitions = animationLogic.getEnabledTransitions(tokenCounts);
        boolean anyTimed = false;
        boolean anyImmediate = false;
        for (Transition transition : enabledTransitions) {
            if (transition.isTimed()) {
                anyTimed = true;
            } else {
                anyImmediate = true;
            }
        }
        return enabledTransitions.isEmpty() || (anyTimed && !anyImmediate);
    }


    /**
     * Calculates the set of transitions that will take you from one state to the successor.
     * <p/>
     * Uses the current underlying cached methods so that duplicate calls to this method
     * will not result in another computation
     *
     * @param state     initial state
     * @param successor successor state, must be directly reachable from the state
     * @return enabled transitions that take you from state to successor, if it is not directly reachable then
     * an empty Collection will be returned
     */
    @Override
    public Collection<Transition> getTransitions(ClassifiedState state, ClassifiedState successor) {
        Map<ClassifiedState, Collection<Transition>> stateTransitions = getSuccessorsWithTransitions(state);
            if (stateTransitions.containsKey(successor)) {
                return stateTransitions.get(successor);
            }

        return new LinkedList<>();
    }


    /**
     * Sums up the weights of the transitions. Transitions may have functional rates
     *
     * @param transitions
     * @return summed up the weight of the transitions specified
     */
    @Override
    public double getWeightOfTransitions(Iterable<Transition> transitions) {
        double weight = 0;
        PetriNetWeightParser parser = new PetriNetWeightParser(petriNet);
        for (Transition transition : transitions) {
            FunctionalResults<Double> results = parser.evaluateExpression(transition.getRateExpr());
            if (!results.hasErrors()) {
                weight += results.getResult();
            } else {
                //TODO:
            }
        }
        return weight;
    }

    /**
     * @param state
     * @return all enabled transitions for the specified state
     */
    @Override
    public Collection<Transition> getAllEnabledTransitions(ClassifiedState state) {
        Collection<Transition> results = new LinkedList<>();
        for (Collection<Transition> transitions : getSuccessorsWithTransitions(state).values()) {
            results.addAll(transitions);
        }
        return results;
    }

    /**
     * Clears the cache
     */
    @Override
    public void clear() {
        cachedSuccessors.clear();
    }

}