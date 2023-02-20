package com.bjj.ligabjj.config;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bjj.ligabjj.utils.ConvertDate;
import com.bjj.ligabjj.utils.EncryptString;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

	private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
	private static final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

	private static final String KEY_PASSWORD = "ligabjj_verify";

	@Override
	public String encode(CharSequence rawPassword) {
		return ENCODER.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			log.warn("Empty encoded password");
			return false;
		}

		String password = rawPassword.toString();
		encodedPassword = encodedPassword.substring(8, encodedPassword.length());

		// Descrypt password user
		byte[] urlEncodeByte = null;
		try {
			urlEncodeByte = Base64.getUrlDecoder().decode(password.getBytes());
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			return false;
		}
		String urlEncode = new String(urlEncodeByte);
		log.debug("URL Encode: " + urlEncode);

		if (urlEncode.split("\\|").length == 2) {
			String passwordEncrypted = urlEncode.split("\\|")[0];
			String keyEncrypted = urlEncode.split("\\|")[1];

			password = EncryptString.descriptAES(passwordEncrypted);
			String keyVerify = EncryptString.descriptAES(keyEncrypted);

			Boolean expired = this.isExpired(keyVerify);
			if (!keyVerify.equals(KEY_PASSWORD) && expired)
				return false;
		}

		if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
			log.warn("Encoded password does not look like BCrypt");
			return false;
		}

		return BCrypt.checkpw(password, encodedPassword);
	}
	
	private Boolean isExpired(String keyReq) {
		try {
			Date dateReq = ConvertDate.formatDate("dd/MM/yyyy HH:mm:ss", keyReq);
			Calendar cReq = Calendar.getInstance();
			cReq.setTime(dateReq);
			cReq.add(Calendar.MINUTE, 5);
			
			Calendar cVer = Calendar.getInstance();
			Long times = System.currentTimeMillis();
			cVer.setTimeInMillis(times);
			
			return !cReq.after(cVer);
			
		} catch (Exception e) {
			log.error("Valid expired timestamp password failure");
			return true;
		}
	}

}
