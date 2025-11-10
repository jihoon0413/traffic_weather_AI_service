# 🚌 버스 혼잡도 예측 시스템 (Bus Congestion Prediction)
기상 데이터와 시간 요인을 기반으로 버스 혼잡도를 예측하는 LSTM 모델 프로젝트

# 🧩 프로젝트 개요

이 프로젝트는 날짜, 계절, 요일, 출퇴근 시간, 기상 데이터(기온, 강수량, 습도 등) 을 기반으로
버스의 혼잡도를 예측하기 위해 설계되었습니다.

실제 운행 데이터와 기상 정보를 수집하고, 이를 Spring Batch를 활용해 전처리 및 일괄 처리하여
Deeplearning4j 기반 LSTM(Long Short-Term Memory) 모델을 학습시켰습니다.

# ⚙️ 시스템 구성

| 구성 요소                            | 역할                       |
| -------------------------------- |--------------------------|
| **Spring Batch**                 | 대규모 데이터 수집 및 전처리 자동화     |
| **LSTM (Deeplearning4j)**        | 시계열 패턴 기반 혼잡도 예측 모델 학습   |
| **H2 / MySQL (선택)**              | 중간 데이터 저장 및 로깅용          |
| **Jupyter / Visualization Tool** | 예측 결과 시각화     ( * 진행 예정) |


# 🖼️ 학습 데이터
<details>
<summary>학습 데이터 이미지</summary>
<div markdown="1">

  
  * 승차 및 기상 데이터
<img width="1275" height="484" alt="img" src="https://github.com/user-attachments/assets/871754c0-730b-4096-bf49-04a80c440fd9" />


  * 전치리를 거치 feature 데이터
<img width="1432" height="619" alt="img_1" src="https://github.com/user-attachments/assets/02d8f52e-6ece-437d-b0e8-e04ca7120b76" />


</div>
</details>


# 📬 향후 개선 계획

* 실시간 예측을 위한 REST API 서버화
* 예측 결과 대시보드 구축
* 모델 정확도 측정 및 성능 개선

# 🪛 스택
![js](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![js](https://img.shields.io/badge/Spring_Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![js](https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![js](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=pink)

