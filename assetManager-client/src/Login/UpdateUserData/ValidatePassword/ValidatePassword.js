import React, { useState } from 'react'

import { logIn } from '../../../utils/userUtils/userUtilities'
import useStore from '../../../mobx/useStore'
import RoundButton from '../../../components/Button/RoundButton/RoundButton'

import './ValidatePassword.css'

const ValidatePassword = (props) => {

    const { loginUser } = useStore()

    const [ password, setPassowrd ] = useState('')


    // -----------------------------------------------------
    // Handlers

    const okButtonClieckedHandler = () => {
        logIn(loginUser.loginUserId, password, () => {
            props.setValidateStatus(true)
        })
    }

    const onChangeHandler = (event) => {
        setPassowrd(event.target.value)
    }

    return (
        // refactoring: 참고: https://github.com/flowertaekk-dev/assetManager/issues/20
        <section className='ValidatePassword'>
            <div className='input__group'>
                <label htmlFor="validatePw" className='validatePw__label'>비밀번호</label>
                <input type="password"
                    id="validatePw" name="validatePw" placeholder="PASSWORD"
                    className='validatePw__input'
                    value={password}
                    onChange={(event) => onChangeHandler(event)}/>
            </div>
            <RoundButton clickHandler={okButtonClieckedHandler}>OK</RoundButton>
        </section>
    )
}

export default ValidatePassword