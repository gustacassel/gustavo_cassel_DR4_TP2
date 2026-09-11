package br.edu.infnet.ecommerce.pagamento.infrastructure.persistence;

import br.edu.infnet.ecommerce.pagamento.domain.Pagamento;
import br.edu.infnet.ecommerce.pagamento.domain.PagamentoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PagamentoRepositoryAdapter implements PagamentoRepository {

    private final PagamentoJpaRepository jpaRepository;

    public PagamentoRepositoryAdapter(PagamentoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        PagamentoJpaEntity entidadeSalva = jpaRepository.save(PagamentoMapper.paraEntidade(pagamento));
        return PagamentoMapper.paraDominio(entidadeSalva);
    }

    @Override
    public Optional<Pagamento> buscarPorPedidoId(Long pedidoId) {
        return jpaRepository.findByPedidoId(pedidoId).map(PagamentoMapper::paraDominio);
    }
}
