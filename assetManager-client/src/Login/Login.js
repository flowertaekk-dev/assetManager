import React from 'react'

import Button from '../components/Button/Button'

import './Login.css'

const Login = () => {
    return (
        <section className="Login">
            <div className="Login__field">
                <div className="Login__item">
                    <label htmlFor="loginId">ID</label>
                    <input type="text" id="loginId" name="loginId" placeholder="ID"/>
                </div>

                <div className="Login__item">
                    <label htmlFor="loginPw">PASSWORD</label>
                    <input type="password" id="loginPw" name="loginPw" placeholder="PASSWORD"/>
                </div>

                <div className="Login__item">
                    <label htmlFor="loginEmail">E-MAIL</label>
                    <input type="email" id="loginEmail" name="loginEmail" placeholder="E-MAIL"/>
                </div>
            </div>

            <div>
                <Button name="Sign in" />
                <Button name="Cancel" />
            </div>
        </section>
    )
}

export default Login