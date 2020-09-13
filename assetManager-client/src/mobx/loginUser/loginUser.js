import { observable } from 'mobx'

const loginUser = observable({
    loginUserId: window.localStorage.getItem('loginUser'),
    updateLoginUser(id) {
        this.loginUserId = id
    },
    deleteLoginUser() {
        this.loginUserId = undefined
    },
})

export { loginUser }