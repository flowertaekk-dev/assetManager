import React, { useState } from 'react'
import { Redirect, withRouter } from 'react-router-dom'
import { useObserver } from 'mobx-react'

import Button from '../components/Button/Button'
import customAxios from '../customAxios'
import useStore from '../mobx/useStore'

import './Login.css'

const Login = (props) => {

    const { loginUser } = useStore()

    const [id, setId] = useState('')
    const [password, setPassword] = useState('')

    const loginClickHandler = () => {
        customAxios('/login', (data) => {

            if (data.resultStatus === 'SUCCESS') {
                loginUser.updateLoginUser(id)
                window.localStorage.setItem('loginUser', id)
                // TODO 로그인 후 메인화면으로 이동하도록!
                props.history.push('/tableMap')
            } else {
                alert(data.reason)
            }

        }, {
            id: id,
            password: password
        })
    }

    return useObserver(() => (
        <section className="Login">
            <div className="Login__field">
                <div className="Login__item">
                    <label htmlFor="loginId">ID</label>
                    <input type="text"
                        id="loginId" name="loginId" placeholder="ID"
                        value={id}
                        onChange={(event) => setId(event.target.value)}/>
                </div>

                <div className="Login__item">
                    <label htmlFor="loginPw">PASSWORD</label>
                    <input type="password"
                        id="loginPw" name="loginPw" placeholder="PASSWORD"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}/>
                </div>

            </div>

            <div>
                <Button name="Sign in" clickHandler={loginClickHandler} />
                <Button name="Cancel" />
            </div>
        </section>
    ))
}

export default withRouter(Login)