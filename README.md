# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.


## Mission 1. 웹 서버 1단계 - index.html 응답

### 기능요구사항
- 정적인 html 파일 응답: http://localhost:8080/index.html로 접속 시 해당 파일 응답.
- HTTP Request 내용 출력: HTTP Request의 내용을 로거를 이용해 출력.

### 프로그래밍 요구사항
- 프로젝트 분석
- 구조 변경
  - [자바 스레드 모델](https://github.com/jjy0709/be-was/wiki/%5BJava%5D-Thread-Class)에 대해 학습한다. 
  - [자바 Concurrent 패키지](https://github.com/jjy0709/be-was/wiki/%5BJava%5D-Concurrent-Package)에 대해 학습한다.
  - 기존의 프로젝트를 Concurrent 패키지를 사용하도록 변경한다.
- OOP와 클린 코딩
  - 기능요구사항을 만족하는 코드 작성
  - 유지보수에 좋은 구조에 대해 고민

### 구현에 대한 설명
1. HttpRequest가 들어왔을 때 RequestHandler로 내용이 들어온다.
2. 이를 처리하고 Request에 따라 다른 작업을 실행하기 위해서는 Request를 공유해야 한다.
    - Request 공유를 위해 Java의 Request class를 사용하려 하였다.
    - method 사용이 불편 -> version, uri 등의 type/null check 문제와 header의 메소드 등
    - Java.HttpRequest class를 참고하여 새롭게 HttpRequest class 및 builder를 생성하였다.
3. 새롭게 생성한 HttpRequest 클래스를 이용하여 코드를 구현하였다.
4. Concurrent Package의 ExecutorService와 Executors 등을 이용해 코드를 수정하였다.

<br/>

## Mission 2. 웹 서버 2단계 - GET으로 회원가입

### 기능요구사항
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동, 회원가입 폼을 표시한다.
- 회원가입 폼을 통해 가입할 수 있다.

### 프로그래밍 요구사항
- get 요청 뒤의 parameter을 파싱하여 사용자의 입력값을 받아 온다.

### 구현에 대한 설명
1. /user/create에 대한 로직을 처리할 수 있는 Controller 클래스를 생성한다.
2. ? 문자를 통해 파라미터 부분을 구분하고 파싱을 통해 입력값을 받아온다.
3. DB addUser를 통해 사용자 정보를 저장한다.

### 고려한 부분
- 같은 아이디를 가진 요청이 올 경우 가입할 수 없다.
- 동시에 같은 아이디를 가진 요청이 올 경우 lock을 통해 concurrency를 관리한다.
- Contorller 클래스가 너무 커지지 않도록 분리하였다.

<br/>

## Mission 3. 웹 서버 3단계 - 다양한 컨텐츠 타입 지원

### 기능요구사항
- .html 파일 외에도 다양한 컨텐츠 타입을 지원하도록 한다.

### 프로그래밍 요구사항
- 기능요구사항의 정상 작동 + static 폴더의 파일들에 대한 요청이 잘 처리되는지 확인한다.

### 구현에 대한 설명
1. 처음에는 요청의 Accept 헤더를 참고하여 response의 content-type을 결정하였다.
2. MIME 타입에 대해 공부한 뒤 Enum을 추가하여 파일의 확장자에 따라 response의 Content-type 헤더를 넣어주었다.
3. 구조에 대한 피드백을 통해 더 좋은 구조를 만들기 위하여 수정하였다.

<br/>

## Mission 4. 웹 서버 4단계 - POST로 회원 가입

### 기능요구사항
- 로그인을 GET에서 POST로 수정 후 동작하도록 작동
- 가입 완료 시 index.html로 이동

### 프로그래밍 요구사항
- form.html에서 post 방식으로 수정
- http redirection 구현

### 구현에 대한 설명
1. POST 요청은 쿼리가 body를 통해 오기 때문에 body를 parse 해주어야 한다.
2. Content-Length 헤더가 있을 경우 읽어서 그 길이 만큼만 read 하도록 해주었다.
3. GET/POST 구분과 확장성을 위해 annotation과 container를 추가하였다. 

### 고민 
- Container 클래스도 테스트를 해야할 것 같은데 어떻게 해야할지 고민이다..
- 리팩토링에 대한 고민..

<br/>

## Mission 5. 쿠키를 이용한 로그인
 
### 기능요구사항
- 가입한 회원 정보로 로그인 할 수 있다.
- 로그인 성공 시 index.html로 이동, 실패 시 login_failed.html로 이동

### 프로그래밍 요구사항
- 로그인 성공 시 HTTP 헤더의 쿠키 값에 세션 아이디 반환
- 서버는 세션 아이디에 해당하는 User 정보에 접근할 수 있어야 한다.

### TODO
[X] HTTP Cookie & Session 공부
[X] 공부한 내용 Wiki 작성
[X] 로그인 구현
[X] Cookie 구현
[X] Session 구현
[X] 리다이렉션 구현

### 고민
- Session과 Cookie 클래스를 생성하였는데 필요한 필드를 모두 가지고 있는지 확신이 서지 않는다..
- 테스트케이스를 잘 짠 것인지 고민....

<br/>

## Mission 6. 동적인 HTML

### 기능요구사항
- 동적인 HTML 구현
  - 로그인 상태일 경우 사용자의 이름 표시
  - 로그인 상태가 아닐 경우 로그인 버튼 표시
  - user/list 또한 로그인 상태에 따라 다른 화면을 표시

### 프로그래밍 요구사항
- 쿠키와 세션 활용

### 구현에 대한 설명
1. 사용자의 요청이 왔을 때 헤더에서 Cookie 필드가 있다면 파싱하여 쿠키를 HttpRequest 객체 내에 저장한다.
2. 각 Controller에서 HttpRequest의 Cookie가 유효한지를 확인하고 유효할 경우 해당 로직을 수행한다.
3. 동적 HTML의 경우 HttpResponse를 Model처럼 활용하여 렌더링에 필요한 정보들을 넣어 HttpResponseRenderer에서 렌더링 하도록 하였다.
4. .html 파일에 수정이 필요한 부분에 ${key}를 넣어 수정해 둔 뒤, HttpResponse의 attribute entry들에 따라 해당 부분을 replace 하는 식으로 구현하였다.

### 고민
- Controller에서 html tag를 String 으로 생성하는 것이 효율적인가?
- 여러 방법으로 테스트를 해보는 것이 필요해 보임
- html 파일 내용 수정 시 String replace의 연쇄적인 호출로 인해 할당 반복...

<br/>

## Mission 7. 게시판 기능 완성

### 기능요구사항
- 글쓰기 기능 구현
  - 로그인 한 사용자는 write.html 에서 글 작성 가능
  - 로그인 하지 않을 경우 불가
  - 작성한 글의 제목을 index.html에 표시
- 세부 글 표시 기능 구현
  - 로그인 한 사용자는 게시글의 내용 확인 가능
  - 그렇지 않은 경우 불가

### 프로그래밍 요구사항
- 404 에러 및 에러 처리 페이지 구현
  - 404 에러 외에도 여러 가능한 에러 상황을 처리

### TODO
[X] write.html 작성
[X] 버튼 클릭 시 이동하도록 수정
[ ] 관련 Controller 추가
[ ] 작성된 글을 저장할 수 있는 DB 추가
[ ] index.html에 작성된 글 로드하는 로직 추가
[ ] 에러 처리
