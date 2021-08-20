package com.example.abnerlucss.msvticket.services;

import com.example.abnerlucss.msvticket.DTOs.*;
import com.example.abnerlucss.msvticket.exceptions.CreateException;
import com.example.abnerlucss.msvticket.exceptions.NotFoundException;
import com.example.abnerlucss.msvticket.mappers.EmbarqueMapper;
import com.example.abnerlucss.msvticket.mappers.PassagemCompradaMapper;
import com.example.abnerlucss.msvticket.mappers.PassagemMapper;
import com.example.abnerlucss.msvticket.models.Classe;
import com.example.abnerlucss.msvticket.models.CompanhiaAerea;
import com.example.abnerlucss.msvticket.models.Passageiro;
import com.example.abnerlucss.msvticket.models.Passagem;
import com.example.abnerlucss.msvticket.repositories.ClasseRepository;
import com.example.abnerlucss.msvticket.repositories.CompanhiaAereaRepository;
import com.example.abnerlucss.msvticket.repositories.PassageiroRepository;
import com.example.abnerlucss.msvticket.repositories.PassagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PassagemService {

    @Autowired
    private PassagemRepository passagemRepository;

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private CompanhiaAereaRepository companhiaAereaRepository;

    @Autowired
    private MsvValidationBoardingService msvValidationBoardingService;

    private final PassagemMapper passagemMapper;

    private final PassagemCompradaMapper passagemCompradaMapper;

    private final EmbarqueMapper embarqueMapper;


    public List<PassagemDTO> listarPassagensDisponiveis() {

        List<Passagem> passagens = passagemRepository.findAllPassagensDisponiveis();
        return passagemMapper.converterListaEntidadeParaDTO(passagens);
    }

    public PassagemCompradaDTO comprarPassagem(DadosCompraDTO body) throws NotFoundException, CreateException {
        Passagem passagem = passagemRepository.findById(body.getIdPassagem()).orElseThrow(
                () -> new NotFoundException("Passagem não econtrada"));
        Passageiro passageiro = passageiroRepository.findById(body.getIdPassageiro()).orElseThrow(
                () -> new NotFoundException("Passageiro não encontrado")
        );


        try {
            passagem.setPassageiro(passageiro);

            passagemRepository.save(passagem);

            PassagemCompradaDTO novaPassagemComprada = passagemCompradaMapper.converteEntidadeParaDTO(passageiro, passagem);
            EmbarqueDTO embarqueDTO = embarqueMapper.converteEntidadeParaDTO(novaPassagemComprada, passagem, passageiro);

            System.out.println(embarqueDTO.toString());

            if (msvValidationBoardingService.cadastrarPassagemComprada(embarqueDTO)) {
                return novaPassagemComprada;
            }
        } catch (Exception e) {
            throw new CreateException("Não foi possível comprar passagem");

        }
        return null;
    }

    public PassagemDTO cadastrarPassagem(CadPassagemDTO body) throws NotFoundException, CreateException {
        Classe classe = classeRepository.findById(body.getIdClasse()).orElseThrow(
                () -> new NotFoundException("Classe não encontrada")
        );

        CompanhiaAerea companhiaAerea = companhiaAereaRepository.findById(body.getIdCompanhia()).orElseThrow(
                () -> new NotFoundException("Companhia aérea não encontrada")
        );

        try {
            Passagem passagem = passagemMapper.converterDTOParaEntidade(body, classe, companhiaAerea);
            return passagemMapper.converterEntidadeParaDTO(passagemRepository.save(passagem), classe, companhiaAerea);

        } catch (Exception e) {
            throw new CreateException("Erro ao cadastrar passagem", e.getCause());
        }


    }
}
