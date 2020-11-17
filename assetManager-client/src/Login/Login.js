import React, { useState } from 'react'
import { withRouter } from 'react-router-dom'
import { useObserver } from 'mobx-react'
import crypto from 'crypto'

import Button from '../components/Button/Button'
import customAxios from '../customAxios'
import useStore from '../mobx/useStore'
import { encryptPassword, querySalt } from '../utils/userUtils/userUtilities'
import { REPEAT_COUNT, BYTE_LENGTH, ENCODING_TYPE, ENCRYPT_TYPE } from '../utils/userUtils/encryptUtils'

import './Login.css'

const Login = (props) => {

    const { loginUser } = useStore()

    const [id, setId] = useState('')
    const [password, setPassword] = useState('')

    // ------------------------------------------------------
    // Handlers

    const loginClickHandler = async () => {
        const saltKey = await querySalt(id)
        const _password = await encryptPassword(saltKey, password)
        customAxios('/login', (response) => {
            if (response.resultStatus === 'SUCCESS') {
                // store에 저장
                // session에 저장
                loginUser.updateLoginUser(id)
                // 메인 테이블화면으로 이동
                props.history.push('/tableMap')
            } else {
                alert(response.reason)
            }

        }, {
            id: id,
            password: _password
        })
    }

    // ------------------------------------------------------
    // utils

    // /**
    //  * 암호화를 위한 salt키를 요청
    //  *
    //  * @param {string} id
    //  * @returns {Promise} Promise Object
    //  */
    // const querySalt = (id) => {
    //     return new Promise((resolve, reject) => {
    //         customAxios('/requestSalt', (response) => {
    //             if (response.resultStatus === 'FAILURE') {
    //                 alert(response.reason)
    //                 reject(response.reason)
    //             }

    //             resolve(response.salt)
    //         }, {
    //             id
    //         })
    //     })
    // }

    // /**
    //  * 비밀번호 암호화
    //  *
    //  * @param {string} salt
    //  * @param {string} password
    //  * @returns {Promise} Promise Object
    //  */
    // const encryptPassword = (salt, password) => {
    //     return new Promise((resolve, reject) => {
    //         crypto.pbkdf2(password, salt, REPEAT_COUNT, BYTE_LENGTH, ENCRYPT_TYPE, (err, key) => {
    //             resolve(key.toString(ENCODING_TYPE))
    //         })
    //     })
    // }

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