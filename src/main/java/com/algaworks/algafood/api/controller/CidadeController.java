package com.algaworks.algafood.api.controller;

import java.util.List;

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

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.listar();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long id) {

		Cidade cidade = cidadeRepository.buscar(id);

		if (cidade != null) {
			return ResponseEntity.ok(cidade);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody Cidade cidade) {

		try {
			cidade = cidadeService.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {

		try {

			Cidade cidadeAtual = cidadeRepository.buscar(id);

			if (cidadeAtual != null) {
				cidade = cidadeService.atualizar(cidadeAtual, cidade);
				return ResponseEntity.ok(cidade);
			}

			return ResponseEntity.notFound().build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			cidadeService.excluir(id);
			return ResponseEntity.noContent().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
