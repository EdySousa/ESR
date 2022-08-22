package com.algaworks.algafood.domain.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoRepository.buscar(estadoId);

		if (estado == null) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe cadastro de estado com o código %d", estadoId));
		}

		cidade.setEstado(estado);
		return cidadeRepository.salvar(cidade);
	}

	public Cidade atualizar(Cidade cidadeAtual, Cidade cidade) {
		BeanUtils.copyProperties(cidade, cidadeAtual, "id");

		return salvar(cidadeAtual);
	}

	public void excluir(Long id) {

		try {
			cidadeRepository.remover(id);

		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("Não existe um cadastro de Cidade com código %d", id));

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Cidade de código %d não pode ser removida, pois está em uso");
		}

	}

}
