import React from 'react'

import './Item.css'

const Item = (props) => {

    return (
        <li className='Table__Item'>
            <span className='Table__Item__name'>{props.menu}</span>
            <div className='Table__Item__bills'>
                <span className='Table__Item__count'>{`${props.count} 개`}</span>
                <span className='Table__Item__totalPrice'>{`${props.totalPrice} 원`}</span>
            </div>
        </li>
    )
}

export default Item
