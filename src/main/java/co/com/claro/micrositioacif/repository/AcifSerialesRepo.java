package co.com.claro.micrositioacif.repository;

import co.com.claro.micrositioacif.entity.AcifSerialesEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AcifSerialesRepo extends JpaRepository<AcifSerialesEntity, Long> {
    List<AcifSerialesEntity> findByIdCargueFk(Long idCargueFk);
    Page<AcifSerialesEntity> findByIdCargueFk(Long idCargueFk, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE AcifSerialesEntity s SET s.estado = '1', s.idActaFk = 0 WHERE s.idSerial = :idSerial")
    int liberarSerial(@Param("idSerial") Long idSerial);

    @Modifying
    @Transactional
    @Query("UPDATE AcifSerialesEntity s SET s.estado = '0', s.idActaFk = :idActaFk WHERE s.idSerial = :idSerial")
    int marcarSerial(@Param("idActaFk") Long idActaFk, @Param("idSerial") Long idSerial);

}
