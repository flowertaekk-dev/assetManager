import React from 'react'

import CustomModal from '../../components/Modal/CustomModal'

import './SettingBusiness.css'

const SettingBusiness = () => {
    return (
        <section className='SettingBusiness'>
            <div className='SettingBusiness__header'>
                <h1>상호명(닉네임) 설정</h1>
                <CustomModal
                    toggleButtonText='ADD'>
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