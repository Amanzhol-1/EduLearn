package spring.educhainminiapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * Сервис для валидации данных авторизации, полученных от Telegram Mini App.
 */
@Service
public class TelegramAuthService {

    /**
     * Токен вашего Telegram бота.
     * Убедитесь, что он хранится безопасно и не публикуется публично.
     */
    @Value("${bot.token}")
    private String botToken;

    /**
     * Проверяет подлинность initData, полученных от Telegram Mini App.
     *
     * @param initData строка initData, полученная от клиента.
     * @return true, если данные валидны и не устарели, иначе false.
     * @throws Exception если произошла ошибка при обработке данных.
     */
    public boolean checkAuthorization(String initData) throws Exception {
        // Разбираем initData в Map параметров
        Map<String, String> authData = parseInitData(initData);
        System.out.println("authData: " + authData);

        // Извлекаем хэш и удаляем его из данных
        String receivedHash = authData.get("hash");
        if (receivedHash == null || receivedHash.isEmpty()) {
            System.out.println("Hash отсутствует");
            return false; // Хэш отсутствует
        }
        authData.remove("hash");

        // Сортируем ключи в алфавитном порядке
        SortedSet<String> sortedKeys = new TreeSet<>(authData.keySet());

        // Формируем data_check_string
        List<String> dataCheckList = new ArrayList<>();
        for (String key : sortedKeys) {
            String value = authData.get(key);
            dataCheckList.add(key + "=" + value);
        }
        String dataCheckString = String.join("\n", dataCheckList);
        System.out.println("dataCheckString: " + dataCheckString);

        // Вычисляем секретный ключ (HMAC-SHA256 от botToken с ключом "WebAppData")
        byte[] secretKey = getSecretKey();

        // Вычисляем HMAC-SHA256 хэш от dataCheckString с использованием секретного ключа
        String computedHash = hmacSHA256(dataCheckString, secretKey);
        System.out.println("computedHash: " + computedHash);
        System.out.println("receivedHash: " + receivedHash);

        // Сравниваем вычисленный хэш с полученным
        if (!computedHash.equals(receivedHash)) {
            System.out.println("Хэши не совпадают");
            return false; // Хэши не совпадают
        }

        // Проверяем актуальность данных по времени
        long authDate = Long.parseLong(authData.getOrDefault("auth_date", "0"));
        long currentTime = System.currentTimeMillis() / 1000;
        long timeDifference = currentTime - authDate;
        long maxAge = 86400; // Максимально допустимое время в секундах (1 день)

        if (timeDifference > maxAge) {
            System.out.println("Данные устарели");
            return false; // Данные устарели
        }

        System.out.println("Данные валидны");
        return true; // Данные валидны
    }

    /**
     * Вычисляет секретный ключ как HMAC-SHA256 от токена бота с ключом "WebAppData".
     *
     * @return байтовый массив секретного ключа.
     * @throws Exception если произошла ошибка при вычислении.
     */
    private byte[] getSecretKey() throws Exception {
        String key = "WebAppData";
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(keySpec);
        return sha256Hmac.doFinal(botToken.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Разбирает строку initData в Map.
     *
     * @param initData строка initData в формате query string.
     * @return Map с ключами и значениями из initData.
     */
    public Map<String, String> parseInitData(String initData) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
                queryParams.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return queryParams;
    }

    /**
     * Вычисляет HMAC-SHA256 хэш.
     *
     * @param data данные для хэширования.
     * @param key  секретный ключ.
     * @return строка хэша в шестнадцатеричном формате.
     * @throws Exception если произошла ошибка при вычислении HMAC.
     */
    private String hmacSHA256(String data, byte[] key) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        sha256Hmac.init(secretKeySpec);
        byte[] hashBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    /**
     * Преобразует байтовый массив в строку в шестнадцатеричном формате.
     *
     * @param bytes байтовый массив.
     * @return строка в шестнадцатеричном формате.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp & 0xff));
        }
        return result.toString();
    }
}
