import React from 'react'

import './LabelInput.css'

const LabelInput = (props) => {
    return (
        <div className="LabelInput">
            <label htmlFor={props.componentId}>{props.labelTitle}</label>
            <input type={props.inputType}
                id={props.componentId} name={props.componentId} placeholder={props.placeholder}
                className={props._className}
                value={props._value}
                onChange={props.onChangeHandler}/>
        </div>
    )
}

export default LabelInput