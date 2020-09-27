import React, { useEffect } from 'react'

import Item from './Item/Item'

import './Table.css'

const Table = (props) => {

    // TODO 테이블 번호는 아코디언 메뉴식으로 구현하면 어떨까

    /**
     * 테이블 정보 생성
     */
    const renderTables = () => {
        return (
            <div className='Table'>
                {/* <h2 className='Table__title'>{ props.tableTitle }</h2> */}
                {/* <Item /> */}

                <input type="checkbox" id={props.tableTitle}/>
                <label htmlFor={props.tableTitle} className={props.isLast ? 'last' : ''}>
                    { props.tableTitle }
                </label>
                <ul>
                    <Item />
                    <Item />
                    <Item />
                </ul>
            </div>
        )
    }


    return (
        <React.Fragment>
            { renderTables() }
        </React.Fragment>
    )
}

export default Table