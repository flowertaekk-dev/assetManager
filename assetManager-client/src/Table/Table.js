import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import Item from './Item/Item'
import Menu from './Menu/Menu'
import RoundButton from '../components/Button/RoundButton/RoundButton'
import CustomModal from '../components/Modal/CustomModal'
import useStore from '../mobx/useStore'
import { getAccountBook,  updateSelectedBusienssAccountBook } from '../utils/localStorageManager'

import './Table.css'
import customAxios from '../customAxios'

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
        const accountBook = getAccountBook()
            [selectedBusiness.selectedBusinessId]
            [props.tableId]

        // 새로운 메뉴가 있으면 추가한다. (localStorage에)
        const _menuList = initMenuList(_menus)

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

    /**
     * Array 상태의 _menus 를 Object로 변환하고,
     * property에 'totalPrice', 'count'를 추가해서 반환
     *
     * @param {Array} _menus
     */
    const initMenuList = (_menus) => {
        return _.reduce(_menus, (result, _menu) => {
            // property 추가
            _menu['totalPrice'] = 0
            _menu['count'] = 0

            result[_menu.menuId] = _menu
            return result
        }, {})
    }

    /**
     * Edit용 인보이스를 초기화
     */
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
            // 이래서 typescript 쓰나??
            let count = parseInt(finalInvoice[key].count)
            let price = parseInt(finalInvoice[key].price)
            let totalCount = count + parseInt(value)

            finalInvoice[key].count = totalCount
            finalInvoice[key].totalPrice = ( totalCount * price )
        })

        // localStorage에도 저장
        updateSelectedBusienssAccountBook(selectedBusiness.selectedBusinessId, props.tableId, finalInvoice)

        initUpdatingInvoice()
        callback()
    }

    /**
     * 결제를 진행한다
     *
     * @param {function} callback
     */
    const okCalCulateModalHandler = (callback) => {
        // DB에 저장
        customAxios("/account/save", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                callback()
            } else {
                alert(response.reason)
            }
        }, {
            businessId: selectedBusiness.selectedBusinessId,
            contents: JSON.stringify(finalInvoice)
        })

        // finalInvoice 초기화
        setFinalInvoice(initMenuList(menus))

        // 해당 localStorage 초기화
        updateSelectedBusienssAccountBook(selectedBusiness.selectedBusinessId, props.tableId, finalInvoice)

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
    // utils

    /**
     * 총 합계 금액을 반환한다
     */
    const calculateSum = () => {
        return _.reduce(
            _.filter(finalInvoice, invoice => {
                return invoice.count !== 0
            }), (result, value, key) => {
                return result += value.totalPrice
            }, 0)
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
                                    <RoundButton>Edit</RoundButton>
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
                                <RoundButton>Calculate</RoundButton>
                            )
                        }
                        okBtnTitle={ '결제' }
                        // preCheckHandler={ () => console.log('preCheck') }
                        okButtonClickedHandler={ okCalCulateModalHandler }
                        cancelButtonClickedHandler={ cancelModalHandler } >
                            {/* content */}
                            <ul>
                                { renderCurrentMenu() }
                                <li className='Table__sum'>합계: { calculateSum() } 원</li>
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
        return _.map(
            _.filter(finalInvoice, invoice => {
                return invoice.count !== 0
            }), invoice => {
                return (
                    <Item
                        key={ invoice.menuId }
                        tableId={ props.tableId }
                        invoice={ invoice }
                        updateInvoice={ setFinalInvoice }  />
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