import React from 'react'

const Item = (props) => {
    return (
        <div className="Signup__item">
            <label htmlFor={props.componentId}>{props.labelTitle}</label>
            <input type={props.inputType}
                id={props.componentId} name={props.componentId} placeholder={props.placeholder}
                className={props.className}
                value={props.val}
                onChange={props.onChangeHandler}/>
        </div>
    )
}

export default Item