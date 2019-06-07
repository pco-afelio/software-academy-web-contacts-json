package be.afelio.software_academy.pco;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.afelio.software_academy.pco.controllers.contacts.ContactController;
import be.afelio.software_academy.pco.controllers.countries.CountryController;
import be.afelio.software_academy.pco.controllers.tags.TagController;
import be.afelio.software_academy.pco.repository.DataRepository;

@WebServlet("/json/*")
public class FrontController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected TagController tagController;
	protected CountryController countryController;
	protected ContactController contactController;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			Class.forName("org.postgresql.Driver");
			String path = getServletContext().getRealPath("/WEB-INF/conf/database.properties");
			Properties properties = new Properties();
			try (
				InputStream in = new FileInputStream(path)
			) {
				properties.load(in);
			}
			String url = properties.getProperty("database.url");
			String user = properties.getProperty("database.user");
			String password = properties.getProperty("database.password");
			
			DataRepository repository = new DataRepository(url, user, password);
			ObjectMapper mapper = new ObjectMapper();
			
			tagController = new TagController(repository, mapper);
			countryController = new CountryController(repository, mapper);
			contactController = new ContactController(repository, mapper);
			
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			response.setContentType("application/json");
			if (pathInfo.startsWith("/tag/id/")) {
				tagController.findOneTagById(request, response);
			} else if (pathInfo.startsWith("/tag/value/")) {
				tagController.findOneTagByValue(request, response);
			} else if (pathInfo.startsWith("/country/abbreviation/")) {
				countryController.findOneCountryByAbbreviation(request, response);
			} else if (pathInfo.startsWith("/country/name/")) {
				countryController.findOneCountryByName(request, response);
			} else if (pathInfo.equals("/country/all")) {
				countryController.findAllCountries(request, response);
			} else if (pathInfo.equals("/tag/all")) {
				tagController.findAllTags(request, response);
			} else if (pathInfo.equals("/contact/all")) {
				contactController.findAllContact(request, response);
			} else if (pathInfo.startsWith("/contact/")) {
				contactController.findOneContactByFirstnameAndName(request, response);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			response.setContentType("application/json");
			switch (pathInfo) {
			case "/tag":
				tagController.addTag(request, response);
				break;
			case "/country":
				countryController.addCountry(request, response);
				break;
			case "/contact":
				contactController.addContact(request, response);
				break;				
			default:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}			
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			response.setContentType("application/json");
			if (pathInfo.startsWith("/tag/")) {
				tagController.deleteTagById(request, response);
			} else if (pathInfo.startsWith("/country/")) {
				countryController.deleteCountryById(request, response);
			} else if (pathInfo.startsWith("/contact/")) {
				contactController.deleteContactById(request, response);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}			
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
