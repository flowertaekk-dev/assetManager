import React from 'react'

import './Button.css'

const Button = (props) => {
    return (
        <button className="Button" onClick={props.clickHandler}>{props.children}</button>
    )
}

export default Button