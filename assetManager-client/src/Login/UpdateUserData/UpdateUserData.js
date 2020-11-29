import React, { useState } from 'react';

import ValidatePassword from './ValidatePassword/ValidatePassword'

import './UpdateUserData.css'

const UpdateUserData = () => {

    const [validateStatus, setValidateStatus] = useState(false)

    const mainLayout = () => (
        <section className='userData__layout'>
            <h2>input</h2>
            {/* userId 변경불가 */}
            <label htmlFor='id'>ID</label>
            <input id='id' type='text' />
            {/* 패스워드 변경가능 */}
            {/* email 변경불가 */}
            {/* 회원탈퇴 버튼 */}
        </section>
    )

    return (
        <section className='UpdateUserData'>
            { !validateStatus && <ValidatePassword
                setValidateStatus={setValidateStatus} /> }

            { validateStatus && mainLayout() }

        </section>
    )
}

export default UpdateUserData