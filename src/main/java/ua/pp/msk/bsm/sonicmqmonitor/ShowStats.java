package ua.pp.msk.bsm.sonicmqmonitor;

import com.mercury.infra.bus.monitor.MonitorBusJMX;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Hello world!
 *
 */
public class ShowStats 
{
    
    private JMXServiceURL url = null;
    private String mBeanName = "Foundations:service=MonitorBus";
    private static Options options = new Options();
    static {
        options.addOption("h", "host", true, "JMX host");
        options.addOption("p", "port", true, "JMX port");
        options.addOption("u", "url", true, "JMX Resource URL");
        options.addOption("f", "format", false, "Format output");
        options.addOption("q", "queue", true, "Extract queue length");
        options.addOption("help", false, "Show this help information");
        
    }
    
    private static void usage(){
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("java -jar SonicMQMonitor [options]\nHost is like bsm-gw1.sdab.sn\nPort is like 29601", options);
    }
    
    private String getQueueInfo() throws IOException, MalformedObjectNameException{
        JMXConnector jmxc = JMXConnectorFactory.connect(url);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        MonitorBusJMX mbJMX = JMX.newMBeanProxy(mbsc, new ObjectName(mBeanName), MonitorBusJMX.class, false);
        String result = mbJMX.showQueueInfo();
        jmxc.close();
        return result;
    }
    
    public static void main( String[] args ) throws ParseException, MalformedURLException
    {
        CommandLineParser parser  = new GnuParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("help")) {
            usage();
            System.exit(0);
        }   
        ShowStats showStats = new ShowStats();
        if (cmd.hasOption("url")){
            showStats.url = new JMXServiceURL(cmd.getOptionValue("url"));
        } else if (cmd.hasOption("host") && cmd.hasOption("port")){
            showStats.url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://"+ cmd.getOptionValue("host") + ":"+ cmd.getOptionValue("port")+"/jmxrmi");
        } else {
            usage();
            Logger.getLogger(showStats.getClass().getName()).severe("You should provide connection url or host and port at least");
            System.exit(1);
        }
        try {
            String info = showStats.getQueueInfo();
            if (cmd.hasOption("f")) {
            System.out.println(info.replace("<BR>", "\n"));
        } else if (cmd.hasOption("q")){
            
            Pattern pattern;
            String ptrn = ".*Queue Name: "+cmd.getOptionValue("q")+" Message Count: (\\d+) .*$";
                pattern = Pattern.compile(ptrn);
            Matcher matcher = pattern.matcher(info);
            String queueInfo = null;
            if (matcher.matches()) {
                queueInfo = matcher.group(1);
            } else {
                Logger.getLogger(ShowStats.class.getName()).log(Level.SEVERE, "Cannot match queue name");
            }
            if (queueInfo != null){
                System.out.print(Integer.parseInt(queueInfo));
            }
            
        }else {
            System.out.println(info);
        }
        } catch (IOException ex) {
            Logger.getLogger(ShowStats.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedObjectNameException ex) {
            Logger.getLogger(ShowStats.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
