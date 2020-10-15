import React, { useEffect, useState } from 'react'

import CustomModal from '../../components/Modal/CustomModal'
import customAxios from '../../customAxios'
import useStore from '../../mobx/useStore'
import KEYS from '../../utils/LocalStorageKeys'

import './SettingBusiness.css'

const SettingBusiness = (props) => {

    const { loginUser, selectedBusiness } = useStore()

    const [ businessNames, setBusinessNames ] = useState([])
    const [ newBusinessName, setNewBusinessName ] = useState('') // add & update 공유
    const [ selectedBusinessId, setSelectedBusinessId ] = useState(selectedBusiness.selectedBusinessId)

    const [ addButtonHoverStatus, setAddButtonHoverStatus ] = useState(false)

    useEffect(() => {
        retrieveAllBusinessNames()
    }, [])

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
            userId: loginUser.loginUserId
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
            userId: loginUser.loginUserId,
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
                alert('테이블 정보 초기화에 실패했습니다.')
            }
        }, {
            userId: loginUser.loginUserId,
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
        let accountBook = JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))

        // 장부(accountBook) 데이터가 없으면 초기화
        if (!accountBook) {
            // TODO userId를 root로 하는게 좋으려나? 다수의 아이디를 하나의 컴퓨터에서 쓰러면?
            accountBook = {}
        }

        // 공통 로직
        accountBook = {...accountBook,
            [ businessId ]: {}
        }

        localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))
    }

    /**
     * 세션에 장부(accountBook)에 테이블 정보 생성
     *
     * @param {string} businessId
     */
    const addTableInfoToAccountBook = (businessId) => {
        let accountBook = JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))

        // 장부(accountBook) 데이터가 없으면 초기화
        if (!accountBook) {
            // TODO 이 에러메세지는 수정이 필요해. 지금은 귀찮아. 아마도 '상호명 추가 중에 에러가 발생했으니 다시 한 번 부탁드립니다' 정도?
            alert('테이블 정보 등록을 위한 데이터를 못 찾았습니다.')
            return
        }

        // 공통 로직
        accountBook = {...accountBook,
            [ businessId ]: [
                {} // 테이블 하나 추가 (default)
            ]
        }

        // 세션에 저장
        window.localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))
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
            userId: loginUser.loginUserId,
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
            userId: loginUser.loginUserId,
            businessName: deleteBusinessName
        })
    }

    /**
     * AccountBook에서 비지니스를 삭제한다
     *
     * @param {string} businessId 
     */
    const deleteBusinessFromAccountBook = ( businessId ) => {
        let accountBook = JSON.parse(localStorage.getItem(KEYS.ACCOUNT_BOOK))

        // 삭제
        delete accountBook[ businessId ]

        localStorage.setItem(KEYS.ACCOUNT_BOOK, JSON.stringify(accountBook))
    }

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
     * newBusiness state 값이 비어있는지 확인
     * 
     * @returns {Boolean} preCheck에 문제가 없으면 true, 있으면 false
     */
    const checkNewBusinessIsEmpty = () => {
        if ( newBusinessName === '' ) {
            alert('새로운 상호명을 입력해주세요!')
            return false
        }

        return true
    }

    /**
     * 특정 상호명을 선택했을 때 이벤트
     */
    const businessNameClickedHandler = (businessId) => {
        // 선택된 상호명 색상 채우기
        setSelectedBusinessId(businessId)

        // LocalStorage에 저장
        // mobx store에 저장
        selectedBusiness.updateSelectedBusinessId(businessId)
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
                            modalTitle={`상호명(닉네임) 수정: ${businessNameJson.businessName}`}
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
                                        새 상호명(닉네임):
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
                                    placeholder="새 상호명(닉네임)"/>
                        </CustomModal>

                        {/* DELETE */}
                        <CustomModal
                            modalTitle={`삭제할 상호명: ${businessNameJson.businessName}`}
                            toggleButton={
                                (
                                    <button>Delete</button>
                                )
                            }
                            okButtonClickedHandler={ deleteBusinessNameHandler(businessNameJson.businessName) } >
                                <p>정말 삭제할까요?</p>
                        </CustomModal>
                    </div>
            </li>
        ))
    }

    return (
        <section className='SettingBusiness'>
            <div className='SettingBusiness__header'>
                <h1>상호명(닉네임) 설정</h1>
                <CustomModal
                    modalTitle='상호명(닉네임) 추가'
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
                                상호명(닉네임):
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
                            onChange={(event) => { setNewBusinessName(event.target.value) }}
                            placeholder="상호명(닉네임)"/>
                </CustomModal>
            </div>
            
            <div className='SettingBusiness__list'>
                { businessNames.length === 0 && <h3>상호명을 등록해주세요!</h3>}

                <ul>
                    { renderBusinessNames() }
                </ul>
            </div>
        </section>
    )
}

export default SettingBusiness