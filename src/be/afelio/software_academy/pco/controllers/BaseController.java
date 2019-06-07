package be.afelio.software_academy.pco.controllers;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import be.afelio.software_academy.pco.repository.DataRepository;

public abstract class BaseController {

	protected DataRepository repository;
	protected ObjectMapper mapper;
	
	public BaseController(DataRepository repository, ObjectMapper mapper) {
		super();
		this.repository = repository;
		this.mapper = mapper;
	}
	
	protected String[] getPathParameters(String path) {
		String[] parts = path.substring(1).split("/");
		String[] parameters = new String[parts.length-1];
		System.arraycopy(parts, 1, parameters, 0, parameters.length);
		return parameters;
	}
	
	protected String getPathParameter(String path) {
		String[] parameters = getPathParameters(path);
		if (parameters.length >= 1) {
			return parameters[parameters.length-1];
		}
		return null;	
	}
	
	protected String objectToJson(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch(JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	protected <T> T jsonStreamToObject(InputStream jsonStream, Class<T> type) {
		try {
			return mapper.readValue(jsonStream, type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
}
