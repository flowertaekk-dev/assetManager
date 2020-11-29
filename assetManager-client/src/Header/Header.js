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
    }, [currentPage, loginUser.loginUser.id])

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

                if (!loginUser.loginUser.id) {
                    menus.push(<Link to="/login">로그인</Link>)
                }

                break;
            case "/login":
                menus.push(<Link to="/signup">회원가입</Link>)
                break;
            case "/tableMap":
                menus.push(<Link to="/setting">설정</Link>)
                break;

            case "/setting":
                break;

            default:
                // ..?
                break;
        }

        if (loginUser.loginUser.id) {
            if (path !== '/updateUserData')
                menus.push(<Link to="/updateUserData">회원정보수정</Link>)
            menus.push(<span onClick={ logoutHandler }>로그아웃</span>)
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