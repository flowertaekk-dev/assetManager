import React, { useEffect, useState, useMemo } from 'react'

import useStore from '../../mobx/useStore'
import CustomModal from '../../components/Modal/CustomModal'
import { getAccountBook, setAccountBook } from '../../utils/localStorageManager'
import './Item.css'

const Item = (props) => {

    const { selectedBusiness } = useStore()

    const [count, setCount] = useState(props.invoice.count)

    useEffect(() => {
        setCount(props.invoice.count)
    }, [ props.invoice.count ])

    // -------------------------------------------------------------------
    // Handlers

    /**
     * 주문 수량을 수정한다
     *
     * @param {function} callback
     */
    const okModalHandler = (callback) => {
        let accountBook = getAccountBook()
        let targetAccountBook = accountBook
            [selectedBusiness.selectedBusinessId]
            [props.tableId]

        // localStorage 변경
        targetAccountBook[props.invoice.menuId].count = count
        targetAccountBook[props.invoice.menuId].totalPrice = count * props.invoice.price

        // state 변경 (TODO: 좋은 방법이 아닌데... 다른 방법을 찾아보자)
        props.invoice.count = count
        props.invoice.totalPrice = count * props.invoice.price

        props.updateInvoice(prev => { return { ...prev, [props.invoice.menuId]: props.invoice } })
        setAccountBook(accountBook)

        callback() // 모달 닫기
    }

    /**
     * 모달을 닫는다
     *
     * @param {function} callback
     */
    const cancelModalHandler = (callback) => {
        callback() // 모달 닫기
    }

    // -------------------------------------------------------------------

    return (
        <li className='Table__Item'>
            <CustomModal
                modalTitle={`주문 수량 수정`}
                toggleButton={
                    (
                        <div className='Table__Item__bills'>
                            <span className='Table__Item__name'>{props.invoice.menu}</span>
                            <span className='Table__Item__count'>{`${props.invoice.count} 개`}</span>
                            <span className='Table__Item__totalPrice'>{`${props.invoice.totalPrice} 원`}</span>
                        </div>
                    )
                }
                okBtnTitle={ '수정' }
                // preCheckHandler={ () => console.log('preCheck') }
                okButtonClickedHandler={ okModalHandler }
                cancelButtonClickedHandler={ cancelModalHandler } >
                    {/* content */}
                    <label
                        htmlFor="menuName"
                        style={{
                            "fontSize": "1.4em"
                        }}>
                            { props.invoice.menu }
                    </label>
                    <input
                        id="menuName"
                        type="number"
                        className="modal__tableCount"
                        style={{
                            "marginLeft": "12px",
                            "width": "120px",
                            "height": "4vh",
                            "fontSize": "1.2em",
                            "float": "right"
                        }}
                        value={ count }
                        onChange={(event) => { setCount(event.target.value) }}
                        min='0'
                        placeholder="주문 수량" />
            </CustomModal>
        </li>
    )
}

export default Item
