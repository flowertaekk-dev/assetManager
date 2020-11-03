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
 * @param {Object} accountBook 
 */
export function setAccountBook(accountBook) {
    localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))
}