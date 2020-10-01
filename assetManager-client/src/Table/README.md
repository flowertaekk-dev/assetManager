## ACCOUNT_BOOK 데이터 구조

``` json
{
  "상호명ID": {
    "테이블 번호": [
      {
        "메뉴1": {
          "id": "메뉴ID",
          "count": 0,
          "price": 20000
        }
      },
      {
        "메뉴2": {
          "id": "메뉴ID",
          "count": 0,
          "price": 6000
        }
      },
      {
        "메뉴3": {
          "id": "메뉴ID",
          "count": 0,
          "price": 7000
        }
      }
    ]
  }
}
```

### console test

``` js
console.log('json',
`
    ${
        JSON.stringify(
            {
                [selectedBusiness.selectedBusinessId]: {
                    [props.tableTitle]: response.menus.map(menu => {
                        {
                            return {
                                [menu.menu]: {
                                'id': menu.menuId,
                                'count': 0,
                                'price': menu.price
                                }
                            }
                        }
                    })
                },
            }
        )
    }
`)
```
