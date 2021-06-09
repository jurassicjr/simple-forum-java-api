package br.com.alura.forum.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controllers.dtos.TopicoDTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.TopicoRepository;

@RestController
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@RequestMapping("/topicos")
	public List<TopicoDTO> lista(String nomeCurso) {
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();		
			return TopicoDTO.converter(topicos);	
		}
		List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);		
		return TopicoDTO.converter(topicos);	
		
	}
}
