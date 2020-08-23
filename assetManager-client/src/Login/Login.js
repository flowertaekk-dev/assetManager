import React from 'react'

import Button from '../components/Button/Button'

import './Login.css'

const Login = () => {
    return (
        <section className="Login">
            <div>
                <label htmlFor="loginId">ID</label>
                <input type="text" id="loginId" name="loginId" placeholder="ID"/>
            </div>

            <div>
                <label htmlFor="loginPw">PASSWORD</label>
                <input type="password" id="loginPw" name="loginPw" placeholder="PASSWORD"/>
            </div>

            <div>
                <label htmlFor="loginEmail">E-MAIL</label>
                <input type="email" id="loginEmail" name="loginEmail" placeholder="E-MAIL"/>
            </div>

            <div>
                <Button name="Sign in" />
                <Button name="Cancel" />
            </div>
        </section>
    )
}

export default Login