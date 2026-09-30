package co.com.claro.micrositioacif.repository;

import co.com.claro.micrositioacif.entity.AcifBaseActasEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;

import java.util.List;

public interface AcifBaseActasRepo extends JpaRepository<AcifBaseActasEntity, Long> {
    List<AcifBaseActasEntity> findByIdCargueFk(Long idCargueFk);
    Page<AcifBaseActasEntity> findByIdCargueFk(Long idCargueFk, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE AcifBaseActasEntity a SET a.qtyFinal = :qtyFinal WHERE a.idBaseActas = :idBaseActas")
    int corregirQty(@Param("idBaseActas") Long idBaseActas, @Param("qtyFinal") BigDecimal qtyFinal);

    @Modifying
    @Transactional
    @Query("UPDATE AcifBaseActasEntity a SET a.qtyFinal = :qtyFinal, a.vrFinal = :vrFinal "
            + "WHERE a.idBaseActas = :idBaseActas")
    int corregirQtyYVr(@Param("idBaseActas") Long idBaseActas,
                      @Param("qtyFinal") BigDecimal qtyFinal,
                      @Param("vrFinal") BigDecimal vrFinal);
}
