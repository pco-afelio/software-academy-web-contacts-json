package be.afelio.software_academy.pco.controllers.tags;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.afelio.software_academy.pco.controllers.BaseController;
import be.afelio.software_academy.pco.repository.DataRepository;

public class TagController extends BaseController {

	public TagController(DataRepository repository, ObjectMapper mapper) {
		super(repository, mapper);
	}
	
	public void findAllTags(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object o = repository.findAllTags();
		response.getWriter().write(objectToJson(o));
	}
	
	public void findOneTagById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = -1;
		try {
			id = Integer.parseInt(getPathParameter(request.getPathInfo()));
		} catch(NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		Object o = repository.findOneTagById(id);
		response.getWriter().write(objectToJson(o));
	}
	
	public void findOneTagByValue(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String value = getPathParameter(request.getPathInfo());
		Object o = repository.findOneTagByValue(value);
		response.getWriter().write(objectToJson(o));
	}
	
	public void addTag(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String value = request.getParameter("value");
		repository.addTag(value);
		Object o = repository.findOneTagByValue(value);
		response.getWriter().write(objectToJson(o));
	}
	
	public void deleteTagById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = 0;
		try {
			id = Integer.parseInt(getPathParameter(request.getPathInfo()));
		} catch(NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		Object o = null;
		if (id > 0) {
			o = repository.findOneTagById(id);
			if (o != null) {
				repository.deleteTagById(id);
			}
		}
		response.getWriter().write(objectToJson(o));
	}
}
