package GDIS.tools.debugger;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Assaf, On 11/11/2021
 * Description:
 */
public class Logger
{
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static Map<Class,Logger> loggerMap = new HashMap<>();

    public static boolean debug = true;

    private Class logClass;
    private int minLevelToLog = Level.INFO.ordinal();
    private PrintStream destination = System.out; // new PrintStream("fileName");

    public enum Level
    {
        DEBUG, INFO, WARNING, ERROR
    }

    private Logger(Class c)
    {
        logClass = c;
    }

    public static Logger get(Class c)
    {
        if(!loggerMap.containsKey(c)) loggerMap.put(c,new Logger(c));

        return loggerMap.get(c);
    }

    public Logger setMinLevelToLog(Level levelToLog) {
        this.minLevelToLog = levelToLog.ordinal();
        return this;
    }

    public void log(Level level, String txt)
    {
        if(!debug || level.ordinal() < this.minLevelToLog) return;
        LocalDateTime now = LocalDateTime.now();
        destination.println(dtf.format(now) + " -- " + level + " -- <" + logClass.getName() + "> -- : " + txt);
    }

    public static void dispose()
    {
        loggerMap.clear();
    }
}
