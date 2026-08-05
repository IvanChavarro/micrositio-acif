package co.com.claro.micrositioacif.controller;

import co.com.claro.micrositioacif.dto.AcifBaseActasDTO;
import co.com.claro.micrositioacif.dto.AcifSerialesDTO;
import co.com.claro.micrositioacif.dto.PageResponseDTO;
import co.com.claro.micrositioacif.service.AcifService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@RestController
@RequestMapping("api")
public class AcifController {

    private static final DateTimeFormatter CSV_FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final AcifService acifService;

    public AcifController(AcifService acifService) {
        this.acifService = acifService;
    }

    @GetMapping("/base-actas/{idCargueFk}")
    public ResponseEntity<PageResponseDTO<AcifBaseActasDTO>> findBaseActasByIdCargue(
            @PathVariable("idCargueFk") Long idCargueFk,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size) {
        return ResponseEntity.ok(acifService.findBaseActasByIdCargue(idCargueFk, page, size));
    }

    @GetMapping("/seriales/{idCargueFk}")
    public ResponseEntity<PageResponseDTO<AcifSerialesDTO>> findAcifSerialByIdCargue(
            @PathVariable("idCargueFk") Long idCargueFk,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "100") Integer size) {
        return ResponseEntity.ok(acifService.findSerialesByIdCargue(idCargueFk, page, size));
    }

    @GetMapping("/base-actas/{idCargueFk}/csv")
    public ResponseEntity<StreamingResponseBody> generateBaseActasCsv(
            @PathVariable("idCargueFk") Long idCargueFk) {
        return csvResponse(
                "acif-base-actas-" + idCargueFk,
                acifService.generateBaseActasCsv(idCargueFk));
    }

    @GetMapping("/seriales/{idCargueFk}/csv")
    public ResponseEntity<StreamingResponseBody> generateSerialesCsv(
            @PathVariable("idCargueFk") Long idCargueFk) {
        return csvResponse(
                "acif-seriales-" + idCargueFk,
                acifService.generateSerialesCsv(idCargueFk));
    }

    @GetMapping("/liberar-seriales/{idSerial}")
    public ResponseEntity<String> generateLiberarSeriales(@PathVariable Long idSerial) {
        return ResponseEntity.ok(acifService.liberarSeriales(idSerial));
    }

    @GetMapping("/marcar-seriales/{idActa}/{idSerial}")
    public ResponseEntity<String> generateMarcarSeriales(@PathVariable Long idActa, @PathVariable Long idSerial) {
        return ResponseEntity.ok(acifService.marcarSeriales(idActa, idSerial));
    }

    private ResponseEntity<StreamingResponseBody> csvResponse(
            String filePrefix,
            StreamingResponseBody csvStream) {
        String fileName = filePrefix + "-" + LocalDateTime.now().format(CSV_FILE_TIMESTAMP) + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(csvStream);
    }
}
