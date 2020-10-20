import React from 'react'

import './Item.css'

const Item = (props) => {

    // TODO css 디자인
    // TODO 삭제 및 수정 기능 추가

    return (
    <li>{`${props.menu} : ${props.count} : ${props.totalPrice}`}</li>
    )
}

export default Item
