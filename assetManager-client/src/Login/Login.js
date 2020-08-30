import React, { useState } from 'react'

import Button from '../components/Button/Button'
import customAxios from '../customAxios'

import './Login.css'

const Login = () => {

    const [id, setId] = useState('')
    const [password, setPassword] = useState('')

    const loginClickHandler = () => {
        customAxios('/login', (data) => {
            // TODO 로그인 실패했을 때 로직 처리!
            console.log('login', data)
        }, {
            id: id,
            password: password
        })
    }

    return (
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

                {/* <div className="Login__item">
                    <label htmlFor="loginEmail">E-MAIL</label>
                    <input type="email" id="loginEmail" name="loginEmail" placeholder="E-MAIL"/>
                </div> */}
            </div>

            <div>
                <Button name="Sign in" clickHandler={loginClickHandler} />
                <Button name="Cancel" />
            </div>
        </section>
    )
}

export default Login