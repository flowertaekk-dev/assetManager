import React from 'react'

import './RectangleButton.css'

const RectangleButton = (props) => {
    return (
        <button
            className={`Rectangle__button Rectangle__button_${props.colour}`}
            onClick={ props.clickHandler } >

                { props.children }

        </button>
    )
}

export default RectangleButton