package GDIS.engine.program.state;

import GDIS.engine.program.SimulationProgram;
import GDIS.tools.debugger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class StateMachine
{
    private SimulationProgram program;

    private Map<String, State> states;
    private String initState;

    private Stack<String> stateStack;

    private boolean initialize;

    public StateMachine(SimulationProgram simulationProgram)
    {
        states = new HashMap<>();
        stateStack = new Stack<>();
        this.program = simulationProgram;
    }

    public void setInitializeState(String initState)
    {
        if(!states.containsKey(initState))
            throw new IllegalArgumentException("State '" + initState + "' is not exists");
        this.initState = initState;
    }

    public void initialize()
    {
        if(initState == null)
            throw new IllegalStateException("Initialize state (name) has not been set");
        if(initialize)
            throw new IllegalStateException("State Machine has already been initialized");

        pushStateToStack(initState);

        initialize = true;
    }

    public void addState(String name, State state)
    {
        if(name == null) throw new IllegalArgumentException("name cannot be null");
        if(this.states.containsKey(name)) throw new IllegalArgumentException("State '" + name + "' already exists");

        String s = "Adding state '" + name + "' <" + state.getClass() + "> ";
        if(state instanceof StateGDIS)
        {
            // TODO: how to receive Environment vars from program?
            ((StateGDIS) state).setEnvVars(this,getProgram().getConfiguration());
            s += "(StateGDIS) ";
        }
        s += "into the machine.";
        Logger.get(getClass()).log(Logger.Level.INFO,s);
        this.states.put(name, state);
    }

    public void changeState(String name)
    {
        if(name == null)
        {
            popCurrentState();
            return;
        }

        if(!this.states.containsKey(name))
        {
            throw new IllegalArgumentException("State '" + name + "' does not exists");
        }

        if(!stateStack.isEmpty())
        {
            String outProgram = stateStack.pop();
            states.get(outProgram).exit();
        }

        stateStack.push(name);
        states.get(name).enter();
    }

    public void pushStateToStack(String name)
    {
        if(!this.states.containsKey(name))
        {
            throw new IllegalArgumentException("State '" + name + "' does not exists");
        }

        Logger.get(getClass()).log(Logger.Level.INFO,"Starting state '" + name + "' (Push it into top of state-stack).");
        if(!stateStack.isEmpty()) getCurrent().exit();
        stateStack.push(name);
        states.get(name).enter();
    }

    public State popCurrentState()
    {
        if(this.stateStack.isEmpty()) return null;

        Logger.get(getClass()).log(Logger.Level.INFO,"End current state '" + stateStack.peek() + "' (Pop top state from state-stack).");
        State outState = states.get(stateStack.pop());
        outState.exit();

        State inState = getCurrent();
        if(inState != null) inState.enter();

        return outState;
    }

    public State getCurrent()
    {
        if(this.stateStack.isEmpty()) return null;
        return this.states.get(this.stateStack.peek());
    }

    public boolean hasActiveState()
    {
        return !this.stateStack.isEmpty();
    }

    public SimulationProgram getProgram()
    {
        return program;
    }

    // Program State Interface

    public boolean update(float delta)
    {
        State current = getCurrent();

        if(current != null)
        {
            try
            {
                boolean isStateOver = !current.updateState(delta);
                if(isStateOver) popCurrentState();
            }
            catch (Exception e)
            {
                Logger.get(getClass()).log(Logger.Level.ERROR,"Caught an Exception from the current state while 'update'");
//                System.err.println("INFO <State-Machine> Caught an Exception from the current state while 'update' (pop state from machine).");
                e.printStackTrace();
                popCurrentState();
            }
        }

        return hasActiveState();
    }

    public void render(float alpha)
    {
        State current = getCurrent();

        if(current != null)
        {
            try
            {
                current.render(alpha);
            }
            catch (Exception e)
            {
                Logger.get(getClass()).log(Logger.Level.ERROR,"Caught an Exception from the current state while 'render'");
                e.printStackTrace();

//                System.err.println("State-Machine: Caught an Exception from the current state while 'render' (pop state from machine):");
                popCurrentState();

            }
        }
    }

    public void handleInput()
    {
        State current = getCurrent();

        if(current != null)
        {
            try
            {
                current.handleInput();
            }
            catch (Exception e)
            {
                Logger.get(getClass()).log(Logger.Level.ERROR,"Caught an Exception from the current state while 'handleInput'");
//                System.err.println("State-Machine: Caught an Exception from the current state while 'handleInput' (pop state from machine):");
                e.printStackTrace();

                popCurrentState();
            }
        }
    }
}
