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
   
배포 환경
   - 프로젝트의 Hash Key 값을 Kakao Developer 사이트에 등록해서 배포 시에도 지도 보이게 적용
   - 카카오 API Key를 숨기기 위해 local properties 에 숨겨서 배포 시 안보이게 적용

### 프로젝트 구조

## 프로젝트 설치 방법
   - 프로젝트 clone
   - 프로젝트를 clone 하면 ERROR 가 발생하는 것을 볼 수 있습니다. 이 때, Gradle Scripts/local.properties 에 해당 정보를 추가해야 합니다.
   ```xml
    sdk.dir="이 부분은 자동으로 채워져 있을 겁니다. 건들일 필요 없습니다"
    kakao_api_key = "카카오 네이티브 앱 키"
    kakao_rest_api_key = "KakaoAK 카카오 REST API 키"
   ```
## 프로젝트 사용법
   1. 프로젝트를 설치하고 실행하면 작동합니다.
## 프로젝트 Activity

#### SignInActivity (activity_sign_in.xml)
   앱 실행 시 처음으로 볼 수 있는 화면으로, 로그인 기능을 제공한다.<br> 
   아이디와 비밀번호를 입력 후 로그인 버튼을 누르면 계정 정보가 올바른지 확인한다. <br>
   기기에서 로그인을 한 적 있으면 바로 전체 게시물 확인 페이지로 이동하는 자동 로그인 서비스를 지원한다. <br>

   <img src="https://user-images.githubusercontent.com/68294499/170962274-69a6a506-6eeb-4b0c-8e77-4219828b5835.jpg" width="30%" height="30%">
   
#### SignUpActivity (activity_sign_up.xml)
   회원가입 페이지로 이름, 아이디, 나이, 비밀번호, 닉네임, 성별을 입력받는다.<br>
   아이디, 닉네임은 중복검사를 통해 유일한 아이디와 닉네임만 사용 가능하고, 비밀번호는 입력 후 비밀번호 확인을 거친다.<br>
   
   <img src="https://user-images.githubusercontent.com/68294499/170963400-9c1da438-e9fe-4bc0-8183-39697896fb17.png" width="30%" height="30%">
   
#### MainActivity
앱의 기본 화면으로 사용자의 현재 위치에 기반하여 신청 가능한 게시물을 보여준다. 게시글 검색 및 등록이 가능하다. <br>
게시글을 선택하는 경우 해당 게시물의 상세 정보 페이지를 확인 할 수 있다.  버튼을 통해 채팅, 알림 화면, 마이 페이지로 이동할 수 있다. <br>

<img src ="https://user-images.githubusercontent.com/67624124/171014777-48463fa3-ac68-4389-8852-00f090921bfb.png" width="30%" height="30%">

#### ShowPostActivity (activity_show_post.xml)
   사용자가 선택한 게시물의 상세 정보를 보여준다. 사용자는 음식점 이름, 위치 , 만날 장소, 거리, 인원, 시간 등을 확인할 수 있다. <br>
   상세 정보를 확인한 뒤 신청 버튼으로 해당 게시물의 같이 먹기 신청이 가능하다. <br>

#### PostingActivity (activity_posting.xml)
 음식점, 만날 장소, 음식점 카테고리, 모일 인원 및 모인인원, 만날 날짜, 만날 시간, 게시글 내용을 입력받아 게시물을 만들어 등록한다.<br>
 이때 작성자의 성별 공개/비공개를 설정할 수 있다.<br>
 
<img width="1137" alt="제목 없음" src="https://user-images.githubusercontent.com/68294499/170965375-662d6615-9b71-4e87-9764-79b7c3cfae27.png">

#### SearchRestaurantActivity (activity_search_restaurant.xml)
   찾고자 하는 음식점 또는 장소를 검색 후 지도에서 정확한 장소를 선택한다.<br>
   
   <img src="https://user-images.githubusercontent.com/68294499/170966463-989abfef-42e5-43ff-b20a-a7e5e96370aa.png" width="30%" height="30%">
   
#### MyPageActivity (activity_my_page.xml)
   현재 사용자의 정보(닉네임, 이름, 아이디, 나이, 성별)를 표시한다.<br>
   이곳에서 사용자가 작성한 게시물을 수정 및 삭제하는 페이지로 이동하거나,<br>
   사용자의 회원정보를 수정하는 페이지로 이동하거나 로그아웃할 수 있다.<br>
   
   <img src="https://user-images.githubusercontent.com/68294499/170967100-0fab088f-cd77-41c1-801f-05b4d6db0608.png" width="30%" height="30%">
   
#### EditUserProfileAcitivity (activity_edit_user_profile.xml)
   기존 회원정보에서 나이, 비밀번호를 수정 가능하다.<br>
   
   <img src="https://user-images.githubusercontent.com/68294499/170970381-ea0f1447-681c-4b74-9cc8-1a78ad15d953.png" width="30%" height="30%">
   
#### MyPostListActivity (activity_my_post_list.xml.xml)
   기기에 로그인 되어있는 사용자가 작성한 글들을 리스트로 확인할 수 있다.<br>
   
   <img src="https://user-images.githubusercontent.com/68294499/170970647-b3ad6390-fb73-46c8-baf4-fe3f0f5323cd.png" width="30%" height="30%">
   
#### MyPostListActivity (activity_my_post_list.xml.xml)
   기존 올렸던 게시물을 수정한다.<br>
   음식점, 만날 장소, 음식점 카테고리, 모일인원, 만날 날짜, 만날 시간, 게시물 내용, 성별 공개/비공개 를 수정할 수 있다.<br>
   다만 현재 모인 인원은 수정 불가능하다.<br>
   이 기능은 ‘내 게시물 보기’에서 수정할 게시물을 선택해서 진행할 수 있다.<br>
   
   <img src="https://user-images.githubusercontent.com/68294499/170970881-656af35f-1f21-4df5-aff6-3ae30a099497.png" width="30%" height="30%">
   
#### alarmActivity (activity_alarm_activity.xml)
   현재 사용자에게 온 알림을 모두 확인할 수 있다. <br>
   같이 먹기 신청, 신청 수락, 신청 거부 알림이 있으며, 같이 먹기 신청 알림의 경우 사용자는 수락과 거부를 결정할 수 있다. <br>

#### ChattingRoomActivity (activity_chatting_room.xml)
   사용자가 현재 사용할 수 있는 모든 채팅방 목록을 보여준다. <br>
   채팅방 목록에서는 채팅방 제목, 마지막으로 온 메시지, 마지막 메시지가 온 시간이 표시된다. <br>
   각 채팅방을 선택하면 해당 채팅방으로 들어가진다. <br>
   
#### GroupMessageActivity (activity_group_messag.xml)
   게시물별 모이기로 한 사용자들의 단체채팅방으로, 실시간 채팅 기능을 제공한다. <br>
   작성자가 신청자의 신청을 수락하면 신청자는 채팅방에 자동으로 초대된다. <br>
   게시물을 작성한 작성자는 채팅방을 없앨 수 있으며, 채팅방을 없애면 동시에 그 게시물이 삭제된다. <br>
   
## 버그
  -5월 30일(배포일 기준) 에서는 현재 버그는 없습니다.

## 프로젝트 작성자 및 도움을 준 사람
#### 작성자
- 김예진 [Github](https://github.com/originalchaltteokcookie)
  <br>이메일 wndrnrdk@naver.com
- 정현진 [Github](https://github.com/Hyunjin-Jung)
  <br>이메일 hjjung0810@naver.com
#### 도움을 준 사람
- 오창묵 [Github](https://github.com/Godmook)
  <br>이메일 cmoh4135@naver.com
