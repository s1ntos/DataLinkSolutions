package crypto;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

public class Encrypthash {

    public static PublicKey loadPublickey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        X509EncodedKeySpec Spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(Spec);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        PublicKey publicKey = loadPublickey("publickey.txt");

        Scanner sc = new Scanner(System.in);
        System.out.println("Digite a mensagem a ser criptografada");
        String message = sc.nextLine();
        sc.close();

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageBytes);

        Files.write(Paths.get("mensagemencrypted.enc"), encryptedBytes);
        System.out.println("Mensagem salva em 'mensagemEncrypted'");

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(encryptedBytes);
        String hashHex = bytesToHex(digest);

        Files.write(Paths.get("hash.text"), hashHex.getBytes(StandardCharsets.UTF_8));
        System.out.println("Mensagem salva em 'hashText'");
        System.out.println(hashHex);
    }
}
