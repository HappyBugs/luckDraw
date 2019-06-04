package com.likuncheng.sso.entity;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



/**
 * 创建登录token令牌
 * 
 * @author Administrator
 *
 */
public class JWTTokenUtil {

//	  "iss":"Issuer —— 用于说明该JWT是由谁签发的", 
//    "sub":"Subject —— 用于说明该JWT面向的对象", 
//    "aud":"Audience —— 用于说明该JWT发送给的用户", 
//    "exp":"Expiration Time —— 数字类型，说明该JWT过期的时间", 
//    "nbf":"Not Before —— 数字类型，说明在该时间之前JWT不能被接受与处理", 
//    "iat":"Issued At —— 数字类型，说明该JWT何时被签发", 
//    "jti":"JWT ID —— 说明标明JWT的唯一ID", 
//    "user-definde1":"自定义属性举例", 
//    "user-definde2":"自定义属性举例"
	
	
	//读取配置文件 秘匙 （这是用来后面接收到token的时候用于解密用的秘匙
	private static final String secretKey = "JwtToken";

	public static String createJWT(String subject) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		JwtBuilder builder = Jwts.builder()
				.setSubject(subject)
				.signWith(signatureAlgorithm, signingKey);
		return builder.compact();
	}

	public static Claims parseJWT(String jwtToken) {
	    Claims claims = Jwts.parser()         
	       .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
	       .parseClaimsJws(jwtToken).getBody();
	    return claims;
	    
	}
	

}
