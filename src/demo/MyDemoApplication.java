package demo;

import GDIS.application.ApplicationGDIS;
import GDIS.engine.ConfigurationGDIS;
import GDIS.engine.program.state.StateMachine;

/**
 * Created By: Assaf, On 06/11/2021
 * Description:
 */
public class MyDemoApplication extends ApplicationGDIS
{

    public static void main(String[] args) {
        MyDemoApplication application = new MyDemoApplication();
        application.startApplication(args);
    }

    @Override
    public void setConfigurations(ConfigurationGDIS configurations)
    {
        configurations.setTitle("My First Demo With - GDIS");
    }

    @Override
    public void setStates(StateMachine stateMachine) {
        stateMachine.addState("Demo",new MyDemoState());

        stateMachine.setInitializeState("Demo");
    }
}
