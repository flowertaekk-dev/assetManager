import React, { useEffect, useState } from 'react'
import { observer } from 'mobx-react'

import useStore from '../../mobx/useStore'
import customAxios from '../../customAxios'
import CustomModal from '../../components/Modal/CustomModal'

import './SettingMenu.css'

const SettingMenu = observer(() => {
    // TODO 인터넷 접속이 안 되어 있으면 localStorage에서도 못 불러오도록 처리해야해. (healthCheck 이용하면 괜찮으려나?)

    const { loginUser, selectedBusiness } = useStore()

    const [ menus, setMenus ] = useState([]) // add & update 공유
    const [ price, setPrice ] = useState(0)  // add & update 공유
    const [ newMenu, setNewMenu ] = useState('')

    const [ addButtonHoverStatus, setAddButtonHoverStatus ] = useState(false)

    useEffect(() => {
        retrieveAllMenus()
    }, [ selectedBusiness.selectedBusinessId ])

    /**
     * 상호에 해당하는 메뉴 전부 불러온다
     */
    const retrieveAllMenus = () =>{
        customAxios("/menu/readAll", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                // 메뉴 저장
                setMenus(response.menus)
            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessId: selectedBusiness.selectedBusinessId
        })
    }

    /**
     * 메뉴 추가 쿼리
     * 
     * @param callback Modal를 닫는 콜백
     */
    const addMenuHandler = (callback) => {
        customAxios("/menu/add", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllMenus()  // refresh
                setNewMenu('')      // 초기화
                setPrice(0)         // 초기화
                callback()          // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessId: selectedBusiness.selectedBusinessId,
            menu: newMenu,
            price: price
        })
    }

    /**
     * 메뉴 및 가격 갱신 쿼리
     * 
     * @param {string} existingMenu 기존 메뉴명
     */
    const updateMenuHandler = (existingMenu) => {
        return (callback) => customAxios("/menu/update", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllMenus()  // refresh
                setNewMenu('')      // 초기화
                setPrice(0)         // 초기화
                callback()          // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessId: selectedBusiness.selectedBusinessId,
            existingMenu,
            newMenu,
            price
        })
    }

    /**
     * 메뉴 삭제 쿼리
     * 
     * @param {string} deleteMenu 삭제할 메뉴
     */
    const deleteMenuHandler = (deleteMenu) => {
        return (callback) => customAxios("/menu/delete", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllMenus()  // refresh
                callback()          // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessId: selectedBusiness.selectedBusinessId,
            menu: deleteMenu
        })
    }

    // ----------------------------------------------------------------------

    /**
     * newMenu, price state 값이 비어있는지 확인
     * 
     * @returns {Boolean} preCheck에 문제가 없으면 true, 있으면 false
     */
    const checkNewMenuAndPriceAreEmpty = () => {
        if ( newMenu === '' ) {
            alert('새로운 메뉴명을 입력해주세요!')
            return false
        }

        if ( price === '' ) {
            alert('가격을 입력해주세요!')
            return false
        }

        return true
    }

    /**
     * 기존의 값들을 state에 세팅한다
     * 
     * @param {string} menu 기존 메뉴명
     * @param {number} price 기존 가격
     */
    const setDefaultMenuAndPrice = (menu, price) => {
        setNewMenu(menu)
        setPrice(price)
    }

    /**
     * 모달이 닫힐 때 newBusiness state를 초기화
     * 
     * @param {function} callback 
     */
    const cancelModalHandler = (callback) => {
        setNewMenu('')  // 초기화
        setPrice(0)     // 초기화
        callback()      // 모달 닫기
    }

    /**
     * 메뉴 리스트를 랜더링한다
     */
    const renderMenus = () => {
        return menus.map(menuJson => (
            <li
                key={ menuJson.menuId }
                className='SettingMenu__list__item' >
                    <p>{`${menuJson.menu}  ${menuJson.price}`}</p>
                    <div className='SettingMenu__list__buttons'>
                        {/* EDIT */}
                        <CustomModal
                            modalTitle={`메뉴 수정: ${menuJson.menu}`}
                            toggleButton={
                                (
                                    <button onClick={ () => setDefaultMenuAndPrice(menuJson.menu, menuJson.price) }>Edit</button>
                                )
                            }
                            preCheckHandler={ checkNewMenuAndPriceAreEmpty }
                            okButtonClickedHandler={ updateMenuHandler(menuJson.menu) }
                            cancelButtonClickedHandler={ cancelModalHandler } >

                                <div className='modal__item'>
                                    <label
                                        htmlFor="menu"
                                        style={{
                                            "display": "inline-block",
                                            "width": "96px",
                                            "fontSize": "1.4em"
                                        }}>
                                            새 메뉴명:
                                    </label>
                                    <input
                                        id="menu"
                                        type="text"
                                        className="modal__menu"
                                        style={{
                                            "marginLeft": "12px",
                                            "width": "50vw",
                                            "height": "4vh",
                                            "fontSize": "1.2em"
                                        }}
                                        value={newMenu}
                                        onChange={(event) => {setNewMenu(event.target.value)}}
                                        placeholder="새 메뉴명"/>
                                </div>

                                <div className='modal__item'>
                                    <label
                                        htmlFor="price"
                                        style={{
                                            "display": "inline-block",
                                            "width": "96px",
                                            "fontSize": "1.4em"
                                        }}>
                                            가격:
                                    </label>
                                    <input
                                        id="price"
                                        type="number"
                                        className="modal__menu"
                                        style={{
                                            "marginLeft": "12px",
                                            "width": "50vw",
                                            "height": "4vh",
                                            "fontSize": "1.2em"
                                        }}
                                        value={price}
                                        onChange={(event) => {setPrice(event.target.value)}}
                                        placeholder="가격"/>
                                </div>
                        </CustomModal>

                        {/* DELETE */}
                        <CustomModal
                            modalTitle={`삭제할 메뉴: ${menuJson.menu}`}
                            toggleButton={
                                (
                                    <button>Delete</button>
                                )
                            }
                            okButtonClickedHandler={ deleteMenuHandler(menuJson.menu) } >
                                <p>정말 삭제할까요?</p>
                        </CustomModal>
                    </div>
            </li>
        ))
    }

    return (
        <section className='SettingMenu'>
            
            <div className='SettingMenu__header'>
                <h1>메뉴 설정</h1>
                <CustomModal
                    modalTitle='메뉴 추가'
                    toggleButton={
                        (<button
                            onMouseEnter={() => setAddButtonHoverStatus(true)}
                            onMouseLeave={() => setAddButtonHoverStatus(false)}
                            style={{
                                "backgroundColor": "white",
                                "border": "2px solid #008CBA",
                                "color": "black",
                                "padding": "8px 24px",
                                "textAlign": "center",
                                "textDecoration": "none",
                                "display": "inline-block",
                                "fontSize": "1.3vw",
                                "margin": "4px 2px",
                                "WebkitTransitionDuration": "0.4s",
                                "cursor": "pointer",
                                ...( addButtonHoverStatus && {
                                        "backgroundColor": "#008CBA",
                                        "color": "white"} )
                            }}>
                                ADD
                        </button>)
                    }
                    preCheckHandler={ checkNewMenuAndPriceAreEmpty }
                    okButtonClickedHandler={ addMenuHandler }
                    cancelButtonClickedHandler={ cancelModalHandler } >

                        {/* 메뉴 */}
                        <div className='modal__item'>
                            <label
                                htmlFor="menu"
                                style={{
                                    "fontSize": "1.4em"
                                }}>
                                    메뉴:
                            </label>
                            <input
                                id="menu"
                                type="text"
                                className="modal__menu"
                                style={{
                                    "marginLeft": "12px",
                                    "width": "50vw",
                                    "height": "4vh",
                                    "fontSize": "1.2em"
                                }}
                                value={newMenu}
                                onChange={(event) => { setNewMenu(event.target.value) }}
                                placeholder="메뉴명"/>
                        </div>

                        {/* 가격 */}
                        <div className='modal__item'>
                            <label
                                htmlFor="price"
                                style={{
                                    "fontSize": "1.4em"
                                }}>
                                    가격:
                            </label>
                            <input
                                id="price"
                                type="number"
                                className="modal__menu"
                                style={{
                                    "marginLeft": "12px",
                                    "width": "50vw",
                                    "height": "4vh",
                                    "fontSize": "1.2em"
                                }}
                                value={price}
                                onChange={(event) => { setPrice(event.target.value) }}
                                placeholder="가격"/>
                        </div>
                </CustomModal>
            </div>
            
            <div className='SettingMenu__list'>
                <ul>
                    { renderMenus() }
                </ul>
            </div>

        </section>
    )
})

export default SettingMenu