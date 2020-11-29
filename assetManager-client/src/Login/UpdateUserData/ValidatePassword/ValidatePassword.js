import React, { useState } from 'react'

import { logIn } from '../../../utils/userUtils/userUtilities'
import useStore from '../../../mobx/useStore'
import RoundButton from '../../../components/Button/RoundButton/RoundButton'

import './ValidatePassword.css'
import LabelInput from '../../../components/LabelInput/LabelInput'

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
        <section className='ValidatePassword'>
            <LabelInput
                    componentId='validatePw'
                    inputType='password'
                    labelTitle='Password'
                    placeholder='Password'
                    _className={'validatePw__input'}
                    _value={password}
                    onChangeHandler={(event) => onChangeHandler(event)} />
            <RoundButton clickHandler={okButtonClieckedHandler}>OK</RoundButton>
        </section>
    )
}

export default ValidatePassword