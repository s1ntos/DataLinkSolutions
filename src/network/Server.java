package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class Server {

    private static PrivateKey loadPrivateKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static void main(String[] args) throws Exception {
        PrivateKey privateKey = loadPrivateKey("private.key");

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Servidor aguardando na porta 5000...");

        Socket socket = serverSocket.accept();
        System.out.println("Cliente conectado: " + socket.getInetAddress());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String base64Cipher = reader.readLine();

        byte[] cipherBytes = java.util.Base64.getDecoder().decode(base64Cipher);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] plainBytes = cipher.doFinal(cipherBytes);

        String message = new String(plainBytes, java.nio.charset.StandardCharsets.UTF_8);
        System.out.println("Mensagem recebida (após descriptografia): " + message);

        socket.close();
        serverSocket.close();
    }
}