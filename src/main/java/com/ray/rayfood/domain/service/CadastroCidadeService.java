package com.ray.rayfood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.ray.rayfood.domain.entities.Cidade;
import com.ray.rayfood.domain.entities.Estado;
import com.ray.rayfood.domain.exception.EntidadeEmUsoException;
import com.ray.rayfood.domain.exception.EntidadeNaoEncontradaException;
import com.ray.rayfood.domain.repository.CidadeRepository;
import com.ray.rayfood.domain.repository.EstadoRepository;

@Service
public class CadastroCidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade) throws EntidadeNaoEncontradaException {
	Long estadoId = cidade.getEstado().getId();
	Estado estado = estadoRepository.findById(estadoId).orElseThrow(() -> new EntidadeNaoEncontradaException("Não existe Estado cadastrado com id " + estadoId));
	cidade.setEstado(estado);
	return this.cidadeRepository.save(cidade);
    }
    
    public void remover(Long id) throws EntidadeNaoEncontradaException, EntidadeEmUsoException {
	try {
	    this.cidadeRepository.deleteById(id);
	}catch (EmptyResultDataAccessException e) {
	    throw new EntidadeNaoEncontradaException("Não existe cidade cadastrada com id " + id);
	}catch (DataIntegrityViolationException e) {
	    throw new EntidadeEmUsoException(String.format("Cidade de código %d não pode ser removida, pois está em uso", id));
	}
    }
    
}
