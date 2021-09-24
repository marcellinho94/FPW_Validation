package br.com.lg.smb;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeradorTicket {

	public static void main(String[] args) {
		try {

			LocalDateTime dt = LocalDateTime.now();
			String baseForEncrypt = dt.format(DateTimeFormatter.ofPattern("DDddMMyyyy-HHmmss"));
			baseForEncrypt = baseForEncrypt + "RONALDO-MELHOR-DO-MUNDOasfdjpaoisdjfaisdfpoajisdpfijasiidjfpaisdjfpaijsdvmakdnnb";

			MessageDigest digest;
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encodedhash = digest.digest(baseForEncrypt.getBytes(StandardCharsets.UTF_8));

			String passwordSHA256 = bytesToHex(encodedhash);

			System.out.println(baseForEncrypt);
			System.out.println(passwordSHA256.substring(0, 20));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
