import React, { useState } from 'react'

import Button from '../../components/Button/Button'
import customAxios from '../../customAxios'

import './Signup.css'

const Signup = () => {

    const [id, setId] = useState('')
    const [idStatus, setIdStatus] = useState('')

    const [password, setPassword] = useState('')
    const [passwordStatus, setPasswordStatus] = useState('')

    const [email, setEmail] = useState('')
    const [emailStatus, setEmailStatus] = useState('')

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
 
    // ---------------------------------------------------------
    // Handlers

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
            default:
                // it should be an error..
                break;
        }
    }

    const okButtonClickHandler = () => {
        let result = [idStatus, passwordStatus, emailStatus]
            .filter(status => status === VALIDATE_OK)

        // send request
        if (result.length === 3) {
            customAxios('/signup', (data) => {
                // TODO 성공하면 로그인 페이지로?
                // TODO 실패하면 실패메세지 띄우기
                console.log(data)
                console.log(data.resultStatus)
            }, {
                id,
                password,
                email
            })
        }

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
                    <label htmlFor="signupEmail">E-MAIL</label>
                    <input type="email"
                         id="signupEmail" name="signupEmail" placeholder="E-MAIL"
                         className={emailStatus}
                         value={email}
                         onChange={(event) => onChangeHandler(event)}/>
                </div>
            </div>

            <div>
                <Button name="OK" clickHandler={okButtonClickHandler}/>
                <Button name="Cancel" />
            </div>
        </section>
    )
}

export default Signup