//package com.example.JWTOauth.security;
//
//import com.example.JWTOauth.dto.Token;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JWTTokenGeneration {
//        private String secret = "springsecretforconvertaccesstokentojwttokenusingbase64encodeformat";
//        private String refreshSecret = "springrefreshsecretforconvertrefreshtokentojwttokenusingbase64encodeformat";
//
//
//        public String extractUsername(String token) {
//            return extractClaim(token, Claims::getSubject);
//        }
//
//        public Date extractExpiration(String token) {
//            return extractClaim(token, Claims::getExpiration);
//        }
//
//        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//            final Claims claims = extractAllClaims(token);
//            return claimsResolver.apply(claims);
//        }
//
//        private Claims extractAllClaims(String token) {
//            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//        }
//
//        private Boolean isTokenExpired(String token) {
//            return extractExpiration(token).before(new Date());
//        }
//
//        public Token generateToken(String username) {
//            Map<String, Object> claims = new HashMap<>();
//            return createToken(claims, username);
//        }
//
////    private String createToken(Map<String, Object> claims, String subject) {
////
////        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
////                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
////                .signWith(SignatureAlgorithm.HS256, secret).compact();
////    }
//
//        public Boolean validateToken(String token, UserDetails userDetails) {
//            final String username = extractUsername(token);
//            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//        }
//
//        public String generateRefreshToken(String username) {
//            Map<String, Object> claims = new HashMap<>();
//            return createRefreshToken(claims, username);
//        }
//
//        private String createRefreshToken(Map<String, Object> claims, String subject) {
//            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Set expiration to 1 week
//                    .signWith(SignatureAlgorithm.HS256, refreshSecret).compact();
//        }
//
//        private Token createToken(Map<String, Object> claims, String subject) {
//            Token token = new Token();
//            String refreshToken = generateRefreshToken(subject);
//            claims.put("refresh_token", refreshToken);
//            String accessToken = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                    .signWith(SignatureAlgorithm.HS256, secret).compact();
//            token.setRefreshToken(refreshToken);
//            token.setAccessToken(accessToken);
//            return token;
//        }
//}
