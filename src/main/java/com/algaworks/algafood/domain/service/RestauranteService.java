package com.algaworks.algafood.domain.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CozinhaRepository cozinhaRepository;

	public List<Restaurante> listar() {
		return restauranteRepository.listar();
	}

	public Restaurante buscar(Long id) {
		return restauranteRepository.buscar(id);
	}

	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de cozinha com o código %d", cozinhaId));
		}

		restaurante.setCozinha(cozinha);

		return restauranteRepository.salvar(restaurante);
	}

	public Restaurante atualizar(Restaurante restauranteAtual, Restaurante restaurante) {
		BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
		
		return salvar(restauranteAtual);
	}

	public void excluir(Long id) {
		try {
			restauranteRepository.remover(id);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de Restaurante com código %d", id));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("Restaurante de código %d não pode ser removida, pois está em uso", id));
		}

	}

}
