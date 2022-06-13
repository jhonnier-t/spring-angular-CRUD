package com.clientes.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clientes.springboot.backend.apirest.models.entity.Cliente;
import com.clientes.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping(path = "/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clients")
	public List<Cliente> index() {
		return clienteService.findAll();
	}
	
	@GetMapping("/clients/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 2);
		return clienteService.findAll(pageable);
	}

	@GetMapping(path = "/clients/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("msg", "The client cann't created");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (clienteService.findById(id) == null) {
			response.put("msg", "The client with ID " + id.toString() + " doesn't exist in databases");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	@PostMapping("/clients")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Cliente cliente) {
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		try {
			clienteNew = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("msg", "Client wasn't created");
			response.put("mesage", "Error:" + e.getMessage() + " " + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("msg", "The client created sucessfully");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/clients/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if (clienteActual == null) {
			response.put("msg", "Can't update database");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			clienteActual.setName(cliente.getName());
			clienteActual.setLastName(cliente.getLastName());
			clienteActual.setEmail(cliente.getEmail());

			clienteUpdated = clienteService.save(clienteActual);

		} catch (DataAccessException e) {
			response.put("msg", "Client wasn't updated");
			response.put("mesage", "Error:" + e.getMessage() + " " + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("msg", "The client updated sucessfully");
		response.put("Client:  ", clienteUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/clients/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();
		try {
			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("msg", "The client deleted sucessfully");
			response.put("mesage", "Error:" + e.getMessage() + " " + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("msg", "The client deleted sucessfully");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
