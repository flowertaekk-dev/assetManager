import React, { useEffect, useState } from 'react'
import { observer } from 'mobx-react'
import _ from 'lodash'

import useStore from '../../mobx/useStore'
import customAxios from '../../customAxios'
import CustomModal from '../../components/Modal/CustomModal'

import './SettingTable.css'

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
            userId: loginUser.loginUserId,
            businessName: selectedBusiness.selectedBusiness
        })
    }, [ selectedBusiness.selectedBusiness ])

    /**
     * tableCount에 빈 값이 들어있는지 확인한다
     */
    const checkTableCountIsEmpty = () => {

        let result = true

        if (tableCount === '') {
            alert('값을 입력해주세요!')
            result = false
        }

        if (tableCount < 0) {
            alert('정확한 테이블 개수를 등록해주세요!')
            result = false
        }
        if (!result)
            setTableCount(_.cloneDeep(initTableCountValue))

        return result
    }

    /**
     * TableCount update 쿼리
     */
    const updateTableCountHandler = (callback) => {
        customAxios("/table/update", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                callback()
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessName: selectedBusiness.selectedBusiness,
            tableCount
        })
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

    return (
        <section className='SettingTable'>
            
            <div className='SettingTable__header'>
                <h1>테이블 설정</h1>
            </div>

            <div className='SettingTable__list'>
                <ul>
                    <li className='SettingTable__list__item'>
                        <p>개수: { tableCount }</p>
                        <div className='SettingTable__list__buttons'>
                            <CustomModal
                                modalTitle='테이블 개수 수정'
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
                                            개수:
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
                                        placeholder="테이블 개수"/>
                            </CustomModal>
                        </div>
                    </li>
                </ul>
            </div>
        </section>
    )
})

export default SettingTable