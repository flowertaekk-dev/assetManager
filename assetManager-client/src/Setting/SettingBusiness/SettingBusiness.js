import React, { useEffect, useState } from 'react'

import CustomModal from '../../components/Modal/CustomModal'
import customAxios from '../../customAxios'
import useStore from '../../mobx/useStore'
import { getAccountBook, setAccountBook } from '../../utils/localStorageManager'

import './SettingBusiness.css'

/**
 * 상호명 설정 화면
 *
 * @param {*} props
 */
const SettingBusiness = (props) => {

    const { loginUser, selectedBusiness } = useStore()

    const [ businessNames, setBusinessNames ] = useState([])
    const [ newBusinessName, setNewBusinessName ] = useState('') // add & update 공유
    const [ selectedBusinessId, setSelectedBusinessId ] = useState(selectedBusiness.selectedBusinessId)

    const [ addButtonHoverStatus, setAddButtonHoverStatus ] = useState(false)

    useEffect(() => {
        retrieveAllBusinessNames()
    }, [])

    // -------------------------------------------------------------
    // Rest API 연동

    /**
     * 소유한 상호명(닉네임)을 전부 불러온다
     */
    const retrieveAllBusinessNames = () =>{
        customAxios("/business/readAll", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                setBusinessNames(response.businessNames)
            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUser.id
        })
    }

    /**
     * 상호명 추가 쿼리
     *
     * @param callback Modal를 닫는 콜백
     */
    const addBusinessNameHandler = (callback) => {
        customAxios("/business/add", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                const businessId = response.business.businessId

                addBusinessIdToAccountBook(businessId) // LocalStorage 장부(AccountBook)에 상호명 추가
                addTableCountHandler(businessId)       // 테이블 정보 초기화
                businessNameClickedHandler(businessId) // 생성한 상호명 자동 선택
                retrieveAllBusinessNames()             // refresh
                setNewBusinessName('')                 // 초기화
                callback()                             // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUser.id,
            businessName: newBusinessName
        })
    }

    /**
     * DB에 테이블 카운트 row 등록
     *
     * @param {string} businessId
     */
    const addTableCountHandler = (businessId) => {
        customAxios("/table/add", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                addTableInfoToAccountBook(response.tableInfo.businessId)
            } else {
                alert('Failed to set initial information for table')
            }
        }, {
            userId: loginUser.loginUser.id,
            businessId: businessId,
            tableCount: 0 // 초기값
        })
    }

    /**
     * 세션에 장부(accountBook) 생성 및 상호명ID 추가
     *
     * @param {string} businessId
     */
    const addBusinessIdToAccountBook = (businessId) => {
        let accountBook = getAccountBook()

        // 장부(accountBook) 데이터가 없으면 초기화
        if (!accountBook) {
            // TODO userId를 root로 하는게 좋으려나? 다수의 아이디를 하나의 컴퓨터에서 쓰러면?
            accountBook = {}
        }

        // 공통 로직
        accountBook = {...accountBook,
            [ businessId ]: {}
        }

        setAccountBook(accountBook)
    }

    /**
     * 상호명 갱신 쿼리
     *
     * @param {string} existingBusinessName 기존 상호명
     */
    const updateBusinessNameHandler = (existingBusinessName) => {
        return (callback) => customAxios("/business/update", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllBusinessNames()  // refresh
                setNewBusinessName('')      // 초기화
                callback()                  // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUser.id,
            existingBusinessName,
            newBusinessName
        })
    }

    /**
     * 상호명 삭제 쿼리
     *
     * @param {string} deleteBusinessName 삭제할 상호명
     */
    const deleteBusinessNameHandler = (deleteBusinessName) => {
        return (callback) => customAxios("/business/delete", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllBusinessNames()  // refresh
                deleteBusinessFromAccountBook(response.business.businessId)
                callback()                  // 모달 닫기
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUser.id,
            businessName: deleteBusinessName
        })
    }

    // -------------------------------------------------------------
    // 장부(accountBook) 관련

    /**
     * 세션에 장부(accountBook)에 테이블 정보 생성
     *
     * @param {string} businessId
     */
    const addTableInfoToAccountBook = (businessId) => {
        let accountBook = getAccountBook()

        // 장부(accountBook) 데이터가 없으면 초기화
        if (!accountBook) {
            // TODO 이 에러메세지는 수정이 필요해. 지금은 귀찮아. 아마도 '상호명 추가 중에 에러가 발생했으니 다시 한 번 부탁드립니다' 정도?
            alert('Could not find current business')
            return
        }

        // 공통 로직
        accountBook = {...accountBook,
            [ businessId ]: [
                {} // 테이블 하나 추가 (default)
            ]
        }

        // 세션에 저장
        setAccountBook(accountBook)
    }

    /**
     * AccountBook에서 비지니스를 삭제한다
     *
     * @param {string} businessId
     */
    const deleteBusinessFromAccountBook = ( businessId ) => {
        let accountBook = getAccountBook()

        // 삭제
        delete accountBook[ businessId ]
        selectedBusiness.deleteSelectedBusinessId()

        setAccountBook(accountBook)
    }

    // -------------------------------------------------------------
    // Handlers

    /**
     * 모달이 닫힐 때 newBusiness state를 초기화
     *
     * @param {function} callback
     */
    const cancelModalHandler = (callback) => {
        setNewBusinessName('')  // 초기화
        callback()              // 모달 닫기
    }

    /**
     * 특정 상호명을 선택했을 때 이벤트
     */
    const businessNameClickedHandler = (businessId) => {
        // 선택된 상호명 색상 채우기
        setSelectedBusinessId(businessId)

        // LocalStorage에 저장 & mobx store에 저장
        selectedBusiness.updateSelectedBusinessId(businessId)
    }

    // -------------------------------------------------------------
    // utils

    /**
     * newBusiness state 값이 비어있는지 확인
     *
     * @returns {Boolean} preCheck에 문제가 없으면 true, 있으면 false
     */
    const checkNewBusinessIsEmpty = () => {
        if ( newBusinessName === '' ) {
            alert('Please enter new Business name!')
            return false
        }

        return true
    }

    /**
     * 상호명 리스트를 랜더링한다
     */
    const renderBusinessNames = () => {
        return businessNames.map(businessNameJson => (
            <li
                key={ businessNameJson.businessId }
                className={
                    `SettingBusiness__list__item
                    ${selectedBusinessId === businessNameJson.businessId
                            ? 'SettingBusiness__list__item__active'
                            : ''}`
                }
                onClick={ () => businessNameClickedHandler(businessNameJson.businessId) } >
                    <p>{businessNameJson.businessName}</p>
                    <div className='SettingBusiness__list__buttons'>
                        {/* EDIT */}
                        <CustomModal
                            modalTitle={`Edit Business: ${businessNameJson.businessName}`}
                            toggleButton={
                                (
                                    <button>Edit</button>
                                )
                            }
                            preCheckHandler={ checkNewBusinessIsEmpty }
                            okButtonClickedHandler={ updateBusinessNameHandler(businessNameJson.businessName) }
                            cancelButtonClickedHandler={ cancelModalHandler } >
                                <label
                                    htmlFor="businessName"
                                    style={{
                                        "fontSize": "1.4em"
                                    }}>
                                        New Business:
                                </label>
                                <input
                                    id="businessName"
                                    type="text"
                                    className="modal__businessName"
                                    style={{
                                        "marginLeft": "12px",
                                        "width": "50vw",
                                        "height": "4vh",
                                        "fontSize": "1.2em"
                                    }}
                                    value={newBusinessName}
                                    onChange={(event) => {setNewBusinessName(event.target.value)}}
                                    placeholder="New Business name"/>
                        </CustomModal>

                        {/* DELETE */}
                        <CustomModal
                            modalTitle={`Deleting Business: ${businessNameJson.businessName}`}
                            toggleButton={
                                (
                                    <button>Delete</button>
                                )
                            }
                            okButtonClickedHandler={ deleteBusinessNameHandler(businessNameJson.businessName) } >
                                <p>Are you sure to delete it?</p>
                        </CustomModal>
                    </div>
            </li>
        ))
    }

    return (
        // Header
        <section className='SettingBusiness'>
            <div className='SettingBusiness__header'>
                <h1>Setting Business</h1>
                <CustomModal
                    modalTitle='Add Business'
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
                                ...( addButtonHoverStatus &&
                                        {
                                            "backgroundColor": "#008CBA",
                                            "color": "white"
                                        }
                                    )
                            }}>
                                ADD
                        </button>)
                    }
                    preCheckHandler={ checkNewBusinessIsEmpty }
                    okButtonClickedHandler={ addBusinessNameHandler }
                    cancelButtonClickedHandler={ cancelModalHandler } >
                        <label
                            htmlFor="businessName"
                            style={{
                                "fontSize": "1.4em"
                            }}>
                                Business :
                        </label>
                        <input
                            id="businessName"
                            type="text"
                            className="modal__businessName"
                            style={{
                                "marginLeft": "12px",
                                "width": "50vw",
                                "height": "4vh",
                                "fontSize": "1.2em"
                            }}
                            value={ newBusinessName }
                            onChange={(event) => { setNewBusinessName(event.target.value) }}
                            placeholder="Business Name"/>
                </CustomModal>
            </div>

            {/* 리스트 */}
            <div className='SettingBusiness__list'>
                { businessNames.length === 0 && <h3>Please add business</h3>}

                <ul>
                    { renderBusinessNames() }
                </ul>
            </div>
        </section>
    )
}

export default SettingBusiness