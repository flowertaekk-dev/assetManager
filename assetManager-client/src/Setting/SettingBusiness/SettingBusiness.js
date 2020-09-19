import React, { useState } from 'react'

import CustomModal from '../../components/Modal/CustomModal'
import customAxios from '../../customAxios'
import useStore from '../../mobx/useStore'

import './SettingBusiness.css'

const SettingBusiness = () => {

    const { loginUser } = useStore()

    const [newBusinessName, setNewBusinessName] = useState('')

    /**
     * 
     * @param callback Modal를 닫는 콜백
     */
    const addBusinessNameHandler = (callback) => {
        customAxios("/business/add", (response) => {
            if (response.resultStatus === 'SUCCESS') {
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
                    <li className='SettingBusiness__list__item'>
                        <p>하용이네 삼겹살</p>
                        <button>Edit</button>
                    </li>
                    <li className='SettingBusiness__list__item'>
                        <p>하용이네 육회</p>
                        <button>Edit</button>
                    </li>
                    <li className='SettingBusiness__list__item'>
                        <p>하용이네 함흥냉면</p>
                        <button>Edit</button>
                    </li>
                </ul>
            </div>
        </section>
    )
}

export default SettingBusiness