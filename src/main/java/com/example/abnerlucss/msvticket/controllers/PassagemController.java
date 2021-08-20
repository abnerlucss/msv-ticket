package com.example.abnerlucss.msvticket.controllers;

import com.example.abnerlucss.msvticket.DTOs.CadPassagemDTO;
import com.example.abnerlucss.msvticket.DTOs.DadosCompraDTO;
import com.example.abnerlucss.msvticket.DTOs.PassagemCompradaDTO;
import com.example.abnerlucss.msvticket.DTOs.PassagemDTO;
import com.example.abnerlucss.msvticket.exceptions.CreateException;
import com.example.abnerlucss.msvticket.exceptions.NotFoundException;
import com.example.abnerlucss.msvticket.services.PassagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/passagem")
@SuppressWarnings("unused")
public class PassagemController {

    @Autowired
    private PassagemService passagemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassagemDTO cadastrarPassagem(@RequestBody CadPassagemDTO body) throws NotFoundException, CreateException {
        return passagemService.cadastrarPassagem(body);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public PassagemCompradaDTO comprarPassagem(@RequestBody DadosCompraDTO body) throws NotFoundException, CreateException {
        return passagemService.comprarPassagem(body);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PassagemDTO> listarPassagensDisponiveis(){
        return passagemService.listarPassagensDisponiveis();
    }

}
