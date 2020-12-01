import React, { useState } from 'react';

import ValidatePassword from './ValidatePassword/ValidatePassword'

import './UpdateUserData.css'
import useStore from '../../mobx/useStore';
import RoundButton from '../../components/Button/RoundButton/RoundButton';

const UpdateUserData = () => {

    const { loginUser } = useStore()
    const { id, email } = loginUser.loginUser

    const [validateStatus, setValidateStatus] = useState(false)

    const [password, setPassword] = useState('')
    const [rePassword, setRePassword] = useState('')

    // -------------------------------------------------
    // Handlers

    const onChangeHandler = (value, setter) => {
        setter(value)
    }

    // -------------------------------------------------

    const mainLayout = () => (
        <section className='userData__layout'>
            {/* userId 변경불가 */}
            <div className='label__input'>
                <label htmlFor='id'>ID</label>
                <input id='id' type='text' value={ id } readOnly />
            </div>
            {/* 패스워드 변경가능 */}
            <div className='label__input'>
                <label htmlFor='password'>Password</label>
                <input id='password' type='password' className='enable__update' value={password} onChange={(event) => onChangeHandler(event.target.value, setPassword)} />
            </div>
            {/* 패스워드 재확인 */}
            <div className='label__input'>
                <label htmlFor='re_password'>Password double check</label>
                <input id='re_password' type='password' className='enable__update' value={rePassword} onChange={(event) => onChangeHandler(event.target.value, setRePassword)} />
            </div>
            {/* email 변경불가 */}
            <div className='label__input'>
                <label htmlFor='email'>E-Mail</label>
                <input id='email' type='text' value={ email } readOnly />
            </div>
            {/* 수정 버튼 */}
            <RoundButton>OK</RoundButton>
            {/* 회원탈퇴 버튼 */}
        </section>
    )

    return (
        <section className='UpdateUserData'>
            {/* { !validateStatus && <ValidatePassword */}
                {/* setValidateStatus={setValidateStatus} /> } */}

            {/* { validateStatus && mainLayout() } */}
                {mainLayout()}

        </section>
    )
}

export default UpdateUserData