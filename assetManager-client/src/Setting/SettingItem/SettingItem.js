import React from 'react'

import Button from '../../components/Button/Button'
import CustomModal from '../../components/Modal/CustomModal'

import './SettingItem.css'

const SettingItem = () => {
    return (
        <section className='SettingItem'>
            
            <div className='SettingItem__header'>
                <h1>메뉴 설정</h1>
                <CustomModal
                    toggleButtonText='ADD'>
                </CustomModal>
            </div>
            
            <div className='SettingItem__list'>
                <ul>
                    <li className='SettingItem__list__item'>
                        <p>삼겹살</p>
                        <p>5000원</p>
                    </li>
                    <li className='SettingItem__list__item'>
                        <p>육회</p>
                        <p>12000원</p>
                    </li>
                    <li className='SettingItem__list__item'>
                        <p>함흥냉면</p>
                        <p>9000원</p>
                    </li>
                </ul>
            </div>

        </section>
    )
}

export default SettingItem