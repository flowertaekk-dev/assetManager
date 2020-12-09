import React, { useEffect, useState } from 'react'
import { observer } from 'mobx-react'
import _ from 'lodash'

import useStore from '../../mobx/useStore'
import customAxios from '../../customAxios'
import CustomModal from '../../components/Modal/CustomModal'
import { getAccountBook, setAccountBook } from '../../utils/localStorageManager'

import './SettingTable.css'

/**
 * 테이블 정보 설정 컴포넌트
 */
const SettingTable = observer(() => {

    const { loginUser, selectedBusiness } = useStore()

    const [ tableCount, setTableCount ] = useState(0)
    const [ initTableCountValue, setInitTableCountValue ] = useState(0) // 유저가 옳지 않은 값을 입력했을시 원상태로 돌리기 위한 값!

    useEffect(() => {
        customAxios("/table/read", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                // 카운트 저장
                setTableCount(response.tableCount)
                setInitTableCountValue(response.tableCount)
            } else {
                alert('ERROR', response.reason)
            }
        }, {
            userId: loginUser.loginUser.id,
            businessId: selectedBusiness.selectedBusinessId
        })
    }, [ selectedBusiness.selectedBusinessId ])

    // --------------------------------------------------------
    // Checkers

    /**
     * tableCount에 빈 값이 들어있는지 확인한다
     */
    const checkTableCountIsEmpty = () => {

        let result = true

        if (tableCount === '') {
            alert('Enter Price!')
            result = false
        }

        if (tableCount < 0) {
            alert('Table count must be larger than 0')
            result = false
        }
        if (!result)
            setTableCount(_.cloneDeep(initTableCountValue))

        return result
    }

    // --------------------------------------------------------
    // Handlers

    /**
     * TableCount update 쿼리
     */
    const updateTableCountHandler = (callback) => {
        customAxios("/table/update", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                updateTableInfoToAccountBook(response.tableInfo) // accountBook에 등록
                callback()
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUser.id,
            businessId: selectedBusiness.selectedBusinessId,
            tableCount
        })
    }


    /**
     * 세션 장부(accountBook)에 테이블 카운트를 수정한다.
     *
     * @param {object} tableInfo
     */
    const updateTableInfoToAccountBook = (tableInfo) => {
        let accountBook = getAccountBook()

        if (!accountBook) {
            alert('Could not find target business')
            return
        }

        const currentAccountBook = accountBook[tableInfo.businessId] // 현재 상호명의 테이블 정보
        const currentNumberOfTableCount = currentAccountBook.length  // 현재 상호명의 테이블 개수
        const updatedNumberOfTableCount = tableInfo.tableCount       // 유저가 변경한 테이블 개수

        // 테이블 수가 증가했으면 테이블 추가
        if ( currentNumberOfTableCount < updatedNumberOfTableCount ) {
            _.times(updatedNumberOfTableCount - currentNumberOfTableCount, () => {
                currentAccountBook.push({})
            })
        }

        // 테이블 수가 감소했으면 테이블 삭제
        if ( currentNumberOfTableCount > updatedNumberOfTableCount ) {
            _.times(currentNumberOfTableCount - updatedNumberOfTableCount, () => {
                currentAccountBook.pop()
            })
        }

        // TODO DB도 갱신이 필요하잖아!!!! 왜 안 했지 어떻게..!?

        // accountBook 갱신
        accountBook = {...accountBook,
            [ tableInfo.businessId ]: currentAccountBook
        }
        setAccountBook(accountBook)
    }

    /**
     * 모달이 닫힐 때 newBusiness state를 초기화
     *
     * @param {function} callback
     */
    const cancelModalHandler = (callback) => {
        setTableCount(_.cloneDeep(initTableCountValue))
        callback()              // 모달 닫기
    }

    // --------------------------------------------------------

    return (
        <section className='SettingTable'>

            <div className='SettingTable__header'>
                <h1>Setting Table</h1>
            </div>

            <div className='SettingTable__list'>
                <ul>
                    <li className='SettingTable__list__item'>
                        <p>Count: { tableCount }</p>
                        <div className='SettingTable__list__buttons'>
                            <CustomModal
                                modalTitle='Edit table count'
                                toggleButton={
                                    (
                                        <button >Edit</button>
                                    )
                                }
                                preCheckHandler={ checkTableCountIsEmpty }
                                okButtonClickedHandler={ updateTableCountHandler }
                                cancelButtonClickedHandler={ cancelModalHandler } >
                                    <label
                                        htmlFor="tableCount"
                                        style={{
                                            "fontSize": "1.4em"
                                        }}>
                                            Count:
                                    </label>
                                    <input
                                        id="tableCount"
                                        type="number"
                                        className="modal__tableCount"
                                        style={{
                                            "marginLeft": "12px",
                                            "width": "50vw",
                                            "height": "4vh",
                                            "fontSize": "1.2em"
                                        }}
                                        value={ tableCount }
                                        onChange={(event) => { setTableCount(event.target.value) }}
                                        min='0'
                                        placeholder="Table count"/>
                            </CustomModal>
                        </div>
                    </li>
                </ul>
            </div>
        </section>
    )
})

export default SettingTable