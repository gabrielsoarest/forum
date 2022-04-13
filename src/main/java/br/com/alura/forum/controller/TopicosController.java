package br.com.alura.forum.controller;

import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.dto.DetalhesTopicoDTO;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.dto.TopicoDTO;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDTO> lista(String nomeCurso){

        if(nomeCurso==null){
            List<Topico> topicos = topicoRepository.findAll();
            return TopicoDTO.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
            return TopicoDTO.converter(topicos);
        }

    }
    @PostMapping()
    @Transactional
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){

        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity <DetalhesTopicoDTO> detalhar(@PathVariable Long id){
        Optional<Topico> optional = this.topicoRepository.findById(id);

        if(optional.isPresent()){
              return  ResponseEntity.ok(new DetalhesTopicoDTO(optional.get()));
        }

        return ResponseEntity.notFound().build();

    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
        Optional<Topico> optional = this.topicoRepository.findById(id);
        if(optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id){
        Optional<Topico> optional = this.topicoRepository.findById(id);
        if(optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
