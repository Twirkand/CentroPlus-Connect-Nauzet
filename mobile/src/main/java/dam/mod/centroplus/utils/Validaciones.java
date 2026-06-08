package dam.mod.centroplus.utils;

/**
 * Clase de utilidades para validaciones generales del sistema.
 */
public final class Validaciones {

    /**
     * Constructor privado para evitar instanciación.
     */
    private Validaciones() {
    }

    /**
     * Valida el tipo de usuario permitido en el sistema.
     *
     * @param tipo tipo de usuario (ALUMNO, SOCIO, AMBOS)
     * @throws IllegalArgumentException si el tipo es nulo o inválido
     */
    public static void validarTipoUsuario(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de usuario no puede ser null");
        }

        if (!tipo.equals("ALUMNO") &&
                !tipo.equals("SOCIO") &&
                !tipo.equals("AMBOS")) {

            throw new IllegalArgumentException("Tipo de usuario inválido: " + tipo);
        }
    }

    /**
     * Valida el tipo de actividad permitido en el sistema.
     *
     * @param tipo tipo de actividad (ACADEMICA, DEPORTIVA)
     * @throws IllegalArgumentException si el tipo es nulo o inválido
     */
    public static void validarTipoActividad(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de actividad no puede ser null");
        }

        if (!tipo.equals("ACADEMICA") &&
                !tipo.equals("DEPORTIVA")) {

            throw new IllegalArgumentException("Tipo de actividad inválido: " + tipo);
        }
    }

    /**
     * Valida el estado de una reserva.
     *
     * @param estado estado de la reserva (ACTIVA, CANCELADA)
     * @throws IllegalArgumentException si el estado es nulo o inválido
     */
    public static void validarEstadoReserva(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado de reserva no puede ser null");
        }

        if (!estado.equals("ACTIVA") &&
                !estado.equals("CANCELADA")) {

            throw new IllegalArgumentException("Estado de reserva inválido: " + estado);
        }
    }

    /**
     * Valida el estado de una incidencia.
     *
     * @param estado estado de la incidencia (ABIERTA, EN_PROCESO, CERRADA)
     * @throws IllegalArgumentException si el estado es nulo o inválido
     */
    public static void validarEstadoIncidencia(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado de incidencia no puede ser null");
        }

        if (!estado.equals("ABIERTA") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("CERRADA")) {

            throw new IllegalArgumentException("Estado de incidencia inválido: " + estado);
        }
    }

    /**
     * Valida que un email tenga formato correcto.
     *
     * @param email correo electrónico a validar
     * @throws IllegalArgumentException si el email es nulo o inválido
     */
    public static void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
    }

    /**
     * Valida el formato de un DNI español.
     *
     * @param dni DNI a validar (8 números + letra)
     * @throws IllegalArgumentException si el DNI es nulo o inválido
     */
    public static void validarDNI(String dni) {
        if (dni == null || !dni.matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("DNI inválido: " + dni);
        }
    }

    /**
     * Valida que un campo no esté vacío ni sea null.
     *
     * @param valor valor a validar
     * @param campo nombre del campo (para mensajes de error)
     * @throws IllegalArgumentException si el valor es nulo o vacío
     */
    public static void validarNoVacio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío");
        }
    }

public static void validarTelefono(String telefono) {

    if (telefono == null) {
        throw new IllegalArgumentException("Teléfono nulo");
    }

    String telefonoValido = telefono.replaceAll("[\\s-]", "");

    if (!telefonoValido.matches("^(\\+34)?[0-9]{9}$")) {
        throw new IllegalArgumentException("Teléfono inválido: " + telefono);
    }
}
}
