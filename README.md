# Cooffee-member

- - -

## 프로젝트 개요

- - -

## 🧑‍🤝‍🧑 개발자

### Backend

|                 김성준                  |                        윤형진                         |                  방세현                   |                                                                                                               
|:------------------------------------:|:--------------------------------------------------:|:--------------------------------------:|
| [@kadun1](https://github.com/kadun1) | [@0113bernoyoun](https://github.com/0113bernoyoun) | [@dev-bsh](https://github.com/dev-bsh) |

### Frontend

- 절찬리 구인중

- - -

## 💻 개발환경

- **Version** : Kotlin 1.9.20
- **IDE** : IntelliJ
- **Framework** : SpringBoot 3.2.0
- **ORM** : JPA

- - -

## ⚙️ 기술 스택

- **Server** : Personal Server(from Rester)
- **DataBase** : PostgreSQL
- **WS/WAS** : Tomcat
- **Security** : JWT, Spring Security, Redis(Refresh Token)
- **Collaboration Tool** : Slack, Git, Google Meet
- **Infra** : Docker, Kubernetes

## 📝 프로젝트 아키텍쳐

- - -

## ✒️ API

- - - 

## 사용자 상세 정보

### Query parameter

| Parameter | Description |                                                                                                               
|:---------:|:-----------:|
|   email   |   회원 이메일    |

### Request

    GET /v1/member/details?email=dummy1%40test.com HTTP/1.1
    Host: localhost:8080

### Response

    HTTP/1.1 200 OK
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 0
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Length: 212
    {"statusCode":200,"status":"OK","data":{"email":"dummy1@test.com","name":"dummy1","phone":"010-1111-2222","address":{"mainAddress":"서울특별시 강남구","subAddress":"논현동 어딘가","zipcode":12345}}}

- - -

## 사용자 가입

### Request Header

|     Name      | Description |                                                                                                               
|:-------------:|:-----------:|
| Authorization |    인증 토큰    |

### Request

    POST /v1/member/signUp HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Authorization: local_token
    Content-Length: 178
    Host: localhost:8080
    
    {"email":"test@test.com","name":"test","password":"123","phone":"010-1234-5678","mainAddress":"경기도 성남시 분당구","
    subAddress":"코코아빌딩 1층","zipcode":54321}

### Response

    HTTP/1.1 200 OK
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 0
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Length: 84
    
    {"statusCode":201,"status":"CREATED","data":{"email":"test@test.com","name":"test"}}

- - -

## 사용자 로그인

### Request Header

|     Name      | Description |                                                                                                               
|:-------------:|:-----------:|
| Authorization |    인증 토큰    |

### Request

    POST /v1/member/signIn HTTP/1.1
    Content-Type: application/json;charset=UTF-8
    Authorization: local_token
    Content-Length: 44
    Host: localhost:8080
    {"email":"dummy1@test.com","password":"123"}

### Response

    HTTP/1.1 200 OK
    Vary: Origin
    Vary: Access-Control-Request-Method
    Vary: Access-Control-Request-Headers
    Content-Type: application/json
    X-Content-Type-Options: nosniff
    X-XSS-Protection: 0
    Cache-Control: no-cache, no-store, max-age=0, must-revalidate
    Pragma: no-cache
    Expires: 0
    X-Frame-Options: DENY
    Content-Length: 488
    {"statusCode":200,"status":"OK","data":{"accessToken":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjb29mZmVlIiwic3
