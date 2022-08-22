package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	@Autowired
	private EstadoRepository estadoRepository;

	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.listar();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Estado> buscar(@PathVariable("id") Long id) {
		Estado estado = estadoRepository.buscar(id);

		return estado != null ? ResponseEntity.ok(estado) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Estado> salvar(@RequestBody Estado estado) {
		estado = estadoRepository.salvar(estado);

		return ResponseEntity.status(HttpStatus.CREATED).body(estado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Estado> atualizar(@PathVariable Long id, @RequestBody Estado estado) {
		Estado estadoAtual = estadoRepository.buscar(id);

		if (estadoAtual != null) {
			BeanUtils.copyProperties(estado, estadoAtual, "id");

			estadoRepository.salvar(estadoAtual);
			return ResponseEntity.ok(estadoAtual);
		}

		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id){
		Estado estadoAtual = estadoRepository.buscar(id);

		if (estadoAtual != null) {

			estadoRepository.remover(estadoAtual);
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.notFound().build();
	}
	
}
