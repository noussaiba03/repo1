package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Partner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Partner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartnerRepository extends ReactiveCrudRepository<Partner, Long>, PartnerRepositoryInternal {
    Flux<Partner> findAllBy(Pageable pageable);

    @Override
    <S extends Partner> Mono<S> save(S entity);

    @Override
    Flux<Partner> findAll();

    @Override
    Mono<Partner> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PartnerRepositoryInternal {
    <S extends Partner> Mono<S> save(S entity);

    Flux<Partner> findAllBy(Pageable pageable);

    Flux<Partner> findAll();

    Mono<Partner> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Partner> findAllBy(Pageable pageable, Criteria criteria);
}
