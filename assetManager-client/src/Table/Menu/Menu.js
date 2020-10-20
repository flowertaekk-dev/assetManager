import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import './Menu.css'

const Menu = (props) => {

    const [ count, setCount ] = useState(0)

    /**
     * count에 변경이 있을 때 마다 인보이스 갱신
     */
    useEffect(() => {
        props.setInvoice({...props.invoice, [props.menu.menuId]: count})
    }, [ count ])

    const incrementCount = () => {
        setCount(count+1)
    }

    const decrementCount = () => {
        if (count === 0) {
            return
        }

        setCount(count-1)
    }

    return (
        <div className='Table__Menu'>
            <p className='Table__Menu__title'>{props.menu.menu}</p>

            <div className='up__down__btn__container'>
                <p className='up__down__count'>{ count }</p>
                
                {/* TODO 좀 더 큰 버튼이 UX적으로 좋으려나..? */}
                <p
                    className='up__down__arrow'
                    onClick={ incrementCount } >
                        <i className="arrow up" />
                </p>
                <p
                    className='up__down__arrow'
                    onClick={ decrementCount }>
                        <i className="arrow down" />
                </p>
            </div>
        </div>
    )
}

export default Menu