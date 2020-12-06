import React, { useState } from 'react';

import ValidatePassword from './ValidatePassword/ValidatePassword'

import './UpdateUserData.css'
import useStore from '../../mobx/useStore';
import RectangleButton from '../../components/Button/RectangleButton/RectangleButton';
import { deleteUser, doubleCheckPassword, updatePassword } from '../../utils/userUtils/userUtilities';
import { withRouter } from 'react-router-dom';

const UpdateUserData = (props) => {

    const { loginUser } = useStore()
    const { id, email } = loginUser.loginUser

    const [validateStatus, setValidateStatus] = useState(false)

    const [password, setPassword] = useState('')
    const [rePassword, setRePassword] = useState('')

    // -------------------------------------------------
    // Handlers

    const updatePasswordHandler = async () => {

        // password 조건 확인: 8자 이상
        if (password.length < 8) {
            alert('Password must be at least 8 characters')
            return
        }

        // password 확인
        if (!doubleCheckPassword(password, rePassword)) {
            alert('Different password is entered for double check')
            return
        }

        // TODO db 저장
        updatePassword(id, email, password, () => {
            props.history.push('/')
        })
    }

    const deleteUserHandler = () => {
        let answer = window.confirm('Are you sure to delete account?')

        if (answer) {
            deleteUser(id, () => {
                loginUser.deleteLoginUser()
                props.history.push('/')
            })
        }
    }

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
                <label htmlFor='re_password'>Confirm password</label>
                <input id='re_password' type='password' className='enable__update' value={rePassword} onChange={(event) => onChangeHandler(event.target.value, setRePassword)} />
            </div>
            {/* email 변경불가 */}
            <div className='label__input'>
                <label htmlFor='email'>E-Mail</label>
                <input id='email' type='text' value={ email } readOnly />
            </div>
            <div>
                {/* 수정 버튼 */}
                <RectangleButton colour='blue' clickHandler={ updatePasswordHandler }>Update</RectangleButton>
                {/* 회원탈퇴 버튼 */}
                <RectangleButton colour='red' clickHandler={ deleteUserHandler }>Delete Account</RectangleButton>
            </div>
        </section>
    )

    return (
        <section className='UpdateUserData'>

            { !validateStatus && <ValidatePassword setValidateStatus={ setValidateStatus } /> }

            { validateStatus && mainLayout() }

        </section>
    )
}

export default withRouter(UpdateUserData)