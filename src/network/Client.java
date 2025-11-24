package network;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import javax.crypto.Cipher;

public class Client {

    private static PublicKey loadPublicKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static void main(String[] args) throws Exception {
        PublicKey publicKey = loadPublicKey("public.key");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a mensagem a ser enviada: ");
        String message = scanner.nextLine();
        scanner.close();

        // Criptografar com RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherBytes = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        String base64Cipher = java.util.Base64.getEncoder().encodeToString(cipherBytes);

        // Enviar para o servidor
        Socket socket = new Socket("127.0.0.1", 5000);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write(base64Cipher);
        writer.newLine();
        writer.flush();

        System.out.println("Mensagem criptografada enviada ao servidor.");
        socket.close();
    }
}