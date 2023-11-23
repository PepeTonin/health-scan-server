package com.health.scan.service;

import com.health.scan.dto.ComparacaoDTO;
import com.health.scan.dto.ProdutoComparadoDTO;
import com.health.scan.entity.Comparacao;
import com.health.scan.entity.Produto;
import com.health.scan.entity.ProdutoComparado;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IComparacaoRepository;
import com.health.scan.repository.IProdutoComparadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ComparacaoService {

    @Autowired
    private IComparacaoRepository repository;
    @Autowired
    private IProdutoComparadoRepository produtoComparadoRepository;

    @Transactional
    public ObjectResponse deletarComparacaoById(Long idComparacao){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        produtoComparadoRepository.deleteByIdComparacao(idComparacao);
        repository.deleteById(idComparacao);

        return response;
    }
    public ObjectResponse buscarUltimasComparacoes(Long idUsuario, String query){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Pageable pageable = PageRequest.of(0, 10); // Buscar os primeiros 10 resultados
        Page<Comparacao> comparacaoPage = repository.buscarUltimasComparacoes(idUsuario, query, pageable);

        List<Long> idsComparacao = comparacaoPage.getContent()
                .stream()
                .map(Comparacao::getId)
                .collect(Collectors.toList());

        List<ProdutoComparado> produtoComparadoList = !idsComparacao.isEmpty()
                ? produtoComparadoRepository.buscarPorIdComparacao(idsComparacao)
                : new ArrayList<>();

        processProdutosComparados(comparacaoPage, produtoComparadoList);
        List<ComparacaoDTO> comparacaoDTOList = convertComparacaoToDTO(comparacaoPage.toList());

        response.setResult(comparacaoDTOList);
        return response;
    }

    private void processProdutosComparados(Page<Comparacao> comparacaoPage, List<ProdutoComparado> produtoComparadoList){
        for(Comparacao comparacao : comparacaoPage.getContent()){
            List<ProdutoComparado> produtoComparadoListNova = new ArrayList<>();
            for (ProdutoComparado produtoComparado : produtoComparadoList){
                if(Objects.equals(comparacao.getId(), produtoComparado.getComparacao().getId())){
                    produtoComparadoListNova.add(produtoComparado);
                }
            }
            comparacao.setProdutoComparadoList(produtoComparadoListNova);
        }
    }

    private List<ComparacaoDTO> convertComparacaoToDTO(List<Comparacao> comparacaoList){
        List<ComparacaoDTO> comparacaoDTOList = new ArrayList<>();

        for(Comparacao comparacao: comparacaoList){
            ComparacaoDTO comparacaoDTO = new ComparacaoDTO();
            comparacaoDTO.setId(comparacao.getId());
            comparacaoDTO.setDataHoraInc(comparacao.getDataHoraInc());
            comparacaoDTO.setId(comparacao.getId());
            comparacaoDTO.setNome(comparacao.getNome());

            List<ProdutoComparadoDTO> produtoComparadoDTOList = convertProdutoComparadotoDTO(comparacao.getProdutoComparadoList(), comparacaoDTO);
            comparacaoDTO.setProdutoComparadoList(produtoComparadoDTOList);
            comparacaoDTOList.add(comparacaoDTO);
        }

        return comparacaoDTOList;
    }

    private List<ProdutoComparadoDTO> convertProdutoComparadotoDTO(List<ProdutoComparado> produtoComparadoList, ComparacaoDTO comparacaoDTO) {
        List<ProdutoComparadoDTO> produtoComparadoDTOList = new ArrayList<>();

        for(ProdutoComparado produtoComparado: produtoComparadoList){
            ProdutoComparadoDTO produtoComparadoDTO = new ProdutoComparadoDTO();
            produtoComparadoDTO.setId(produtoComparado.getId());

            Produto produto = new Produto();
            produto.setNome(produtoComparado.getProduto().getNome());
            produto.setImage(produtoComparado.getProduto().getImage());

            produtoComparadoDTO.setProduto(produto);
            produtoComparadoDTO.setIdcomparacao(comparacaoDTO.getId());

            produtoComparadoDTOList.add(produtoComparadoDTO);
        }

        return produtoComparadoDTOList;
    }
}
