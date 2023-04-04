package com.example.JWTOauth.security;


import com.example.JWTOauth.dto.Token;
import com.example.JWTOauth.entity.User;
import com.example.JWTOauth.exception.CustomException;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Component
public class TokenGenerator {

    @Autowired
    private JwtEncoder jwtAccessTokenEncoder;

    @Autowired
    @Qualifier("jwtRefreshTokenEncoder")
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    private CustomClientDetailsService customClientDetailsService;

    @Autowired
    private CustomClientRepoImpl customClientRepo;

    private String createAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("brim")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(user.getId())
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claimsSet);

        String accessToken = jwtAccessTokenEncoder.encode(jwtEncoderParameters).getTokenValue();
        return accessToken;
    }

    private String createRefreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("brim")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(user.getId())
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claimsSet);

        String refreshToken = jwtRefreshTokenEncoder.encode(jwtEncoderParameters).getTokenValue();
        return refreshToken;
    }

    public Token createToken(Authentication authentication) {
        final Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User type", authentication.getPrincipal().getClass())
            );
        }

        User user = (User) principal;
        Token tokenDTO = new Token();
        tokenDTO.setUserId(user.getId());
        tokenDTO.setAccessToken(createAccessToken(authentication));

        String refreshToken;
        final Object credentials = authentication.getCredentials();
        if (credentials instanceof Jwt) {
            Jwt jwt = (Jwt) credentials;
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(authentication);
        }
        tokenDTO.setRefreshToken(refreshToken);

        return tokenDTO;
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws CustomException {

        boolean isValid = false;
        try {

            String userName = userDetails.getUsername();
            JWT accessToken = JWTParser.parse(token);
            String tokenUserName = accessToken.getJWTClaimsSet().getIssuer();
            Date expirationTime = accessToken.getJWTClaimsSet().getExpirationTime();
            Date now = new Date();

            if (now.before(expirationTime) && userName.equalsIgnoreCase(tokenUserName)) {
                isValid = true;
            }
        } catch (ParseException e){
            throw new CustomException("parse failed",401);
        }
        return isValid;
    }


    public String userNameExtraction(String token) throws ParseException {
        JWT accessToken = JWTParser.parse(token);
        return accessToken.getJWTClaimsSet().getIssuer();
    }
}
