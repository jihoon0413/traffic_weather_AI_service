# 🚌 버스 혼잡도 예측 시스템 (Bus Congestion Prediction)
기상 데이터와 시간 요인을 기반으로 버스 혼잡도를 예측하는 LSTM 모델을 만들고 테스트한 프로젝트

<img width="2294" height="1296" alt="image" src="https://github.com/user-attachments/assets/033cbfe6-3f22-40dc-af9b-e8a41fb88804" />


# 🧩 프로젝트 개요

이 프로젝트는 날짜, 계절, 요일, 출퇴근 시간, 기상 데이터(기온, 강수량, 습도 등) 을 기반으로
버스의 혼잡도를 예측하기 위해 설계되었습니다.

실제 운행 데이터와 기상 정보를 수집하고, 이를 Spring Batch를 활용해 전처리 및 일괄 처리하여
Deeplearning4j 기반 LSTM(Long Short-Term Memory) 모델을 학습시켰습니다.

모델의 예측 결과를 실제로 활용할 수 있도록 **API 및 간단한 프론트엔드 프로토타입**까지 구현한 프로젝트입니다.


# ⚙️ 시스템 구성

| 구성 요소                            | 역할                       |
| -------------------------------- |--------------------------|
| **Spring Batch**                 | 대규모 데이터 수집 및 전처리 자동화     |
| **LSTM (Deeplearning4j)**        | 시계열 패턴 기반 혼잡도 예측 모델 학습   |
| **H2 / MySQL (선택)**              | 중간 데이터 저장 및 로깅용          |
| **XChart / JFreeChart** | 모델 학습 및 예측 결과를 시각적으로 분석(라인 차트, 비교 그래프 등) |
| **React** | 예측 결과와 모델 분석 정보를 UI로 시각화 |


# 🖼️ 사진 자료

<details>
<summary>테스트 결과 </summary>
<div markdown="1">
  
  * real vs predict
<img width="900" height="400" alt="image" src="https://github.com/user-attachments/assets/118da1e2-1e0a-44e6-bd38-f201f8ee5c8f" />
  
  * predict scatter
<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/8c25312a-a877-4e92-8faa-6960d77f4272" />

</div>
</details>

<details>
<summary>학습 데이터 이미지</summary> 
<div markdown="1">
  
  * 승차 및 기상 데이터
<img width="1275" height="484" alt="img" src="https://github.com/user-attachments/assets/871754c0-730b-4096-bf49-04a80c440fd9" />

  * 전처리를 거친 feature 데이터
<img width="1432" height="619" alt="img_1" src="https://github.com/user-attachments/assets/02d8f52e-6ece-437d-b0e8-e04ca7120b76" />
</div>
</details>

# 🔍 문제 및 해결

실제 승·하차 데이터에서 **하차 승객의 미등록률로 인해 실제 혼잡도보다 낮게 집계되는 문제**를 발견했습니다.

이로 인해:

### 해결 방법: 보정계수 기반 데이터 보정

- 하차 승객 미등록률을 반영하기 위한 **보정계수(Correction Factor)** 를 산출
- 기존 하차 인원 데이터에 보정계수를 적용하여 **현실에 가까운 학습 데이터셋 구성**
- 보정 전·후 데이터를 비교하며 모델 성능과 예측 경향을 함께 분석

이를 통해 단순 모델 개선이 아니라

**데이터 자체의 신뢰도를 높이는 방향으로 접근**했습니다.

<img width="450" height="200" alt="real-predicted" src="https://github.com/user-attachments/assets/72b3137f-5874-4d64-a31d-a95514c0fd7f" /> <img width="450" height="200" alt="line_after" src="https://github.com/user-attachments/assets/b901e276-7aeb-4cda-9e84-3e53ac12a7c4" />


# 📈 모델 학습 및 성능 평가

| 주요 지표 | 보정 전 | 보정 후 |
| --- | --- | --- |
| MAE | 0.059 | 0.069 |
| MSE | 0.006 | 0.008 |
| RMSE | 0.074 | 0.091 |
| SMAPE | 21.743 | 79.81 |
| R² | 0.880 | 0.778 |

보정 후 전반적으로 성능이 떨어졌지만 이는 버스 정류장을 각 특성에 맞게 보정한 것이 아니라 일괄적으로 보정했기 때문에 성능이 떨어진 것으로 추측됩니다. 하지만 문제 해결 과정에서 확인했던 것처럼 모델의 현실성을 높일 수 있었습니다.

# 📬 향후 개선 계획

* [x] 실시간 예측을 위한 REST API 서버화
* [x] 예측 결과 대시보드 구축
* [x] 모델 정확도 측정 및 성능 개선

# 🪛 스택
![js](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![js](https://img.shields.io/badge/Spring_Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![js](https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![js](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=pink)
![js](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black)

