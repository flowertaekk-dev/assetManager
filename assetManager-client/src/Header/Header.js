import React, { useState, useEffect } from 'react'
import { Link, withRouter } from 'react-router-dom'
import { useObserver } from 'mobx-react'

import useStore from '../mobx/useStore'

import './Header.css'

const Header = (props) => {

    const { loginUser } = useStore()

    const [currentPage, setCurrentPage] = useState('')
    const [menus, setMenus] = useState(<Link to="/login">Log in</Link>)
    const [title, setTitle] = useState('')

    props.history.listen((location, action) => {
        setCurrentPage(props.history.location.pathname)
    })

    useEffect(() => {
        setMenus(shownMenu(currentPage))
        setTitle(showTitle(currentPage))
    }, [currentPage, loginUser.loginUserId])

    /**
     * Header의 메뉴 설정
     */
    const shownMenu = (path) => {
        let menu;

        switch (path) {
            case "":
            case "/":
                let loginUser = window.localStorage.getItem('loginUser')

                if (loginUser) {
                    menu = <span onClick={logoutHandler}>Log out</span>
                } else {
                    menu = <Link to="/login">Log in</Link>
                }
                
                break;
            case "/login": 
                menu = <Link to="/signup">Sign up</Link>
                break;

            default:
                // ..?
                break;
        }

        return menu
    }

    /**
     * 로그아웃 처리는 여기서! 딱히 로그아웃 로직만 따로 빼는 것도 낭비같다는 생각.
     */
    const logoutHandler = () => {
        // loginUser 세션에서 삭제
        window.localStorage.removeItem('loginUser')
        // mobx store에서도 삭제
        loginUser.deleteLoginUser()

        // 메인페이지로 이동
        props.history.replace('/')
    }

    /**
     * index 페이지에서는 title을 보여주지 않는다 
     */
    const showTitle = (path) => {
        let title = '';

        if (path && path !== '/') {
            title = <Link to="/">AssetManager</Link>
        }

        return title;
    }

    return useObserver(() => (
        <header className="Header">
            <div className="Header__home">
                {title}
            </div>

            <p>{window.localStorage.getItem('loginUser')}</p>
            
            <p className="Header__login__text">
                {menus}
            </p>
        </header>
    ))
}

export default withRouter(Header)