import React, { useState, useEffect } from 'react'
import { Link, withRouter } from 'react-router-dom'

import './Header.css'

const Header = props => {

    const [currentPage, setCurrentPage] = useState('')
    const [menus, setMenus] = useState(<Link to="/login">Log in</Link>)
    const [title, setTitle] = useState('')

    props.history.listen((location, action) => {
        setCurrentPage(props.history.location.pathname)
    })

    useEffect(() => {
        setMenus(shownMenu(currentPage))
        setTitle(showTitle(currentPage))
    }, [currentPage])

    const shownMenu = (path) => {
        let menu;

        switch (path) {
            case "/login": 
                menu = <Link to="/signup">Sign up</Link>
                break;

            default:
                menu = <Link to="/login">Log in</Link>
                break;
        }

        return menu
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

    return (
        <header className="Header">
            <div className="Header__home">
                {title}
            </div>
            
            <p className="Header__login__text">
                {menus}
            </p>
        </header>
    )
}

export default withRouter(Header)