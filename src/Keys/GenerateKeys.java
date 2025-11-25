package Keys;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenerateKeys {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();


        Files.write(Paths.get("public.key"), publicKey.getEncoded());
        Files.write(Paths.get("private.key"), privateKey.getEncoded());

        System.out.println("Chaves geradas com sucesso:");
        System.out.println(" - public.key");
        System.out.println(" - private.key");
    }
}
