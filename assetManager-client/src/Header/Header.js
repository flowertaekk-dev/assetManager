import React, { useState, useEffect } from 'react'
import { Link, withRouter, useLocation } from 'react-router-dom'
import { useObserver } from 'mobx-react'

import useStore from '../mobx/useStore'

import './Header.css'

const Header = (props) => {

    const { loginUser } = useStore()
    let location = useLocation()

    const [currentPage, setCurrentPage] = useState('')
    const [menus, setMenus] = useState([<Link to="/login">Log in</Link>])
    const [title, setTitle] = useState('')

    props.history.listen((location, action) => {
        setCurrentPage(location.pathname)
    })

    useEffect(() => {
        // refresh 에서 생각대로 안 움직여서 우선은 DRY 위반.. 사실은 currentPage를 쓰고 싶었다.
        setMenus(shownMenu(location.pathname))
        setTitle(showTitle(location.pathname))
    }, [currentPage, loginUser.loginUser])

    // ---------------------------------------------------------
    // utils

    /**
     * Header의 메뉴 설정
     */
    const shownMenu = (path) => {
        const menus = [];

        switch (path) {
            case "":
            case "/":

                if (!loginUser.loginUser) {
                    menus.push(<Link to="/login">Log in</Link>)
                }

                break;
            case "/login":
                menus.push(<Link to="/signup">Sign up</Link>)
                break;
            case "/tableMap":
                menus.push(<Link to="/setting">Setting</Link>)
                break;

            case "/setting":
                break;

            default:
                // ..?
                break;
        }

        if (loginUser.loginUser) {
            if (path !== '/updateUserData')
                menus.push(<Link to="/updateUserData">Edit Account</Link>)
            menus.push(<span onClick={ logoutHandler }>Log out</span>)
        }

        return menus
    }

    // ---------------------------------------------------------
    // Handlers

    /**
     * 로그아웃 처리는 여기서! 딱히 로그아웃 로직만 따로 빼는 것도 낭비같다는 생각.
     */
    const logoutHandler = () => {
        // loginUser 세션에서 삭제
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

         if (path || path !== '/') {
            title = <Link to="/">AssetManager</Link>
        }

        return title;
    }

    return useObserver(() => (
        <header className="Header">
            <div className="Header__home">
                {title}
            </div>

            <ul className="Header__list">
                { menus.map((menu, i) => (<li key={i}>{menu}</li>)) }
            </ul>
        </header>
    ))
}

export default withRouter(Header)