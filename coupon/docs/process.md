# 작업 프로세스 지침

## 워크플로 규칙
1. **공통 기반 코드**(Entity/Repository, 예외 처리, Parser, Storage 등 API 1·2가 함께 쓰는 코드)는 `main`에서 직접 구현하고 커밋 후 바로 push한다.
2. 공통 기반 코드 push 이후, **API 단위 기능 작업부터는 브랜치를 먼저 생성**한 뒤 그 브랜치 위에서 진행한다. (main에 직접 커밋하지 않는다)
3. 작업이 끝나면 원격에 push 하고 **PR을 생성**한다.
4. 리뷰 승인 후에만 `main`에 머지한다.
5. 새 단계를 시작하거나 끝낼 때마다 아래 진행 단계 표를 갱신한다.

## 진행 단계

| 단계 | 작업 내용 | 브랜치 | 상태 | PR |
|---|---|---|---|---|
| 1 | 프로젝트 개요 문서화 (`docs/project.md`) | main | 완료 | - |
| 2 | 요구사항 정리 (쿠폰 대량 발급, 파일 업로드/다운로드) | main | 완료 | - |
| 3 | API 1/2 설계 및 공통 설계(예외 처리 등) | main | 완료 | - |
| 4 | `docs/process.md` 작성 | main | 완료 | - |
| 5 | 공통 기반 코드 구현 (Entity/Repository, 예외 처리, Parser, Storage) | main | 완료 | - |
| 6 | API 1 (파일 업로드) 구현 | feature/coupon-file-upload-api | 완료 | https://github.com/minjibyeongho/mentoring-coupon/pull/1 |
| 7 | API 2 (파일 다운로드) 구현 | feature/coupon-file-download-api | 진행중 | - |

## 컬럼 작성 규칙
- **상태**: `예정` / `진행중` / `완료` 중 하나로 기입
- **브랜치**: 실제로 그 단계를 작업한 feature 브랜치명 (예: `feature/coupon-file-upload-api`)
- **PR**: 해당 단계의 PR이 생성되면 PR 링크로 교체
