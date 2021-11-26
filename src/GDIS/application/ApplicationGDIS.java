package GDIS.application;

import GDIS.engine.ConfigurationGDIS;
import GDIS.engine.WindowGDIS;
import GDIS.engine.program.SimulationProgram;
import GDIS.engine.program.ProgramGDIS;
import GDIS.engine.program.state.StateMachine;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public abstract class ApplicationGDIS
{
    public abstract void setConfigurations(ConfigurationGDIS configurations);
    public abstract void setStates(StateMachine stateMachine);

    public SimulationProgram getProgram() { return new ProgramGDIS(); }

    protected void startApplication(String[] args)
    {
        ConfigurationGDIS configuration = new ConfigurationGDIS();
        setConfigurations(configuration);

        WindowGDIS windowGDIS = new WindowGDIS(configuration);

        SimulationProgram program = getProgram();
        windowGDIS.setProgram(program);

        setStates(program.getStateMachine());

        windowGDIS.show();
    }
}
