import React from 'react'

import './LabelInput.css'

/**
 * props._className 에
 * validate_ok 가 오면 초록색,
 * validate_not_ok 가 오면 빨간색으로 테두리를 생성한다.
 *
 * @param {*} props
 */
const LabelInput = (props) => {
    return (
        <div className="LabelInput">
            <label htmlFor={props.componentId}>{props.labelTitle}</label>
            <input type={props.inputType}
                id={props.componentId} name={props.componentId} placeholder={props.placeholder}
                className={`label__input ${props._className}`}
                value={props._value}
                onChange={props.onChangeHandler}/>
        </div>
    )
}

export default LabelInput