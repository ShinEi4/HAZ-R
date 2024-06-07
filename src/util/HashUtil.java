package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String hashString(String input) {
        try {
            // Créer une instance de MessageDigest avec l'algorithme SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Appliquer le hachage à l'entrée
            byte[] encodedhash = digest.digest(input.getBytes());
            
            // Convertir le tableau de bytes en une chaîne de caractères hexadécimale
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode utilitaire pour convertir un tableau de bytes en une chaîne de caractères hexadécimale
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
