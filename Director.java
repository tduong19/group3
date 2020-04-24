import static j2html.TagCreator.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Director extends HttpServlet {

    /**
     * Print the directory of tasks for the slumlords app.  Note that this 
     * should be dynamically built from a more well thought out class hierarchy 
     * and data structure.  This is too static and is a bit brittle if things 
     * change.  In particular, this needs to be tied to what happens in 
     * WebServer.java.
     *
     * @param request Class with data about the request
     * @param response Class with data about the response.
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter client = response.getWriter();
        client.println(FormUtils.docType);
        String title = "Slumlords web app directory";
        html(
                head(title(title)),
                body(
                        h1(title),
                        img().withSrc("C:\\Users\\Jacob\\IdeaProjects\\HTML\\SlumLordsExample.jpg"),
                        p(a("Property data entry").withHref("/property")),
                        p(a("Tenant data entry").withHref("/tenant"))
                )
        ).render(client);
    }
}