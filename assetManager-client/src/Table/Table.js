import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import Item from './Item/Item'
import CustomModal from '../components/Modal/CustomModal'
import Menu from './Menu/Menu'
import useStore from '../mobx/useStore'
import KEYS from '../utils/LocalStorageKeys'

import './Table.css'

const Table = (props) => {

    const { loginUser, selectedBusiness } = useStore()

    const [ menus, setMenus ] = useState([])
    const [ updatingInvoice, setUpdatingInvoice ] = useState({})
    const [ finalInvoice, setFinalInvoice ] = useState({})
    /**
     * 구조
     * {
     *  menuId,
     *  menu,
     *  count,
     *  totalPrice
     * }
     */

    useEffect(() => {
        setMenus(_.cloneDeep(props.menus))
    }, [ props.menus ])
    
    useEffect(() => {
        initFinalInvoice(menus)
    }, [ menus ])

    // -------------------------------------------------------------------
    // 초기화 관련

    const initFinalInvoice = (_menus) => {
        setFinalInvoice(_.reduce(_menus, (result, _menu) => {

            // 이미 등록된 메뉴는 Don't touch
            if (!result[_menu.menuId]) {
                _menu['totalPrice'] = 0
                _menu['count'] = 0

                result[_menu.menuId] = _menu
            }

            return result
        }, finalInvoice))
    }

    const initUpdatingInvoice = () => {
        setUpdatingInvoice({})
    }


    // -------------------------------------------------------------------

    /**
     * 변경된 메뉴를 최종 인보이스에 적용한다
     * 
     * @param {function} callback
     */
    const okEditModalHandler = (callback) => {
        _.forEach(updatingInvoice, (value, key) => {
            finalInvoice[key].count += value
            finalInvoice[key].totalPrice = ( finalInvoice[key].count * finalInvoice[key].price )
        })

        const accountBook = JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))
        const myAccountBook = accountBook[selectedBusiness.selectedBusinessId]
        myAccountBook[props.tableId] = finalInvoice
        console.log('okEdit', myAccountBook)
        console.log(`okEdit:businessId: ${[props.tableId]}`, myAccountBook)

        console.log('final', finalInvoice)


        localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))

        // TODO localStorage에도 저장할 필요있을까? 정전 또는 실수로 꺼버렸을 경우를 위해??
        // 그렇다면 이런 느낌?
        /**
         * {
         *      비지니스ID: {
         *          테이블번호: {내용},
         *          테이블번호: {내용},
         *          .
         *          .
         *      }
         * }
         */

        initUpdatingInvoice()
        callback()
    }

    /**
     * 결제를 진행한다
     * 
     * @param {function} callback 
     */
    const okCalCulateModalHandler = (callback) => {
        // TODO 결제 항목 DB 등록

        // TODO finalInvoice 초기화

    }

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

                    <CustomModal
                            modalTitle={`주문목록`}
                            toggleButton={
                                (
                                    <button>Edit</button>
                                )
                            }
                            // preCheckHandler={ () => console.log('preCheck') }
                            okButtonClickedHandler={ okEditModalHandler }
                            cancelButtonClickedHandler={ cancelModalHandler } >
                                {/* content */}
                                <ul>
                                    {
                                        menus.map(menu =>
                                            <li
                                                key={ menu.menuId }
                                                className='Table__modal__menu__item'>
                                                    <Menu 
                                                        menu={ menu }
                                                        invoice={ updatingInvoice }
                                                        setInvoice={ setUpdatingInvoice } />
                                            </li>
                                        )
                                    }
                                </ul>
                        </CustomModal>

                        <CustomModal
                            modalTitle={`합계`}
                            toggleButton={
                                (
                                    <button>Calculate</button>
                                )
                            }
                            okBtnTitle={ '결제' }
                            // preCheckHandler={ () => console.log('preCheck') }
                            okButtonClickedHandler={ () => console.log('결제하기') }
                            cancelButtonClickedHandler={ cancelModalHandler } >
                                {/* content */}
                                <ul>
                                    {
                                        // 메뉴의 카운트가 1 이상인 것만 화면에 표시
                                        _.map(
                                            _.filter(finalInvoice, invoice => {
                                                return invoice.count !== 0 
                                            }), invoice => {
                                                return (
                                                    <Item
                                                        key={ invoice.menuId }
                                                        menu={ invoice.menu }
                                                        count={ invoice.count }
                                                        totalPrice={ invoice.totalPrice } />
                                                )
                                        })
                                    }
                                </ul>
                        </CustomModal>

                </div>
                <ul>
                    {
                        // 메뉴의 카운트가 1 이상인 것만 화면에 표시
                        _.map(
                            _.filter(finalInvoice, invoice => {
                                return invoice.count !== 0 
                            }), invoice => {
                                return (
                                    <Item
                                        key={ invoice.menuId }
                                        menu={ invoice.menu }
                                        count={ invoice.count }
                                        totalPrice={ invoice.totalPrice } />
                                )
                        })
                    }
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