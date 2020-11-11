import React, { useState } from 'react'
import { withRouter } from 'react-router-dom'
import { useObserver } from 'mobx-react'
import crypto from 'crypto'

import Button from '../components/Button/Button'
import customAxios from '../customAxios'
import useStore from '../mobx/useStore'

import './Login.css'

const Login = (props) => {

    const { loginUser } = useStore()

    const [id, setId] = useState('')
    const [password, setPassword] = useState('')

    // ------------------------------------------------------
    // Handlers

    const loginClickHandler = () => {
        encryptPassword(password)
        // customAxios('/login', (data) => {

        //     if (data.resultStatus === 'SUCCESS') {
        //         // store에 저장
        //         // session에 저장
        //         loginUser.updateLoginUser(id)
        //         // 메인 테이블화면으로 이동
        //         props.history.push('/tableMap')
        //     } else {
        //         alert(data.reason)
        //     }

        // }, {
        //     id: id,
        //     password: password
        // })
    }

    // ------------------------------------------------------
    // utils

    /**
     * 패스워드 암호화
     *
     * @param {string} password
     */
    const encryptPassword = (password) => {
        crypto.randomBytes(64, (err, buffer) => {                                                       // salt 생성
            console.log('salt', buffer.toString('base64'))
            crypto.pbkdf2(password, buffer.toString('base64'), 103872, 64, 'sha512', (err, key) => {    // hash 생성
                console.log('hash', key.toString('base64'))
            })
        })
    }

    // ------------------------------------------------------

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
                <Button clickHandler={loginClickHandler}>Sign in</Button>
                <Button>Cancel</Button>
            </div>
        </section>
    ))
}

export default withRouter(Login)