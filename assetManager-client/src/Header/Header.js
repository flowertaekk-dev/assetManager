import React, { useState, useEffect } from 'react'
import { Link, withRouter } from 'react-router-dom'

import './Header.css'

const Header = props => {

    const [currentPage, setCurrentPage] = useState('')
    const [menus, setMenus] = useState(<Link to="/login">Log in</Link>)

    props.history.listen((location, action) => {
        setCurrentPage(props.history.location.pathname)
    })

    useEffect(() => {
        setMenus(shownMenu(currentPage))
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

    return (
        <header className="Header">
            <div className="Header__home">
                <Link to="/">AssetManager</Link>
            </div>
            
            <p className="Header__login__text">
                {menus}
            </p>
        </header>
    )
}

export default withRouter(Header)