package com.veterinaria.Sistema_Veterinario.config;

import com.veterinaria.Sistema_Veterinario.domain.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JwtService {

    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final Pattern SUB_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern ROLE_PATTERN = Pattern.compile("\"role\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXP_PATTERN = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public long getExpirationMs() {
        return expirationMs;
    }

    public String generateToken(Usuario usuario) {
        long agora = System.currentTimeMillis();
        String header = base64Url(HEADER_JSON.getBytes(StandardCharsets.UTF_8));
        String payloadJson = "{\"sub\":\"" + escapar(usuario.getEmail()) + "\","
                + "\"role\":\"" + usuario.getRole().name() + "\","
                + "\"iat\":" + (agora / 1000) + ","
                + "\"exp\":" + ((agora + expirationMs) / 1000) + "}";
        String payload = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
        String assinatura = assinar(header + "." + payload);
        return header + "." + payload + "." + assinatura;
    }

    public String extractUsername(String token) {
        return extrair(payload(token), SUB_PATTERN);
    }

    public String extractRole(String token) {
        return extrair(payload(token), ROLE_PATTERN);
    }

    public long extractExpirationEpochSeconds(String token) {
        String valor = extrair(payload(token), EXP_PATTERN);
        if (valor == null) {
            throw new IllegalArgumentException("Token sem expiração");
        }
        return Long.parseLong(valor);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            long expiracao = extractExpirationEpochSeconds(token);
            return username != null
                    && username.equalsIgnoreCase(userDetails.getUsername())
                    && expiracao * 1000 > System.currentTimeMillis();
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private String payload(String token) {
        String[] partes = token.split("\\.");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Token JWT inválido");
        }
        String assinaturaEsperada = assinar(partes[0] + "." + partes[1]);
        if (!MessageDigest.isEqual(
                assinaturaEsperada.getBytes(StandardCharsets.UTF_8),
                partes[2].getBytes(StandardCharsets.UTF_8))) {
            throw new IllegalArgumentException("Assinatura do token inválida");
        }
        return new String(Base64.getUrlDecoder().decode(partes[1]), StandardCharsets.UTF_8);
    }

    private String extrair(String payload, Pattern pattern) {
        Matcher matcher = pattern.matcher(payload);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String assinar(String conteudo) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Url(mac.doFinal(conteudo.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Não foi possível assinar o token JWT", ex);
        }
    }

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
