# 나랑 같이 먹을래...? (GetEatWithMe) -FrontEnd

## 프로젝트 구성 안내 
나랑 같이 먹을래...? 어플은 안드로이드 기반 어플리케이션입니다.<br>
혼자 밥을 먹기 두려워 하는 사람들을 위해 같이 밥을 먹을 사람을 찾아주는 어플입니다.<br>
같이 밥을 먹기 위한 게시물을 올리면 신청을 통해 채팅으로 연결해주는 프로그램입니다.<br>
해당 Repository는 FrontEnd 코드를 포함하고 있습니다. <br>

### 제작 이유


### 기술 스택
- Java
- Retrofit
- Firebase Datasnapshot
- Variable Layout Component
- Native App (카카오 지도 API 이용)
- Rest API (카카오 검색 API, 서버 통신)

### 개발 환경
개발 환경
   - Android Studio 를 이용하여서 개발을 진행하였습니다.
   - REST API 방식으로 통신하도록 만들었습니다.
   - 채팅과 Push Message 를 구현하기 위하여 Firebase 와 통신을 하였습니다.
   - Firebase 는 Realtime Database 를 이용하여서 채팅 상황을 쉽게 받아올 수 있도록 구성하였습니다.
   - 유저 정보, 게시물 정보, 알람 정보는 서버의 데이터베이스에서 정보를 가지고 왔습니다.
   - 안드로이드와 Retrofit 을 이용한 통신을 하기 위해서 Retrofit dependency 사용
   - json 을 쉽게 변환하기 위해서 convertor-gson dependency 사용
   - firebase 를 이용하기 위해서 firebase-database, firbase-messaging dependency 사용

2. 배포 환경
   - 프로젝트의 Hash Key 값을 Kakao Developer 사이트에 등록해서 배포 시에도 지도 보이게 적용
   - 카카오 API Key를 숨기기 위해 local properties 에 숨겨서 배포 시 안보이게 적용

### 프로젝트 구조

## 프로젝트 설치 방법
   - 프로젝트 clone
   - 프로젝트를 clone 하면 ERROR 가 발생하는 것을 볼 수 있습니다. 이 때, Gradle Scripts/local.properties 에 해당 정보를 추가해야 합니다.
   ```xml
    sdk.dir=C\:\\Users\\cmoh4\\AppData\\Local\\Android\\Sdk
    kakao_api_key = "카카오 네이티브 앱 키"
    kakao_rest_api_key = "KakaoAK 카카오 REST API 키"
   ```
## 프로젝트 사용법
   1. 프로젝트를 설치하고 실행하면 작동합니다.
## 프로젝트 Activity

## 버그
  -5월 30일(배포일 기준) 에서는 현재 버그는 없습니다.

## 프로젝트 작성자 및 도움을 준 사람
