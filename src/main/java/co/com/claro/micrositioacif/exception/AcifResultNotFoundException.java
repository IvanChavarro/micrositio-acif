package co.com.claro.micrositioacif.exception;

public class AcifResultNotFoundException extends RuntimeException {

    public AcifResultNotFoundException(Long idCargueFk) {
        super("No se encontraron resultados de ACIF para el idCargueFk: " + idCargueFk);
    }
}
