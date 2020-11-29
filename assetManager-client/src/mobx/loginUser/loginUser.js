import { observable } from 'mobx'

const loginUser = observable({
    loginUser: JSON.parse(window.localStorage.getItem('loginUser')),
    updateLoginUser(user) {
        this.loginUser = user
        window.localStorage.setItem('loginUser', JSON.stringify(user))
    },
    deleteLoginUser() {
        this.loginUser = undefined
        window.localStorage.removeItem('loginUser')
    },
})

export { loginUser }