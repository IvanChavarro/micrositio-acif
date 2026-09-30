package co.com.claro.micrositioacif.service;

import co.com.claro.micrositioacif.dto.AcifBaseActasDTO;
import co.com.claro.micrositioacif.dto.AcifSerialesDTO;
import co.com.claro.micrositioacif.dto.PageResponseDTO;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.math.BigDecimal;

public interface AcifService {
    PageResponseDTO<AcifBaseActasDTO> findBaseActasByIdCargue(Long idCargueFk, Integer page, Integer size);
    PageResponseDTO<AcifSerialesDTO> findSerialesByIdCargue(Long idCargueFk, Integer page, Integer size);
    StreamingResponseBody generateBaseActasCsv(Long idCargueFk);
    StreamingResponseBody generateSerialesCsv(Long idCargueFk);
    String liberarSeriales(Long idSerial);
    String marcarSeriales(Long idActa, Long idSerial);
    String corregirQty(Long idBaseActas, BigDecimal qtyFinal);
    String corregirQtyYVr(Long idBaseActas, BigDecimal qtyFinal, BigDecimal vrFinal);

}
