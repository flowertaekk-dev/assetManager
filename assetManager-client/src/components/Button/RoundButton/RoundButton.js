import React from 'react'

import './RoundButton.css'

const RoundButton = (props) => {
    return (
        <button
            className="RoundButton"
            onClick={props.clickHandler}>
                {props.children}
        </button>
    )
}

export default RoundButton