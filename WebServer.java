import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;


public class WebServer {

    public static void main(String[] args) throws Exception {
        // Create a basic jetty server object that will listen on port 8080.
        Server server = new Server(8085);

        // The ServletHandler is a dead simple way to create a context handler
        // that is backed by an instance of a Servlet.
        // This handler then needs to be registered with the Server object.
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        // Passing in the class for the Servlet allows jetty to instantiate an
        // instance of that Servlet and mount it on a given context path.
        // IMPORTANT:
        // This is a raw Servlet, not a Servlet that has been configured
        // through a web.xml @WebServlet annotation, or anything similar.
        handler.addServletWithMapping(PropertyForm.class, "/property/*");
        handler.addServletWithMapping(TenantForm.class, "/tenant/*");
        handler.addServletWithMapping(Director.class, "/");

        // Alternate, more general approach.  Not finished.
        // See https://git.eclipse.org/c/jetty/org.eclipse.jetty.project.git/tree/examples/embedded/src/main/java/org/eclipse/jetty/embedded/ManyHandlers.java
//        ContextHandler context = new ContextHandler();
//        context.setContextPath("/hello");
//        context.setHandler(new HelloHandler());

        // Note that a server is a thread.  Start it.
        server.start();

        // Wait for the server thread to finish.
        server.join();
    }

}
