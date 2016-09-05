package org.sakaiproject.resource.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.util.Web;

/** 
 * <p>
 * Sakai Servlet to use for all Struts tools.
 * </p>
 * 
 * @author UNISA, Sakai Software Development Team
 * @version $Revision: 1.0 $
 */
public class StrutsTool extends HttpServlet {

    /**
     * Eclipse Generated.
     */
    private static final long serialVersionUID = 2143351826440316048L;

    /** Our log (commons). */
    private static Log M_log = LogFactory.getLog(StrutsTool.class);

    /** The file extension to get to STRUTS. */
    protected static final String STRUTS_EXT = ".do";

    /** Session attribute to hold the last view visited. */
    public static final String LAST_VIEW_VISITED = "sakai.struts.tool.last.view.visited";
    
    /** Session attribute to test the state of the session */
    public static final String SESSION_STATE = "sakai.struts.tool.session.state";

    /**
     * Request attribute we set to help the return URL know what extension we
     * (or struts) add (does not need to be in the URL.
     */
    public static final String URL_EXT = "sakai.struts.tool.URL.ext";

    /**
     * Request attribute we set to help the return URL know what path we add
     * (does not need to be in the URL.
     */
    public static final String URL_PATH = "sakai.struts.tool.URL.path";

    /** The default target, as configured. */
    protected String m_default = null;

    /**
     * if true, we preserve the last visit per placement / user, and use it if
     * we get a request with no path.
     */
    protected boolean m_defaultToLastView = false;

    /** The folder to the struts files, as configured. Does not end with a "/". */
    protected String m_path = null;

    /**
     * Compute a target (i.e. the servlet path info, not including folder root
     * or struts extension) for the case of the actual path being empty.
     * 
     * @return The servlet info path target computed for the case of empty
     *         actual path.
     */
    protected String computeDefaultTarget() {
        // setup for the default view as configured
        String target = "/" + m_default;

        // if we are doing lastVisit and there's a last-visited view, for this
        // tool placement / user, use that
        if (m_defaultToLastView) {
            ToolSession session = SessionManager.getCurrentToolSession();
            String last = (String) session.getAttribute(LAST_VIEW_VISITED);
            if (last != null) {
                target = last;
            }
        }
        return target;
    }

    /**
     * Shutdown the servlet.
     */
    public void destroy() {
        M_log.info("destroy");
        super.destroy();
    }

    /**
     * Respond to requests.
     * 
     * @param req
     *            The servlet request.
     * @param res
     *            The servlet response.
     * @throws ServletException.
     * @throws IOException.
     */
    protected void dispatch(
        HttpServletRequest req, 
        HttpServletResponse res
    ) throws ServletException, IOException {
        // NOTE: this is a simple path dispatching, taking the path as the view
        // id = jsp file name for the view,
        // with default used if no path and a path prefix as configured.
        // TODO: need to allow other sorts of dispatching, such as pulling out
        // drill-down ids and making them
        // available to the STRUTS

        // build up the target that will be dispatched to
        String target = req.getPathInfo();

        // see if we have a resource request - i.e. a path with an extension,
        // and one that is not the STRUTS_EXT
        if (isResourceRequest(target)) {
            // get a dispatcher to the path
            RequestDispatcher resourceDispatcher = getServletContext().getRequestDispatcher(target);
            if (resourceDispatcher != null) {
                resourceDispatcher.forward(req, res);
                return;
            }
        }

        // Not being used since 2.0.1 ?!
        if ("Title".equals(req.getParameter("panel"))) {
            // This allows only one Title Struts for each tool
            target = "/title.jsp";
        } else {
            if ((target != null) && (!"/".equals(target)) && (!computeDefaultTarget().equals(target))) {
            	if (req.getSession().getAttribute(SESSION_STATE) == null) {
            		M_log.warn("Invalid state: trying to access "+req.getRequestURI()+" without a session.");
            		target = null;
            	}
            }
            
            req.getSession().setAttribute(SESSION_STATE, Boolean.TRUE);
            
            if (target == null || "/".equals(target)) {
                target = computeDefaultTarget();

                // make sure it's a valid path
                if (!target.startsWith("/")) {
                    target = "/" + target;
                }
                // now that we've messed with the URL, send a redirect to make
                // it official
                res.sendRedirect(Web.returnUrl(req, target));
                return;
            }

            // see if we want to change the specifically requested view
            String newTarget = redirectRequestedTarget(target);

            // make sure it's a valid path
            if (!newTarget.startsWith("/")) {
                newTarget = "/" + newTarget;
            }

            if (!newTarget.equals(target)) {
                // now that we've messed with the URL, send a redirect to make
                // it official
                res.sendRedirect(Web.returnUrl(req, newTarget));
                return;
            }
            target = newTarget;
        }

        // add the configured folder root and extension (if missing)
        target = m_path + target;

        // add the default STRUTS extension (if we have no extension)
        int lastSlash = target.lastIndexOf("/");
        int lastDot = target.lastIndexOf(".");
        if (lastDot < 0 || lastDot < lastSlash) {
            target += STRUTS_EXT;
        }

        // set the information that can be removed from return URLs
        req.setAttribute(URL_PATH, m_path);
        req.setAttribute(URL_EXT, ".jsp");

        // set the sakai request object wrappers to provide the native, not
        // Sakai set up, URL information
        // - this assures that the FacesServlet can dispatch to the proper view
        // based on the path info
        req.setAttribute(Tool.NATIVE_URL, Tool.NATIVE_URL);

        // TODO: Should setting the HTTP headers be moved up to the portal level
        // as well?
        res.setContentType("text/html; charset=UTF-8");
        res.addDateHeader(
            "Expires", 
            System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 365L)
        );
        res.addDateHeader("Last-Modified", System.currentTimeMillis());
        res.addHeader(
            "Cache-Control",
            "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0"
        );
        res.addHeader("Pragma", "no-cache");

        // dispatch to the target
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(target);

        dispatcher.forward(req, res);

        // restore the request object
        req.removeAttribute(Tool.NATIVE_URL);
        req.removeAttribute(URL_PATH);
        req.removeAttribute(URL_EXT);
    }

    /**
     * Respond to requests.
     * 
     * @param req
     *            The servlet request.
     * @param res
     *            The servlet response.
     * @throws ServletException.
     * @throws IOException.
     */
    protected void doGet(
        HttpServletRequest req, 
        HttpServletResponse res
    ) throws ServletException, IOException {
        dispatch(req, res);
    }

    /**
     * Respond to requests.
     * 
     * @param req
     *            The servlet request.
     * @param res
     *            The servlet response.
     * @throws ServletException.
     * @throws IOException.
     */
    protected void doPost(
        HttpServletRequest req, 
        HttpServletResponse res
    ) throws ServletException, IOException {
        dispatch(req, res);
    }

    /**
     * Access the Servlet's information display.
     * 
     * @return servlet information.
     */
    public String getServletInfo() {
        return "Sakai Struts Tool Servlet";
    }

    /**
     * Initialize the servlet.
     * 
     * @param config
     *            The servlet config.
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        m_default = config.getInitParameter("default");
        m_path = config.getInitParameter("path");
        if(m_path==null){
        	m_path = "";
        }
        m_defaultToLastView = "true".equals(config.getInitParameter("default.last.view"));
        M_log.info("Default to last view: "+m_defaultToLastView);

        // make sure there is no "/" at the end of the path
        if (m_path != null && m_path.endsWith("/")) {
            m_path = m_path.substring(0, m_path.length() - 1);
        }
    }

    /**
     * Recognize a path that is a resource request. It must have an "extension",
     * i.e. a dot followed by characters that do not include a slash.
     * 
     * @param path
     *            The path to check
     * @return true if the path is a resource request, false if not.
     */
    protected boolean isResourceRequest(String path) {
        // we need some path
        if ((path == null) || (path.length() == 0))
            return false;

        // we need a last dot
        int pos = path.lastIndexOf(".");
        if (pos == -1)
            return false;

        // we need that last dot to be the end of the path, not burried in the
        // path somewhere (i.e. no more slashes after the last dot)
        String ext = path.substring(pos);
        if (ext.indexOf("/") != -1)
            return false;

        // we need the ext to not be the STRUTS_EXT
        if (ext.equals(STRUTS_EXT))
            return false;

        // ok, it's a resource request
        return true;
    }

    /**
     * Compute a new target (i.e. the servlet path info, not including folder
     * root or struts extension) if needed based on the requested target.
     * 
     * @param target
     *            The servlet path info target requested.
     * @return The target we will actually respond with.
     */
    protected String redirectRequestedTarget(String target) {
        return target;
    }
}