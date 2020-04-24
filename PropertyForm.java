import static j2html.TagCreator.*;
import j2html.tags.Tag;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PropertyForm extends HttpServlet {

    /**
     * Convenience method to create a radio button set for a property type.
     *
     * @return the HTML5 DOM tag for a radio button set
     */
    private static Tag propertyType() {
        HashMap<String,String> types = new HashMap<>();
        types.put("A", "Apartment");
        types.put("S", "Single family");
        types.put("V", "VRBO or AirBnB");
        return FormUtils.radioSet("Property type", "propertytype", types);
    }

    /**
     * Draw an empty property data entry form.
     *
     * @param client the connection to the web client
     * @throws IOException
     */
    private void renderPropertyForm(PrintWriter client) throws IOException {
        String title = "Form data";
        html(
                head(title(title)),
                body(
                        h1("Property data entry"),
                        form().withMethod("post").with(   //Type, CityCode, Address, ZipCode, BedCount
                                p(
                                        span("Address: "), FormUtils.textboxInput("address", "123 4th NW"), br(),
                                        span("City: "), FormUtils.textboxInput("city", "Albuquerque"), br(),
                                        span("ZipCode: "), FormUtils.textboxInput("zipCode", "88011"), br(),
                                        span("BedCount: "), FormUtils.textboxInput("bedCount", "2"), br(),
                                        propertyType(), br()
                                ),
                                FormUtils.submitButton("Submit")
                        )
                )
        ).render(client);
    }
    //span("Email: "), emailInput("user@host.com"), br(),

    /**
     * Process data from the form.  This method must be consistent with
     * printForm.
     *
     * @param request Class with data about the request
     * @param client the connection to the web client
     * @throws IOException
     */
    private void processPropertyForm(HttpServletRequest request, PrintWriter client) throws IOException {
        HashMap<String, String> params = new HashMap<>();
        var iter = request.getParameterNames().asIterator();
        while (iter.hasNext()) {
            String name = iter.next();
            params.put(name, request.getParameter(name));
        }

        // @@@ Can work with parameters as appropriate here.

        // Demo showing more j2html and lambda opertions.
        String title = "POST results";
        html(
                head(title(title)),
                body(
                        h1(title),
                        h2("Parameters: "),
                        table(
                                tbody(
                                        tr(th("name"), th("value")),
                                        each(params.keySet(), k -> {
                                            return tr(td(k), td(params.get(k)));
                                        })
                                )
                        ),
                        h3(p(a("Return to main page").withHref("/")))
                )
        ).render(client);
    }

    /**
     * Primary worker that processes a request.  Decides based on the presence
     * of form data whether to draw the form or process the data.
     *
     * @param request Class with data about the request
     * @param response Class with data about the response.
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK); // is everything really OK?

        PrintWriter client = response.getWriter();
        client.println(FormUtils.docType);

        if (request.getParameterNames().asIterator().hasNext()) {
            processPropertyForm(request, client);
        } else {
            renderPropertyForm(client);
        }
    }

    /**
     * Method to handle POST method request by passing it to doGet
     * @param request Class with data about the request
     * @param response Class with data about the response.
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}