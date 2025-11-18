package core.utils;

public class Validator {

    public static boolean isValidId(long id) {
        if (id < 0) {
            return false;
        }
        String idStr = String.valueOf(id);
        return idStr.length() <= 15;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidNit(String nit) {
        if (nit == null || nit.isEmpty()) {
            return false;
        }
        return nit.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d$");
    }

    public static boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        return isbn.matches("^\\d{3}-\\d-\\d{2}-\\d{6}-\\d$");
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

}
