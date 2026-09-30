package co.com.claro.micrositioacif.controller;

import co.com.claro.micrositioacif.exception.GlobalExceptionHandler;
import co.com.claro.micrositioacif.repository.AcifBaseActasRepo;
import co.com.claro.micrositioacif.repository.AcifSerialesRepo;
import co.com.claro.micrositioacif.service.AuditLogService;
import co.com.claro.micrositioacif.service.impl.AcifServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AcifCorreccionesTest {
    private AcifBaseActasRepo repo;
    private AuditLogService audit;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        repo = mock(AcifBaseActasRepo.class);
        audit = mock(AuditLogService.class);
        var service = new AcifServiceImpl(repo, mock(AcifSerialesRepo.class), audit);
        mvc = MockMvcBuilders.standaloneSetup(new AcifController(service))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void corregirSoloQty() throws Exception {
        when(repo.corregirQty(12L, new BigDecimal("2.5"))).thenReturn(1);
        mvc.perform(patch("/api/base-actas/12/qty").param("qtyFinal", "2.5"))
                .andExpect(status().isOk())
                .andExpect(content().string("QTY_FINAL corregido correctamente"));
        verify(repo).corregirQty(12L, new BigDecimal("2.5"));
        verifyNoMoreInteractions(repo);
        verify(audit).saveAuditLog(any());
    }

    @Test
    void corregirQtyYVr() throws Exception {
        when(repo.corregirQtyYVr(12L, new BigDecimal("2.5"), new BigDecimal("1500.75"))).thenReturn(1);
        mvc.perform(patch("/api/base-actas/12/qty-vr")
                        .param("qtyFinal", "2.5").param("vrFinal", "1500.75"))
                .andExpect(status().isOk())
                .andExpect(content().string("QTY_FINAL y VR_FINAL corregidos correctamente"));
        verify(repo).corregirQtyYVr(12L, new BigDecimal("2.5"), new BigDecimal("1500.75"));
        verifyNoMoreInteractions(repo);
        verify(audit).saveAuditLog(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"qty", "qty-vr"})
    void actaInexistente(String endpoint) throws Exception {
        mvc.perform(patch("/api/base-actas/12/" + endpoint)
                        .param("qtyFinal", "2.5").param("vrFinal", "1500.75"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se encontro acta ACIF para idBaseActas: 12"));
        verifyNoInteractions(audit);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc"})
    void qtyInvalido(String qty) throws Exception {
        mvc.perform(patch("/api/base-actas/12/qty").param("qtyFinal", qty))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(repo, audit);
    }

    @Test
    void parametrosObligatorios() throws Exception {
        mvc.perform(patch("/api/base-actas/12/qty"))
                .andExpect(status().isBadRequest());
        mvc.perform(patch("/api/base-actas/12/qty-vr").param("qtyFinal", "2"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(repo, audit);
    }
}
