## 구조

1. localStorage에 있는 값을 불러와서 화면에 랜더링 & state에 저장
2. 값에 변화가 생길때마다 localStorage에 저장 & state에 저장
    * state에는 setter를 안 쓰고 객체에 직접 세팅하고 있는데 이렇게도 가능한 듯 하다. (이 방법이 좋지 않을지도 모르겠지만..)


## ACCOUNT_BOOK 데이터 구조

``` json
{
  "비지니스ID": [
    // 테이블 1
    { 
      "menuId": {
        "businessId": "BS-00",
        "menuId": "메뉴ID",
        "count": 0,
        "menu": "메뉴명",
        "price": 20000,
        "totalPrice": 0,
        "userId": "유저Id"
      },
      "menuId": {
        "businessId": "BS-01",
        "menuId": "메뉴ID",
        "count": 0,
        "menu": "메뉴명",
        "price": 7000,
        "totalPrice": 0,
        "userId": "유저Id"
      },
      "menuId": {
        "businessId": "BS-02",
        "menuId": "메뉴ID",
        "count": 0,
        "menu": "메뉴명",
        "price": 10000,
        "totalPrice": 0,
        "userId": "유저Id"
      }
    },
    // 테이블 2
    { ... }
  ]
}
```

