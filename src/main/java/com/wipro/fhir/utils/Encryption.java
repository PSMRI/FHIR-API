package com.wipro.fhir.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.wipro.fhir.utils.exception.FHIRException;

@Service
public class Encryption {
	
	@Value("${ndhmuserAuthenticate}")
	private String ndhmUserAuthenticate;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public String encrypt(String text, String publicKeyString) throws FHIRException, NoSuchAlgorithmException, 
	InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		String encryptedTextBase64 = "";
		byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

		// Cipher used in ABHA v3 APIs
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedText = cipher.doFinal(text.getBytes());
		encryptedTextBase64 = Base64.getEncoder().encodeToString(encryptedText);
		logger.info("encryptedTextBase64 :" + encryptedTextBase64);
		
		return encryptedTextBase64;

	}

}
