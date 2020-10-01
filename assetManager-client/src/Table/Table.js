import React, { useEffect } from 'react'

import Item from './Item/Item'
import CustomModal from '../components/Modal/CustomModal'
import useStore from '../mobx/useStore'
import KEYS from '../utils/LocalStorageKeys'
import { customAxiosWithResponse } from '../customAxios'

import './Table.css'

const Table = (props) => {

    const { loginUser, selectedBusiness } = useStore()

    /**
     * 장부(accountBook)이 로컬에 등록되어 있지 않으면 등록한다
     */
    // useEffect(() => {
    //     //TODO 여기 테이블이 하나씩 밖에 등록이 안돼!!!
    //     let accountBook = window.localStorage.getItem(KEYS.ACCOUNT_BOOK)
    //     // accountBook이 없다
    //     if ( !accountBook ) {

    //         const response = customAxiosWithResponse("/menu/readAll", {
    //             userId: loginUser.loginUserId,
    //             businessId: selectedBusiness.selectedBusinessId
    //         })

    //         let accountBook = {};
    //         response.then(res => {
    //             const menus = res.data.menus

    //             console.log('accountBook', accountBook)
    //             accountBook = {...accountBook,
    //                 [selectedBusiness.selectedBusinessId]: {
    //                     [props.tableTitle]: menus.map(menu => {
    //                         {
    //                             return {
    //                                 [menu.menu]: {
    //                                 'id': menu.menuId,
    //                                 'count': 0,
    //                                 'price': menu.price
    //                                 }
    //                             }
    //                         }
    //                     })
    //                 }
    //             }

    //             window.localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))
    //         })
    //     } else {
    //         //console.log('what', accountBook)    
    //     }
    //     window.localStorage.removeItem(KEYS.ACCOUNT_BOOK)
    // }, [selectedBusiness.selectedBusinessId])

    // useEffect(() => {
    //     let accountBook = window.localStorage.getItem(KEYS.ACCOUNT_BOOK)
    //     console.log('aa', accountBook)
    // }, [ window.localStorage.getItem(KEYS.ACCOUNT_BOOK) ])

    /**
     * 모달을 닫는다
     * 
     * @param {function} callback 
     */
    const cancelModalHandler = (callback) => {
        callback()              // 모달 닫기
    }

    /**
     * 테이블 정보 생성
     */
    const renderTables = () => {
        return (
            <div className='Table'>
                <input type="checkbox" id={ props.tableTitle } />
                <div className={ `Table__header ${props.isLast ? 'last' : ''}` }>
                    <label
                        htmlFor={ props.tableTitle } 
                        className='Table__header__title' >
                            { props.tableTitle }
                    </label>

                    {/* TODO */}
                    <CustomModal
                            modalTitle={`주문목록`}
                            toggleButton={
                                (
                                    <button>Edit</button>
                                )
                            }
                            preCheckHandler={ () => console.log('preCheck') }
                            okButtonClickedHandler={ () => console.log('okButtonClicked') }
                            cancelButtonClickedHandler={ cancelModalHandler } >
                                {/* content */}
                        </CustomModal>

                    {/* /TODO */}
                </div>
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