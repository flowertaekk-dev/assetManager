import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import Item from './Item/Item'
import CustomModal from '../components/Modal/CustomModal'
import Menu from './Menu/Menu'
import useStore from '../mobx/useStore'
import KEYS from '../utils/LocalStorageKeys'

import './Table.css'

const Table = (props) => {

    const { selectedBusiness } = useStore()

    const [ menus, setMenus ] = useState([])
    const [ updatingInvoice, setUpdatingInvoice ] = useState({})
    const [ finalInvoice, setFinalInvoice ] = useState({})

    useEffect(() => {
        setMenus(_.cloneDeep(props.menus))
    }, [ props.menus ])
    
    useEffect(() => {
        // 선택된 상호명이 있을경우에만 세팅
        if (selectedBusiness.selectedBusinessId) {
            initFinalInvoice(menus)
        }
    }, [ menus ])

    // -------------------------------------------------------------------
    // 초기화 관련

    /**
     * 새로운 메뉴가 있으면 추가하고, localStorage에서 기존 장부 정보를 불러온다.
     * 
     * @param {Array} _menus 메뉴리스트
     */
    const initFinalInvoice = (_menus) => {
        // localStorage에서 불러온다.
        const accountBook = JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))
            [selectedBusiness.selectedBusinessId]
            [props.tableId]

        // 새로운 메뉴가 있으면 추가한다. (localStorage에)
        const _menuList = _.reduce(_menus, (result, _menu) => {
            // property 추가
            _menu['totalPrice'] = 0
            _menu['count'] = 0

            result[_menu.menuId] = _menu
            return result
        }, {})

        // 새로운 테이블에 메뉴 등록
        _.forEach(_.filter(_menuList, (_menu) =>{
                return !accountBook[_menu.menuId]
            }), _menu => {
                return accountBook[_menu.menuId] = _menu
            }
        )

        // state에 세팅
        setFinalInvoice(accountBook)

    }

    const initUpdatingInvoice = () => {
        setUpdatingInvoice({})
    }

    // -------------------------------------------------------------------
    // Handlers

    /**
     * 변경된 메뉴를 최종 인보이스에 적용한다
     * 
     * @param {function} callback
     */
    const okEditModalHandler = (callback) => {
        // finalInvoice의 내용 업데이트는 setter없이도 가능?!(한 듯 하다.)
        _.forEach(updatingInvoice, (value, key) => {
            finalInvoice[key].count += value
            finalInvoice[key].totalPrice = ( finalInvoice[key].count * finalInvoice[key].price )
        })

        // localStorage에도 저장
        const accountBook = JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))
        const myAccountBook = accountBook[selectedBusiness.selectedBusinessId]
        myAccountBook[props.tableId] = finalInvoice

        localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))

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

        // TODO 해당 localStorage 초기화
    }

    /**
     * 모달을 닫는다
     * 
     * @param {function} callback 
     */
    const cancelModalHandler = (callback) => {
        callback()              // 모달 닫기
    }

    // -------------------------------------------------------------------
    // 랜더링

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
                                    { renderCurrentMenu() }
                                </ul>
                        </CustomModal>

                </div>
                <ul>
                    { renderCurrentMenu() }
                </ul>
            </div>
        )
    }

    /**
     * 현재 메뉴 정보를 화면에 랜더링한다 (메뉴의 카운트가 1 이상인 것만)
     */
    const renderCurrentMenu = () => {
       return  _.map(
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


    return (
        <React.Fragment>
            { renderTables() }
        </React.Fragment>
    )
}

export default Table