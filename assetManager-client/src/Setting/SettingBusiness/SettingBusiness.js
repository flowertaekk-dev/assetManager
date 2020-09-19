import React, { useEffect, useState } from 'react'
import _ from 'lodash'

import CustomModal from '../../components/Modal/CustomModal'
import customAxios from '../../customAxios'
import useStore from '../../mobx/useStore'

import './SettingBusiness.css'

const SettingBusiness = (props) => {

    const { loginUser } = useStore()

    const [ businessNames, setBusinessNames ] = useState([])
    const [ newBusinessName, setNewBusinessName ] = useState('')

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
     * 상호명 리스트를 랜더링한다
     */
    const renderBusinessNames = () => {
        return businessNames.map(businessNameJson => (
            <li
                key={businessNameJson.seq}
                className='SettingBusiness__list__item'>
                    <p>{businessNameJson.businessName}</p>
                    <button>Edit</button>
            </li>
        ))
    }

    /**
     * 상호명 추가 쿼리
     * 
     * @param callback Modal를 닫는 콜백
     */
    const addBusinessNameHandler = (callback) => {
        customAxios("/business/add", (response) => {
            if (response.resultStatus === 'SUCCESS') {
                retrieveAllBusinessNames()
                callback()
            } else {
                alert(response.reason)
            }
        }, {
            userId: loginUser.loginUserId,
            businessName: newBusinessName
        })
    }

    return (
        <section className='SettingBusiness'>
            <div className='SettingBusiness__header'>
                <h1>상호명(닉네임) 설정</h1>
                <CustomModal
                    modalTitle='상호명(닉네임) 추가'
                    okButtonClickedHandler={addBusinessNameHandler}
                    toggleButtonText='ADD'>
                        <React.Fragment>
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
                                onChange={(event) => {setNewBusinessName(event.target.value)}}
                                placeholder="상호명(닉네임)"/>
                        </React.Fragment>
                </CustomModal>
                
            </div>
            
            <div className='SettingBusiness__list'>
                <ul>
                    { renderBusinessNames() }
                </ul>
            </div>
        </section>
    )
}

export default SettingBusiness