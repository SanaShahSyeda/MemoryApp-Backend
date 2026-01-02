# Authentication diagrams

This file contains Mermaid sequence diagrams that describe the authentication flows used by the project (refresh-token exchange and JWT validation filter).

## JWT request filter and validation

```mermaid
sequenceDiagram
    autonumber
    participant Client
    participant SecurityFilter as JWTAuthenticationFilter
    participant TokenProvider as JWTTokenProvider
    participant UserService as CustomUserDetailsService
    participant SecurityContext
    participant Controller

    Client->>SecurityFilter: HTTP Request + Authorization: Bearer <JWT>

    SecurityFilter->>SecurityFilter: Extract JWT from header

    SecurityFilter->>TokenProvider: validateToken(jwt)

    alt Token missing or invalid
        SecurityFilter-->>Client: 401 Unauthorized
    else Token valid
        TokenProvider-->>SecurityFilter: valid

        SecurityFilter->>TokenProvider: getUsernameFromToken(jwt)
        TokenProvider-->>SecurityFilter: email

        SecurityFilter->>UserService: loadUserByUsername(email)
        UserService-->>SecurityFilter: UserDetails

        SecurityFilter->>SecurityContext: setAuthentication(UserDetails)
    end

    SecurityContext-->>Controller: Authenticated principal available
    Controller-->>Client: Protected resource response

```

## Refresh token exchange

```mermaid
sequenceDiagram
    autonumber
    participant Client
    participant Controller as AuthController
    participant AuthService
    participant RefreshService as RefreshTokenService
    participant RefreshRepo as RefreshTokenRepository
    participant TokenProvider as JWTTokenProvider
    participant Database

    Client->>Controller: POST /auth/refresh { refreshToken }

    Controller->>AuthService: refreshToken(rawToken)

    AuthService->>RefreshService: findByToken(rawToken)
    RefreshService->>RefreshService: hash(refreshToken)

    RefreshService->>RefreshRepo: findByTokenHash(hash)
    RefreshRepo-->>RefreshService: RefreshToken entity

    RefreshService->>RefreshService: verifyExpiration()

    alt Token expired
        RefreshService-->>AuthService: InvalidTokenException
        AuthService-->>Client: 401 Unauthorized
    else Token valid
        AuthService->>TokenProvider: generateTokenFromUsername(email)

        AuthService->>RefreshService: createRefreshToken(user)
        RefreshService->>RefreshRepo: deleteByUser(user)
        RefreshService->>Database: save new refresh token (hashed)

        AuthService-->>Client: New JWT + New Refresh Token
    end

```

## Notes

- These diagrams reflect the implementation in `src/main/java/com/spring/memory`.
- See `AuthController`, `RefreshTokenService`, `JWTAuthenticationFilter`, and `JWTTokenProvider` for the concrete code paths.
