# coupon 프로젝트 개요

## 기본 정보
- 프로젝트명: `coupon`
- 그룹(Group): `com.min`
- 버전: `0.0.1-SNAPSHOT`

## 현재 상태
초기 스켈레톤 단계로, 아직 비즈니스 로직은 구현되지 않았다.
- `CouponApplication` 클래스가 Spring Boot 애플리케이션의 기본 진입점으로 존재
- `application.properties`에는 `spring.application.name=coupon` 설정만 있음
- 기능/도메인 모델은 아직 정의되지 않음

## 기술 스택
- Java 17
- Spring Boot 4.1.1
- Spring Web MVC (`spring-boot-starter-webmvc`)
- Thymeleaf (`spring-boot-starter-thymeleaf`)
- Gradle (빌드 도구)
- JUnit Platform (테스트)

## 디렉터리 구조
```
coupon/
├── src/
│   ├── main/
│   │   ├── java/com/min/coupon/
│   │   │   └── CouponApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/min/coupon/
│           └── CouponApplicationTests.java
├── build.gradle
└── settings.gradle
```

## 기술 과제 요구사항

### 과제 개요
쿠폰시스템 ADMIN을 개발 중이며, 특정 사용자들에게 쿠폰을 대량으로 발급하는 기능이 필요하다.
산출물은 코드와 그 외(+a) 산출물로 구성된다.

### 사용자 목록 업로드 파일 명세
사용자 목록은 CSV 또는 Excel 파일로 업로드한다. 파일 형식은 다음과 같다.

```
customer_id
11111
22222
33333
```

### 파일 검증 항목
업로드된 CSV/Excel 파일에 대해 아래 항목만 검증한다.
- 헤더값이 `customer_id`가 맞는지
- 회원 목록이 비어있지 않은지
- (검증 제외) 각 회원번호 자체의 유효성은 검사하지 않음

### 파일 재다운로드
업로드한 파일은 디버깅을 위해 사용자가 다시 다운로드할 수 있어야 한다.

### 구현 대상 API
- API 1: 파일 업로드 API
- API 2: 파일 다운로드 API

### 과제 범위
이번 작업은 파일 업로드 검증과 재다운로드(API 1, 2) 구현까지로 한정한다.
업로드된 customer_id 목록을 바탕으로 실제 쿠폰을 발급/저장하는 로직은 범위에서 제외한다.

## 설계

### 패키지 구조 (`com.min.coupon`)
- `domain` — `CouponIssueFile` 엔티티 (업로드 메타데이터)
- `file` — CSV/Excel 파싱 및 검증
  - `CouponFileParser` 인터페이스, `CsvCouponFileParser` / `ExcelCouponFileParser` 구현체 (확장자·Content-Type으로 선택)
- `storage` — 원본 파일 바이트를 로컬 디스크에 저장하는 `FileStorageService`
- `web` — `CouponIssueFileController`, 요청/응답 DTO, `@RestControllerAdvice` 예외 처리

### 저장 방식
- 메타데이터: H2 파일 모드 DB (`jdbc:h2:file:...`) + Spring Data JPA로 영속화 → 서버 재시작 후에도 업로드 기록/다운로드 유지
- 원본 파일: 로컬 디스크에 저장 (예: `./file-storage/{storedFileName}`), DB에는 저장 경로만 기록

### 데이터 모델: `CouponIssueFile`
| 필드 | 설명 |
|---|---|
| id | PK (auto increment) |
| originalFileName | 업로드 시 원본 파일명 |
| storedFileName | 디스크에 저장된 파일명 (UUID 기반) |
| fileType | CSV / EXCEL |
| customerCount | 검증된 customer_id 행 개수 |
| createdAt | 업로드 시각 |

### API 명세

**API 1. 파일 업로드**
- `POST /api/coupon-issue-files` (multipart/form-data, part명 `file`)
- 성공 201: `{ "id": 1, "originalFileName": "customers.csv", "customerCount": 3, "uploadedAt": "2026-10-22T13:00:00" }`
- 실패 400: `{ "message": "..." }` (헤더가 customer_id가 아님 / 회원 목록이 비어있음 / 지원하지 않는 파일 형식)

**API 2. 파일 다운로드**
- `GET /api/coupon-issue-files/{id}/file`
- 성공 200: 원본 파일 스트림, `Content-Disposition: attachment; filename="<originalFileName>"`
- 실패 404: `{ "message": "파일을 찾을 수 없습니다." }`

### 검증 규칙
- 헤더값이 정확히 `customer_id`인지 확인
- 헤더 이후 데이터 행이 1개 이상 존재하는지 확인 (비어있으면 실패)
- 각 customer_id 값 자체의 유효성은 검사하지 않음

### 사용 라이브러리 (추가 예정)
- `spring-boot-starter-data-jpa`, `com.h2database:h2`
- `org.apache.commons:commons-csv` (CSV 파싱)
- `org.apache.poi:poi-ooxml` (Excel(.xlsx) 파싱)

### 공통 설계 (API 1, 2 공통)

**예외 계층 구조**
- `CouponFileException` — 공통 상위 예외 (RuntimeException 기반, `code` 보유)
  - `InvalidCouponFileException` — 헤더가 `customer_id`가 아님 / 회원 목록이 비어있음
  - `UnsupportedFileTypeException` — csv/xlsx가 아닌 파일
  - `CouponIssueFileNotFoundException` — 다운로드 시 존재하지 않는 id

**공통 에러 응답 DTO**
```
ErrorResponse {
  code: String     // 예: "INVALID_HEADER"
  message: String  // 사용자에게 보여줄 메시지
}
```

**공통 예외 처리**
`@RestControllerAdvice` 클래스(`CouponIssueFileExceptionHandler`) 하나에서 아래와 같이 매핑한다.

| 예외 | HTTP | code |
|---|---|---|
| InvalidCouponFileException (헤더 불일치) | 400 | `INVALID_HEADER` |
| InvalidCouponFileException (목록 비어있음) | 400 | `EMPTY_CUSTOMER_LIST` |
| UnsupportedFileTypeException | 400 | `UNSUPPORTED_FILE_TYPE` |
| CouponIssueFileNotFoundException | 404 | `FILE_NOT_FOUND` |

**공통 파일 타입 판별**
확장자(`.csv` / `.xlsx`) 기준으로 `CsvCouponFileParser` / `ExcelCouponFileParser` 중 선택한다. 둘 다 해당하지 않으면 `UnsupportedFileTypeException`을 던진다. 이 판별 로직은 업로드 시 파서 선택과 다운로드 시 응답 Content-Type 결정에서 공통으로 사용한다.

**공통 상수**
허용 헤더명(`customer_id`)과 허용 확장자 목록(`.csv`, `.xlsx`)은 `CouponFileConstants` 한 곳에 모아 파서·검증기가 참조하도록 한다.

## 향후 계획
위 설계를 바탕으로 feature 브랜치에서 API 1, 2 구현 진행.
