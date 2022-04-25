package com.algaworks.algalog.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algalog.api.assembler.EntregaMapper;
import com.algaworks.algalog.api.model.EntregaModel;
import com.algaworks.algalog.domain.model.Entrega;
import com.algaworks.algalog.domain.model.input.EntregaInput;
import com.algaworks.algalog.domain.repository.EntregaRepository;
import com.algaworks.algalog.domain.service.SolicitacaoEntregaService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/entregas")
public class EntregaController {
	
	private EntregaRepository entregaRepository;
	private SolicitacaoEntregaService solicitacaoEntregaService;
	private EntregaMapper entregaMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EntregaModel solicitar(@Valid @RequestBody EntregaInput entregaInput) {
		Entrega novaEntrega = entregaMapper.toEntity(entregaInput);		
		Entrega entregaSolicitada = solicitacaoEntregaService.solicitar(novaEntrega);
		return entregaMapper.toModel(entregaSolicitada);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<EntregaModel> listarEntregas(){
		return entregaMapper.toCollectionModel(entregaRepository.findAll());
	}
	
	@GetMapping("/{entregaid}")
	public ResponseEntity<EntregaModel> buscar(@PathVariable Long entregaid){
		return entregaRepository.findById(entregaid)
				.map(entrega -> ResponseEntity.ok(entregaMapper.toModel(entrega)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{entregaid}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteEntrega(@PathVariable Long entregaid){
		if(!entregaRepository.existsById(entregaid)) {
			return ResponseEntity.notFound().build();
		}
		
		entregaRepository.deleteById(entregaid);
		
		return ResponseEntity.noContent().build();
	}
}
