import React, { useState } from 'react'
import { withRouter } from 'react-router-dom'
import { useObserver } from 'mobx-react'

import Button from '../components/Button/Button'
import useStore from '../mobx/useStore'
import { logIn } from '../utils/userUtils/userUtilities'

import './Login.css'

const Login = (props) => {

    const { loginUser } = useStore()

    const [id, setId] = useState('')
    const [password, setPassword] = useState('')

    // ------------------------------------------------------
    // Handlers

    const loginClickHandler = () => {
        logIn(id, password, (user) => {
            // store에 저장
            // session에 저장
            if (loginUser){
                loginUser.updateLoginUser(user)
            }
            // 메인 테이블화면으로 이동
            props.history.push('/tableMap')
        })
    }

    // ------------------------------------------------------

    return useObserver(() => (
        // TODO LabelInput으로 공통화!
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
                <Button clickHandler={ loginClickHandler }>Sign in</Button>
                <Button>Cancel</Button>
            </div>
        </section>
    ))
}

export default withRouter(Login)