import React, { useState } from 'react'
import { querySalt } from '../../../utils/userUtils/userUtilities'
import useStore from '../../../mobx/useStore'

import './ValidatePassword.css'

const ValidatePassword = () => {

    const { loginUser } = useStore()

    const [ password, setPassowrd ] = useState('')

    // -----------------------------------------------------
    // API request

    const okButtonClieckedHandler = async () => {
        const _salt = await querySalt(loginUser.loginUserId)
        console.log(_salt)
        // TODO code from here
        // 로그인 로직을 이용해서 패스워드 확인해도 될듯
            // 그러면 로그인 쿼리를 공통화해도 되겠네? -> utils/userUtilities.js
        // 화이팅해보자!!!
    }

    // -----------------------------------------------------
    // utils

    const querySaltKey = () => {

    }

    // -----------------------------------------------------
    // Handlers

    const onChangeHandler = (event) => {
        setPassowrd(event.target.value)
    }

    return (
        <section className='ValidatePassword'>
            <div className='input__group'>
                <label htmlFor="validatePw" className='validatePw__label'>비밀번호</label>
                <input type="password"
                    id="validatePw" name="validatePw" placeholder="PASSWORD"
                    className='validatePw__input'
                    value={password}
                    onChange={(event) => onChangeHandler(event)}/>
            </div>
            <button onClick={okButtonClieckedHandler}>OK</button>
        </section>
    )
}

export default ValidatePassword