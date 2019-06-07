package be.afelio.software_academy.pco.controllers.contacts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.afelio.software_academy.pco.beans.Contact;
import be.afelio.software_academy.pco.beans.Country;
import be.afelio.software_academy.pco.controllers.BaseController;
import be.afelio.software_academy.pco.repository.DataRepository;

public class ContactController extends BaseController {

	public ContactController(DataRepository repository, ObjectMapper mapper) {
		super(repository, mapper);
	}
	
	public void findOneContactByFirstnameAndName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstname = null;
		String name = null;
		try {
			String[] parameters = getPathParameters(request.getPathInfo());
			firstname = parameters[0];
			name = parameters[1];
		} catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		if (firstname != null && name != null) {
			Object o = repository.findOneContactByFirstnameAndName(firstname, name);
			response.getWriter().write(objectToJson(o));
		}
	}
	
	public void findAllContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object o = repository.findAllContact();
		response.getWriter().write(objectToJson(o));
	}
	
	public void addContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CreateContactRequestData data = null;
		try {
			data = jsonStreamToObject(request.getInputStream(), CreateContactRequestData.class);
		} catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		Contact contact = null;
		if (repository.findOneContactByFirstnameAndName(data.getFirstname(), data.getName()) == null) {
			Integer countryId = null;
			if (data.getCountryName() != null) {
				Country country = repository.findOneCountryByName(data.getCountryName());
				if (country != null) {
					countryId = country.getId();
				}
			}
			if (data.getCountryName() == null || countryId != null) {
				repository.addContact(data.getFirstname(), data.getName(), data.getEmail(), countryId, data.getTagIds());
				contact = repository.findOneContactByFirstnameAndName(data.getFirstname(), data.getName());
			}
		}
		response.getWriter().write(objectToJson(contact));
	}
	
	public void deleteContactById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = 0;
		try {
			id = Integer.parseInt(getPathParameter(request.getPathInfo()));
		} catch(NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		Object o = null;
		if (id > 0) {
			o = repository.findOneContactById(id);
			if (o != null) {
				repository.deleteContactById(id);
			}
		}
		response.getWriter().write(objectToJson(o));
	}

}
