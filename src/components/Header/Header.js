import React, { useEffect, useState } from 'react'

import './Header.css'

const Header = props => {

    return (
        <header className="Header">
            <p className="Header__login__text">Log in</p>
            <p className="Header__login__text">Sign in</p>
            <p className="Header__login__text">Sign out</p>
        </header>
    )
}

export default Header