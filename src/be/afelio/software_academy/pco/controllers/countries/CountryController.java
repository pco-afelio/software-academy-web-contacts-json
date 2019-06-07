package be.afelio.software_academy.pco.controllers.countries;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.afelio.software_academy.pco.beans.Country;
import be.afelio.software_academy.pco.controllers.BaseController;
import be.afelio.software_academy.pco.repository.DataRepository;

public class CountryController extends BaseController {

	public CountryController(DataRepository repository, ObjectMapper mapper) {
		super(repository, mapper);
	}

	public void findOneCountryByAbbreviation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String abbreviation = getPathParameter(request.getPathInfo());
		Object o = repository.findOneCountryByAbbreviation(abbreviation);
		response.getWriter().write(objectToJson(o));
	}
	
	public void findOneCountryByName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = getPathParameter(request.getPathInfo());
		Object o = repository.findOneCountryByName(name);
		response.getWriter().write(objectToJson(o));	
	}
	
	public void findAllCountries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object o = repository.findAllCountries();
		response.getWriter().write(objectToJson(o));
	}
	
	public void addCountry(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Country country = null;
		try {
			country = jsonStreamToObject(request.getInputStream(), Country.class);
		} catch(Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		if (country != null) {
			repository.addCountry(country.getName(), country.getAbbreviation());
			country = repository.findOneCountryByName(country.getName());
		} 
		response.getWriter().write(objectToJson(country));
	}

	public void deleteCountryById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = 0;
		try {
			id = Integer.parseInt(getPathParameter(request.getPathInfo()));
		} catch(NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		Object o = null;
		if (id > 0) {
			o = repository.findOneCountryById(id);
			if (o != null) {
				repository.deleteCountryById(id);
			}
		}
		response.getWriter().write(objectToJson(o));
	}
}
