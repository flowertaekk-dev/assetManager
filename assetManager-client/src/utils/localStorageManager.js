import KEYS from './localStorageKeys'

/**
 * 장부(account book)을 불러온다
 */
export function getAccountBook() {
    return JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))
}

/**
 * 장부(account book)을 localStorage에 저장한다
 *
 * @param {Object} accountBook { businessId: [ { menuId:{...}, menuId:{...} }, {...} ] }
 */
export function setAccountBook(accountBook) {
    localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))
}

 /**
  * 선택된 상호(business)의 장부(account book)를 불러온다
  *
  * @param {string} businessId
  */
export function getSelectedBusienssAccountBook(businessId) {
    return getAccountBook()[businessId]
}

/**
 * 선택된 상호(business)의 장부(account book)를 갱신한다
 *
 * @param {string} businessId
 * @param {string} tableId
 * @param {Object} invoice { menuId:{...}, menuId:{...} }
 */
export function updateSelectedBusienssAccountBook(businessId, tableId, invoice) {
    const accountBook = getAccountBook()
    const myAccountBook = accountBook[businessId]
    myAccountBook[tableId] = invoice

    setAccountBook(accountBook)
}