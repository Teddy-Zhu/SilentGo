import com.silentgo.kit.logger.Logger;
import com.silentgo.kit.logger.LoggerFactory;
import com.silentgo.servlet.SilentGoFilter;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


public class Server {

    private static final Logger LOGGER = LoggerFactory.getLog(Server.class);


    private org.eclipse.jetty.server.Server server;

    private ServletContextHandler context;

    public void start(String contextPath) throws Exception {

        server = new org.eclipse.jetty.server.Server(8080);

        server.setStopAtShutdown(true);

        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(contextPath);

        context.setResourceBase(Thread.currentThread().getContextClassLoader()
                .getResource("").getPath());

        FilterHolder filterHolder = new FilterHolder(SilentGoFilter.class);
        filterHolder.setAsyncSupported(false);

        context.addFilter(filterHolder, "/", EnumSet.allOf(DispatcherType.class));
        server.setHandler(this.context);
        server.start();

        LOGGER.info("Started On http://localhost:8080");
    }


}
