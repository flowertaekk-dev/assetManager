import React, { useState } from 'react'
import { withRouter } from 'react-router-dom'
import crypto from 'crypto'

import Button from '../../components/Button/Button'
import customAxios from '../../customAxios'

import './Signup.css'

const Signup = (props) => {

    const [id, setId] = useState('')
    const [idStatus, setIdStatus] = useState('')

    const [password, setPassword] = useState('')
    const [passwordStatus, setPasswordStatus] = useState('')

    const [email, setEmail] = useState('')
    const [emailStatus, setEmailStatus] = useState('')

    const [doubleCheckPassword, setDoubldCheckEmail] = useState('')
    const [doubleCheckPasswordStatus, setDoubleCheckPasswordStatus] = useState('')

    const [emailAuthCode, setEmailAuthCode] = useState('')

    // ---------------------------------------------------------
    // Validators

    const VALIDATE_OK = 'validate__ok'
    const VALIDATE_NOT_OK = 'validate__not__ok'

    const validateId = (id) => {
        if ( !id.length )
            return ''

        if ( id.length < 6 )
            return VALIDATE_NOT_OK

        return VALIDATE_OK
    }

    const validatePassword = (password) => {
        if ( !password.length )
            return ''

        if ( password.length < 8 )
            return VALIDATE_NOT_OK

        return VALIDATE_OK
    }

    const validateEmail = (email) => {
        if ( !email.length )
            return ''

        const emailRegex = /^(([^<>()\[\].,;:\s@"]+(\.[^<>()\[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;

        if ( !emailRegex.test(email) )
            return VALIDATE_NOT_OK

        return VALIDATE_OK
    }

    const validateDoubleCheckPassword = (doubleCheckPassword) => {
        if ( !doubleCheckPassword.length )
            return ''

        if ( password !== doubleCheckPassword)
            return VALIDATE_NOT_OK

        return VALIDATE_OK
    }

    // ---------------------------------------------------------
    // Handlers

    /**
     * Input 변경 핸들러
     */
    const onChangeHandler = (event) => {
        let valueId = event.target.id
        let value = event.target.value

        switch (valueId) {
            case 'signupId':
                setIdStatus(validateId(value))
                setId(value)
                break;
            case 'signupPw':
                setPasswordStatus(validatePassword(value))
                setPassword(value)
                break;
            case 'signupEmail':
                setEmailStatus(validateEmail(value))
                setEmail(value)
                break;
            case 'doubleCheckPassword':
                setDoubleCheckPasswordStatus(validateDoubleCheckPassword(value))
                setDoubldCheckEmail(value)
                break;
            case 'signupEmailAuth':
                setEmailAuthCode(value)
                break;
            default:
                // it should be an error..
                break;
        }
    }

    /**
     * OK 버튼 클릭 핸들러; 회원가입
     */
    const okButtonClickHandler = async () => {
        const [_salt, _password] = await encryptPassword(password)
        console.log('salt', _salt)
        console.log('password', _password)
        let result = [idStatus, passwordStatus, emailStatus, doubleCheckPasswordStatus]
            .filter(status => status !== VALIDATE_OK)

        // send request
        if (!result.length) {
            customAxios('/signup', (data) => {

                if (data.resultStatus === 'SUCCESS') {
                    props.history.goBack()
                    // props.history.replace('/login')
                } else {
                    alert(data.reason)
                }

            }, {
                id,
                salt: _salt,
                password: _password,
                email,
                emailAuthCode
            })
        } else {
            alert('틀린 항목이 없는지 확인해주세요!')
        }
    }

    const authEmailClickHandler = () => {
        if ( emailStatus !== VALIDATE_OK ) {
            alert('먼저 E-mail을 입력해주세요!')
            return
        }

        customAxios('/email/requestCode', (data) => {
            // console.log(data)
            // console.log(data.resultStatus)
            // do nothing
        }, {
            addressTo: email
        })
    }

    // ---------------------------------------------------------
    // Handlers

    /**
     * 패스워드 암호화
     *
     * @param {string} password
     */
    const encryptPassword = (password) => {
        let _salt, _password;

        return new Promise((resolve, reject) => {
            crypto.randomBytes(64, (err, buffer) => {                                  // salt 생성
                if (err) reject()

                _salt = buffer.toString('base64')
                crypto.pbkdf2(password, _salt, 103872, 64, 'sha512', (err, key) => {   // hash 생성
                    _password = key.toString('base64')
                    resolve([_salt, _password])
                })
            })
        })
    }

    // ---------------------------------------------------------

    return (
        <section className="Signup">
            <div className="Signup__field">
                <div className="Signup__item">
                    <label htmlFor="signupId">ID</label>
                    <input type="text"
                        id="signupId" name="signupId" placeholder="ID"
                        className={idStatus}
                        value={id}
                        onChange={(event) => onChangeHandler(event)}/>
                </div>

                <div className="Signup__item">
                    <label htmlFor="signupPw">PASSWORD</label>
                    <input type="password"
                        id="signupPw" name="signupPw" placeholder="PASSWORD"
                        className={passwordStatus}
                        value={password}
                        onChange={(event) => onChangeHandler(event)}/>
                </div>

                <div className="Signup__item">
                    <label htmlFor="doubleCheckPassword">E-MAIL</label>
                    <input type="password"
                         id="doubleCheckPassword" name="doubleCheckPassword" placeholder="Password 확인"
                         className={doubleCheckPasswordStatus}
                         value={doubleCheckPassword}
                         onChange={(event) => onChangeHandler(event)}/>
                </div>

                <div className="Signup__item">
                    <label htmlFor="signupEmail">E-MAIL</label>
                    <input type="email"
                         id="signupEmail" name="signupEmail" placeholder="E-MAIL"
                         className={emailStatus}
                         value={email}
                         onChange={(event) => onChangeHandler(event)}/>
                </div>

                <div className="Signup__item">
                    <label htmlFor="signupEmailAuth">Email 인증코드</label>
                    <div className="auth__container">
                        <input type="text" id="signupEmailAuth" name="signupEmailAuth" placeholder="이메일 인증코드"
                            value={emailAuthCode}
                            onChange={(event) => onChangeHandler(event)}/>
                        <p className="auth__request__button" onClick={authEmailClickHandler}>인증코드 요청</p>
                    </div>

                </div>
            </div>

            <div className="btn__container">
                <Button clickHandler={okButtonClickHandler}>OK</Button>
                <Button>Cancel</Button>
            </div>
        </section>
    )
}

export default withRouter(Signup)