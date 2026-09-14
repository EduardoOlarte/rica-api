package com.rica.ricaapi.publicaciones;

import com.rica.ricaapi.compartido.RecursoNoEncontradoException;
import com.rica.ricaapi.investigadores.Investigador;
import com.rica.ricaapi.investigadores.InvestigadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final InvestigadorRepository investigadorRepository;
    private final LimitePublicacionesAnualesService limitePublicacionesAnualesService;

    public PublicacionService(
            PublicacionRepository publicacionRepository,
            InvestigadorRepository investigadorRepository,
            LimitePublicacionesAnualesService limitePublicacionesAnualesService) {

        this.publicacionRepository = publicacionRepository;
        this.investigadorRepository = investigadorRepository;
        this.limitePublicacionesAnualesService = limitePublicacionesAnualesService;
    }

    public Publicacion registrar(Publicacion publicacion) {

        Investigador investigador = investigadorRepository
                .findByCorreoInstitucional_Valor(publicacion.getInvestigadorCorreo())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un investigador con correo "
                                + publicacion.getInvestigadorCorreo()));

        if (!limitePublicacionesAnualesService
                .puedeRegistrar(investigador, publicacion)) {

            throw new LimiteAnualExcedidoException(
                    "El investigador no puede registrar más de 5 publicaciones en el mismo año");
        }

        return publicacionRepository.save(publicacion);
    }

    public List<Publicacion> listarPorInvestigador(String investigadorCorreo) {
        return publicacionRepository.findByInvestigadorCorreo(investigadorCorreo);
    }

    public Publicacion buscarPorId(String id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una publicación con id " + id));
    }

}