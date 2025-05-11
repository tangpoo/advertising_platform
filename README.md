# Advertising-Platform
사장님의 가게를 광고할 수 있도록 도움을 드리고, 사용자에게는 주변의 맛집을 소개합니다.
- 광고를 노출할 지역의 범위와 기간을 정하실 수 있습니다.
- 정액형, 수수료형 중 원하는 결제 유형을 선택하실 수 있습니다.
  - 정액형은 사장님이 지정한 날짜에 정기 결제가 이루어집니다.
  - 수수료형은 조회 건 수 마다 일정량의 수수료가 차감되며, 모두 소진될 시 광고가 비활성화 됩니다.
- 서비스를 이용하며 발생한 광고비를 산출하여 청구서를 조회할 수 있습니다.
- 소비자가 필터링과 정렬 조건을 설정하여 원하는 광고를 검색할 수 있습니다.

## Architecture
![Pasted image 20250201224204](https://github.com/user-attachments/assets/3d2e36ca-062b-4b4f-bc74-d08e8ee4db0e)

## 주요 기술
- 언어 : Kotlin 1.9
- 프레임워크 : Spring Framework (Spring Boot, Spring Batch)
- 빌드 도구 : Gradle
- 배포 및 운영 :
    - 서버 : AWS EC2, Docker
    - CI/CD : GitHub Actions
    - 데이터베이스 : MySQL
    - 검색 엔진 : Elasticsearch
    - 메시징 시스템 : RabbitMQ
- 모니터링 : Prometheus + Grafana, Brave, Zipkin
- 테스트 : JUnit5
- 품질 관리 : JaCoCo, SonarQube

## ERD
![Pasted image 20250201221003](https://github.com/user-attachments/assets/a371d4d0-3374-4167-bc4c-257f8528629b)

## 주요 API 상태 다이어그램
![image](https://github.com/user-attachments/assets/d13d82a3-2fca-4956-8d73-ba5a6efa899c)

![image](https://github.com/user-attachments/assets/b15d5c73-b305-4c25-8776-4ac11ace2c33)


