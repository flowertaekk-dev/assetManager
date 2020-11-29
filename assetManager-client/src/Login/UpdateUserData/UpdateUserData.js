import React, { useState } from 'react';

import ValidatePassword from './ValidatePassword/ValidatePassword'

import './UpdateUserData.css'

const UpdateUserData = () => {

    const [validateStatus, setValidateStatus] = useState(false)

    const [id] = useState('')
    const [password, setPassword] = useState('')
    const [rePassword, setRePassword] = useState('')
    const [email] = useState('')

    const mainLayout = () => (
        <section className='userData__layout'>
            {/* userId 변경불가 */}
            <div className='label__input'>
                <label htmlFor='id'>ID</label>
                <input id='id' type='text' value={id} readOnly />
            </div>
            {/* 패스워드 변경가능 */}
            <div className='label__input'>
                <label htmlFor='password'>Password</label>
                <input id='password' type='password' value={password} />
            </div>
            {/* 패스워드 재확인 */}
            <div className='label__input'>
                <label htmlFor='re_password'>Password double check</label>
                <input id='re_password' type='password'value={rePassword} />
            </div>
            {/* email 변경불가 */}
            <div className='label__input'>
                <label htmlFor='email'>E-Mail</label>
                <input id='email' type='text' value={email} readOnly />
            </div>
            {/* 수정 버튼 */}
            {/* 회원탈퇴 버튼 */}
        </section>
    )

    return (
        <section className='UpdateUserData'>
            { !validateStatus && <ValidatePassword
                setValidateStatus={setValidateStatus} /> }

            { validateStatus && mainLayout() }
                {/* {mainLayout()} */}

        </section>
    )
}

export default UpdateUserData