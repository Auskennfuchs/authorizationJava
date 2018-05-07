package de.afb.authorization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class StaticKeyProvider {
    private final static String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALWsUnX4B7J/LZ/i\n" +
            "5KY1IFkaT6kuIdiYi111+F73KmUh1x0f/tLNYQyqBTeTCZ58tvISFZbJxwnL1Tuy\n" +
            "Fv7yRMQhPYeoZtJ5Iq7kllxRFkhIW/F9r2z/0EJUNDa83tg2sTMGdGP6LZn4J/l+\n" +
            "rQLmDuhAQuNQvuRwh75rkEAfZB23AgMBAAECgYEAsmH3vfnCpROXWuzQkhqMHrFl\n" +
            "ErtHZIO0JBrXu3d52z91FxX5gkdDScoPwVfRFBtlDx+OrzaFBytrvmfJiJ+dwEae\n" +
            "Pdif3Qzdqex8TKEzoqeVnPYL5EtzEhNqBuET1+qzXIgh+R3db9jHkL7NmhqR4VUU\n" +
            "/3YEMCTkSUQi5usWNmECQQDkrfTOEhFmkFvgN6wmG83uf4a2DnFyGSZs9iMXPLXZ\n" +
            "N37h2u1BaQ+QEBOc4pTsrO+OOwmtW96Hi0cx1h5K1+jZAkEAy2CzO3kVtfSJOVnq\n" +
            "89CmuDEq3d8tWQ/PT+zzg6oJ/gQuc72a6JN5YP2UbhH4gP4+x8IuVL0CpMYE0098\n" +
            "tZWhDwJAMBKFoQBZWhdEySjK7qPvsZBrcEAvBw5Ubpl7nPntPIGXEcGlGJgOZnmK\n" +
            "tfNH3U0petvcV+41JIdZ6xcY/SDQWQJAYQRVG7bMS4tGdqxVL0/GaDFUMJkJCeZH\n" +
            "z+nJ1HIl6B3Yhu/Y2L+Y9FKjHsrFz8r46dPQxC2R+EZaAO93HywsswJBAKjeqQVu\n" +
            "lti+h+d78fYUG7XCUP64J0p1E5xbNzo5jHxdkKwR8E7MIwSNZDSKWV3Y4nH0FnKT\n" +
            "EeNtvUID/PXzjQQ=\n" +
            "-----END PRIVATE KEY-----\n";

    private final static String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1rFJ1+Aeyfy2f4uSmNSBZGk+p\n" +
            "LiHYmItddfhe9yplIdcdH/7SzWEMqgU3kwmefLbyEhWWyccJy9U7shb+8kTEIT2H\n" +
            "qGbSeSKu5JZcURZISFvxfa9s/9BCVDQ2vN7YNrEzBnRj+i2Z+Cf5fq0C5g7oQELj\n" +
            "UL7kcIe+a5BAH2QdtwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    public RSAPrivateKey getPrivateKey() {
        String pkcs8Pem = prepareKeyString(PRIVATE_KEY,"-----BEGIN PRIVATE KEY-----","-----END PRIVATE KEY-----");

        // Base64 decode the result

        try {
            byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);

            // extract the private key
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPrivateKey pk = (RSAPrivateKey)kf.generatePrivate(keySpec);
            return pk;
        } catch (InvalidKeySpecException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public RSAPublicKey getPublicKey() {
        String pkcs8Pem = prepareKeyString(PUBLIC_KEY,"-----BEGIN PUBLIC KEY-----","-----END PUBLIC KEY-----");

        // Base64 decode the result

        try {
            byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(pkcs8Pem);

            // extract the private key
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey)kf.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String prepareKeyString(String key, String cutBegin, String cutEnd) {
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(key));
        String line;
        try {
            while ((line = rdr.readLine()) != null) {
                pkcs8Lines.append(line);
            }
        } catch (IOException e) {
            return null;
        }

        // Remove the "BEGIN" and "END" lines, as well as any whitespace

        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replace(cutBegin, "");
        pkcs8Pem = pkcs8Pem.replace(cutEnd, "");
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");

        return pkcs8Pem;
    }
}
