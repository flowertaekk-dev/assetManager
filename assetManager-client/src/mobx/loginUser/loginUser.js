import { observable } from 'mobx'

const loginUser = observable({
    loginUserId: window.localStorage.getItem('loginUser'),
    updateLoginUser(id) {
        this.loginUserId = id
        window.localStorage.setItem('loginUser', id)
    },
    deleteLoginUser() {
        this.loginUserId = undefined
        window.localStorage.removeItem('loginUser')
    },
})

export { loginUser }