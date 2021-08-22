package br.com.alura.forum.controllers;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controllers.dtos.DetalhesDoTopicoDTO;
import br.com.alura.forum.controllers.dtos.TopicoDTO;
import br.com.alura.forum.controllers.forms.AtualizacaoTopicoForm;
import br.com.alura.forum.controllers.forms.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repositories.CursoRepository;
import br.com.alura.forum.repositories.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	@Cacheable(value = "topicList")
	public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable paginacao) {

		/*
		 * Pageable paginacao = PageRequest.of(pagina,
		 * quantidadeDeElementos,Direction.ASC, ordenacao); Pode ser feito dessa forma
		 * manualmente ou de forma automatizada recebendo um pageable no método, porém
		 * precisa habilitar um módulo no spring boot na classe main
		 * (@EnableSpringDataWebSupport).
		 * 
		 * Métodos antingos @RequestParam int pagina,
		 * 
		 * @RequestParam int quantidadeDeElementos, @RequestParam String ordenacao
		 */

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDTO.converter(topicos);
		}
		Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
		return TopicoDTO.converter(topicos);

	}

	@Transactional
	@PostMapping
	@CacheEvict(value = "topicList", allEntries = true)
	public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

		return ResponseEntity.created(uri).body(new TopicoDTO(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "topicList", allEntries = true)
	public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> topico = topicoRepository.findById(id);

		if (topico.isPresent()) {

			Topico updatedTopico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDTO(updatedTopico));

		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "topicList", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {

		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();

	}
}
