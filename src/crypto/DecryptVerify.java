package crypto;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class DecryptVerify {

    private static PrivateKey loadPrivateKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {

        PrivateKey privateKey = loadPrivateKey("private.key");


        byte[] encryptedBytes = Files.readAllBytes(Paths.get("mensagem.enc"));


        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        String decryptedMessage = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("Mensagem descriptografada:");
        System.out.println(decryptedMessage);


        String originalHash = new String(
                Files.readAllBytes(Paths.get("hash.txt")),
                StandardCharsets.UTF_8
        ).trim();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] currentHashBytes = md.digest(encryptedBytes);
        String currentHash = bytesToHex(currentHashBytes);

        System.out.println("Hash original: " + originalHash);
        System.out.println("Hash atual:    " + currentHash);

        if (originalHash.equals(currentHash)) {
            System.out.println("Integridade OK: o arquivo NÃO foi alterado.");
        } else {
            System.out.println("FALHA de integridade: o arquivo foi modificado.");
        }
    }
}