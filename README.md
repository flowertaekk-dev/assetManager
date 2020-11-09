# assetManager

## 프로젝트 설명

* 포스(POS) 시스템의 무료화.

### 구조

1. 유저는 web을 통해서 로그인한다.
    * assetManager는 웹서비스!

### 이익구조

* 웹의 사이드에 광고를 넣어서 이익을 창출한다.
  * 유저는 무료로 서비스를 이용 가능하기 때문에 유저의 입장에서는 단순한 무료 서비스다.
* 유저가 광고를 직접 넣기를 원하는 경우가 있을까?
  * 있다면 이것도 대응하고 싶지만 우선은 보류.

### 프로젝트 진행 규칙

1. 코딩에 들어가기 전에 [Issue](https://github.com/flowertaekk-dev/assetManager/issues) 를 생성한다.
    1. [Issue](https://github.com/flowertaekk-dev/assetManager/issues) 에 접속
    2. 오른쪽 상단에 'New Issue' 버튼 클릭
    3. 'Title'란에 관련된 프로젝트로 시작해서 간략한 코딩 내용을 기재한다.
        * server/...
        * client/...
        * ... (공통인 경우는 기재 불필요)
    4.'Leave a comment' 란에 자세한 구현 내용 기록
    5. 오른쪽 사이드 바에서 'Assignees'를 본인으로 설정! 관련있는 다른 사람도 추가해도 OK!
    6. 오른쪽 사이드 바에서 'Labels'를 설정
    7. 하단의 'Submit new issue' 클릭해서 이슈를 생성한다.
2. 생성된 이슈의 이슈번호를 확인한다.
    * 이슈화면 타이틀에서 확인 할 수 있다. ex) `회원정보 관리 기능 추가 #8`
3. dev branch로부터 새로운 branch를 생성한다.
    * 네이밍 규칙은 feature/<이슈번호> ex) `feature/8 or fix/8 or hotfix/8`
4. 해당 이슈에 'being handled' label을 설정한다.
5. 프로젝트 진행관련 내용을 이슈에 기록한다.
6. 작업이 끝나면 PR을 마음에 드는 누군가에게 날린다.
7. PR까지 끝나면 'being handled'를 제거하고, 이슈를 close 한다.

## 프로젝트 용어집

* AccountBook:
    * 장부 데이터. 해당 유저가 소유하고 있는 전체적인 인보이스를 의미한다.
    * `{userId: {businessId:{menuId:{...}}, businessId:{menuId:{...}, menuId:{...}}}}` 이런 구조.
* Business:
    * 상호명 데이터. 유저가 소유하고 있는 상호명을 의미한다.
* Table:
    * 테이블 정보 데이터. 2020-11-09 시점에서는 '테이블 개수' 데이터만 관리하고 있다.
* Menu:
    * 메뉴 데이터. Business 데이터에 포함되어 있는 특정 메뉴.
* Invoice:
    * 특정 Business가 소유하고 있는 인보이스를 의미.
    * Business와 Invoice는 1:N 관계이다.
