## 개요
- 인스타그램을 모티브로한 소셜 서비스 프로젝트

  기술 : Java, Spring, JPA, Mysql, Docker, AWS, SSE, Elasticsearch, Web Socket, RabbitMQ
  
  목표 : 직장인을 타겟으로 피드를 올리고 서로 채팅할 수 있는 서버를 구축.

## 회원
- [x]  회원가입
- [x]  인증 ( 이메일 )
- [x]  인증코드 발행 및 확인
- [x]  로그인 토큰 발행
- [x]  로그인 토큰을 통한 제어 확인 ( JWT, Filter 사용으로 구현 )
- [x]  회원 상세페이지

## 게시글 (뉴스피드, 릴스)
### 공통
- [x]  게시글에 댓글 CRUD
- [x]  게시글과 댓글에 '좋아요' 표시
- [x]  검색을 위한 해시태그 작성
- [x]  유저 태그
- [x]  채팅기능을 활용한 공유하기
- [x]  관심있는 게시글 저장하기
### 뉴스피드
- [x]  게시글 CRUD
- [x]  게시글 상세페이지
### 릴스
- [ ]  게시글 CRUD

## 스토리
- [x]  스토리 생성 및 삭제
- [x]  스토리 사진 저장을 위한 firebase 연동
- [x]  팔로워에게만 노출
- [x]  스케줄링 사용하여 24시 후 자동 삭제

## 채팅
- [x]  Websocket을 사용한 양방향 통신으로 사용자간 1:1 실시간 채팅
- [x]  Stomp로 pub/sub 구축하여 메세지 전달
- [x]  외부 message broker Rabbitmq 사용하여 message queue 관리
- [x]  채팅 알림 사용하여 내 채팅방 리스트 조회

## 검색
- [x]  회원 아이디와 해시태그 검색을 위한 elastic seacrh 연동
      
## 알림
- [x]  팔로잉, 좋아요, 댓글, 대댓글, 채팅 알림 실시간으로 전송
- [x]  SSE 사용하여 클라이언트-서버 통신

## 팔로우
- [x]  팔로우 추가 및 삭제
- [x]  팔로우 목록 조회

## 좋아요
- [x]  좋아요 on/off
- [x]  게시글, 댓글 좋아하는 회원 조회