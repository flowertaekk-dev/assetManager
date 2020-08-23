import React from 'react'
import { Link } from 'react-router-dom'

import './Header.css'

const Header = props => {

    return (
        <header className="Header">
            <p className="Header__login__text"><Link to="/login">Log in</Link></p>
        </header>
    )
}

export default Header